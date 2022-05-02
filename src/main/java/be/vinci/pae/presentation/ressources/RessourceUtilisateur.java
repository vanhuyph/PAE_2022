package be.vinci.pae.presentation.ressources;

import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.business.utilisateur.UtilisateurUCC;
import be.vinci.pae.presentation.ressources.filtres.Autorisation;
import be.vinci.pae.presentation.ressources.filtres.AutorisationAdmin;
import be.vinci.pae.presentation.ressources.utilitaires.Json;
import be.vinci.pae.utilitaires.Config;
import be.vinci.pae.utilitaires.exceptions.PresentationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import java.util.Date;
import java.util.List;
import org.glassfish.jersey.server.ContainerRequest;

@Singleton
@Path("/utilisateurs")
public class RessourceUtilisateur {

  // (86.400.000 ms) -> 1 jour.
  private static final long EXPIRATION_TIME = 86400 * 1000;
  private static final ObjectMapper jsonMapper = new ObjectMapper();
  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getPropriete("JWTSecret"));
  @Inject
  private UtilisateurUCC utilisateurUCC;

  /**
   * Méthode qui connecte l'utilisateur à l'application.
   *
   * @param json : json reçu du formulaire de connexion
   * @return noeud : l'objet json contenant le token et l'utilisateur
   * @throws PresentationException : est lancée si pseudo ou mot de passe incorrect ou manquant
   */
  @POST
  @Path("connexion")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode connexion(JsonNode json) {
    if (!json.hasNonNull("pseudo") || !json.hasNonNull("mdp") || json.get("pseudo").asText()
        .isBlank() || json.get("mdp").asText().isBlank()) {
      throw new PresentationException("Pseudo ou mot de passe manquant", Status.BAD_REQUEST);
    }
    String pseudo = json.get("pseudo").asText();
    String mdp = json.get("mdp").asText();
    UtilisateurDTO utilisateurDTO = utilisateurUCC.connexion(pseudo, mdp);
    ObjectNode noeud = creationToken(utilisateurDTO);
    return noeud;
  }

  /**
   * Méthode qui inscrit l'utilisateur dans l'application.
   *
   * @param utilisateurDTO : utilisateur reçu du formulaire d'inscription
   * @return noeud : l'objet json contenant le token et l'utilisateur
   * @throws PresentationException : est lancée s'il y a eu un problème lors de l'inscription
   */
  @POST
  @Path("inscription")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode inscription(UtilisateurDTO utilisateurDTO) {
    UtilisateurDTO utilisateur;
    if (utilisateurDTO.getPseudo().isBlank() || utilisateurDTO.getNom().isBlank()
        || utilisateurDTO.getPrenom().isBlank() || utilisateurDTO.getMdp().isBlank()
        || utilisateurDTO.getAdresse().getRue().isBlank()
        || utilisateurDTO.getAdresse().getNumero() < 1
        || utilisateurDTO.getAdresse().getCodePostal() < 1 || utilisateurDTO.getAdresse()
        .getCommune().isBlank()) {
      throw new PresentationException("Des champs sont manquants", Status.BAD_REQUEST);
    }
    utilisateur = utilisateurUCC.inscription(utilisateurDTO);
    ObjectNode noeud = creationToken(utilisateur);
    return noeud;
  }

  /**
   * Récupère un utilisateur depuis un  id dans un token se trouvant dans le header.
   *
   * @param request : header avec le token
   * @return noeud : un nouveau token avec l'utilisateur
   * @throws PresentationException : est lancée si l'utilisateur n'a pas été trouvé
   */
  @GET
  @Path("moi")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public ObjectNode recupererUtilisateur(@Context ContainerRequest request) {
    UtilisateurDTO utilisateur = (UtilisateurDTO) request.getProperty("utilisateur");
    if (utilisateur == null) {
      throw new PresentationException("L'utilisateur n'a pas été trouvé", Status.BAD_REQUEST);
    }
    utilisateur = utilisateurUCC.rechercheParPseudo(utilisateur.getPseudo());
    ObjectNode noeud = creationToken(utilisateur);
    return noeud;
  }

  /**
   * Confirme l'inscription d'un utilisateur et précise s'il est admin ou pas.
   *
   * @param json : json contenant l'information s'il est admin ou pas
   * @param id   : l'id de l'utilisateur que l'on veut confirmer
   * @return utilisateurDTO : l'utilisateur confirmé
   * @throws PresentationException : est lancée s'il y a eu un problème dans la confirmation
   */
  @PUT
  @Path("confirme/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AutorisationAdmin
  public UtilisateurDTO confirmerUtilisateur(JsonNode json, @PathParam("id") int id) {
    if (id < 1) {
      throw new PresentationException("L'utilisateur n'existe pas", Status.BAD_REQUEST);
    }
    if (!json.hasNonNull("estAdmin")) {
      throw new PresentationException("Information sur le role manquante", Status.BAD_REQUEST);
    }
    boolean admin = json.get("estAdmin").asBoolean();
    UtilisateurDTO utilisateur = utilisateurUCC.confirmerInscription(id, admin);
    return utilisateur;
  }

  /**
   * Refuse l'inscription de l'utilisateur et ajoute un commentaire.
   *
   * @param json : json contenant le commentaire du refus
   * @param id   : l'id de l'utilisateur
   * @return utilisateurDTO : l'utilisateur refusé
   * @throws PresentationException : est lancée s'il y a eu un problème lors du refus
   */
  @PUT
  @Path("refuse/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AutorisationAdmin
  public UtilisateurDTO refuserUtilisateur(JsonNode json, @PathParam("id") int id) {
    String commentaire = json.get("commentaire").asText();
    if (commentaire.isBlank()) {
      throw new PresentationException("Commentaire manquant", Status.BAD_REQUEST);
    }
    if (id < 1) {
      throw new PresentationException("L'utilisateur n'existe pas", Status.BAD_REQUEST);
    }
    UtilisateurDTO utilisateurDTO = utilisateurUCC.refuserInscription(id, commentaire);
    return utilisateurDTO;
  }

  /**
   * Méthode permettant de créer le token de l'utilisateur avec une durée de validité de 1 jour.
   *
   * @param utilisateur : l'utilisateur qui aura le token
   * @return noeud : l'objet json contenant le token et l'utilisateur
   * @throws PresentationException : est lancée s'il y a eu un problème lors de la création du
   *                               token
   */
  private ObjectNode creationToken(UtilisateurDTO utilisateur) {
    String token;
    try {
      token = JWT.create().withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
          .withIssuer("auth0")
          .withClaim("utilisateur", utilisateur.getIdUtilisateur())
          .withClaim("auth", utilisateur.isEstAdmin()).sign(jwtAlgorithm);
    } catch (Exception e) {
      throw new PresentationException("Erreur lors de la création du token",
          Status.INTERNAL_SERVER_ERROR);
    }
    UtilisateurDTO utilisateurDTO = Json.filtrePublicJsonVue(utilisateur,
        UtilisateurDTO.class);
    ObjectNode noeud = jsonMapper.createObjectNode().put("token", token)
        .putPOJO("utilisateur", utilisateurDTO);
    return noeud;
  }

  /**
   * Liste tous les utilisateurs avec une inscription à l'état "refusé".
   *
   * @return liste : la liste des utilisateurs avec une inscription refusée
   */
  @GET
  @Path("refuse")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AutorisationAdmin
  public List<UtilisateurDTO> listerInscriptionsRefusees() {
    List<UtilisateurDTO> liste;
    liste = utilisateurUCC.listerUtilisateursEtatsInscriptions("Refusé");
    return liste;
  }

  /**
   * Liste tous les utilisateurs avec une inscription à l'état "en attente".
   *
   * @return liste : la liste des utilisateurs avec une inscription en attente
   */
  @GET
  @Path("attente")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AutorisationAdmin
  public List<UtilisateurDTO> listerInscriptionsEnAttente() {
    List<UtilisateurDTO> liste;
    liste = utilisateurUCC.listerUtilisateursEtatsInscriptions("En attente");
    return liste;
  }

  /**
   * Liste tous les utilisateurs avec une inscription à l'état "en attente".
   *
   * @return liste : la liste des utilisateurs avec une inscription en attente
   */
  @GET
  @Path("confirme")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AutorisationAdmin
  public List<UtilisateurDTO> listerInscriptionsConfirme() {
    List<UtilisateurDTO> liste;
    liste = utilisateurUCC.listerUtilisateursEtatsInscriptions("Confirmé");
    return liste;
  }

  /**
   * Récupère l'utilisateur pour visualiser son profil.
   *
   * @param idUtilisateur : l'id de l'utilisateur à récupérer
   * @return utilisateurDTO : l'utilisateur que l'on veut visualiser son profil
   */
  @GET
  @Path("voirProfil/{idUtilisateur}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public UtilisateurDTO voirProfilUtilisateur(@PathParam("idUtilisateur") int idUtilisateur) {
    UtilisateurDTO utilisateurDTO = utilisateurUCC.rechercheParId(idUtilisateur);
    return utilisateurDTO;
  }

  /**
   * Liste tous les utilisateurs en fonction d'un critère de recherche (nom, code postal ou ville).
   *
   * @param recherche : le critère de recherche
   * @return liste : la liste des utilisateurs correspondante au critère de recherche
   */
  @GET
  @Path("recherche/{recherche}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AutorisationAdmin
  public List<UtilisateurDTO> rechercherMembres(@PathParam("recherche") String recherche) {
    List<UtilisateurDTO> liste;
    liste = utilisateurUCC.rechercherMembres(recherche);
    return liste;
  }

  /**
   * Modifie le profil de l'utilisateur.
   *
   * @param utilisateurDTO : l'utilisateur à modifier les informations
   * @return utilisateurDTO : l'utilisateur avec ses informations modifiées
   * @throws PresentationException : est lancée si des champs sont manquants lors de la
   *                               modification
   */
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public UtilisateurDTO modifierProfilUtilisateur(UtilisateurDTO utilisateurDTO) {
    if (utilisateurDTO.getPseudo().isBlank() || utilisateurDTO.getNom().isBlank()
        || utilisateurDTO.getPrenom().isBlank() || utilisateurDTO.getAdresse().getRue().isBlank()
        || utilisateurDTO.getAdresse().getNumero() < 1
        || utilisateurDTO.getAdresse().getCodePostal() < 1 || utilisateurDTO.getAdresse()
        .getCommune().isBlank()) {
      throw new PresentationException("Des champs sont manquants", Status.BAD_REQUEST);
    }
    return utilisateurUCC.miseAJourUtilisateur(utilisateurDTO);
  }

  /**
   * Modifie l'état de l'utilisateur de "Empêché à Confirmé".
   *
   * @param idUtilisateur : l'id de l'utilisateur dont on veut modifier l'état
   * @return utilisateurDTO ayant son état modifié
   * @throws PresentationException : si l'utilisateur n'existe pas ou si le json ne possède pas
   *                               l'état de l'utilisateur
   */
  @PUT
  @Path("indiquerConfirmerUtilisateur/{idUtilisateur}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public UtilisateurDTO indiquerConfirmerUtilisateur(
      @PathParam("idUtilisateur") int idUtilisateur) {
    if (idUtilisateur < 1) {
      throw new PresentationException("L'utilisateur n'existe pas", Status.BAD_REQUEST);
    }
    UtilisateurDTO utilisateurDTO = utilisateurUCC.rechercheParId(idUtilisateur);
    if (utilisateurDTO.getIdUtilisateur() != idUtilisateur) {
      throw new PresentationException("L'utilisateur n'existe pas", Status.BAD_REQUEST);
    }
    return utilisateurUCC.indiquerConfirmerUtilisateur(utilisateurDTO);
  }

  /**
   * Modifie l'état de l'utilisateur de "Confirmé à Empêché".
   *
   * @param idUtilisateur : l'id de l'utilisateur dont on veut modifier l'état
   * @return utilisateurDTO ayant son etat modifié
   * @throws PresentationException : si l'utilisateur n'existe pas ou si le json ne possède pas
   *                               l'état de l'utilisateur
   */
  @PUT
  @Path("indiquerEmpecherUtilisateur/{idUtilisateur}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public UtilisateurDTO indiquerEmpecherUtilisateur(@PathParam("idUtilisateur") int idUtilisateur) {
    if (idUtilisateur < 1) {
      throw new PresentationException("L'utilisateur n'existe pas", Status.BAD_REQUEST);
    }
    UtilisateurDTO utilisateurDTO = utilisateurUCC.rechercheParId(idUtilisateur);
    if (utilisateurDTO.getIdUtilisateur() != idUtilisateur) {
      throw new PresentationException("L'utilisateur n'existe pas", Status.BAD_REQUEST);
    }
    return utilisateurUCC.indiquerEmpecherUtilisateur(utilisateurDTO);
  }

  /**
   * Modifie le mot de passe de l'utilisateur.
   *
   * @param idUtilisateur : l'id de l'utilisateur
   * @param json          : json avec le mot de passe actuel et le nouveau mot de passe
   * @return utilisateurDTO : l'utilisateur
   * @throws PresentationException : est lancée si le mot de passe est vide
   */
  @PUT
  @Path("modifierMdp/{idUtilisateur}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public UtilisateurDTO modifierMdpUtilisateur(@PathParam("idUtilisateur") int idUtilisateur,
      JsonNode json) {
    if (idUtilisateur < 1) {
      throw new PresentationException("L'utilisateur n'existe pas", Status.BAD_REQUEST);
    }
    if (!json.hasNonNull("mdpActuel") || json.get("mdpActuel").asText().isBlank()
        || !json.hasNonNull("nouvMdp") || json.get("nouvMdp").asText().isBlank()
        || !json.hasNonNull(
        "confNouvMdp") || json.get("confNouvMdp").asText().isBlank()) {
      throw new PresentationException("Des champs sont manquant", Status.BAD_REQUEST);
    }
    String mdpActuel = json.get("mdpActuel").asText();
    String nouvMdp = json.get("nouvMdp").asText();
    String confNouvMdp = json.get("confNouvMdp").asText();
    if (!nouvMdp.equals(confNouvMdp)) {
      throw new PresentationException("Confirmation du mot de passe est incorrecte",
          Status.BAD_REQUEST);
    }
    return utilisateurUCC.modifierMdp(idUtilisateur, mdpActuel, nouvMdp);
  }

}

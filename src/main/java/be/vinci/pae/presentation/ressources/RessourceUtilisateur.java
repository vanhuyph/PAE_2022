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
    if (!json.hasNonNull("pseudo") || !json.hasNonNull("mdp")) {
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
    
    UtilisateurDTO utilisateur = null;

    utilisateurUCC.rechercheParPseudoInscription(utilisateurDTO.getPseudo());

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
      throw new PresentationException("L'utilisateur n'a pas été retrouvé",
          Status.BAD_REQUEST);
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
    if (!json.hasNonNull("estAdmin")) {
      throw new PresentationException("Information si l'utilisateur est admin ou non",
          Status.BAD_REQUEST);
    }
    if (id < 1) {
      throw new PresentationException("L'utilisateur n'existe pas", Status.BAD_REQUEST);
    }
    boolean estAdmin = json.get("estAdmin").asBoolean();

    UtilisateurDTO utilisateurDTO = utilisateurUCC.confirmerInscription(id, estAdmin);

    return utilisateurDTO;
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
    if (commentaire.equals("")) {
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
    liste = utilisateurUCC.listerUtilisateursEtatsInscriptions("refusé");
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
    liste = utilisateurUCC.listerUtilisateursEtatsInscriptions("en attente");
    return liste;
  }

}

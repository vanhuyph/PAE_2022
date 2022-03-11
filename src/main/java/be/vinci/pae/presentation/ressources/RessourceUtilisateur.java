package be.vinci.pae.presentation.ressources;

import be.vinci.pae.business.adresse.AdresseDTO;
import be.vinci.pae.business.adresse.AdresseUCC;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.business.utilisateur.UtilisateurUCC;
import be.vinci.pae.presentation.ressources.filtres.AutorisationAdmin;
import be.vinci.pae.presentation.ressources.utilitaires.Json;
import be.vinci.pae.utilitaires.Config;
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
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.Date;
import java.util.List;

@Singleton
@Path("/utilisateurs")
public class RessourceUtilisateur {

  private static final long EXPIRATION_TIME = 86400 * 1000;
  private static final ObjectMapper jsonMapper = new ObjectMapper();
  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getPropriete("JWTSecret"));
  @Inject
  private UtilisateurUCC utilisateurUCC;
  @Inject
  private AdresseUCC adresseUCC;

  /**
   * Méthode qui connecte l'utilisateur à l'application.
   *
   * @param json : json reçu du formulaire de connexion
   * @return noeud : l'objet json contenant le token et l'utilisateur
   * @throws WebApplicationException : est lancée si pseudo ou mot de passe incorrect ou manquant
   */
  @POST
  @Path("connexion")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode connexion(JsonNode json) {
    if (!json.hasNonNull("pseudo") || !json.hasNonNull("mdp")) {
      throw new WebApplicationException(
          Response.status(Response.Status.BAD_REQUEST)
              .entity("Pseudo ou mot de passe manquant").type("text/plain").build());
    }
    String pseudo = json.get("pseudo").asText();
    String mdp = json.get("mdp").asText();
    UtilisateurDTO utilisateurDTO = utilisateurUCC.connexion(pseudo, mdp);
    if (utilisateurDTO == null) {
      throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
          .entity("Pseudo ou mot de passe incorrect").type(MediaType.TEXT_PLAIN)
          .build());
    }
    ObjectNode noeud = creationToken(utilisateurDTO);
    return noeud;
  }

  /**
   * Méthode qui inscrit l'utilisateur dans l'application.
   *
   * @param json : json reçu du formulaire d'inscription
   * @return noeud : l'objet json contenant le token et l'utilisateur
   * @throws WebApplicationException : est lancée s'il y a eu un problème lors de l'inscription
   */
  @POST
  @Path("inscription")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode inscription(JsonNode json) {
    if (!json.hasNonNull("pseudo") || !json.hasNonNull("nom")
        || !json.hasNonNull("prenom") || !json.hasNonNull("mdp")
        || !json.hasNonNull("rue") || !json.hasNonNull("numero")
        || !json.hasNonNull("code_postal") || !json.hasNonNull("commune")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("Des champs sont manquants").type("text/plain").build());
    }
    String pseudo = json.get("pseudo").asText();
    String nom = json.get("nom").asText();
    String prenom = json.get("prenom").asText();
    String mdp = json.get("mdp").asText();
    String rue = json.get("rue").asText();
    int numero = json.get("numero").asInt();
    int boite = json.get("boite").asInt();
    int codePostal = json.get("code_postal").asInt();
    String commune = json.get("commune").asText();
    try {
      if (utilisateurUCC.rechercheParPseudoInscription(pseudo).getIdUtilisateur() < 1) {
        AdresseDTO adresse = adresseUCC.ajouterAdresse(rue, numero, boite, codePostal, commune);
        if (adresse == null) {
          throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
              .entity("L'adresse n'a pas pu être ajoutée").type("text/plain").build());
        }
        UtilisateurDTO utilisateur = utilisateurUCC.inscription(pseudo, nom, prenom, mdp,
            adresse.getIdAdresse());
        if (utilisateur == null) {
          throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
              .entity("L'utilisateur n'a pas pu être ajouté").type(MediaType.TEXT_PLAIN)
              .build());
        }
        ObjectNode noeud = creationToken(utilisateur);
        return noeud;
      }
    } catch (Exception e) {
      throw new WebApplicationException(Response.status(Status.CONFLICT)
          .entity("Le pseudo existe déjà").type("text/plain").build());
    }
    throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
        .entity("Erreur lors de l'inscription").type("text/plain").build());
  }

  /**
   * Confirme l'inscription d'un utilisateur et précise s'il est admin ou pas.
   *
   * @param json : json contenant l'information s'il est admin ou pas
   * @param id   : l'id de l'utilisateur que l'on veut confirmer
   * @return utilisateurDTO : l'utilisateur confirmé
   * @throws WebApplicationException : est lancée s'il y a eu un problème dans la confirmation
   */
  @PUT
  @Path("confirme/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AutorisationAdmin
  public UtilisateurDTO confirmerUtilisateur(JsonNode json, @PathParam("id") int id) {
    if (!json.hasNonNull("estAdmin")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("Information si l'utilisateur est admin ou non").type(MediaType.TEXT_PLAIN)
          .build());
    }
    if (id == 0) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("L'utilisateur n'existe pas").type(MediaType.TEXT_PLAIN)
          .build());
    }
    boolean estAdmin = json.get("estAdmin").asBoolean();
    UtilisateurDTO utilisateurDTO = null;
    try {
      utilisateurDTO = utilisateurUCC.confirmerInscription(id, estAdmin);
      if (utilisateurDTO == null) {
        throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
            .entity("L'utilisateur n'a pas pu être confimé").type(MediaType.TEXT_PLAIN)
            .build());
      }
    } catch (Exception e) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("L'utilisateur n'a pas pu être confimé").type(MediaType.TEXT_PLAIN)
          .build());
    }
    return utilisateurDTO;
  }

  /**
   * Refuse l'inscription de l'utilisateur et ajoute un commentaire.
   *
   * @param json : json contenant le commentaire du refus
   * @param id   : l'id de l'utilisateur
   * @return utilisateurDTO : l'utilisateur refusé
   * @throws WebApplicationException : est lancée s'il y a eu un problème lors du refus
   */
  @PUT
  @Path("refuse/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AutorisationAdmin
  public UtilisateurDTO refuserUtilisateur(JsonNode json, @PathParam("id") int id) {
    String commentaire = json.get("commentaire").asText();
    if (commentaire.equals("")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("Commentaire manquant").type(MediaType.TEXT_PLAIN)
          .build());
    }
    if (id < 1) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("L'utilisateur n'existe pas").type(MediaType.TEXT_PLAIN)
          .build());
    }
    UtilisateurDTO utilisateurDTO = null;
    try {
      utilisateurDTO = utilisateurUCC.refuserInscription(id, commentaire);
      if (utilisateurDTO == null || utilisateurDTO.getIdUtilisateur() < 1) {
        throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
            .entity("L'utilisateur n'a pas pu être refusé").type(MediaType.TEXT_PLAIN)
            .build());
      }
    } catch (Exception e) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("L'utilisateur n'a pas pu être refusé").type(MediaType.TEXT_PLAIN)
          .build());
    }
    return utilisateurDTO;
  }

  /**
   * Méthode permettant de créer le token de l'utilisateur avec une durée de validité de 1 jour.
   *
   * @param utilisateur : l'utilisateur qui aura le token
   * @return noeud : l'objet json contenant le token et l'utilisateur
   * @throws WebApplicationException : est lancée s'il y a eu un problème lors de la création du
   *                                 token
   */
  private ObjectNode creationToken(UtilisateurDTO utilisateur) {
    String token;
    try {
      token = JWT.create().withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
          .withIssuer("auth0")
          .withClaim("utilisateur", utilisateur.getIdUtilisateur())
          .withClaim("auth", utilisateur.isEstAdmin()).sign(jwtAlgorithm);
    } catch (Exception e) {
      throw new WebApplicationException("Erreur lors de la création du token", e,
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

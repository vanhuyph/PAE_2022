package be.vinci.pae.presentation.ressources;

import be.vinci.pae.business.adresse.AdresseDTO;
import be.vinci.pae.business.adresse.AdresseUCC;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.business.utilisateur.UtilisateurUCC;
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
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/utilisateurs")
public class RessourceUtilisateur {

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
   * @throws WebApplicationException si pseudo ou mot de passe incorrect ou manquant
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
    System.out.println("Dans inscription");
    String pseudo = json.get("pseudo").asText();
    String nom = json.get("nom").asText();
    String prenom = json.get("prenom").asText();
    String mdp = json.get("mdp").asText();

    String rue = json.get("rue").asText();
    int numero = json.get("numero").asInt();
    int boite = json.get("boite").asInt();
    int codePostal = json.get("code_postal").asInt();
    String commune = json.get("commune").asText();

    AdresseDTO adresse = adresseUCC.ajouterAdresse(rue, numero, boite, codePostal, commune);
    if (adresse == null) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("L'adresse n'a pas pu être ajoutée").type("text/plain").build());
    }

    UtilisateurDTO utilisateur = utilisateurUCC.inscription(pseudo, nom, prenom, mdp,
        adresse.getIdAdresse());

    if (utilisateur == null) {
      throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
          .entity("L'utilisateur n'a pas pu être ajouté").type(MediaType.TEXT_PLAIN)
          .build());
    }

    ObjectNode noeud = creationToken(utilisateur);
    System.out.println(noeud);
    return noeud;

  }

  /**
   * Méthode permettant de créer le token de l'utilisateur.
   *
   * @param utilisateur : l'utilisateur qui aura le token
   * @return noeud : l'objet json contenant le token et l'utilisateur
   */
  private ObjectNode creationToken(UtilisateurDTO utilisateur) {
    String token;
    try {
      token = JWT.create().withIssuer("auth0")
          .withClaim("utilisateur", utilisateur.getIdUtilisateur())
          .withClaim("auth", utilisateur.isEstAdmin()).sign(jwtAlgorithm);
    } catch (Exception e) {
      throw new WebApplicationException("Erreur lors de la création du token", e,
          Status.INTERNAL_SERVER_ERROR);
    }
    UtilisateurDTO utilisateurDTO = Json.filtrePublicJsonVue((UtilisateurDTO) utilisateur,
        UtilisateurDTO.class);
    ObjectNode noeud = jsonMapper.createObjectNode().put("token", token)
        .putPOJO("utilisateur", utilisateurDTO);
    return noeud;
  }

}

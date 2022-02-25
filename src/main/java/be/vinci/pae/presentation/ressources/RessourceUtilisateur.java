package be.vinci.pae.presentation.ressources;

import be.vinci.pae.business.domaine.UtilisateurDTO;
import be.vinci.pae.business.ucc.UtilisateurUCC;
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
  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  @Inject
  private UtilisateurUCC utilisateurUCC;

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

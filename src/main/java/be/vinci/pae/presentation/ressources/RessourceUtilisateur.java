package be.vinci.pae.presentation.ressources;


import com.fasterxml.jackson.databind.JsonNode;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/utilisateur")
public class RessourceUtilisateur {

  // changer en UtilisateurUCC
  private Object utilisateurUCC;

  /**
   * methode qui connecte l'utilisateur a l'application
   *
   * @return l'utilisateur connecte
   * @throws WebApplicationException si pseudo ou mot de passe incorrects ou manquants
   * @params json reçu du formulaire de connexion
   */
  @POST
  @Path("connexion")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Object connexion(JsonNode json) {
    // appel de connexionToken sur utilisateurUCC

    // verification presence des parametres
    if (!json.hasNonNull("pseudo") || !json.hasNonNull("mdp")) {
      throw new WebApplicationException(
          Response.status(Response.Status.BAD_REQUEST) // vérifier code d'erreur
              .entity("pseudo ou mot de passe manquants").type("text/plain").build());
    }
    String pseudo = json.get("pseudo").asText();
    String mdp = json.get("mdp").asText();

    // tentative de connexion
    Object utilisateurDTO = utilisateurUCC; //.connexion(pseudo,mdp);
    if (utilisateurDTO == null) {
      throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
          .entity("pseudo ou mot de passe incorrect").type(MediaType.TEXT_PLAIN)
          .build());
    }
    return utilisateurDTO;

  }


}

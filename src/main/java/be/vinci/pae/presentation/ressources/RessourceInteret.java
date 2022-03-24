package be.vinci.pae.presentation.ressources;


import be.vinci.pae.business.interet.InteretDTO;
import be.vinci.pae.business.interet.InteretUCC;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Singleton
@Path("/interets")
public class RessourceInteret {

  @Inject
  private InteretUCC interetUCC;


  /**
  * Créer un interet via l'id de l'utilisateur et de l'objet.
  *
  * @param json : json envoyé par le formulaire de créer un interet
  * @return un interetDTO
  * @throws Exception : si problème avec les dates
  */
  @POST
  @Path("creerInteret")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public InteretDTO creetInteret(JsonNode json) throws Exception {

    if (!json.hasNonNull("idUtilisateurInteresse")
        || !json.hasNonNull("idObjet")
        || !json.hasNonNull("dateRdv")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
              .entity("id de l'utilisateur ou de l'objet ou date manquant")
              .type("text/plain").build());
    }
    int idUtilisateurInteresse = json.get("idUtilisateurInteresse").asInt();
    int idObjet = json.get("idObjet").asInt();

    String dateRdvString = json.get("dateRdv").asText();
    Date dateJava = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.FRANCE)
            .parse(dateRdvString);
    long dataJavaInt = dateJava.getTime();
    long dataJavaNow = System.currentTimeMillis();

    if (dataJavaInt < dataJavaNow) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
              .entity("la date de rendez vous ne peut pas être dans le passé")
              .type("text/plain").build());

    }

    InteretDTO interet = interetUCC.creerUnInteret(idUtilisateurInteresse, idObjet, dateJava);
    if (interet == null) {
      throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
              .entity("L'ajout de l'interet a échoué").type(MediaType.TEXT_PLAIN)
              .build());
    }

    return interet;
  }
}

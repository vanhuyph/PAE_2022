package be.vinci.pae.presentation.ressources;

import be.vinci.pae.business.interet.InteretDTO;
import be.vinci.pae.business.interet.InteretUCC;
import be.vinci.pae.utilitaires.exceptions.PresentationException;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Singleton
@Path("/interets")
public class RessourceInteret {

  @Inject
  private InteretUCC interetUCC;

  /**
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
      throw new PresentationException("Des champs sont manquants", Status.BAD_REQUEST);
    }
    int idUtilisateurInteresse = json.get("idUtilisateurInteresse").asInt();
    int idObjet = json.get("idObjet").asInt();
    String dateRdvString = json.get("dateRdv").asText();
    Date dateJava = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.FRANCE).parse(
        dateRdvString);
    long dataJavaInt = dateJava.getTime();
    long dataJavaNow = System.currentTimeMillis();
    if (dataJavaInt < dataJavaNow) {
      throw new PresentationException("La date de rendez-vous ne peut pas être dans le passé",
          Status.BAD_REQUEST);
    }
    InteretDTO interet = interetUCC.creerUnInteret(idUtilisateurInteresse, idObjet, dateJava);
    if (interet == null) {
      throw new PresentationException("L'ajout de l'intérêt à échoué",
          Status.BAD_REQUEST);
    }
    return interet;
  }

}
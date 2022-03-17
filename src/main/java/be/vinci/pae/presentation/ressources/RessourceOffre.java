package be.vinci.pae.presentation.ressources;

import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.business.offre.OffreUCC;
import be.vinci.pae.presentation.ressources.filtres.Autorisation;
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
import jakarta.ws.rs.core.Response.Status;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;


@Singleton
@Path("/offres")
public class RessourceOffre {


  @Inject
  private OffreUCC offreUCC;

  /**
   * Creer une offre.
   *
   * @param json : json reçu du formulaire de connexion *
   * @return noeud : l'objet json contenant le token et l'utilisateur *
   * @throws WebApplicationException si id_objet ou plage_horaire sont null ou si l'offre n'a pas
   *                                 été créée
   */
  @POST
  @Path("creerOffre")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public OffreDTO creerOffre(JsonNode json) {

    if (!json.hasNonNull("idObjet") ||
        !json.hasNonNull("plageHoraire")) {
      throw new WebApplicationException(
          Response.status(Response.Status.BAD_REQUEST)
              .entity("type de l'objet ou description manquant").type("text/plain").build());
    }

    Integer idObjet = json.get("idObjet").asInt();
    String plageHoraire = json.get("plageHoraire").asText();

    OffreDTO offreDTO = offreUCC.creerUneOffre(idObjet, plageHoraire);
    if (offreDTO == null) {
      throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity("L'ajout de l'offre a échoué").type(MediaType.TEXT_PLAIN)
          .build());
    }

    return offreDTO;
  }

  @POST
  @Path("/telechargementPhoto")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.MULTIPART_FORM_DATA)
  @Autorisation
  public Response telechargerPhoto(@FormDataParam("photo") InputStream photo,
      @FormDataParam("photo") FormDataContentDisposition fichierDisposition) throws IOException {
    String nomFichier = fichierDisposition.getName(); //UUID
    Files.copy(photo, Paths.get(nomFichier));
    return Response.ok(nomFichier).header("Access-Control-Allow-Origin", "*").build();
    //return Response.ok().build();
  }

}

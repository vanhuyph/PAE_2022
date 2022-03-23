package be.vinci.pae.presentation.ressources;

import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.business.offre.OffreUCC;
import be.vinci.pae.presentation.ressources.filtres.Autorisation;
import be.vinci.pae.utilitaires.exceptions.PresentationException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
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
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;


@Singleton
@Path("/offres")
public class RessourceOffre {

  @Inject
  private OffreUCC offreUCC;

  /**
   * Créer une offre.
   *
   * @param offreDTO : offre reçu du formulaire de créer une offre
   * @return offreDTO : l'offre créer*
   * @throws WebApplicationException si l'offre est null
   */
  @POST
  @Path("creerOffre")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public OffreDTO creerOffre(OffreDTO offreDTO) {
    if (offreDTO.getObjetDTO().getDescription().isBlank()
        || offreDTO.getObjetDTO().getTypeObjet() == null || offreDTO.getPlageHoraire().isBlank()) {
      throw new WebApplicationException(
          Response.status(Response.Status.BAD_REQUEST)
              .entity("offre null").type("text/plain").build());
    }
    offreDTO = offreUCC.creerUneOffre(offreDTO);

    return offreDTO;
  }


  /**
   * Liste les offres.
   *
   * @return noeud : la liste des offres
   */
  @GET
  @Path("listOffres")
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public List<OffreDTO> listOffres() {
    List<OffreDTO> offreDTO = offreUCC.listOffres();
    if (offreDTO == null) {
      throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity("Liste des offres a echoué").type(MediaType.TEXT_PLAIN)
          .build());
    }
    return offreDTO;
  }

  /**
   * Telechargement de la photo.
   *
   * @param photo
   * @param fichierDisposition
   * @return
   * @throws IOException
   */
  @POST
  @Path("/telechargementPhoto")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.MULTIPART_FORM_DATA)
  //@Autorisation
  public Response telechargerPhoto(@FormDataParam("photo") InputStream photo,
      @FormDataParam("photo") FormDataContentDisposition fichierDisposition) throws IOException {
    String nomFichier = fichierDisposition.getFileName();
    String nomDencodage = UUID.randomUUID().toString() + nomFichier;
    System.out.println("nom du fichier: " + nomFichier);
    System.out.println("télécharger Photo");
    Files.copy(photo, Paths.get("./image/" + nomDencodage), StandardCopyOption.REPLACE_EXISTING);
    return Response.ok(nomFichier).header("Access-Control-Allow-Origin", "*").build();
    //return Response.ok().build();
  }


  /**
   * Liste les offres recentes.
   *
   * @return noeud : la liste des offres recentes
   */
  @GET
  @Path("listOffresRecent")
  @Produces(MediaType.APPLICATION_JSON)
  public List<OffreDTO> listOffresRecent() {
    List<OffreDTO> offreDTO = offreUCC.listOffresRecent();
    if (offreDTO == null) {
      throw new PresentationException("Liste des offres a echoué", Status.BAD_REQUEST);
    }
    return offreDTO;
  }


}

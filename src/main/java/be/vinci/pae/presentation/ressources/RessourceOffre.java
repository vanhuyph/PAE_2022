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
   * @return offreDTO : l'offre créée
   * @throws PresentationException : est lancée s'il y a eu un problème lors de la création d'une
   *                               offre
   */
  @POST
  @Path("creerOffre")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public OffreDTO creerOffre(OffreDTO offreDTO) {
    if (offreDTO.getObjetDTO().getDescription().isBlank()
        || offreDTO.getObjetDTO().getTypeObjet() == null || offreDTO.getPlageHoraire().isBlank()) {
      throw new PresentationException("Des champs sont manquants", Status.BAD_REQUEST);
    }
    offreDTO = offreUCC.creerUneOffre(offreDTO);
    return offreDTO;
  }

  /**
   * Liste les offres.
   *
   * @return liste : la liste des offres
   */
  @GET
  @Path("listerOffres")
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public List<OffreDTO> listerOffres() {
    List<OffreDTO> liste = offreUCC.listerOffres();
    return liste;
  }

  /**
   * Liste les offres les plus récentes.
   *
   * @return liste : la liste des offres les plus récentes
   */
  @GET
  @Path("listerOffresRecentes")
  @Produces(MediaType.APPLICATION_JSON)
  public List<OffreDTO> listerOffresRecent() {
    List<OffreDTO> liste = offreUCC.listerOffresRecentes();
    return liste;
  }

  /**
   * Téléchargement de la photo.
   *
   * @param photo              : la photo à télécharger
   * @param fichierDisposition
   * @return response
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

}
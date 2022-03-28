package be.vinci.pae.presentation.ressources;

import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.business.offre.OffreUCC;
import be.vinci.pae.presentation.ressources.filtres.Autorisation;
import be.vinci.pae.utilitaires.Config;
import be.vinci.pae.utilitaires.exceptions.PresentationException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.io.File;
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
    offreDTO = offreUCC.creerOffre(offreDTO);
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
   * Annule une offre.
   *
   * @param id : l'id de l'offre a annulé
   * @return offreDTO : l'offre annulée
   * @throws PresentationException : est lancée si l'id de l'offre est invalide ou que l'annulation
   *                               a échoué
   */
  @PUT
  @Path("annulerOffre/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public OffreDTO annulerOffre(@PathParam("id") int id) {
    //check id du token == id de l'offreur ?
    if (id <= 0) {
      throw new PresentationException("L'id de l'offre est incorrect", Status.BAD_REQUEST);
    }
    OffreDTO offreDTO = offreUCC.annulerOffre(id);
    if (offreDTO == null) {
      throw new PresentationException("L'annulation de l'offre a échoué", Status.BAD_REQUEST);
    }
    return offreDTO;
  }

  /**
   * Permet de voir les détails de l'offre avec l'id passé en paramètre.
   *
   * @param id : l'id de l'offre
   * @return offreDTO : les détails de l'offre avec l'id passé en paramètre
   * @throws PresentationException : est lancée si l'id de l'offre est invalide ou que l'offre n'a
   *                               pas pu être trouvée
   */
  @GET
  @Path("voirDetailsOffre/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public OffreDTO rechercheOffreParId(@PathParam("id") int id) {
    if (id <= 0) {
      throw new PresentationException("L'id de l'offre est incorrect", Status.BAD_REQUEST);
    }
    OffreDTO offreDTO = offreUCC.rechercheParId(id);
    if (offreDTO == null) {
      throw new PresentationException("L'offre n'a pas été trouvée", Status.BAD_REQUEST);
    }
    return offreDTO;
  }

  /**
   * Permet de voir les détails de l'offre avec l'id passé en paramètre.
   *
   * @param idObjet : l'id de l'objet
   * @return offreDTO : les détails de l'offre avec l'id passé en paramètre
   * @throws PresentationException : est lancée si l'id de l'offre est invalide ou que l'offre n'a
   *                               pas pu être trouvée
   */
  @GET
  @Path("offresPrecedentes/{idObjet}")
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public List<OffreDTO> offresPrecedentes(@PathParam("idObjet") int idObjet) {
    if (idObjet <= 0) {
      throw new PresentationException("L'id de l'offre est incorrect", Status.BAD_REQUEST);
    }
    List<OffreDTO> listeOffreDTO = offreUCC.offresPrecedentes(idObjet);
    if (listeOffreDTO == null) {
      throw new PresentationException("L'offre n'a pas été trouvée", Status.BAD_REQUEST);
    }
    return listeOffreDTO;
  }

  /**
   * Téléchargement de la photo.
   *
   * @param photo              : la photo à télécharger
   * @param fichierDisposition : informations à propos de la photo
   * @return response
   * @throws IOException : est lancée s'il y a eu un problème lors du téléchargement de la photo
   */
  @POST
  @Path("/telechargementPhoto")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.MULTIPART_FORM_DATA)
  @Autorisation
  public Response telechargerPhoto(@FormDataParam("photo") InputStream photo,
      @FormDataParam("photo") FormDataContentDisposition fichierDisposition) throws IOException {
    String nomDencodage = UUID.randomUUID().toString();
    Files.copy(photo, Paths.get(Config.getPropriete("OneDrivePhotos") + nomDencodage),
        StandardCopyOption.REPLACE_EXISTING);
    return Response.ok(nomDencodage).build();
  }

  /**
   * Voir la photo d'une offre.
   *
   * @param uuidPhoto : nom du fichier sur le serveur
   * @return une réponse contenant la photo
   */
  @GET
  @Path("/photos/{uuidPhoto}")
  @Produces({"image/*"})
  public Response voirPhotoOffre(@PathParam("uuidPhoto") String uuidPhoto) {
    return Response.ok(new File(Config.getPropriete("OneDrivePhotos") + uuidPhoto)).build();
  }

  /**
   * Modifie un ou plusieurs détails de l'offre.
   *
   * @param offreAvecModification : l'offre avec les modifications
   * @return l'offre modifiée
   * @throws PresentationException : est lancée s'il y a eu un problème lors de la modification de
   *                               l'offre
   */
  @PUT
  @Path("/modifierOffre")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public OffreDTO modifierOffre(OffreDTO offreAvecModification) {
    //check id du token == id de l'offreur ?

    if (offreAvecModification.getObjetDTO().getDescription().isBlank()
        || offreAvecModification.getPlageHoraire()
        // check de typeObjet ?? pas de modification mais pour être sur?
        .isBlank()) {
      throw new PresentationException("Des champs sont manquants", Status.BAD_REQUEST);
    }
    OffreDTO offreDTO = offreUCC.modifierOffre(offreAvecModification);
    return offreDTO;
  }
}

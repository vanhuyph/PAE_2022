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
    if (liste == null) {
      throw new PresentationException("L'offre n'a pas été trouvée", Status.BAD_REQUEST);
    }
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
    if (liste == null) {
      throw new PresentationException("L'offre n'a pas été trouvée", Status.BAD_REQUEST);
    }
    return liste;
  }

  /**
   * Annule une offre.
   *
   * @param offreDTO : l'offre a annulé
   * @return offreDTO : l'offre annulée
   * @throws PresentationException : est lancée si l'id de l'offre est invalide ou que l'annulation
   *                               a échoué
   */
  @PUT
  @Path("annulerOffre")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public OffreDTO annulerOffre(OffreDTO offreDTO) {
    if (offreDTO.getIdOffre() <= 0) {
      throw new PresentationException("L'id de l'offre est incorrect", Status.BAD_REQUEST);
    }
    offreDTO = offreUCC.annulerOffre(offreDTO);
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
   * Indique un membre receveur et changer l'état de l'objet en confirmé.
   *
   * @param offreDTO : l'offre pour laquelle on va mettre à jour
   * @return offreDTO : l'offre annulée
   * @throws PresentationException : est lancée si l'id de l'offre est invalide ou que l'ajout d'un
   *                               receveur a échoué
   */
  @PUT
  @Path("indiquerMembreReceveur")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public OffreDTO indiquerMembreReceveur(OffreDTO offreDTO) {
    if (offreDTO.getIdOffre() <= 0
        || offreDTO.getObjetDTO().getReceveur().getIdUtilisateur() <= 0) {
      throw new PresentationException("L'id de l'offre ou de l'utilisateur est incorrect",
          Status.BAD_REQUEST);
    }
    offreDTO = offreUCC.indiquerMembreReceveur(offreDTO);
    if (offreDTO == null) {
      throw new PresentationException("L'ajout d'un receveur a échoué", Status.BAD_REQUEST);
    }
    return offreDTO;
  }

  /**
   * donner une offre.
   *
   * @param offreDTO : l'offre a donner
   * @return offreDTO : l'offre donner
   * @throws PresentationException : est lancée si l'id de l'offre est invalide ou que la donation a
   *                               échoué
   */
  @PUT
  @Path("donnerOffre")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  //@Autorisation
  public OffreDTO donnerOffre(OffreDTO offreDTO) {
    if (offreDTO.getIdOffre() <= 0
        || offreDTO.getObjetDTO().getReceveur().getIdUtilisateur() <= 0 ||
        offreDTO.getObjetDTO().getIdObjet() <= 0) {
      throw new PresentationException("L'id de l'offre ou de l'objet est incorrect ou qu'il n'y a"
          + " pas de receveur"
          , Status.BAD_REQUEST);
    }
    offreDTO = offreUCC.donnerOffre(offreDTO);
    if (offreDTO == null) {
      throw new PresentationException("La donation de l'offre a échoué", Status.BAD_REQUEST);
    }
    return offreDTO;
  }

  /**
   * Liste les offres.
   *
   * @param idUtilisateur : l'utilisateur pour lequel on cherche ces offres
   * @return liste : la liste de ces offres
   */
  @GET
  @Path("/mesOffres/{idUtilisateur}")
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public List<OffreDTO> mesOffres(@PathParam("idUtilisateur") int idUtilisateur) {
    if (idUtilisateur <= 0) {
      throw new PresentationException("L'id de l'utilisateur est incorrect", Status.BAD_REQUEST);
    }
    List<OffreDTO> liste = offreUCC.mesOffres(idUtilisateur);
    if (liste == null) {
      throw new PresentationException("L'offre n'a pas été trouvée", Status.BAD_REQUEST);
    }
    return liste;
  }

}

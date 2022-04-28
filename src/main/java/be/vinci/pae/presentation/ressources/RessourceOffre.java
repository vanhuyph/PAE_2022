package be.vinci.pae.presentation.ressources;

import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.business.offre.OffreUCC;
import be.vinci.pae.presentation.ressources.filtres.Autorisation;
import be.vinci.pae.utilitaires.Config;
import be.vinci.pae.utilitaires.exceptions.PresentationException;
import com.fasterxml.jackson.databind.JsonNode;
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
import java.time.LocalDate;
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
   * Réoffrir l'objet.
   *
   * @param offreDTO : l'offre précédente avec l'état annulé.
   * @return offreDTO : l'offre recréée.
   * @throws PresentationException : est lancée s'il y a eu un problème lors de la création d'une
   *                               offre
   */
  @POST
  @Path("reoffrirObjet")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public OffreDTO reoffrirObjet(OffreDTO offreDTO) {
    if (offreDTO.getObjetDTO().getDescription().isBlank()
        || offreDTO.getObjetDTO().getTypeObjet() == null || offreDTO.getPlageHoraire().isBlank()) {
      throw new PresentationException("Des champs sont manquants", Status.BAD_REQUEST);
    }
    offreDTO = offreUCC.reoffrirObjet(offreDTO);
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
   * @param offreDTO : l'offre à annuler
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
      throw new PresentationException("L'annulation de l'offre a échouée", Status.BAD_REQUEST);
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
    String nomFichier =
        nomDencodage + "." + fichierDisposition.getFileName().split("\\.(?=[^\\.]+$)")[1];
    Files.copy(photo, Paths.get(Config.getPropriete("OneDrivePhotos") + nomFichier),
        StandardCopyOption.REPLACE_EXISTING);
    return Response.ok(nomFichier).build();
  }

  /**
   * Permet de voir la photo d'une offre.
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
    if (offreAvecModification.getObjetDTO().getDescription().isBlank()
        || offreAvecModification.getPlageHoraire().isBlank()) {
      throw new PresentationException("Des champs sont manquants", Status.BAD_REQUEST);
    }
    return offreUCC.modifierOffre(offreAvecModification);
  }

  /**
   * Liste toutes les offres en fonction d'un critère de recherche (nom, type, état et période).
   *
   * @param json : le json envoyé depuis le formulaire de recherche
   * @return liste : la liste des offres correspondante au critère de recherche
   */
  @POST
  @Path("recherche")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public List<OffreDTO> rechercherOffres(JsonNode json) {
    String recherche = json.get("recherche").asText();
    LocalDate dateDebut = LocalDate.parse(json.get("dateDebut").asText());
    LocalDate dateFin = LocalDate.parse(json.get("dateFin").asText());
    List<OffreDTO> liste;
    liste = offreUCC.rechercherOffre(recherche, dateDebut, dateFin);
    return liste;
  }

  /**
   * Permet de donner une offre.
   *
   * @param offreDTO : l'offre à donné
   * @return offre : l'offre donnée
   * @throws PresentationException : est lancée si l'id de l'offre/objet est incorrect ou qu'il n'y
   *                               a pas de receveur ou que la donation de l'offre a échoué
   */
  @PUT
  @Path("donnerOffre")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public OffreDTO donnerOffre(OffreDTO offreDTO) {
    if (offreDTO.getIdOffre() <= 0 || offreDTO.getObjetDTO().getIdObjet() <= 0) {
      throw new PresentationException("L'id de l'offre ou de l'objet est incorrect ou qu'il n'y a"
          + " pas de receveur", Status.BAD_REQUEST);
    }
    offreDTO = offreUCC.donnerOffre(offreDTO);
    if (offreDTO == null) {
      throw new PresentationException("La donation de l'offre a échoué", Status.BAD_REQUEST);
    }
    return offreDTO;
  }

  /**
   * Liste les propres offres de l'utilisateur dont l'id est passé en paramètre.
   *
   * @param idUtilisateur : l'id de l'utilisateur à qui lister ses offres
   * @return liste : la liste de toutes ses propres offres
   * @throws PresentationException : est lancée si l'id de l'utilisateur est incorrect ou que
   *                               l'offre n'a pas été trouvée
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

  /**
   * Liste les offres qui ont été attribuées à l'utilisateur dont l'id est passé en paramètre.
   *
   * @param idUtilisateur : l'id de l'utilisateur à qui lister ses offres attribuées
   * @return liste : la liste de toutes ses offres attribuées
   * @throws PresentationException : est lancée si l'id de l'utilisateur est incorrect ou que
   *                               l'offre n'a pas été trouvée
   */
  @GET
  @Path("/voirOffreAttribuer/{idUtilisateur}")
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public List<OffreDTO> voirOffreAttribuer(@PathParam("idUtilisateur") int idUtilisateur) {
    if (idUtilisateur <= 0) {
      throw new PresentationException("L'id de l'utilisateur est incorrect", Status.BAD_REQUEST);
    }
    List<OffreDTO> liste = offreUCC.voirOffreAttribuer(idUtilisateur);
    if (liste == null) {
      throw new PresentationException("L'offre n'a pas été trouvée", Status.BAD_REQUEST);
    }
    return liste;
  }

  /**
   * Recupere tous les objets qui doivent etre évalués par un utilisateur.
   *
   * @param idUtilisateur : id de l'utilisateur
   * @return objetsAEvaluer : la liste des objets que l'utilisateur doit évaluer
   */
  @GET
  @Path("/objetsAEvaluer/{idUtilisateur}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public List<ObjetDTO> objetsAEvaluerParUtilisateur(
      @PathParam("idUtilisateur") int idUtilisateur) {
    List<ObjetDTO> objetsAEvaluer;
    objetsAEvaluer = offreUCC.objetsAEvaluerParUtilisateur(idUtilisateur);

    return objetsAEvaluer;
  }

  /**
   * Recupere tous les objets offerts d'un utilisateur.
   *
   * @param idUtilisateur : id de l'utilisateur
   * @return objets : la liste des objets que l'utilisateur doit évaluer
   */
  @GET
  @Path("/objetsOfferts/{idUtilisateur}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public List<OffreDTO> objetsOffertsUtilisateur(@PathParam("idUtilisateur") int idUtilisateur) {
    List<OffreDTO> objets = null;
    objets = offreUCC.objetsOffertsUtilisateur(idUtilisateur);
    return objets;
  }

  /**
   * Recupere tous les objets reçus d'un utilisateur.
   *
   * @param idUtilisateur : id de l'utilisateur
   * @return objets : la liste des objets que l'utilisateur doit évaluer
   */
  @GET
  @Path("/objetsRecus/{idUtilisateur}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  //@Autorisation
  public List<OffreDTO> objetsRecusUtilisateur(@PathParam("idUtilisateur") int idUtilisateur) {
    List<OffreDTO> objets = null;
    objets = offreUCC.objetsRecusUtilisateur(idUtilisateur);
    return objets;
  }
}

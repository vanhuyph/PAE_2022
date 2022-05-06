package be.vinci.pae.presentation.ressources;

import be.vinci.pae.business.interet.InteretDTO;
import be.vinci.pae.business.interet.InteretUCC;
import be.vinci.pae.presentation.ressources.filtres.Autorisation;
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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;

@Singleton
@Path("/interets")
public class RessourceInteret {

  @Inject
  private InteretUCC interetUCC;

  /**
   * Créer un intérêt pour une offre.
   *
   * @param interetDTO : json envoyé par le formulaire de créer un intérêt
   * @return interet : interetDTO
   * @throws PresentationException : est lancée s'il y a eu une erreur
   */
  @POST
  @Path("creerInteret")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public InteretDTO creerInteret(InteretDTO interetDTO) {
    if (interetDTO.getObjet() == null || interetDTO.getUtilisateur() == null
        || interetDTO.getObjet().getIdObjet() < 1
        || interetDTO.getUtilisateur().getIdUtilisateur() < 1) {
      throw new PresentationException("Objet ou utilisateur manquant", Status.BAD_REQUEST);
    }
    if (interetDTO.getDateRdv() == null) {
      throw new PresentationException("Date de disponibilité manquante", Status.BAD_REQUEST);
    }
    InteretDTO interet = interetUCC.creerUnInteret(interetDTO);
    if (interet == null) {
      throw new PresentationException("L'ajout de l'intérêt a échoué", Status.BAD_REQUEST);
    }
    return interet;
  }

  /**
   * Récupère le nombre de personnes intéressées pour une offre.
   *
   * @param idObjet : l'id de l'objet dont les personnes sont intéressées
   * @return nbInteret : le nombre de personnes intéressées
   * @throws PresentationException : est lancée si l'id de l'objet est incorrect
   */
  @GET
  @Path("nbPersonnesInteresees/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public int nbPersonnesInteresees(@PathParam("id") int idObjet) {
    if (idObjet <= 0) {
      throw new PresentationException("L'id de l'objet est incorrect", Status.BAD_REQUEST);
    }
    int nbInteret = interetUCC.nbPersonnesInteressees(idObjet);
    return nbInteret;
  }

  /**
   * Récupère l'intérêt de l'utilisateur pour un objet.
   *
   * @param idObjet       : l'id de l'objet
   * @param idUtilisateur : l'id de l'utilisateur
   * @return interet : l'interet trouvé, sinon null
   * @throws PresentationException : est lancée si l'id de l'objet est incorrect
   */
  @GET
  @Path("interetUtilisateurPourObjet/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public InteretDTO interetUtilisateurPourObjet(@PathParam("id") int idObjet,
      @QueryParam("utilisateur") int idUtilisateur) {
    if (idObjet <= 0) {
      throw new PresentationException("L'id de l'objet est incorrect", Status.BAD_REQUEST);
    }
    return interetUCC.interetUtilisateurPourObjet(idObjet, idUtilisateur);
  }

  /**
   * Liste les intérêts pour l'objet dont l'id est passé en paramètre.
   *
   * @param idObjet : l'id de l'objet dont les personnes sont intéressées
   * @return liste : la liste des intérêts pour l'objet
   * @throws PresentationException : est lancée si l'id de l'objet est incorrect
   */
  @GET
  @Path("/listeDesPersonnesInteressees/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public List<InteretDTO> listeDesPersonnesInteressees(@PathParam("id") int idObjet) {
    if (idObjet <= 0) {
      throw new PresentationException("L'id de l'objet est incorrect", Status.BAD_REQUEST);
    }
    List<InteretDTO> liste = interetUCC.listeDesPersonnesInteressees(idObjet);
    return liste;
  }

  /**
   * Liste les intérêts non-vues pour l'objet d'un utilisateur dont l'id est passé en paramètre.
   *
   * @param idOffreur : l'id de l'offreur d'un objet dont les personnes sont intéressées
   * @return liste : la liste des intérêts non-vues
   * @throws PresentationException : est lancée si l'id de l'objet est incorrect
   */
  @GET
  @Path("/listeDesPersonnesInteresseesVue/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public List<InteretDTO> listeDesPersonnesInteresseesVue(@PathParam("id") int idOffreur) {
    if (idOffreur <= 0) {
      throw new PresentationException("L'id de l'objet est incorrect", Status.BAD_REQUEST);
    }
    List<InteretDTO> liste = interetUCC.listeDesPersonnesInteresseesVue(idOffreur);
    return liste;
  }

  /**
   * Permet d'indiquer à une personne intéressée comme étant receveur de l'objet.
   *
   * @param interet : l'intérêt de la personne
   * @return interetDTO : interetDTO
   * @throws PresentationException : est lancée si objet/utilisateur incorrect
   */
  @PUT
  @Path("/indiquerReceveur")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public InteretDTO indiquerReceveur(InteretDTO interet) {
    if (interet.getObjet().getIdObjet() < 1 || interet.getUtilisateur().getIdUtilisateur() < 1) {
      throw new PresentationException("Objet ou utilisateur incorrect", Status.BAD_REQUEST);
    }
    interet = interetUCC.indiquerReceveur(interet);
    return interet;
  }

  /**
   * Permet d'indiquer que l'objet a été non remis car le receveur n'est pas venu chercher l'objet.
   *
   * @param idObjet : l'id de l'objet à mettre en non remis
   * @return interetDTO : interetDTO
   */
  @PUT
  @Path("/nonRemis/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public InteretDTO nonRemis(@PathParam("id") int idObjet) {
    InteretDTO interetDTO = interetUCC.nonRemis(idObjet);
    return interetDTO;
  }

  /**
   * Permet de notifier le receveur actuel de l'objet que l'offreur a eu un empêchement.
   *
   * @param idUtilisateur : l'id du receveur qui va recevoir la notification
   * @return liste : la liste des notifications d'empêchements
   * @throws PresentationException : est lancée si l'id de l'utilisateur est incorrect
   */
  @GET
  @Path("/notifierReceveurEmpecher/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public List<InteretDTO> notifierReceveurEmpecher(@PathParam("id") int idUtilisateur) {
    if (idUtilisateur <= 0) {
      throw new PresentationException("L'id de l'utilisateur est incorrect", Status.BAD_REQUEST);
    }
    return interetUCC.notifierReceveurEmpecher(idUtilisateur);
  }

}

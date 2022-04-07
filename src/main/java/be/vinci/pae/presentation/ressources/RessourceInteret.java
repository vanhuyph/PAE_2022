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
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/interets")
public class RessourceInteret {

  @Inject
  private InteretUCC interetUCC;

  /**
   * Créer un intérêt pour une offre.
   *
   * @param interetDTO : json envoyé par le formulaire de créer un interet
   * @return interet : interetDTO
   * @throws PresentationException : est lancée s'il y a eu une erreur
   */
  @POST
  @Path("creerInteret")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public InteretDTO creetInteret(InteretDTO interetDTO) {
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
   * @param id : l'id de l'offre dont les personnes sont intéressées
   * @return nbInteret : le nombre de personnes intéressées
   * @throws PresentationException : est lancée si l'id de l'offre est incorrect
   */
  @GET
  @Path("nbPersonnesInteresees/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public int nbPersonnesInteresees(@PathParam("id") int id) {
    if (id <= 0) {
      throw new PresentationException("L'id de l'offre est incorrect", Status.BAD_REQUEST);
    }
    int nbInteret = interetUCC.nbPersonnesInteressees(id);
    return nbInteret;
  }

}

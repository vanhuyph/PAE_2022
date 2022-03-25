package be.vinci.pae.presentation.ressources;

import be.vinci.pae.business.interet.InteretDTO;
import be.vinci.pae.business.interet.InteretUCC;
import be.vinci.pae.presentation.ressources.filtres.Autorisation;
import be.vinci.pae.utilitaires.exceptions.PresentationException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import java.text.ParseException;

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
   * @throws Exception : est lancée si il y a eu un problème avec la date de rdv
   */
  @POST
  @Path("creerInteret")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public InteretDTO creetInteret(InteretDTO interetDTO) throws ParseException {

    InteretDTO interet = interetUCC.creerUnInteret(interetDTO);
    if (interet == null) {
      throw new PresentationException("L'ajout de l'intérêt à échoué", Status.BAD_REQUEST);
    }
    return interet;
  }

}
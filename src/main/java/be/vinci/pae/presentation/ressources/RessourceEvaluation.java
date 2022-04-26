package be.vinci.pae.presentation.ressources;

import be.vinci.pae.business.evaluation.EvaluationDTO;
import be.vinci.pae.business.evaluation.EvaluationUCC;
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

@Singleton
@Path("/evaluations")
public class RessourceEvaluation {

  @Inject
  private EvaluationUCC evaluationUCC;

  /**
   * Permet de créer une évaluation.
   *
   * @param evaluationDTO : l'evaluation a créer
   * @return l'évaluation créée
   */
  @POST
  @Path("/creerEvaluation")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public EvaluationDTO creerEvaluation(EvaluationDTO evaluationDTO) {
    if (evaluationDTO.getCommentaire().isBlank()) {
      throw new PresentationException("Commentaire manquant", Status.BAD_REQUEST);
    }
    if (evaluationDTO.getObjet().getIdObjet() < 1 || evaluationDTO.getObjet() == null) {
      throw new PresentationException("Objet manquant", Status.BAD_REQUEST);
    }
    EvaluationDTO evaluation = evaluationUCC.creerEvaluation(evaluationDTO);
    if (evaluation == null) {
      throw new PresentationException("la création a échoué ", Status.INTERNAL_SERVER_ERROR);
    }
    return evaluation;
  }

}

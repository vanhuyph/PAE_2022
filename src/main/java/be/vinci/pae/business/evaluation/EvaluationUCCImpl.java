package be.vinci.pae.business.evaluation;

import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.donnees.dao.evaluation.EvaluationDAO;
import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import jakarta.inject.Inject;

public class EvaluationUCCImpl implements EvaluationUCC {

  @Inject
  ServiceDAL serviceDAL;
  @Inject
  EvaluationDAO evaluationDAO;
  @Inject
  ObjetDAO objetDAO;

  /**
   * Permet de créer un eevaluation.
   *
   * @param evaluationDTO : l'évaluation a créer
   * @return l'évaluation créée
   */
  @Override
  public EvaluationDTO creerEvaluation(EvaluationDTO evaluationDTO) {
    serviceDAL.commencerTransaction();
    EvaluationDTO evaluation;
    try {
      ObjetDTO objetEvalue = objetDAO.rechercheParId(evaluationDTO.getObjet());
      if (objetEvalue == null) {
        throw new BusinessException("l'objet n'existe pas");
      }
      //extraire dans une méthode dans evaluation et evaluationImpl
      if (!objetEvalue.getEtatObjet().equals("donné")) {
        throw new BusinessException("l'objet n'est pas prêt");
      }
      evaluation = evaluationDAO.creerEvaluation(evaluationDTO);
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();

    return evaluation;
  }
}

package be.vinci.pae.business.evaluation;

import be.vinci.pae.business.objet.Objet;
import be.vinci.pae.donnees.dao.evaluation.EvaluationDAO;
import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import be.vinci.pae.utilitaires.exceptions.OptimisticLockException;
import be.vinci.pae.utilitaires.exceptions.PasTrouveException;
import jakarta.inject.Inject;

public class EvaluationUCCImpl implements EvaluationUCC {

  @Inject
  ServiceDAL serviceDAL;
  @Inject
  EvaluationDAO evaluationDAO;
  @Inject
  ObjetDAO objetDAO;

  /**
   * Permet de créer une évaluation.
   *
   * @param evaluationDTO : l'évaluation a créer
   * @return evaluation : l'évaluation créée
   * @throws BusinessException       : est lancée si l'objet n'est pas dans un état permettant
   *                                 d'être évalué
   * @throws PasTrouveException      : est lancée si l'objet n'existe pas
   * @throws OptimisticLockException : est lancée si les données sont périmées
   */
  @Override
  public EvaluationDTO creerEvaluation(EvaluationDTO evaluationDTO) {
    serviceDAL.commencerTransaction();
    EvaluationDTO evaluation;
    try {
      Objet objetEvalue = (Objet) objetDAO.rechercheParId(evaluationDTO.getObjet());
      if (objetEvalue == null) {
        throw new PasTrouveException("L'objet n'existe pas");
      }
      if (!objetEvalue.peutEtreEvalue()) {
        throw new BusinessException("l'objet n'est pas prêt a être évalué ou a déjà été évalué");
      }
      if (evaluationDTO.getNote() > -1 && evaluationDTO.getNote() < 6) {
        objetEvalue.estEvalue();
        if (objetDAO.miseAJourObjet(objetEvalue) == null) {
          throw new OptimisticLockException("Données périmées");
        }
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

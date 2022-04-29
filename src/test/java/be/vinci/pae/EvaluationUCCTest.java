package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.evaluation.EvaluationDTO;
import be.vinci.pae.business.evaluation.EvaluationUCC;
import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.donnees.dao.evaluation.EvaluationDAO;
import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import be.vinci.pae.utilitaires.exceptions.PasTrouveException;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;

@TestInstance(Lifecycle.PER_CLASS)
public class EvaluationUCCTest {

  private DomaineFactory domaineFactory;
  private EvaluationDAO evaluationDAO;
  private EvaluationUCC evaluationUCC;
  private EvaluationDTO evaluationDTO;
  private ObjetDAO objetDAO;
  private ObjetDTO objetDTO;

  @BeforeAll
  void initTout() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new MockApplicationBinder());
    this.domaineFactory = locator.getService(DomaineFactory.class);
    this.evaluationDAO = locator.getService(EvaluationDAO.class);
    this.evaluationUCC = locator.getService(EvaluationUCC.class);
    this.objetDAO = locator.getService(ObjetDAO.class);
    objetDTO = domaineFactory.getObjet();
    evaluationDTO = domaineFactory.getEvaluation();
    objetDTO.setIdObjet(1);
    evaluationDTO.setIdEvaluation(1);
    evaluationDTO.setObjet(objetDTO);
    evaluationDTO.setCommmentaire("");
  }

  @Test
  @DisplayName("Test raté : méthode creerEvaluation raté car l'objet n'existe pas.")
  public void testCreerEvaluationV1() {
    Mockito.when(objetDAO.rechercheParId(evaluationDTO.getObjet())).thenReturn(null);
    assertThrows(PasTrouveException.class, () -> evaluationUCC.creerEvaluation(evaluationDTO));
  }

  @Test
  @DisplayName("Test raté : méthode creerEvaluation raté car l'objet n'est pas prêt à être évalué.")
  public void testCreerEvaluationV2() {
    Mockito.when(objetDAO.rechercheParId(evaluationDTO.getObjet()))
        .thenReturn(evaluationDTO.getObjet());
    assertThrows(BusinessException.class, () -> evaluationUCC.creerEvaluation(evaluationDTO));
  }

  @Test
  @DisplayName("Test réussi : méthode creerEvaluation renvoie bien l'évaluation créée.")
  public void testCreerEvaluationV3() {
    objetDTO.setEtatObjet("Donné");
    Mockito.when(objetDAO.rechercheParId(evaluationDTO.getObjet()))
        .thenReturn(evaluationDTO.getObjet());
    Mockito.when(objetDAO.miseAJourObjet(objetDTO)).thenReturn(objetDTO);
    Mockito.when(evaluationDAO.creerEvaluation(evaluationDTO)).thenReturn(evaluationDTO);
    assertEquals(evaluationDTO, evaluationUCC.creerEvaluation(evaluationDTO));
  }

}

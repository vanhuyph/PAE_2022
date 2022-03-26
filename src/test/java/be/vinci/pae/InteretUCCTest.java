package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.interet.InteretDTO;
import be.vinci.pae.business.interet.InteretUCC;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.donnees.dao.interet.InteretDAO;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;

@TestInstance(Lifecycle.PER_CLASS)
public class InteretUCCTest {

  private DomaineFactory domaineFactory;
  private InteretDAO interetDAO;
  private InteretDTO interetDTO;
  private InteretUCC interetUCC;
  private UtilisateurDTO utilisateurDTO;

  @BeforeAll
  void initTout() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new MockApplicationBinder());
    this.domaineFactory = locator.getService(DomaineFactory.class);
    this.interetDAO = locator.getService(InteretDAO.class);
    this.interetUCC = locator.getService(InteretUCC.class);
    interetDTO = domaineFactory.getInteret();
    utilisateurDTO = domaineFactory.getUtilisateur();
    interetDTO.setUtilisateur(utilisateurDTO);
  }

  //  @Test
  //  @DisplayName("Test raté : méthode creerUnInteret avec une date invalide.")
  //  public void testCreerUnInteretV1() {
  //
  //  }
  //
  //  @Test
  //  @DisplayName("Test réussi : méthode creerUnInteret TBD")
  //  public void testCreerUnInteretV2() {
  //
  //  }

  @Test
  @DisplayName("Test raté : méthode nbPersonnesInteressees renvoie -1 car l'objet est introuvable.")
  public void testNbPersonnesInteresseesV1() {
    int id = -1;
    Mockito.when(interetDAO.nbPersonnesInteressees(id)).thenReturn(-1);
    assertThrows(BusinessException.class, () -> interetUCC.nbPersonnesInteressees(id));
  }

  @Test
  @DisplayName("Test réussi : méthode nbPersonnesInteressees renvoie 1 personne intéressée pour "
      + "l'objet avec l'id 1.")
  public void testNbPersonnesInteresseesV2() {
    int id = 1;
    Mockito.when(interetDAO.nbPersonnesInteressees(id)).thenReturn(1);
    assertEquals(1, interetUCC.nbPersonnesInteressees(id));
  }

}

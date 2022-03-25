package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;

import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.business.offre.OffreUCC;
import be.vinci.pae.donnees.dao.offre.OffreDAO;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;

@TestInstance(Lifecycle.PER_CLASS)
public class OffreUCCTest {

  private OffreDAO offreDAO;
  private OffreUCC offreUCC;

  @BeforeAll
  void initTout() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new MockApplicationBinder());
    this.offreDAO = locator.getService(OffreDAO.class);
    this.offreUCC = locator.getService(OffreUCC.class);
  }

  @Test
  @DisplayName("Test réussi : méthode listerOffres renvoie bien une liste")
  public void testListerOffresV1() {
    List<OffreDTO> liste = new ArrayList<>();
    Mockito.when(offreDAO.listerOffres()).thenReturn(liste);
    assertEquals(liste, offreUCC.listerOffresRecentes());
  }

  @Test
  @DisplayName("Test réussi : méthode listerOffresRecentes renvoie bien une liste")
  public void testListerOffresRecentesV1() {
    List<OffreDTO> liste = new ArrayList<>();
    Mockito.when(offreDAO.listerOffresRecentes()).thenReturn(liste);
    assertEquals(liste, offreUCC.listerOffresRecentes());
  }

}

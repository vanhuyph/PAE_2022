package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;

import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.business.offre.OffreUCC;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class OffreUCCTest {

  private OffreUCC offreUCC;


  @BeforeAll
  void initTout() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new MockApplicationBinder());
    this.offreUCC = locator.getService(OffreUCC.class);

  }

  @Test
  @DisplayName("Test reussi : méthode listOffresRecent renvoie bien une liste")
  public void testlistOffresRecent() {
    List<OffreDTO> listOffre = new ArrayList<>();
    assertEquals(offreUCC.listOffresRecent(), listOffre);
  }


}

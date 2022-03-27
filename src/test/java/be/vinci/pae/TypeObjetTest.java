package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;

import be.vinci.pae.business.typeobjet.TypeObjetDTO;
import be.vinci.pae.business.typeobjet.TypeObjetUCC;
import be.vinci.pae.donnees.dao.typeobjet.TypeObjetDAO;
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
public class TypeObjetTest {

  private TypeObjetDAO typeObjetDAO;
  private TypeObjetUCC typeObjetUCC;

  @BeforeAll
  void initTout() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new MockApplicationBinder());
    this.typeObjetDAO = locator.getService(TypeObjetDAO.class);
    this.typeObjetUCC = locator.getService(TypeObjetUCC.class);
  }

  @Test
  @DisplayName("Test réussi : méthode listerTypeObjet renvoie bien une liste.")
  public void testListerTypeObjetV1() {
    List<TypeObjetDTO> liste = new ArrayList<>();
    Mockito.when(typeObjetDAO.listerTypeObjet()).thenReturn(liste);
    assertEquals(liste, typeObjetUCC.listerTypeObjet());
  }

}

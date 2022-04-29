package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.typeobjet.TypeObjetDTO;
import be.vinci.pae.business.typeobjet.TypeObjetUCC;
import be.vinci.pae.donnees.dao.typeobjet.TypeObjetDAO;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
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
public class TypeObjetUCCTest {

  private TypeObjetDAO typeObjetDAO;
  private TypeObjetUCC typeObjetUCC;
  private TypeObjetDTO typeObjetDTO1;
  private TypeObjetDTO typeObjetDTO2;
  private TypeObjetDTO typeObjetDTO3;

  @BeforeAll
  void initTout() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new MockApplicationBinder());
    DomaineFactory domaineFactory = locator.getService(DomaineFactory.class);
    this.typeObjetDAO = locator.getService(TypeObjetDAO.class);
    this.typeObjetUCC = locator.getService(TypeObjetUCC.class);
    typeObjetDTO1 = domaineFactory.getTypeObjet();
    typeObjetDTO1.setIdType(0);
    typeObjetDTO2 = domaineFactory.getTypeObjet();
    typeObjetDTO2.setIdType(5);
    typeObjetDTO3 = domaineFactory.getTypeObjet();
    typeObjetDTO3.setIdType(13);
  }

  @Test
  @DisplayName("Test réussi : méthode listerTypeObjet renvoie bien une liste.")
  public void testListerTypeObjetV1() {
    List<TypeObjetDTO> liste = new ArrayList<>();
    Mockito.when(typeObjetDAO.listerTypeObjet()).thenReturn(liste);
    assertEquals(liste, typeObjetUCC.listerTypeObjet());
  }

  @Test
  @DisplayName("Test raté : méthode creerTypeObjet raté car le type d'objet existe déjà.")
  public void testCreerTypeObjetV1() {
    Mockito.when(typeObjetDAO.verifierUniqueTypeObjet(typeObjetDTO1)).thenReturn(typeObjetDTO2);
    assertThrows(BusinessException.class, () -> typeObjetUCC.creerTypeObjet(typeObjetDTO1));
  }

  @Test
  @DisplayName("Test réussi : méthode creerTypeObjet renvoie bien le type d'objet créé.")
  public void testCreerTypeObjetV2() {
    Mockito.when(typeObjetDAO.verifierUniqueTypeObjet(typeObjetDTO1)).thenReturn(typeObjetDTO1);
    Mockito.when(typeObjetDAO.creerTypeObjet(typeObjetDTO1)).thenReturn(typeObjetDTO3);
    assertEquals(typeObjetDTO3, typeObjetUCC.creerTypeObjet(typeObjetDTO1));
  }

}

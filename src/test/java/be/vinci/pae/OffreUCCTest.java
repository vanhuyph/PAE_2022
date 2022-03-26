package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.business.offre.OffreUCC;
import be.vinci.pae.business.typeobjet.TypeObjetDTO;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import be.vinci.pae.donnees.dao.offre.OffreDAO;
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
public class OffreUCCTest {

  private OffreDAO offreDAO;
  private OffreUCC offreUCC;
  private DomaineFactory domaineFactory;
  private OffreDTO offreDTO1;
  private OffreDTO offreDTO2;
  private ObjetDTO objetDTO1;
  private ObjetDTO objetDTO2;
  private ObjetDAO objetDAO;
  private TypeObjetDTO typeObjetDTO;
  private UtilisateurDTO utilisateurDTO;

  @BeforeAll
  void initTout() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new MockApplicationBinder());
    this.offreDAO = locator.getService(OffreDAO.class);
    this.offreUCC = locator.getService(OffreUCC.class);

    this.domaineFactory = locator.getService(DomaineFactory.class);
    this.offreDAO = locator.getService(OffreDAO.class);
    this.objetDAO = locator.getService(ObjetDAO.class);

    typeObjetDTO = domaineFactory.getTypeObjet();
    typeObjetDTO.setIdType(1);

    utilisateurDTO = domaineFactory.getUtilisateur();
    utilisateurDTO.setIdUtilisateur(1);

    objetDTO1 = domaineFactory.getObjet();
    objetDTO1.setTypeObjet(typeObjetDTO);
    objetDTO1.setDescription("testDescription");
    objetDTO1.setOffreur(utilisateurDTO);
    objetDTO1.setPhoto("testPhoto");

    objetDTO2 = domaineFactory.getObjet();
    objetDTO2.setIdObjet(1);

    offreDTO1 = domaineFactory.getOffre();
    offreDTO1.setPlageHoraire("testPlageHoraire");
    offreDTO1.setObjetDTO(objetDTO1);
    offreDTO2 = domaineFactory.getOffre();

    offreDTO2 = domaineFactory.getOffre();
    offreDTO2.setIdOffre(1);
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

  @Test
  @DisplayName("Test raté : méthode creer une offre avec un créer un objet qui rate. ")
  public void creerUneOffreV1() {
    Mockito.when(objetDAO.creerObjet(objetDTO1)).thenReturn(null);
    assertThrows(BusinessException.class, () -> offreUCC.creerUneOffre(offreDTO1));
  }

  @Test
  @DisplayName("Test raté : méthode creer une offre avec créer offre qui rate. ")
  public void creerUneOffreV2() {
    Mockito.when(objetDAO.creerObjet(objetDTO1)).thenReturn(objetDTO2);
    Mockito.when(offreDAO.creerOffre(offreDTO1)).thenReturn(null);
    assertThrows(BusinessException.class, () -> offreUCC.creerUneOffre(offreDTO1));
  }

  //Test qui crash sur jenkins mais fonctionne en local
  //@Test
  //@DisplayName("Test réussi : méthode creer une offre. ")
  //public void creerUneOffreV3() {

  //  Mockito.when(objetDAO.creerObjet(objetDTO1)).thenReturn(objetDTO2);
  //  Mockito.when(offreDAO.creerOffre(offreDTO1)).thenReturn(offreDTO2);
  //  assertEquals(offreDTO2, offreUCC.creerUneOffre(offreDTO1));
  //}

  @Test
  @DisplayName("Test raté : méthode annuler une offre qui rate. ")
  public void annulerOffreV1() {
    Mockito.when(offreDAO.annulerOffre(1)).thenReturn(null);
    assertThrows(BusinessException.class, () -> offreUCC.annulerUneOffre(1));
  }

  @Test
  @DisplayName("Test réussi : méthode annuler une offre. ")
  public void annulerOffreV2() {
    Mockito.when(offreDAO.annulerOffre(1)).thenReturn(offreDTO2);
    assertEquals(offreDTO2, offreUCC.annulerUneOffre(1));
  }

  @Test
  @DisplayName("Test raté : méthode recherche par id qui rate. ")
  public void rechercheParIdV1() {
    Mockito.when(offreDAO.rechercheParId(1)).thenReturn(null);
    assertThrows(BusinessException.class, () -> offreUCC.rechercheParId(1));
  }

  @Test
  @DisplayName("Test réussi : méthode recherche par id. ")
  public void rechercheParIdV2() {
    Mockito.when(offreDAO.rechercheParId(1)).thenReturn(offreDTO2);
    assertEquals(offreDTO2, offreUCC.rechercheParId(1));
  }

}

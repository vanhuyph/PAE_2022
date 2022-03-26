package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.business.offre.OffreUCC;
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
  private UtilisateurDTO utilisateurDTO;

  @BeforeAll
  void initTout() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new MockApplicationBinder());
    this.domaineFactory = locator.getService(DomaineFactory.class);
    this.offreDAO = locator.getService(OffreDAO.class);
    this.offreUCC = locator.getService(OffreUCC.class);
    this.offreDAO = locator.getService(OffreDAO.class);
    this.objetDAO = locator.getService(ObjetDAO.class);

    utilisateurDTO = domaineFactory.getUtilisateur();
    utilisateurDTO.setIdUtilisateur(1);

    objetDTO1 = domaineFactory.getObjet();
    objetDTO1.setDescription("testDescription");
    objetDTO1.setOffreur(utilisateurDTO);
    objetDTO1.setPhoto("testPhoto");

    objetDTO2 = domaineFactory.getObjet();
    objetDTO2.setIdObjet(1);

    offreDTO1 = domaineFactory.getOffre();
    offreDTO1.setIdOffre(1);
    offreDTO1.setPlageHoraire("testPlageHoraire");

    offreDTO2 = domaineFactory.getOffre();
    offreDTO2.setIdOffre(2);
  }

  @Test
  @DisplayName("Test raté : méthode creerUneOffre renvoie null.")
  public void creerUneOffreV1() {
    Mockito.when(objetDAO.creerObjet(objetDTO1)).thenReturn(null);
    assertThrows(BusinessException.class, () -> offreUCC.creerUneOffre(offreDTO1));
  }

  @Test
  @DisplayName("Test raté : méthode creerUneOffre avec créer offre qui rate.")
  public void creerUneOffreV2() {
    Mockito.when(objetDAO.creerObjet(objetDTO1)).thenReturn(objetDTO2);
    Mockito.when(offreDAO.creerOffre(offreDTO1)).thenReturn(null);
    assertThrows(BusinessException.class, () -> offreUCC.creerUneOffre(offreDTO1));
  }

  @Test
  @DisplayName("Test réussi : méthode listerOffres renvoie bien une liste.")
  public void testListerOffresV1() {
    List<OffreDTO> liste = new ArrayList<>();
    Mockito.when(offreDAO.listerOffres()).thenReturn(liste);
    assertEquals(liste, offreUCC.listerOffresRecentes());
  }

  @Test
  @DisplayName("Test réussi : méthode listerOffresRecentes renvoie bien une liste.")
  public void testListerOffresRecentesV1() {
    List<OffreDTO> liste = new ArrayList<>();
    Mockito.when(offreDAO.listerOffresRecentes()).thenReturn(liste);
    assertEquals(liste, offreUCC.listerOffresRecentes());
  }

  @Test
  @DisplayName("Test raté : méthode annulerUneOffre renvoie null car l'offre n'est pas trouvable.")
  public void annulerUneOffreV1() {
    int id = -1;
    Mockito.when(offreDAO.annulerOffre(id)).thenReturn(null);
    assertThrows(BusinessException.class, () -> offreUCC.annulerUneOffre(id));
  }

  @Test
  @DisplayName("Test réussi : méthode annulerUneOffre renvoie l'offre annulée.")
  public void annulerUneOffreV2() {
    int id = offreDTO1.getIdOffre();
    Mockito.when(offreDAO.annulerOffre(id)).thenReturn(offreDTO1);
    assertEquals(offreDTO1, offreUCC.annulerUneOffre(id));
  }

  @Test
  @DisplayName("Test raté : méthode rechercheParId renvoie null car l'offre n'est pas trouvable.")
  public void rechercheParIdV1() {
    int id = -1;
    Mockito.when(offreDAO.rechercheParId(id)).thenReturn(null);
    assertThrows(BusinessException.class, () -> offreUCC.rechercheParId(id));
  }

  @Test
  @DisplayName("Test réussi : méthode rechercheParId renvoie une offre existante.")
  public void rechercheParIdV2() {
    int id = offreDTO1.getIdOffre();
    Mockito.when(offreDAO.rechercheParId(id)).thenReturn(offreDTO1);
    assertEquals(offreDTO1, offreUCC.rechercheParId(id));
  }

  @Test
  @DisplayName("Test raté : méthode offresPrecedentes renvoie null car l'objet "
      + "n'est pas trouvable.")
  public void testOffresPrecedentesV1() {
    int id = -1;
    Mockito.when(offreDAO.offresPrecedentes(id)).thenReturn(null);
    assertThrows(BusinessException.class, () -> offreUCC.offresPrecedentes(id));
  }

  @Test
  @DisplayName("Test réussi : méthode offresPrecedentes renvoie bien une liste.")
  public void testOffresPrecedentesV2() {
    int id = offreDTO1.getIdOffre();
    List<OffreDTO> liste = new ArrayList<>();
    Mockito.when(offreDAO.offresPrecedentes(id)).thenReturn(liste);
    assertEquals(liste, offreUCC.offresPrecedentes(id));
  }

  @Test
  @DisplayName("Test raté : méthode creerObjet renvoie null car l'objet n'a pas été créé")
  public void testCreerUneOffreV1() {
    Mockito.when(objetDAO.creerObjet(objetDTO1)).thenReturn(null);
    assertThrows(BusinessException.class, () -> offreUCC.creerUneOffre(offreDTO1));
  }

  @Test
  @DisplayName("Test raté : méthode creerOffre renvoie null car l'offre n'a pas été créée")
  public void testCreerUneOffreV2() {
    Mockito.when(objetDAO.creerObjet(objetDTO1)).thenReturn(objetDTO1);
    Mockito.when(offreDAO.creerOffre(offreDTO1)).thenReturn(null);
    assertThrows(BusinessException.class, () -> offreUCC.creerUneOffre(offreDTO1));
  }

  @Test
  @DisplayName("Test réussi : méthode creerUneOffre renvoie l'offre créée")
  public void testCreerUneOffreV3() {
    offreDTO1.setObjetDTO(objetDTO1);
    Mockito.when(objetDAO.creerObjet(offreDTO1.getObjetDTO())).thenReturn(objetDTO1);
    Mockito.when(offreDAO.creerOffre(offreDTO1)).thenReturn(offreDTO1);
    assertEquals(offreDTO1, offreUCC.creerUneOffre(offreDTO1));
  }

}

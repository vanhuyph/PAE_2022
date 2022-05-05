package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.interet.InteretDTO;
import be.vinci.pae.business.interet.InteretUCC;
import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.donnees.dao.interet.InteretDAO;
import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import be.vinci.pae.utilitaires.exceptions.PasTrouveException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
public class InteretUCCTest {

  private DomaineFactory domaineFactory;
  private InteretDAO interetDAO;
  private InteretDTO interetDTO;
  private InteretUCC interetUCC;
  private UtilisateurDTO utilisateurDTO;
  private UtilisateurDAO utilisateurDAO;
  private ObjetDTO objetDTO;
  private ObjetDAO objetDAO;

  @BeforeAll
  void initTout() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new MockApplicationBinder());
    this.domaineFactory = locator.getService(DomaineFactory.class);
    this.interetDAO = locator.getService(InteretDAO.class);
    this.interetUCC = locator.getService(InteretUCC.class);
    this.objetDAO = locator.getService(ObjetDAO.class);
    this.utilisateurDAO = locator.getService(UtilisateurDAO.class);
    interetDTO = domaineFactory.getInteret();
    utilisateurDTO = domaineFactory.getUtilisateur();
    objetDTO = domaineFactory.getObjet();
    utilisateurDTO.setIdUtilisateur(1);
    utilisateurDTO.setGsm("");
    objetDTO.setIdObjet(1);
    interetDTO.setObjet(objetDTO);
    interetDTO.setUtilisateur(utilisateurDTO);
  }

  @Test
  @DisplayName("Test raté : méthode creerUnInteret avec une date invalide.")
  public void testCreerUnInteretV1() {
    ZoneId zone = ZoneId.systemDefault();
    LocalDate dateLocal = LocalDate.of(2010, 01, 01);
    Date date = Date.from(dateLocal.atStartOfDay(zone).toInstant());
    interetDTO.setDateRdv(date);
    assertThrows(BusinessException.class, () -> interetUCC.creerUnInteret(interetDTO));
  }

  @Test
  @DisplayName("Test raté : méthode creerUnInteret renvoie null car "
      + "l'intérêt n'a pas pu être créé.")
  public void testCreerUnInteretV2() {
    ZoneId zone = ZoneId.systemDefault();
    LocalDate dateLocal = LocalDate.of(2023, 01, 01);
    Date date = Date.from(dateLocal.atStartOfDay(zone).toInstant());
    interetDTO.setDateRdv(date);
    Mockito.when(interetDAO.ajouterInteret(interetDTO)).thenReturn(null);
    assertThrows(BusinessException.class, () -> interetUCC.creerUnInteret(interetDTO));
  }

  @Test
  @DisplayName("Test réussi : méthode creerUnInteret.")
  public void testCreerUnInteretV3() {
    ZoneId zone = ZoneId.systemDefault();
    LocalDate dateLocal = LocalDate.of(2023, 01, 01);
    Date date = Date.from(dateLocal.atStartOfDay(zone).toInstant());
    interetDTO.setDateRdv(date);
    Mockito.when(interetDAO.ajouterInteret(interetDTO)).thenReturn(interetDTO);
    assertEquals(interetDTO, interetUCC.creerUnInteret(interetDTO));
  }

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

  @Test
  @DisplayName("Test réussi : méthode listeDesPersonnesInteressees renvoie bien une liste.")
  public void testlisteDesPersonnesInteresseesV1() {
    List<InteretDTO> liste = new ArrayList<>();
    Mockito.when(interetDAO.listeDesPersonnesInteressees(interetDTO.getObjet().getIdObjet()))
        .thenReturn(liste);
    assertEquals(liste,
        interetUCC.listeDesPersonnesInteressees(interetDTO.getObjet().getIdObjet()));
  }

  @Test
  @DisplayName("Test réussi : méthode listeDesPersonnesInteresseesVue renvoie bien une liste.")
  public void testlisteDesPersonnesInteresseesVueV1() {
    List<InteretDTO> liste = new ArrayList<>();
    Mockito.when(interetDAO.listeDesPersonnesInteresseesVue(interetDTO.getObjet().getIdObjet()))
        .thenReturn(liste);
    assertEquals(liste,
        interetUCC.listeDesPersonnesInteresseesVue(interetDTO.getObjet().getIdObjet()));
  }

  @Test
  @DisplayName("Test raté : méthode indiquerReceveur ratée car le receveur n'a pas pu "
      + "être indiqué.")
  public void testIndiquerReceveurV1() {
    Mockito.when(interetDAO.miseAJourInteret(interetDTO)).thenReturn(null);
    assertThrows(BusinessException.class, () -> interetUCC.indiquerReceveur(interetDTO));
  }

  @Test
  @DisplayName("Test réussi : méthode indiquerReceveur indique bien à une personne intéressée "
      + "comme étant receveur de l'objet.")
  public void testIndiquerReceveurV2() {
    Mockito.when(interetDAO.miseAJourInteret(interetDTO)).thenReturn(interetDTO);
    interetDTO.getObjet().setEtatObjet("Confirmé");
    interetDTO.getObjet().setReceveur(utilisateurDTO);
    Mockito.when(objetDAO.miseAJourObjet(interetDTO.getObjet())).thenReturn(interetDTO.getObjet());
    assertEquals(interetDTO, interetUCC.indiquerReceveur(interetDTO));
  }

  @Test
  @DisplayName("Test raté : méthode nonRemis ratée car l'objet n'a pas de receveur actuellement.")
  public void testNonRemisV1() {
    int id = objetDTO.getIdObjet();
    Mockito.when(interetDAO.receveurActuel(id)).thenReturn(null);
    assertThrows(PasTrouveException.class, () -> interetUCC.nonRemis(id));
  }

  @Test
  @DisplayName("Test réussi : méthode nonRemis renvoie bien que l'objet a été non remis "
      + "car le receveur n'est pas venu chercher l'objet.")
  public void testNonRemisV2() {
    int id = objetDTO.getIdObjet();
    interetDTO.setVenuChercher(false);
    interetDTO.setReceveurChoisi(true);
    Mockito.when(interetDAO.receveurActuel(id)).thenReturn(interetDTO);
    Mockito.when(interetDAO.miseAJourInteret(interetDTO)).thenReturn(interetDTO);
    Mockito.when(utilisateurDAO.miseAJourUtilisateur(interetDTO.getUtilisateur()))
        .thenReturn(interetDTO.getUtilisateur());
    assertEquals(interetDTO, interetUCC.nonRemis(id));
  }

  @Test
  @DisplayName("Test réussi : méthode notifierReceveurEmpecher renvoie bien un interet.")
  public void testNotifierReceveurEmpecherV1() {
    interetDTO.setVueEmpecher(false);
    interetDTO.setReceveurChoisi(true);
    List<InteretDTO> liste = new ArrayList<>();
    Mockito.when(interetDAO.notifierReceveurEmpecher(utilisateurDTO.getIdUtilisateur()))
        .thenReturn(liste);
    assertEquals(liste, interetDAO.notifierReceveurEmpecher(utilisateurDTO.getIdUtilisateur()));
  }

  /*@Test
  @DisplayName("Test réussi : méthode notifierReceveurEmpecher renvoie bien une exception car id incorrect.")
  public void testnotifierReceveurEmpecherV2() {
    Mockito.when(interetDAO.notifierReceveurEmpecher(-1)).thenReturn(null);
    assertThrows(BusinessException.class, () -> interetDAO.notifierReceveurEmpecher(-1));
  }*/
}

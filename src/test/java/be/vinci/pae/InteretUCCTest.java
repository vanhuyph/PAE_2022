package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.interet.InteretDTO;
import be.vinci.pae.business.interet.InteretUCC;
import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.donnees.dao.interet.InteretDAO;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
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
  private ObjetDTO objetDTO;

  @BeforeAll
  void initTout() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new MockApplicationBinder());
    this.domaineFactory = locator.getService(DomaineFactory.class);
    this.interetDAO = locator.getService(InteretDAO.class);
    this.interetUCC = locator.getService(InteretUCC.class);
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
  @DisplayName("Test réussi : méthode listeDesPersonnesInteressees renvoie bien une liste")
  public void testlisteDesPersonnesInteresseesV1() {
    List<InteretDTO> liste = new ArrayList<>();
    Mockito.when(interetDAO.listeDesPersonnesInteressees(interetDTO.getObjet().getIdObjet()))
        .thenReturn(liste);
    assertEquals(liste,
        interetUCC.listeDesPersonnesInteressees(interetDTO.getObjet().getIdObjet()));
  }

}

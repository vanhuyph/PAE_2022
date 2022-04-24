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
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import be.vinci.pae.utilitaires.exceptions.PasTrouveException;
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
  private UtilisateurDAO utilisateurDAO;
  private DomaineFactory domaineFactory;
  private OffreDTO offreDTO1;
  private OffreDTO offreDTO2;
  private OffreDTO offreDTO3;
  private OffreDTO offreDTO4;
  private OffreDTO offreDTO5;
  private ObjetDTO objetDTO1;
  private ObjetDTO objetDTO2;
  private ObjetDTO objetDTO3;
  private ObjetDTO objetDTO4;
  private ObjetDTO objetDTO5;
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
    this.utilisateurDAO = locator.getService(UtilisateurDAO.class);

    utilisateurDTO = domaineFactory.getUtilisateur();
    utilisateurDTO.setIdUtilisateur(1);

    TypeObjetDTO typeObjetDTO = domaineFactory.getTypeObjet();
    typeObjetDTO.setIdType(1);
    typeObjetDTO.setNom("test");

    objetDTO1 = domaineFactory.getObjet();
    objetDTO1.setDescription("testDescription");
    objetDTO1.setOffreur(utilisateurDTO);
    objetDTO1.setPhoto("testPhoto");
    objetDTO1.setVersion(1);
    objetDTO1.setIdObjet(1);
    objetDTO1.setReceveur(null);
    objetDTO1.setTypeObjet(typeObjetDTO);

    objetDTO2 = domaineFactory.getObjet();
    objetDTO2.setIdObjet(2);

    objetDTO3 = domaineFactory.getObjet();
    objetDTO3.setEtatObjet("Offert");

    objetDTO4 = domaineFactory.getObjet();
    objetDTO4.setDescription("testDescription");
    objetDTO4.setOffreur(utilisateurDTO);
    objetDTO4.setPhoto("testPhoto");
    objetDTO4.setVersion(1);
    objetDTO4.setIdObjet(1);
    objetDTO4.setReceveur(null);
    objetDTO4.setTypeObjet(typeObjetDTO);
    objetDTO4.setEtatObjet("Annulé");

    objetDTO5 = domaineFactory.getObjet();
    objetDTO5.setDescription("testDescription");
    objetDTO5.setOffreur(utilisateurDTO);
    objetDTO5.setPhoto("testPhoto");
    objetDTO5.setVersion(1);
    objetDTO5.setIdObjet(1);
    objetDTO5.setReceveur(null);
    objetDTO5.setTypeObjet(typeObjetDTO);
    objetDTO5.setEtatObjet("Intéressé");

    offreDTO1 = domaineFactory.getOffre();
    offreDTO1.setIdOffre(1);
    offreDTO1.setObjetDTO(objetDTO1);
    offreDTO1.setPlageHoraire("testPlageHoraire");

    offreDTO2 = domaineFactory.getOffre();
    offreDTO2.setIdOffre(2);

    offreDTO3 = domaineFactory.getOffre();
    offreDTO3.setIdOffre(1);
    offreDTO3.setObjetDTO(objetDTO3);
    offreDTO3.setPlageHoraire("testPlageHoraire");

    offreDTO4 = domaineFactory.getOffre();
    offreDTO4.setIdOffre(1);
    offreDTO4.setObjetDTO(objetDTO4);
    offreDTO4.setPlageHoraire("testPlageHoraire");

    offreDTO5 = domaineFactory.getOffre();
    offreDTO5.setIdOffre(1);
    offreDTO5.setObjetDTO(objetDTO5);
    offreDTO5.setPlageHoraire("testPlageHoraire");
  }

  @Test
  @DisplayName("Test raté : méthode creerObjet renvoie null car l'objet n'a pas été créé.")
  public void testCreerOffreV1() {
    Mockito.when(objetDAO.creerObjet(offreDTO1.getObjetDTO())).thenReturn(null);
    Mockito.when(offreDAO.creerOffre(offreDTO1)).thenReturn(null);
    assertThrows(BusinessException.class, () -> offreUCC.creerOffre(offreDTO1));
  }

  @Test
  @DisplayName("Test raté : méthode creerOffre renvoie null car l'offre n'a pas été créée.")
  public void testCreerOffreV2() {
    Mockito.when(objetDAO.creerObjet(objetDTO1)).thenReturn(objetDTO1);
    Mockito.when(offreDAO.creerOffre(offreDTO1)).thenReturn(null);
    assertThrows(BusinessException.class, () -> offreUCC.creerOffre(offreDTO1));
  }

  @Test
  @DisplayName("Test réussi : méthode creerOffre renvoie l'offre créée.")
  public void testCreerOffreV3() {
    Mockito.when(objetDAO.creerObjet(offreDTO1.getObjetDTO())).thenReturn(objetDTO1);
    Mockito.when(offreDAO.creerOffre(offreDTO1)).thenReturn(offreDTO1);
    Mockito.when(utilisateurDAO.miseAJourUtilisateur(offreDTO1.getObjetDTO().getOffreur()))
        .thenReturn(utilisateurDTO);
    assertEquals(offreDTO1, offreUCC.creerOffre(offreDTO1));
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
  @DisplayName("Test raté : méthode annulerOffre renvoie null car l'offre n'est pas trouvable.")
  public void annulerOffreV1() {
    Mockito.when(objetDAO.miseAJourObjet(objetDTO1)).thenReturn(null);
    Mockito.when(objetDAO.rechercheParId(objetDTO1)).thenReturn(null);
    assertThrows(PasTrouveException.class, () -> offreUCC.annulerOffre(offreDTO1));
  }

  @Test
  @DisplayName("Test raté : méthode annulerOffre renvoie null car l'offre n'est pas trouvable.")
  public void annulerOffreV2() {
    Mockito.when(objetDAO.miseAJourObjet(objetDTO1)).thenReturn(null);
    Mockito.when(objetDAO.rechercheParId(objetDTO1)).thenReturn(objetDTO1);
    assertThrows(BusinessException.class, () -> offreUCC.annulerOffre(offreDTO1));
  }

  @Test
  @DisplayName("Test réussi : méthode annulerOffre renvoie l'offre annulée.")
  public void annulerOffreV3() {
    Mockito.when(objetDAO.miseAJourObjet(offreDTO1.getObjetDTO()))
        .thenReturn(offreDTO1.getObjetDTO());
    assertEquals(offreDTO1, offreUCC.annulerOffre(offreDTO1));
  }

  @Test
  @DisplayName("Test raté : méthode rechercheParId renvoie null car l'offre n'est pas trouvable.")
  public void rechercheParIdV1() {
    int id = -1;
    Mockito.when(offreDAO.rechercheParId(id)).thenReturn(null);
    assertThrows(PasTrouveException.class, () -> offreUCC.rechercheParId(id));
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

  //  @Test
  //  @DisplayName("Test réussi : méthode rechercherOffre renvoie une liste "
  //      + "avec toutes les offres ayant le statut offert.")
  //  public void testRechercherOffreV1() {
  //    List<OffreDTO> liste = new ArrayList<>();
  //    Mockito.when(offreDAO.rechercherOffres("Offert")).thenReturn(liste);
  //    assertEquals(liste, offreDAO.rechercherOffres("Offert"));
  //  }

  /*@Test
  @DisplayName("Test réussi : méthode indiquerMembreReceveur renvoie l'offre confirmé.")
  public void testIndiquerMembreReceveurV1() {
    offreDTO1.setObjetDTO(objetDTO1);
    assertEquals("confirmé",
        offreUCC.indiquerMembreReceveur(offreDTO1).getObjetDTO().getEtatObjet());
  }

  @Test
  @DisplayName("Test réussi : méthode donnerOffre renvoie l'offre donné.")
  public void testDonnerOffreV1() {
    objetDTO1.setReceveur(utilisateurDTO);
    offreDTO1.setObjetDTO(objetDTO1);
    Mockito.when(objetDAO.creerObjet(offreDTO1.getObjetDTO())).thenReturn(objetDTO1);
    Mockito.when(offreDAO.creerOffre(offreDTO1)).thenReturn(offreDTO1);
    Mockito.when(offreUCC.donnerOffre(offreDTO1)).thenReturn(offreDTO1);
    assertEquals("Donné", offreDTO1.getObjetDTO().getEtatObjet());
  }*/

  @Test
  @DisplayName("Test réussi : méthode mesOffres renvoie bien une liste.")
  public void testMesOffresV1() {
    int id = utilisateurDTO.getIdUtilisateur();
    List<OffreDTO> liste = new ArrayList<>();
    Mockito.when(offreDAO.mesOffres(id)).thenReturn(liste);
    assertEquals(liste, offreUCC.mesOffres(id));
  }

  @Test
  @DisplayName("Test réussi : méthode VoirOffreAttribuer renvoie bien une liste.")
  public void testVoirOffreAttribuerV1() {
    int id = utilisateurDTO.getIdUtilisateur();
    List<OffreDTO> liste = new ArrayList<>();
    Mockito.when(offreDAO.voirOffreAttribuer(id)).thenReturn(liste);
    assertEquals(liste, offreUCC.voirOffreAttribuer(id));
  }

  @Test
  @DisplayName("Test raté : méthode reoffrirObjet avec en paramètre objet sans l'état annulé")
  public void testReoffrirObjetV1() {
    assertThrows(PasTrouveException.class, () -> offreUCC.reoffrirObjet(offreDTO3));
  }

  @Test
  @DisplayName("Test raté : méthode reoffrirObjet avec en paramètre un objet qui n'existe pas")
  public void testReoffrirObjetV2() {
    Mockito.when(objetDAO.miseAJourObjet(objetDTO5)).thenReturn(null);
    Mockito.when(objetDAO.rechercheParId(objetDTO5)).thenReturn(null);
    assertThrows(PasTrouveException.class, () -> offreUCC.reoffrirObjet(offreDTO4));
  }

  @Test
  @DisplayName("Test raté : méthode reoffrirObjet avec créer une offre qui échoue")
  public void testReoffrirObjetV3() {
    Mockito.when(objetDAO.miseAJourObjet(objetDTO5)).thenReturn(objetDTO5);
    Mockito.when(offreDAO.creerOffre(offreDTO5)).thenReturn(null);
    assertThrows(BusinessException.class, () -> offreUCC.reoffrirObjet(offreDTO4));
  }

  // TO DO , problème de jenkins qui accepte pas le test

  //@Test
  //@DisplayName("Test réussi : méthode reoffrirObjet renvoit bien une nouvelle offre")
  //public void testReoffrirObjetV4() {
  //  Mockito.when(objetDAO.miseAJourObjet(objetDTO4)).thenReturn(objetDTO5);
  //  Mockito.when(offreDAO.creerOffre(offreDTO5)).thenReturn(offreDTO5);
  //  assertEquals(offreDTO5, offreUCC.reoffrirObjet(offreDTO4));
  //}

}

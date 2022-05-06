package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.adresse.AdresseDTO;
import be.vinci.pae.business.interet.Interet;
import be.vinci.pae.business.interet.InteretDTO;
import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.business.utilisateur.UtilisateurUCC;
import be.vinci.pae.donnees.dao.adresse.AdresseDAO;
import be.vinci.pae.donnees.dao.interet.InteretDAO;
import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import be.vinci.pae.donnees.dao.offre.OffreDAO;
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import be.vinci.pae.utilitaires.exceptions.ConflitException;
import be.vinci.pae.utilitaires.exceptions.NonAutoriseException;
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
public class UtilisateurUCCTest {

  private UtilisateurUCC utilisateurUCC;
  private DomaineFactory domaineFactory;
  private UtilisateurDAO utilisateurDAO;
  private AdresseDTO adresseDTO;
  private AdresseDAO adresseDAO;
  private InteretDAO interetDAO;
  private ObjetDAO objetDAO;
  private ObjetDTO objetDTO1;
  private ObjetDTO objetDTO2;
  private OffreDAO offreDAO;
  private OffreDTO offreDTO1;
  private OffreDTO offreDTO2;
  private UtilisateurDTO utilisateurDTO1;
  private UtilisateurDTO utilisateurDTO2;
  private UtilisateurDTO utilisateurDTO3;
  private UtilisateurDTO utilisateurDTO4;
  private UtilisateurDTO utilisateurDTO5;
  private UtilisateurDTO utilisateurDTO6;



  @BeforeAll
  void initTout() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new MockApplicationBinder());
    this.utilisateurUCC = locator.getService(UtilisateurUCC.class);
    this.domaineFactory = locator.getService(DomaineFactory.class);
    this.utilisateurUCC = locator.getService(UtilisateurUCC.class);
    this.utilisateurDAO = locator.getService(UtilisateurDAO.class);
    this.adresseDAO = locator.getService(AdresseDAO.class);
    this.objetDAO = locator.getService(ObjetDAO.class);
    this.offreDAO = locator.getService(OffreDAO.class);
    this.interetDAO = locator.getService(InteretDAO.class);
    adresseDTO = domaineFactory.getAdresse();
    adresseDTO.setIdAdresse(1);
    adresseDTO.setRue("Rue");
    adresseDTO.setNumero(1);
    adresseDTO.setCodePostal(1040);
    adresseDTO.setCommune("Etterbeek");

    objetDTO1 = domaineFactory.getObjet();
    objetDTO1.setIdObjet(1);
    objetDTO1.setEtatObjet("Empêché");
    objetDTO1.setOffreur(utilisateurDTO6);

    objetDTO2 = domaineFactory.getObjet();
    objetDTO2.setIdObjet(2);
    objetDTO2.setEtatObjet("Intéressé");
    objetDTO2.setOffreur(utilisateurDTO1);

    offreDTO1 = domaineFactory.getOffre();
    offreDTO1.setObjetDTO(objetDTO1);

    offreDTO2 = domaineFactory.getOffre();
    offreDTO2.setObjetDTO(objetDTO2);

    utilisateurDTO1 = domaineFactory.getUtilisateur();
    utilisateurDTO1.setIdUtilisateur(1);
    utilisateurDTO1.setPseudo("test1");
    // mdp = test123
    utilisateurDTO1.setMdp("$2a$10$jAXbw66kyec1S8RV/pnwo.kuEnAbmIsP5h7463ZkxGJocnx1WzLUy");
    utilisateurDTO1.setEstAdmin(false);
    utilisateurDTO1.setEtatInscription("Confirmé");
    utilisateurDTO1.setAdresse(adresseDTO);

    utilisateurDTO2 = domaineFactory.getUtilisateur();
    utilisateurDTO2.setIdUtilisateur(2);
    utilisateurDTO2.setEtatInscription("Confirmé");
    utilisateurDTO2.setEstAdmin(true);

    utilisateurDTO3 = domaineFactory.getUtilisateur();
    utilisateurDTO3.setIdUtilisateur(0);

    utilisateurDTO4 = domaineFactory.getUtilisateur();
    utilisateurDTO4.setIdUtilisateur(3);
    // mdp = test123
    utilisateurDTO4.setMdp("$2a$10$jAXbw66kyec1S8RV/pnwo.kuEnAbmIsP5h7463ZkxGJocnx1WzLUy");

    utilisateurDTO5 = domaineFactory.getUtilisateur();
    utilisateurDTO5.setIdUtilisateur(3);
    // mdp = mdp
    utilisateurDTO5.setMdp("$2a$10$HVuwI6iTIfe9mHpt5JtL8OGWvmN0.R1lP1O8etpe9GcPlXHxzt0e2");

    utilisateurDTO6 = domaineFactory.getUtilisateur();
    utilisateurDTO6.setIdUtilisateur(2);
    utilisateurDTO6.setEtatInscription("Empêché");
  }

  @Test
  @DisplayName("Test raté : méthode connexion avec mauvais pseudo et bon mdp. "
      + "Identifiants corrects : pseudo = test1, mdp = test123")
  public void testConnexionV1() {
    Mockito.when(utilisateurDAO.rechercheParPseudo("test")).thenReturn(null);
    assertThrows(NonAutoriseException.class, () -> utilisateurUCC.connexion("test", "test123"));
  }

  @Test
  @DisplayName("Test raté : méthode connexion avec bon pseudo et mauvais mdp. "
      + "Identifiants corrects : pseudo = test1, mdp = test123")
  public void testConnexionV2() {
    Mockito.when(utilisateurDAO.rechercheParPseudo(utilisateurDTO1.getPseudo()))
        .thenReturn(utilisateurDTO1);
    assertThrows(NonAutoriseException.class, () -> utilisateurUCC.connexion("test1", "test1234"));
  }

  @Test
  @DisplayName("Test réussi : méthode connexion avec les bons identifiants. "
      + "Identifiants corrects : pseudo = test1, mdp = test123")
  public void testConnexionV3() {
    Mockito.when(utilisateurDAO.rechercheParPseudo(utilisateurDTO1.getPseudo()))
        .thenReturn(utilisateurDTO1);
    assertEquals(utilisateurDTO1, utilisateurUCC.connexion("test1", "test123"));
  }

  @Test
  @DisplayName("Test raté : méthode d'inscription avec un pseudo déjà existant.")
  public void testInscriptionV1() {
    Mockito.when(utilisateurDAO.rechercheParPseudo(utilisateurDTO1.getPseudo()))
        .thenReturn(utilisateurDTO1);
    assertThrows(ConflitException.class, () -> utilisateurUCC.inscription(utilisateurDTO1));
  }

  @Test
  @DisplayName("Test réussi : méthode d'inscription avec les bons champs.")
  public void testInscriptionV2() {
    utilisateurDTO1.setMdp("test123");
    Mockito.when(utilisateurDAO.rechercheParPseudo(utilisateurDTO1.getPseudo()))
        .thenReturn(null);
    Mockito.when(adresseDAO.ajouterAdresse(adresseDTO)).thenReturn(adresseDTO);
    Mockito.when(utilisateurDAO.ajouterUtilisateur(utilisateurDTO1)).thenReturn(utilisateurDTO1);
    assertEquals(utilisateurDTO1, utilisateurUCC.inscription(utilisateurDTO1));
  }

  @Test
  @DisplayName("Test réussi : méthode rechercheParId renvoie un utilisateur existant.")
  public void testRecherchePardIdV1() {
    int id = utilisateurDTO1.getIdUtilisateur();
    Mockito.when(utilisateurDAO.rechercheParId(id)).thenReturn(utilisateurDTO1);
    assertEquals(utilisateurDTO1, utilisateurUCC.rechercheParId(id));
  }

  @Test
  @DisplayName("Test raté : méthode rechercheParId renvoie null car l'utilisateur "
      + "n'est pas trouvable.")
  public void testRecherchePardIdV2() {
    int id = -1;
    Mockito.when(utilisateurDAO.rechercheParId(id)).thenReturn(null);
    assertThrows(BusinessException.class, () -> utilisateurUCC.rechercheParId(id));
  }

  @Test
  @DisplayName("Test raté : méthode rechercheParPseudo renvoie null car l'utilisateur "
      + "n'est pas trouvable.")
  public void testRechercheParPseudoV1() {
    String pseudo = "pseudoInexistant";
    Mockito.when(utilisateurDAO.rechercheParPseudo(pseudo)).thenReturn(null);
    assertThrows(PasTrouveException.class, () -> utilisateurUCC.rechercheParPseudo(pseudo));
  }

  @Test
  @DisplayName("Test réussi : méthode rechercheParPseudo renvoie un utilisateur existant.")
  public void testRechercheParPseudoV2() {
    String pseudo = utilisateurDTO1.getPseudo();
    Mockito.when(utilisateurDAO.rechercheParPseudo(pseudo)).thenReturn(utilisateurDTO1);
    assertEquals(utilisateurDTO1, utilisateurUCC.rechercheParPseudo(pseudo));
  }

  @Test
  @DisplayName("Test réussi : méthode confirmerInscription renvoie bien un utilisateur "
      + "avec son état d'inscription à confirmé mais ne le passe pas en admin.")
  public void testConfirmerInscriptionV1() {
    int id = utilisateurDTO4.getIdUtilisateur();
    utilisateurDTO4.setEtatInscription("En attente");
    Mockito.when(utilisateurDAO.rechercheParId(id))
        .thenReturn(utilisateurDTO4);
    Mockito.when(utilisateurDAO.miseAJourUtilisateur(utilisateurDTO4)).thenReturn(utilisateurDTO4);
    assertEquals(utilisateurDTO4, utilisateurUCC.confirmerInscription(id, false));
  }

  @Test
  @DisplayName("Test réussi : méthode confirmerInscription renvoie bien un utilisateur "
      + "avec son état d'inscription à confirmé et le passe en admin.")
  public void testConfirmerInscriptionV2() {
    int id = utilisateurDTO4.getIdUtilisateur();
    utilisateurDTO4.setEtatInscription("En attente");
    Mockito.when(utilisateurDAO.rechercheParId(id))
        .thenReturn(utilisateurDTO4);
    Mockito.when(utilisateurDAO.miseAJourUtilisateur(utilisateurDTO4)).thenReturn(utilisateurDTO4);
    assertEquals(utilisateurDTO4, utilisateurUCC.confirmerInscription(id, true));
  }

  @Test
  @DisplayName("Test raté : méthode confirmerInscription renvoie null car l'utilisateur n'existe "
      + "pas")
  public void testConfirmerInscriptionV3() {
    int id = utilisateurDTO4.getIdUtilisateur();
    Mockito.when(utilisateurDAO.rechercheParId(utilisateurDTO4.getIdUtilisateur()))
        .thenReturn(null);
    Mockito.when(utilisateurDAO.miseAJourUtilisateur(utilisateurDTO4)).thenReturn(null);
    assertThrows(PasTrouveException.class,
        () -> utilisateurUCC.confirmerInscription(id, false));
  }

  @Test
  @DisplayName("Test raté : méthode confirmerInscription renvoie null car la confirmation "
      + "n'a pas pu être faite")
  public void testConfirmerInscriptionV4() {
    int id = utilisateurDTO4.getIdUtilisateur();
    Mockito.when(utilisateurDAO.rechercheParId(utilisateurDTO4.getIdUtilisateur()))
        .thenReturn(utilisateurDTO4);
    Mockito.when(utilisateurDAO.miseAJourUtilisateur(utilisateurDTO4)).thenReturn(null);
    assertThrows(BusinessException.class,
        () -> utilisateurUCC.confirmerInscription(id, false));
  }

  @Test
  @DisplayName("Test raté : méthode refuserInscription renvoie null car l'utilisateur n'a "
      + "pas pu être refusé")
  public void testRefuserInscriptionV1() {
    int id = utilisateurDTO4.getIdUtilisateur();
    Mockito.when(utilisateurDAO.rechercheParId(utilisateurDTO4.getIdUtilisateur()))
        .thenReturn(utilisateurDTO4);
    Mockito.when(utilisateurDAO.miseAJourUtilisateur(utilisateurDTO4))
        .thenReturn(null);
    assertThrows(BusinessException.class, () ->
        utilisateurUCC.refuserInscription(id, "Il faudra attendre quelques jours"));
  }

  @Test
  @DisplayName("Test raté : méthode refuserInscription renvoie null car l'utilisateur n'est "
      + "pas trouvable.")
  public void testRefuserInscriptionV2() {
    int id = utilisateurDTO4.getIdUtilisateur();
    Mockito.when(utilisateurDAO.rechercheParId(utilisateurDTO4.getIdUtilisateur()))
        .thenReturn(null);
    Mockito.when(utilisateurDAO.miseAJourUtilisateur(utilisateurDTO4))
        .thenReturn(null);
    assertThrows(PasTrouveException.class, () ->
        utilisateurUCC.refuserInscription(id, "Il faudra attendre quelques jours"));
  }

  @Test
  @DisplayName("Test réussi : méthode refuserInscription renvoie bien un utilisateur "
      + "avec son état d'inscription à refusé et un commentaire.")
  public void testRefuserInscriptionV3() {
    int id = utilisateurDTO4.getIdUtilisateur();
    utilisateurDTO4.setEtatInscription("En attente");
    Mockito.when(utilisateurDAO.rechercheParId(id))
        .thenReturn(utilisateurDTO4);
    Mockito.when(utilisateurDAO.miseAJourUtilisateur(utilisateurDTO4))
        .thenReturn(utilisateurDTO4);
    assertEquals(utilisateurDTO4,
        utilisateurUCC.refuserInscription(id, "Il faudra attendre quelques jours"));
  }

  @Test
  @DisplayName("Test réussi : méthode listerUtilisateursEtatsInscriptions renvoie une "
      + "liste avec tous les utilisateurs ayant l'état de leur inscription à confirmé.")
  public void testListerUtilisateursEtatsInscriptionsV1() {
    List<UtilisateurDTO> liste = new ArrayList<>();
    Mockito.when(utilisateurDAO.listerUtilisateursEtatsInscriptions("Confirmé")).thenReturn(liste);
    assertEquals(liste, utilisateurUCC.listerUtilisateursEtatsInscriptions("Confirmé"));
  }

  @Test
  @DisplayName("Test réussi : méthode listerUtilisateursEtatsInscriptions renvoie une "
      + "liste avec tous les utilisateurs ayant l'état de leur inscription à refusé.")
  public void testListerUtilisateursEtatsInscriptionsV2() {
    List<UtilisateurDTO> liste = new ArrayList<>();
    Mockito.when(utilisateurDAO.listerUtilisateursEtatsInscriptions("Refusé")).thenReturn(liste);
    assertEquals(liste, utilisateurUCC.listerUtilisateursEtatsInscriptions("Refusé"));
  }

  @Test
  @DisplayName("Test raté : méthode miseAJourUtilisateur n'a pas correctement effectuée "
      + "la mise à jour de l'utilisateur.")
  public void testMiseAJourUtilisateurV1() {
    Mockito.when(utilisateurDAO.miseAJourUtilisateur(utilisateurDTO1)).thenReturn(null);
    assertThrows(PasTrouveException.class,
        () -> utilisateurUCC.miseAJourUtilisateur(utilisateurDTO1));
  }

  @Test
  @DisplayName("Test réussi : méthode miseAJourUtilisateur a correctement effectuée "
      + "la mise à jour de l'utilisateur.")
  public void testMiseAJourUtilisateurV2() {
    Mockito.when(adresseDAO.miseAJourAdresse(adresseDTO)).thenReturn(adresseDTO);
    Mockito.when(utilisateurDAO.miseAJourUtilisateur(utilisateurDTO1)).thenReturn(utilisateurDTO1);
    assertEquals(utilisateurDTO1, utilisateurUCC.miseAJourUtilisateur(utilisateurDTO1));
  }

  @Test
  @DisplayName("Test raté : méthode modifierMdp a échouée car le mot de passe est incorrect lors "
      + "de la demande de changement.")
  public void testModifierMdpV1() {
    int id = utilisateurDTO4.getIdUtilisateur();
    Mockito.when(utilisateurDAO.rechercheParId(id)).thenReturn(utilisateurDTO4);
    Mockito.when(utilisateurDAO.modifierMdp(utilisateurDTO4)).thenReturn(utilisateurDTO5);
    assertThrows(BusinessException.class, () -> utilisateurUCC.modifierMdp(id, "123", "mdp"));
  }

  @Test
  @DisplayName("Test réussi : méthode modifierMdp a correctement effectuée le changement "
      + "de mot de passe de l'utilisateur.")
  public void testModifierMdpV2() {
    int id = utilisateurDTO4.getIdUtilisateur();
    Mockito.when(utilisateurDAO.rechercheParId(id)).thenReturn(utilisateurDTO4);
    Mockito.when(utilisateurDAO.modifierMdp(utilisateurDTO4)).thenReturn(utilisateurDTO5);
    assertEquals(utilisateurDTO5, utilisateurUCC.modifierMdp(id, "test123", "mdp"));
  }

  @Test
  @DisplayName("Test réussi : méthode rechercherMembres renvoie une "
      + "liste avec tous les utilisateurs habitant dans la ville de Verviers.")
  public void testRechercherMembresV1() {
    List<UtilisateurDTO> liste = new ArrayList<>();
    Mockito.when(utilisateurDAO.rechercherMembres("Verviers")).thenReturn(liste);
    assertEquals(liste, utilisateurUCC.rechercherMembres("Verviers"));
  }

  @Test
  @DisplayName("Test réussi : méthode indiquerConfirmerUtilisateur a réussi et renvoit"
      + "l'utilisateur avec son état changé a confirme et ses offres de retour a l'état"
      + "initial")
  public void testIndiquerConfirmerUtilisateurV1() {

    List<OffreDTO> listeOffre = new ArrayList<>();
    listeOffre.add(offreDTO1);

    List<InteretDTO> listeInteret = new ArrayList<>();
    Mockito.when(offreDAO.mesOffresEmpecher(utilisateurDTO6.getIdUtilisateur())).thenReturn(listeOffre);

    Mockito.when(interetDAO.listeDesPersonnesInteressees(offreDTO1.getObjetDTO().getIdObjet())).thenReturn(listeInteret);
    Mockito.when(objetDAO.miseAJourObjet(offreDTO1.getObjetDTO())).thenReturn(objetDTO2);
    Mockito.when(utilisateurDAO.miseAJourUtilisateur(utilisateurDTO6)).thenReturn(utilisateurDTO1);
    assertEquals(utilisateurDTO1, utilisateurUCC.indiquerConfirmerUtilisateur(utilisateurDTO6));
  }

  @Test
  @DisplayName("Test raté : méthode indiquerConfirmerUtilisateur a échoué car "
      + "L'objet n'existe pas")
  public void testIndiquerConfirmerUtilisateurV2() {
    List<OffreDTO> listeOffre = new ArrayList<>();
    listeOffre.add(offreDTO1);

    List<InteretDTO> listeInteret = new ArrayList<>();
    Mockito.when(offreDAO.mesOffresEmpecher(utilisateurDTO6.getIdUtilisateur())).thenReturn(listeOffre);

    Mockito.when(interetDAO.listeDesPersonnesInteressees(offreDTO1.getObjetDTO().getIdObjet())).thenReturn(listeInteret);
    Mockito.when(objetDAO.miseAJourObjet(offreDTO1.getObjetDTO())).thenReturn(null);
    Mockito.when(objetDAO.rechercheParId(offreDTO1.getObjetDTO())).thenReturn(null);
    assertThrows(PasTrouveException.class, () -> utilisateurUCC.indiquerConfirmerUtilisateur(utilisateurDTO6));


  }

  @Test
  @DisplayName("Test raté : méthode indiquerConfirmerUtilisateur a échoué car "
      + "L'utilisateur n'existe pas")
  public void testIndiquerConfirmerUtilisateurV3() {
    List<OffreDTO> listeOffre = new ArrayList<>();
    listeOffre.add(offreDTO1);

    List<InteretDTO> listeInteret = new ArrayList<>();
    Mockito.when(offreDAO.mesOffresEmpecher(utilisateurDTO6.getIdUtilisateur())).thenReturn(listeOffre);

    Mockito.when(interetDAO.listeDesPersonnesInteressees(offreDTO1.getObjetDTO().getIdObjet())).thenReturn(listeInteret);
    Mockito.when(objetDAO.miseAJourObjet(offreDTO1.getObjetDTO())).thenReturn(objetDTO2);
    Mockito.when(utilisateurDAO.miseAJourUtilisateur(utilisateurDTO6)).thenReturn(null);
    Mockito.when(utilisateurDAO.rechercheParId(utilisateurDTO6.getIdUtilisateur())).thenReturn(null);
    assertThrows(PasTrouveException.class, () -> utilisateurUCC.indiquerConfirmerUtilisateur(utilisateurDTO6));

  }

  @Test
  @DisplayName("Test réussi : méthode indiquerEmpecherUtilisateur a réussi et renvoit"
      + "l'utilisateur avec son état changé et ses offres offres en l'état Empêché")
  public void testIndiquerEmpecherUtilisateurV1() {
    List<OffreDTO> listeOffre = new ArrayList<>();
    listeOffre.add(offreDTO2);
    Mockito.when(offreDAO.mesOffresAEmpecher(utilisateurDTO1.getIdUtilisateur())).thenReturn(listeOffre);
    Mockito.when(objetDAO.miseAJourObjet(offreDTO2.getObjetDTO())).thenReturn(objetDTO1);
    Mockito.when(utilisateurDAO.miseAJourUtilisateur(utilisateurDTO1)).thenReturn(utilisateurDTO6);
    assertEquals(utilisateurDTO6, utilisateurUCC.indiquerEmpecherUtilisateur(utilisateurDTO1));

  }



  @Test
  @DisplayName("Test raté : méthode indiquerEmpecherUtilisateur a échoué car "
      + "L'objet n'existe pas")
  public void testIndiquerEmpecherUtilisateurV2() {
    List<OffreDTO> listeOffre = new ArrayList<>();
    listeOffre.add(offreDTO2);
    Mockito.when(offreDAO.mesOffresAEmpecher(utilisateurDTO1.getIdUtilisateur())).thenReturn(listeOffre);
    Mockito.when(objetDAO.miseAJourObjet(offreDTO2.getObjetDTO())).thenReturn(null);
    Mockito.when(objetDAO.rechercheParId(offreDTO2.getObjetDTO())).thenReturn(null);
    assertThrows(PasTrouveException.class, () -> utilisateurUCC.indiquerEmpecherUtilisateur(utilisateurDTO1));

  }

  @Test
  @DisplayName("Test raté : méthode indiquerEmpecherUtilisateur a échoué car "
      + "L'utilisateur n'existe pas")
  public void testIndiquerEmpecherUtilisateurV3() {
    List<OffreDTO> listeOffre = new ArrayList<>();
    listeOffre.add(offreDTO2);
    Mockito.when(offreDAO.mesOffresAEmpecher(utilisateurDTO1.getIdUtilisateur())).thenReturn(listeOffre);
    Mockito.when(objetDAO.miseAJourObjet(offreDTO2.getObjetDTO())).thenReturn(objetDTO1);
    Mockito.when(objetDAO.rechercheParId(offreDTO2.getObjetDTO())).thenReturn(null);
    Mockito.when(utilisateurDAO.miseAJourUtilisateur(utilisateurDTO1)).thenReturn(null);
    Mockito.when(utilisateurDAO.rechercheParId(utilisateurDTO1.getIdUtilisateur())).thenReturn(null);
    assertThrows(PasTrouveException.class, () -> utilisateurUCC.indiquerConfirmerUtilisateur(utilisateurDTO1));

  }



}

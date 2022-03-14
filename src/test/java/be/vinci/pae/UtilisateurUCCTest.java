package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.business.utilisateur.UtilisateurUCC;
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import be.vinci.pae.utilitaires.exceptions.NonAutoriseException;
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
  private UtilisateurDTO utilisateurDTO;
  private UtilisateurDTO utilisateurDTO2;
  private UtilisateurDAO utilisateurDAO;

  @BeforeAll
  void initTout() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new MockApplicationBinder());
    this.utilisateurUCC = locator.getService(UtilisateurUCC.class);
    this.domaineFactory = locator.getService(DomaineFactory.class);
    this.utilisateurUCC = locator.getService(UtilisateurUCC.class);
    this.utilisateurDAO = locator.getService(UtilisateurDAO.class);
    utilisateurDTO = domaineFactory.getUtilisateur();
    utilisateurDTO.setIdUtilisateur(1);
    utilisateurDTO.setPseudo("test1");
    utilisateurDTO.setMdp("$2a$10$6sYdHXd.tocTOh3LsHFGsOlrsygxA0T4HsVJkLnrV4Im5R5whHqT6");
    utilisateurDTO.setEstAdmin(false);
    utilisateurDTO.setEtatInscription("confirmé");

    utilisateurDTO2 = domaineFactory.getUtilisateur();
    utilisateurDTO2.setIdUtilisateur(2);
    utilisateurDTO2.setEtatInscription("confirmé");
    utilisateurDTO2.setEstAdmin(true);
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
    Mockito.when(utilisateurDAO.rechercheParPseudo(utilisateurDTO.getPseudo()))
        .thenReturn(utilisateurDTO);
    assertThrows(NonAutoriseException.class, () -> utilisateurUCC.connexion("test1", "test1234"));
  }

  @Test
  @DisplayName("Test réussi : méthode connexion avec les bons identifiants. "
      + "Identifiants corrects : pseudo = test1, mdp = test123")
  public void testConnexionV3() {
    Mockito.when(utilisateurDAO.rechercheParPseudo(utilisateurDTO.getPseudo()))
        .thenReturn(utilisateurDTO);
    assertEquals(utilisateurDTO, utilisateurUCC.connexion("test1", "test123"));
  }

  @Test
  @DisplayName("Test réussi : méthode rechercheParId renvoie un utilisateur existant.")
  public void testRecherchePardIdV1() {
    int id = utilisateurDTO.getIdUtilisateur();
    Mockito.when(utilisateurDAO.rechercheParId(id)).thenReturn(utilisateurDTO);
    assertEquals(utilisateurDTO, utilisateurUCC.rechercheParId(id));
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
  @DisplayName("Test réussi : méthode confirmerInscription renvoie bien un utilisateur "
      + "avec son état d'inscription à confirmé mais ne le passe pas en admin")
  public void testConfirmerInscriptionV1() {
    int id = utilisateurDTO.getIdUtilisateur();
    Mockito.when(utilisateurDAO.confirmerInscription(id, false)).thenReturn(utilisateurDTO);
    assertEquals(utilisateurDTO, utilisateurUCC.confirmerInscription(id, false));
  }

  @Test
  @DisplayName("Test réussi : méthode confirmerInscription renvoie bien un utilisateur "
      + "avec son état d'inscription à confirmé et le passe en admin")
  public void testConfirmerInscriptionV2() {
    int id = utilisateurDTO2.getIdUtilisateur();
    Mockito.when(utilisateurDAO.confirmerInscription(id, true)).thenReturn(utilisateurDTO2);
    assertEquals(utilisateurDTO2, utilisateurUCC.confirmerInscription(id, true));
  }

  @Test
  @DisplayName("Test réussi : méthode listerUtilisateursEtatsInscriptions renvoie une "
      + "liste avec tous les utilisateurs ayant l'état de leur inscription à confirmé")
  public void testListerUtilisateursEtatsInscriptionsV1() {
    List<UtilisateurDTO> liste = new ArrayList<>();
    Mockito.when(utilisateurDAO.listerUtilisateursEtatsInscriptions("confirmé")).thenReturn(liste);
    assertEquals(liste, utilisateurUCC.listerUtilisateursEtatsInscriptions("confirmé"));
  }

  @Test
  @DisplayName("Test réussi : méthode listerUtilisateursEtatsInscriptions renvoie une "
      + "liste avec tous les utilisateurs ayant l'état de leur inscription à refusé")
  public void testListerUtilisateursEtatsInscriptionsV2() {
    List<UtilisateurDTO> liste = new ArrayList<>();
    Mockito.when(utilisateurDAO.listerUtilisateursEtatsInscriptions("refusé")).thenReturn(liste);
    assertEquals(liste, utilisateurUCC.listerUtilisateursEtatsInscriptions("refusé"));
  }

}

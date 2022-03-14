package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.business.utilisateur.UtilisateurUCC;
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import be.vinci.pae.utilitaires.exceptions.FatalException;
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
  }

  @Test
  @DisplayName("Test raté : méthode connexion avec mauvais pseudo et bon mdp. "
      + "Identifiants corrects : pseudo = test1, mdp = test123")
  public void testConnexionV1() throws FatalException {
    Mockito.when(utilisateurDAO.rechercheParPseudo("test")).thenReturn(null);
    assertThrows(BusinessException.class, () -> utilisateurUCC.connexion("test", "test123"));
  }

  @Test
  @DisplayName("Test raté : méthode connexion avec bon pseudo et mauvais mdp. "
      + "Identifiants corrects : pseudo = test1, mdp = test123")
  public void testConnexionV2() throws FatalException {
    Mockito.when(utilisateurDAO.rechercheParPseudo(utilisateurDTO.getPseudo()))
        .thenReturn(utilisateurDTO);
    assertThrows(BusinessException.class, () -> utilisateurUCC.connexion("test1", "test1234"));
  }

  @Test
  @DisplayName("Test réussi : méthode connexion avec les bons identifiants. "
      + "Identifiants corrects : pseudo = test1, mdp = test123")
  public void testConnexionV3() throws FatalException, BusinessException {
    Mockito.when(utilisateurDAO.rechercheParPseudo(utilisateurDTO.getPseudo()))
        .thenReturn(utilisateurDTO);
    assertEquals(utilisateurDTO, utilisateurUCC.connexion("test1", "test123"));
  }

  @Test
  @DisplayName("Test réussi : méthode rechercheParId renvoie un utilisateur existant.")
  public void testRecherchePardIdV1() throws BusinessException, FatalException {
    int id = utilisateurDTO.getIdUtilisateur();
    Mockito.when(utilisateurDAO.rechercheParId(id)).thenReturn(utilisateurDTO);
    assertEquals(utilisateurDTO, utilisateurUCC.rechercheParId(id));
  }

  @Test
  @DisplayName("Test raté : méthode rechercheParId renvoie null car l'utilisateur "
      + "n'est pas trouvable.")
  public void testRecherchePardIdV2() throws FatalException {
    int id = -1;
    Mockito.when(utilisateurDAO.rechercheParId(id)).thenReturn(null);
    assertThrows(BusinessException.class, () -> utilisateurUCC.rechercheParId(id));
  }

}

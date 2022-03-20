package be.vinci.pae;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.adresse.AdresseDTO;
import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.objet.ObjetUCC;
import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.business.offre.OffreUCC;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import java.time.LocalDateTime;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class OffreUCCTest {

  private UtilisateurDTO utilisateurDTO;
  private ObjetUCC objetUCC;
  private OffreUCC offreUCC;
  private ObjetDAO objetDAO;
  private DomaineFactory domaineFactory;
  private AdresseDTO adresseDTO;
  private ObjetDTO objetDTO1;
  private ObjetDTO objetDTO2;
  private OffreDTO offreDTO1;
  private OffreDTO offreDTO2;


  @BeforeAll
  void initTout() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new MockApplicationBinder());
    this.domaineFactory = locator.getService(DomaineFactory.class);
    this.utilisateurDTO = locator.getService(UtilisateurDTO.class);
    this.offreUCC = locator.getService(OffreUCC.class);
    this.objetUCC = locator.getService(ObjetUCC.class);

    adresseDTO = domaineFactory.getAdresse();
    adresseDTO.setIdAdresse(1);
    adresseDTO.setRue("Rue");
    adresseDTO.setNumero(1);
    adresseDTO.setCodePostal(1040);
    adresseDTO.setCommune("Etterbeek");

    utilisateurDTO = domaineFactory.getUtilisateur();
    utilisateurDTO.setIdUtilisateur(1);
    utilisateurDTO.setPseudo("test1");
    utilisateurDTO.setMdp("$2a$10$jAXbw66kyec1S8RV/pnwo.kuEnAbmIsP5h7463ZkxGJocnx1WzLUy");
    utilisateurDTO.setEstAdmin(false);
    utilisateurDTO.setEtatInscription("confirmé");
    utilisateurDTO.setAdresse(adresseDTO);

    objetDTO1 = domaineFactory.getObjet();
    objetDTO1.setIdObjet(1);
    objetDTO1.setEtatObjet("interrese");
    objetDTO1.setDescription("salut");
    objetDTO1.setOffreur(utilisateurDTO);
    objetDTO1.setReceveur(null);
    objetDTO1.setPhoto("");
    objetDTO1.setTypeObjet("machine");

    objetDTO2 = domaineFactory.getObjet();
    objetDTO2.setIdObjet(2);
    objetDTO2.setEtatObjet("offert");
    objetDTO2.setDescription("salut");
    objetDTO2.setOffreur(utilisateurDTO);
    objetDTO2.setReceveur(null);
    objetDTO2.setPhoto("");
    objetDTO2.setTypeObjet("info");

    offreDTO1 = domaineFactory.getOffre();
    offreDTO1.setIdOffre(1);
    offreDTO1.setObjetDTO(objetDTO1);
    offreDTO1.setDateOffre(LocalDateTime.now());
    offreDTO1.setPlageHoraire("");

    offreDTO2 = domaineFactory.getOffre();
    offreDTO2.setIdOffre(2);
    offreDTO2.setObjetDTO(objetDTO2);
    offreDTO2.setDateOffre(LocalDateTime.now());
    offreDTO2.setPlageHoraire("");

  }

  /*@Test
  @DisplayName("Test reussi : méthode listOffresRecent")
  public void testlistOffresRecent() {
    Mockito.when(objetUCC.rechercheParId(objetDTO1.getIdObjet())).thenReturn(objetDTO1);
    assertEquals(offreUCC.listOffresRecent().get(1), offreDTO1);
  }*/

}

package be.vinci.pae;


import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.adresse.AdresseDTO;
import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.objet.ObjetUCC;
import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.business.offre.OffreUCC;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.donnees.dao.adresse.AdresseDAO;

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
  private DomaineFactory domaineFactory;
  private AdresseDTO adresseDTO;
  private AdresseDAO adresseDAO;
  private ObjetDTO objetDTO;
  private OffreDTO offreDTO;


  @BeforeAll
  void initTout(){
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
  }

}

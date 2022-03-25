package be.vinci.pae;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.DomaineFactoryImpl;
import be.vinci.pae.business.interet.InteretUCC;
import be.vinci.pae.business.interet.InteretUCCImpl;
import be.vinci.pae.business.offre.OffreUCC;
import be.vinci.pae.business.offre.OffreUCCImpl;
import be.vinci.pae.business.utilisateur.UtilisateurUCC;
import be.vinci.pae.business.utilisateur.UtilisateurUCCImpl;
import be.vinci.pae.donnees.dao.adresse.AdresseDAO;
import be.vinci.pae.donnees.dao.interet.InteretDAO;
import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import be.vinci.pae.donnees.dao.offre.OffreDAO;
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
import be.vinci.pae.donnees.services.ServiceBackendDAL;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.donnees.services.ServiceDALImpl;
import be.vinci.pae.utilitaires.ApplicationBinder;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.mockito.Mockito;

@Provider
public class MockApplicationBinder extends ApplicationBinder {

  private UtilisateurDAO utilisateurDAO = Mockito.mock(UtilisateurDAO.class);
  private AdresseDAO adresseDAO = Mockito.mock(AdresseDAO.class);
  private ServiceDAL serviceDAL = Mockito.mock(ServiceDAL.class);
  private OffreDAO offreDAO = Mockito.mock(OffreDAO.class);
  private ObjetDAO objetDAO = Mockito.mock(ObjetDAO.class);
  private InteretDAO interetDAO = Mockito.mock(InteretDAO.class);

  @Override
  protected void configure() {
    bind(DomaineFactoryImpl.class).to(DomaineFactory.class).in(Singleton.class);
    bind(UtilisateurUCCImpl.class).to(UtilisateurUCC.class).in(Singleton.class);
    bind(ServiceDALImpl.class).to(ServiceBackendDAL.class).in(Singleton.class);
    bind(utilisateurDAO).to(UtilisateurDAO.class);
    bind(serviceDAL).to(ServiceDAL.class);
    bind(adresseDAO).to(AdresseDAO.class);
    bind(interetDAO).to(InteretDAO.class);
    bind(offreDAO).to(OffreDAO.class);
    bind(objetDAO).to(ObjetDAO.class);
    bind(OffreUCCImpl.class).to(OffreUCC.class).in(Singleton.class);
    bind(InteretUCCImpl.class).to(InteretUCC.class).in(Singleton.class);
  }

}

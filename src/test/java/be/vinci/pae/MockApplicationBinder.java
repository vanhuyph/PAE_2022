package be.vinci.pae;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.DomaineFactoryImpl;
import be.vinci.pae.business.objet.ObjetImpl;
import be.vinci.pae.business.objet.ObjetUCC;
import be.vinci.pae.business.offre.OffreImpl;
import be.vinci.pae.business.offre.OffreUCC;
import be.vinci.pae.business.utilisateur.UtilisateurUCC;
import be.vinci.pae.business.utilisateur.UtilisateurUCCImpl;
import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import be.vinci.pae.donnees.dao.offre.OffreDAO;
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
import be.vinci.pae.utilitaires.ApplicationBinder;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.mockito.Mockito;

@Provider
public class MockApplicationBinder extends ApplicationBinder {

  private UtilisateurDAO utilisateurDAO = Mockito.mock(UtilisateurDAO.class);
  private OffreDAO offreDAO = Mockito.mock(OffreDAO.class);
  private ObjetDAO objetDAO = Mockito.mock(ObjetDAO.class);

  @Override
  protected void configure() {
    bind(DomaineFactoryImpl.class).to(DomaineFactory.class).in(Singleton.class);
    bind(UtilisateurUCCImpl.class).to(UtilisateurUCC.class).in(Singleton.class);
    bind(ObjetImpl.class).to(ObjetUCC.class).in(Singleton.class);
    bind(OffreImpl.class).to(OffreUCC.class).in(Singleton.class);
    bind(utilisateurDAO).to(UtilisateurDAO.class);
    bind(offreDAO).to(OffreDAO.class);
    bind(objetDAO).to(ObjetDAO.class);

  }

}

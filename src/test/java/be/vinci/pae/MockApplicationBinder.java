package be.vinci.pae;

import be.vinci.pae.business.domaine.DomaineFactory;
import be.vinci.pae.business.domaine.DomaineFactoryImpl;
import be.vinci.pae.business.ucc.UtilisateurUCC;
import be.vinci.pae.business.ucc.UtilisateurUCCImpl;
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
import be.vinci.pae.utilitaires.ApplicationBinder;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.mockito.Mockito;

@Provider
public class MockApplicationBinder extends ApplicationBinder {

  private UtilisateurDAO utilisateurDAO = Mockito.mock(UtilisateurDAO.class);

  @Override
  protected void configure() {
    bind(DomaineFactoryImpl.class).to(DomaineFactory.class).in(Singleton.class);
    bind(UtilisateurUCCImpl.class).to(UtilisateurUCC.class).in(Singleton.class);
    bind(utilisateurDAO).to(UtilisateurDAO.class);
  }

}

package be.vinci.pae.utilitaires;

import be.vinci.pae.business.domaine.DomaineFactory;
import be.vinci.pae.business.domaine.DomaineFactoryImpl;
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAOImpl;
import be.vinci.pae.donnees.services.ServicesDAL;
import be.vinci.pae.donnees.services.ServicesDALImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bind(DomaineFactoryImpl.class).to(DomaineFactory.class).in(Singleton.class);
    bind(UtilisateurDAOImpl.class).to(UtilisateurDAO.class).in(Singleton.class);
    bind(ServicesDALImpl.class).to(ServicesDAL.class).in(Singleton.class);
  }
}

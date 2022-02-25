package be.vinci.pae.utilitaires;

import be.vinci.pae.business.domaine.DomaineFactory;
import be.vinci.pae.business.domaine.DomaineFactoryImpl;
import be.vinci.pae.business.ucc.UtilisateurUCC;
import be.vinci.pae.business.ucc.UtilisateurUCCImpl;
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAOImpl;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.donnees.services.ServiceDALImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bind(DomaineFactoryImpl.class).to(DomaineFactory.class).in(Singleton.class);
    bind(UtilisateurDAOImpl.class).to(UtilisateurDAO.class).in(Singleton.class);
    bind(ServiceDALImpl.class).to(ServiceDAL.class).in(Singleton.class);
    bind(UtilisateurUCCImpl.class).to(UtilisateurUCC.class).in(Singleton.class);
  }
}

package be.vinci.pae.utilitaires;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.DomaineFactoryImpl;
import be.vinci.pae.business.interet.InteretUCC;
import be.vinci.pae.business.interet.InteretUCCImpl;
import be.vinci.pae.business.offre.OffreUCC;
import be.vinci.pae.business.offre.OffreUCCImpl;
import be.vinci.pae.business.typeobjet.TypeObjetUCC;
import be.vinci.pae.business.typeobjet.TypeObjetUCCImpl;
import be.vinci.pae.business.utilisateur.UtilisateurUCC;
import be.vinci.pae.business.utilisateur.UtilisateurUCCImpl;
import be.vinci.pae.donnees.dao.adresse.AdresseDAO;
import be.vinci.pae.donnees.dao.adresse.AdresseDAOImpl;
import be.vinci.pae.donnees.dao.interet.InteretDAO;
import be.vinci.pae.donnees.dao.interet.InteretDAOImpl;
import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import be.vinci.pae.donnees.dao.objet.ObjetDAOImpl;
import be.vinci.pae.donnees.dao.offre.OffreDAO;
import be.vinci.pae.donnees.dao.offre.OffreDAOImpl;
import be.vinci.pae.donnees.dao.typeobjet.TypeObjetDAO;
import be.vinci.pae.donnees.dao.typeobjet.TypeObjetDAOImpl;
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAOImpl;
import be.vinci.pae.donnees.services.ServiceBackendDAL;
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
    bind(ServiceDALImpl.class).to(ServiceBackendDAL.class).to(ServiceDAL.class).in(Singleton.class);
    bind(UtilisateurUCCImpl.class).to(UtilisateurUCC.class).in(Singleton.class);
    bind(AdresseDAOImpl.class).to(AdresseDAO.class).in(Singleton.class);
    bind(OffreDAOImpl.class).to(OffreDAO.class).in(Singleton.class);
    bind(OffreUCCImpl.class).to(OffreUCC.class).in(Singleton.class);
    bind(ObjetDAOImpl.class).to(ObjetDAO.class).in(Singleton.class);
    bind(TypeObjetDAOImpl.class).to(TypeObjetDAO.class).in(Singleton.class);
    bind(TypeObjetUCCImpl.class).to(TypeObjetUCC.class).in(Singleton.class);
    bind(TypeObjetDAOImpl.class).to(TypeObjetDAO.class).in(Singleton.class);
    bind(InteretUCCImpl.class).to(InteretUCC.class).in(Singleton.class);
    bind(InteretDAOImpl.class).to(InteretDAO.class).in(Singleton.class);
  }

}

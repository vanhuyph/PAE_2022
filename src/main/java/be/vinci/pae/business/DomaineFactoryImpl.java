package be.vinci.pae.business;


import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.objet.ObjetImpl;
import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.business.offre.OffreImpl;
import be.vinci.pae.business.adresse.AdresseDTO;
import be.vinci.pae.business.adresse.AdresseImpl;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.business.utilisateur.UtilisateurImpl;

public class DomaineFactoryImpl implements DomaineFactory {

  @Override
  public UtilisateurDTO getUtilisateur() {
    return new UtilisateurImpl();
  }

  @Override

  public OffreDTO getOffre() {
    return new OffreImpl();
  }

  @Override
  public ObjetDTO getObjet() {
    return new ObjetImpl();
  }

  public AdresseDTO getAdresse() {
    return new AdresseImpl();
  }

}

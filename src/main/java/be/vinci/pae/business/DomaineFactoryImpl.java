package be.vinci.pae.business;

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
  public AdresseDTO getAdresse() {
    return new AdresseImpl();
  }

}

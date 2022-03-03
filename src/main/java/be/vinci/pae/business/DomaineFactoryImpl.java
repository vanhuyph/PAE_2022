package be.vinci.pae.business;

import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.business.utilisateur.UtilisateurImpl;

public class DomaineFactoryImpl implements DomaineFactory {

  @Override
  public UtilisateurDTO getUtilisateur() {
    return new UtilisateurImpl();
  }

}

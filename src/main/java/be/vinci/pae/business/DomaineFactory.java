package be.vinci.pae.business;

import be.vinci.pae.business.adresse.AdresseDTO;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;

public interface DomaineFactory {

  UtilisateurDTO getUtilisateur();

  AdresseDTO getAdresse();

  Object getObjet();
}

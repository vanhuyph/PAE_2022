package be.vinci.pae.business.adresse;

import be.vinci.pae.utilitaires.exceptions.BusinessException;
import be.vinci.pae.utilitaires.exceptions.FatalException;

public interface AdresseUCC {

  AdresseDTO ajouterAdresse(String rue, int numero, int boite, int codePostal,
      String commune) throws FatalException, BusinessException;
}

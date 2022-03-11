package be.vinci.pae.donnees.dao.adresse;

import be.vinci.pae.business.adresse.AdresseDTO;
import be.vinci.pae.utilitaires.exceptions.FatalException;

public interface AdresseDAO {

  AdresseDTO ajouterAdresse(String rue, int numero, int boite, int codePostal,
      String commune) throws FatalException;

}

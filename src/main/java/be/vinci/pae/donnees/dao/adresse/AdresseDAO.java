package be.vinci.pae.donnees.dao.adresse;

import be.vinci.pae.business.adresse.AdresseDTO;

public interface AdresseDAO {

  AdresseDTO ajouterAdresse(String rue, int numero, int boite, int codePostal,
      String commune);
  
}

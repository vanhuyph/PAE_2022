package be.vinci.pae.donnees.dao.adresse;

import be.vinci.pae.business.adresse.AdresseDTO;

public interface AdresseDAO {

  AdresseDTO rechercheParId(int id);

  AdresseDTO ajouterAdresse(AdresseDTO adresseDTO);

  AdresseDTO miseAJourAdresse(AdresseDTO adresseDTO);

}

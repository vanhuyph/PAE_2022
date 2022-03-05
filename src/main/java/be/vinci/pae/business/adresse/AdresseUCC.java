package be.vinci.pae.business.adresse;

public interface AdresseUCC {

  AdresseDTO ajouterAdresse(String rue, int numero, int boite, int codePostal,
      String commune);
}

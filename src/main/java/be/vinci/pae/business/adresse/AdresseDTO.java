package be.vinci.pae.business.adresse;

public interface AdresseDTO {

  int getIdAdresse();

  void setIdAdresse(int idAdresse);

  String getRue();

  void setRue(String rue);

  int getNumero();

  void setNumero(int numero);

  int getBoite();

  void setBoite(int boite);

  int getCodePostal();

  void setCodePostal(int codePostal);

  String getCommune();

  void setCommune(String commune);
}

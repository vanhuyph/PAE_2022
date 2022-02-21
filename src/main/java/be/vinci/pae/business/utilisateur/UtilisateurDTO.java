package be.vinci.pae.business.utilisateur;

public interface UtilisateurDTO {

  int getIdUtilisateur();

  void setIdUtilisateur(int idUtilisateur);

  String getPseudo();

  void setPseudo(String pseudo);

  String getNom();

  void setNom(String nom);

  String getPrenom();

  void setPrenom(String prenom);

  String getMdp();

  void setMdp(String mdp);

  String getGsm();

  void setGsm(String gsm);

  boolean isEstAdmin();

  void setEstAdmin(boolean estAdmin);
}

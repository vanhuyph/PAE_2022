package be.vinci.pae.business.utilisateur;

import be.vinci.pae.business.adresse.AdresseDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UtilisateurImpl.class)
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

  String getEtatInscription();

  void setEtatInscription(String etatInscription);

  String getCommentaire();

  void setCommentaire(String commentaire);

  AdresseDTO getAdresse();

  void setAdresse(AdresseDTO adresse);

}

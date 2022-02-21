package be.vinci.pae.donnees.utilisateur;

public interface Utilisateur extends UtilisateurDTO {

  boolean verifierMdp(String mdp);

  String hashMdp(String mdp);
}

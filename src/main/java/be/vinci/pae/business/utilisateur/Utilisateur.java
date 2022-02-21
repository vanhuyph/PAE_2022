package be.vinci.pae.business.utilisateur;

public interface Utilisateur extends UtilisateurDTO {

  boolean verifierMdp(String mdp);

  String hashMdp(String mdp);
}

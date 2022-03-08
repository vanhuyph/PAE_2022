package be.vinci.pae.business.utilisateur;

public interface UtilisateurUCC {

  UtilisateurDTO connexion(String pseudo, String mdp);

  UtilisateurDTO rechercheParId(int id);

  UtilisateurDTO rechercheParPseudo(String pseudo);

  UtilisateurDTO inscription(String pseudo, String nom, String prenom, String mdp,
      int adresse);

  UtilisateurDTO confirmerInscription(int id, boolean estAdmin);
}

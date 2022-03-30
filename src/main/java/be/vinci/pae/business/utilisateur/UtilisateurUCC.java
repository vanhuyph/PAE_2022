package be.vinci.pae.business.utilisateur;

import java.util.List;

public interface UtilisateurUCC {

  UtilisateurDTO connexion(String pseudo, String mdp);

  UtilisateurDTO rechercheParId(int id);

  UtilisateurDTO rechercheParPseudo(String pseudo);

  UtilisateurDTO inscription(UtilisateurDTO utilisateurDTO);

  UtilisateurDTO confirmerInscription(int id, boolean estAdmin);

  UtilisateurDTO refuserInscription(int id, String commentaire);

  List<UtilisateurDTO> listerUtilisateursEtatsInscriptions(String etatInscription);

<<<<<<< HEAD
  UtilisateurDTO miseAJourInfo(UtilisateurDTO utilisateur);
=======
>>>>>>> 7c87eaba1a636c9f1ce51d2578b509b32dcd7e5f
}

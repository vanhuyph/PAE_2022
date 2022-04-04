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

  UtilisateurDTO miseAJourUtilisateur(UtilisateurDTO utilisateur);

  UtilisateurDTO modifierMdp(UtilisateurDTO utilisateurDTO);
}

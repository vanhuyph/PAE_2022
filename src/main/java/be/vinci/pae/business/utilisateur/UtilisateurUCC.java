package be.vinci.pae.business.utilisateur;

import java.util.List;

public interface UtilisateurUCC {

  UtilisateurDTO connexion(String pseudo, String mdp);

  UtilisateurDTO rechercheParId(int id);

  UtilisateurDTO rechercheParPseudo(String pseudo);

  UtilisateurDTO inscription(UtilisateurDTO utilisateurDTO);

  UtilisateurDTO confirmerInscription(UtilisateurDTO utilisateurDTO);

  UtilisateurDTO refuserInscription(int id, String commentaire);

  List<UtilisateurDTO> listerUtilisateursEtatsInscriptions(String etatInscription);

  UtilisateurDTO miseAJourInfo(UtilisateurDTO utilisateur);

}

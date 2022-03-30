package be.vinci.pae.donnees.dao.utilisateur;

import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import java.util.List;

public interface UtilisateurDAO {

  UtilisateurDTO rechercheParPseudo(String pseudo);

  UtilisateurDTO rechercheParId(int id);

  UtilisateurDTO ajouterUtilisateur(UtilisateurDTO utilisateur);

  UtilisateurDTO confirmerInscription(int id, boolean estAdmin);

<<<<<<< HEAD
  List<UtilisateurDTO> listerUtilisateursEtatsInscriptions(String etatInscription);

  UtilisateurDTO refuserInscription(int id, String commentaire);

  UtilisateurDTO miseAJourInfo(UtilisateurDTO utilisateur);
=======
  UtilisateurDTO modifierGsm(UtilisateurDTO utilisateurDTO);

  List<UtilisateurDTO> listerUtilisateursEtatsInscriptions(String etatInscription);

  UtilisateurDTO refuserInscription(int id, String commentaire);

>>>>>>> 7c87eaba1a636c9f1ce51d2578b509b32dcd7e5f
}

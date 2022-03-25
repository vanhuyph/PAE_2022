package be.vinci.pae.donnees.dao.utilisateur;

import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import java.util.List;

public interface UtilisateurDAO {

  UtilisateurDTO rechercheParPseudo(String pseudo);

  UtilisateurDTO rechercheParId(int id);

  UtilisateurDTO ajouterUtilisateur(UtilisateurDTO utilisateur);

  UtilisateurDTO confirmerInscription(int id, boolean estAdmin);

  List<UtilisateurDTO> listerUtilisateursEtatsInscriptions(String etatInscription);

  UtilisateurDTO refuserInscription(int id, String commentaire);

}

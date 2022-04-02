package be.vinci.pae.donnees.dao.utilisateur;

import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import java.util.List;

public interface UtilisateurDAO {

  UtilisateurDTO rechercheParPseudo(String pseudo);

  UtilisateurDTO rechercheParId(int id);

  UtilisateurDTO ajouterUtilisateur(UtilisateurDTO utilisateur);

  UtilisateurDTO miseAJourUtilisateur(UtilisateurDTO utilisateurDTO);

  UtilisateurDTO modifierGsm(UtilisateurDTO utilisateurDTO);

  List<UtilisateurDTO> listerUtilisateursEtatsInscriptions(String etatInscription);

}

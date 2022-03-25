package be.vinci.pae.donnees.dao.utilisateur;

import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.utilitaires.exceptions.FatalException;
import java.util.List;

public interface UtilisateurDAO {

  UtilisateurDTO rechercheParPseudo(String pseudo) throws FatalException;

  UtilisateurDTO rechercheParId(int id) throws FatalException;

  UtilisateurDTO ajouterUtilisateur(UtilisateurDTO utilisateur) throws FatalException;

  UtilisateurDTO confirmerInscription(int id, boolean estAdmin) throws FatalException;

  UtilisateurDTO modifierGsm(UtilisateurDTO utilisateurDTO);

  List<UtilisateurDTO> listerUtilisateursEtatsInscriptions(String etatInscription)
      throws FatalException;

  UtilisateurDTO refuserInscription(int id, String commentaire) throws FatalException;
}

package be.vinci.pae.business.utilisateur;

import be.vinci.pae.utilitaires.exceptions.BusinessException;
import be.vinci.pae.utilitaires.exceptions.FatalException;
import java.util.List;

public interface UtilisateurUCC {

  UtilisateurDTO connexion(String pseudo, String mdp) throws FatalException, BusinessException;

  UtilisateurDTO rechercheParId(int id) throws FatalException, BusinessException;

  UtilisateurDTO rechercheParPseudo(String pseudo) throws FatalException, BusinessException;

  UtilisateurDTO rechercheParPseudoInscription(String pseudo)
      throws FatalException, BusinessException;

  UtilisateurDTO inscription(String pseudo, String nom, String prenom, String mdp,
      int adresse) throws FatalException, BusinessException;

  UtilisateurDTO confirmerInscription(int id, boolean estAdmin)
      throws FatalException, BusinessException;

  UtilisateurDTO refuserInscription(int id, String commentaire)
      throws FatalException, BusinessException;

  List<UtilisateurDTO> listerUtilisateursEtatsInscriptions(String etatInscription)
      throws FatalException;
}

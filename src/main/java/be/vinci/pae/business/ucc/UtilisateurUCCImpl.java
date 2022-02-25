package be.vinci.pae.business.ucc;

import be.vinci.pae.business.domaine.Utilisateur;
import be.vinci.pae.business.domaine.UtilisateurDTO;
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
import jakarta.inject.Inject;

public class UtilisateurUCCImpl implements UtilisateurUCC {

  @Inject
  UtilisateurDAO utilisateurDAO;

  /**
   * Vérifie si le mot de passe de l'utilisateur est correct à sa connexion.
   *
   * @param pseudo : le pseudo de l'utilisateur
   * @param mdp    : le mot de passe de l'utilisateur
   * @return : l'utilisateur si le mot de passe est bon
   * @throws IllegalStateException : est lancée si le mot de passe est incorrect
   */
  @Override
  public UtilisateurDTO connexion(String pseudo, String mdp) {

    Utilisateur utilisateur = (Utilisateur) utilisateurDAO.rechercheParPseudo(pseudo);

    if (utilisateur.verifierMdp(mdp)) {
      return utilisateur;
    } else {
      throw new IllegalStateException("Non autorisé");
    }
  }

  /**
   * Renvoie un utilisateur en fonction de son id.
   *
   * @param id : l'id de l'utilisateur
   * @return : l'utilisateur possèdant l'id passé en paramètre
   */
  @Override
  public UtilisateurDTO rechercheParId(int id) {
    return utilisateurDAO.rechercheParId(id);
  }

}

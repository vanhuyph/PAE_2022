package be.vinci.pae.business.utilisateur;

import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
import be.vinci.pae.utilitaires.exceptions.ExceptionBusiness;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response.Status;

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
      throw new ExceptionBusiness("Pseudo ou mot de passe incorrect.", Status.UNAUTHORIZED);
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
    UtilisateurDTO utilisateur = utilisateurDAO.rechercheParId(id);
    if (utilisateur == null) {
      throw new ExceptionBusiness("L'utilisateur n'existe pas.", Status.BAD_REQUEST);
    }
    return utilisateur;
  }

}

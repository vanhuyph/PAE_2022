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
   * @throws ExceptionBusiness : est lancée si le mot de passe est incorrect
   */
  @Override
  public UtilisateurDTO connexion(String pseudo, String mdp) {
    Utilisateur utilisateur = (Utilisateur) utilisateurDAO.rechercheParPseudo(pseudo);
    if (utilisateur == null || !utilisateur.verifierMdp(mdp)) {
      throw new ExceptionBusiness("Pseudo ou mot de passe incorrect.", Status.UNAUTHORIZED);
    }
    return utilisateur;
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

  /**
   * Vérifie si l'utilisateur a bien été ajouté et hash le mot de passe
   *
   * @param pseudo
   * @param nom
   * @param prenom
   * @param mdp
   * @return
   */
  @Override
  public UtilisateurDTO inscription(String pseudo, String nom, String prenom, String mdp,
      int adresse) {

    Utilisateur utilisateur = new UtilisateurImpl();
    utilisateur.setPseudo(pseudo);
    utilisateur.setNom(nom);
    utilisateur.setPrenom(prenom);
    utilisateur.setMdp(utilisateur.hashMdp(mdp));
    utilisateur.setAdresse(adresse);
    utilisateur.setEtatInscription("en attente");
    utilisateur.setCommentaire(null);

    utilisateur = (Utilisateur) utilisateurDAO.ajouterUtilisateur(utilisateur);
    if (utilisateur == null) {
      throw new ExceptionBusiness("L'utilisateur n'a pas pu être ajouté",
          Status.BAD_REQUEST);
    }
    return utilisateur;
  }


}

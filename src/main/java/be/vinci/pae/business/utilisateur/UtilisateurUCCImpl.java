package be.vinci.pae.business.utilisateur;

import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
import be.vinci.pae.utilitaires.exceptions.ExceptionBusiness;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;

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
    if (utilisateur == null || utilisateur.getIdUtilisateur() < 1 || !utilisateur.verifierMdp(
        mdp)) {
      throw new ExceptionBusiness("Pseudo ou mot de passe incorrect.",
          Status.UNAUTHORIZED);
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
    if (utilisateur == null || utilisateur.getIdUtilisateur() < 1) {
      throw new ExceptionBusiness("L'utilisateur n'existe pas.", Status.BAD_REQUEST);
    }
    return utilisateur;
  }

  /**
   * Renvoie un utilisateur en fonction de son pseudo.
   *
   * @param pseudo : le pseudo de l'utilisateur
   * @return : l'utilisateur possèdant l'id passé en paramètre
   */
  @Override
  public UtilisateurDTO rechercheParPseudo(String pseudo) {
    UtilisateurDTO utilisateur = utilisateurDAO.rechercheParPseudo(pseudo);
    if (utilisateur == null || utilisateur.getIdUtilisateur() < 1) {
      throw new ExceptionBusiness("L'utilisateur n'existe pas.", Status.BAD_REQUEST);
    }
    return utilisateur;
  }

  /**
   * Verifie si un utilisateur avec ce pseudo existe.
   *
   * @param pseudo : le pseudo de l'utilisateur
   * @return : l'utilisateur vide si aucun utilisateur correspond au pseudo
   * @throws ExceptionBusiness : lancé si un utilisateur possede ce pseudo
   */
  @Override
  public UtilisateurDTO rechercheParPseudoInscription(String pseudo) {
    UtilisateurDTO utilisateur = utilisateurDAO.rechercheParPseudo(pseudo);
    if (utilisateur == null || utilisateur.getIdUtilisateur() > 0) {
      throw new ExceptionBusiness("L'utilisateur existe déjà.", Status.BAD_REQUEST);
    }
    return utilisateur;
  }

  /**
   * Vérifie si l'utilisateur a bien été ajouté et hash le mot de passe.
   *
   * @param pseudo  : le pseudo de l'utilisateur
   * @param nom     : le nom de l'utilisateur
   * @param prenom  : de l'utilisateur
   * @param mdp     : le mot de passe de l'utilisateur
   * @param adresse : l'id de l'adresse de l'utilisateur
   * @return
   */
  @Override
  public UtilisateurDTO inscription(String pseudo, String nom, String prenom, String mdp,
      int adresse) {

    if (utilisateurDAO.rechercheParPseudo(pseudo).getIdUtilisateur() > 0) {
      throw new ExceptionBusiness("Pseudo déjà utilisé",
          Status.CONFLICT);
    }

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

  /**
   * Vérifie si l'utilisateur a été confirmé.
   *
   * @param id       :       l'id de l'utilisateur
   * @param estAdmin : si l'utilisateur est admin
   * @return utilisateurDTO : l'utilisateur confirmé
   */
  @Override
  public UtilisateurDTO confirmerInscription(int id, boolean estAdmin) {
    UtilisateurDTO utilisateurDTO = utilisateurDAO.confirmerInscription(id, estAdmin);
    if (utilisateurDTO == null || utilisateurDTO.getIdUtilisateur() < 1) {
      throw new ExceptionBusiness("L'utilisateur n'a pas pu être confirmé", Status.BAD_REQUEST);
    }
    return utilisateurDTO;
  }

  /**
   * Vérifie si l'utilisateur a été refusé.
   *
   * @param id          : l'id de l'utilisateur
   * @param commentaire : le commentaire du refus
   * @return utilisateurDTO : l'utilisateur refusé
   */
  @Override
  public UtilisateurDTO refuserInscription(int id, String commentaire) {
    UtilisateurDTO utilisateurDTO = utilisateurDAO.refuserInscription(id, commentaire);
    if (utilisateurDTO == null || utilisateurDTO.getIdUtilisateur() < 1) {
      throw new ExceptionBusiness("L'utilisateur n'a pas pu être refusé", Status.BAD_REQUEST);
    }
    return utilisateurDTO;
  }

  /**
   * Liste les utilisateurs en fonction de l'état de leur inscription.
   *
   * @param etatInscription : l'état de l'inscription
   * @return liste : la liste des utilisateurs avec l'état d'inscription passé en paramètre
   */
  @Override
  public List<UtilisateurDTO> listerUtilisateursEtatsInscriptions(String etatInscription) {
    List<UtilisateurDTO> liste = utilisateurDAO.listerUtilisateursEtatsInscriptions(
        etatInscription);
    return liste;
  }

}

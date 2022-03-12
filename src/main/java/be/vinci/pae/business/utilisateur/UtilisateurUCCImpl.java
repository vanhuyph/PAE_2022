package be.vinci.pae.business.utilisateur;

import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import be.vinci.pae.utilitaires.exceptions.FatalException;
import jakarta.inject.Inject;
import java.util.List;

public class UtilisateurUCCImpl implements UtilisateurUCC {

  @Inject
  UtilisateurDAO utilisateurDAO;

  /**
   * Vérifie si le mot de passe de l'utilisateur est correct à sa connexion.
   *
   * @param pseudo : le pseudo de l'utilisateur
   * @param mdp    : le mot de passe de l'utilisateur
   * @return utilisateur : l'utilisateur si le mot de passe est bon
   * @throws BusinessException : est lancée si le pseudo ou le mot de passe est incorrect
   * @throws FatalException    : est lancée si il y a un problème côté serveur
   */
  @Override
  public UtilisateurDTO connexion(String pseudo, String mdp)
      throws FatalException, BusinessException {
    Utilisateur utilisateur = (Utilisateur) utilisateurDAO.rechercheParPseudo(pseudo);
    if (utilisateur == null || utilisateur.getIdUtilisateur() < 1 || !utilisateur.verifierMdp(
        mdp)) {
      throw new BusinessException("Pseudo ou mot de passe incorrect");
    }
    return utilisateur;
  }

  /**
   * Renvoie un utilisateur en fonction de son id.
   *
   * @param id : l'id de l'utilisateur
   * @return utilisateur : l'utilisateur possèdant l'id passé en paramètre
   * @throws BusinessException : est lancée si l'utilisateur avec l'id passé en paramètre n'est pas
   *                           trouvé
   * @throws FatalException    : est lancée si il y a un problème côté serveur
   */
  @Override
  public UtilisateurDTO rechercheParId(int id) throws FatalException, BusinessException {
    UtilisateurDTO utilisateur = utilisateurDAO.rechercheParId(id);
    if (utilisateur == null || utilisateur.getIdUtilisateur() < 1) {
      throw new BusinessException("L'utilisateur n'existe pas");
    }
    return utilisateur;
  }

  /**
   * Renvoie un utilisateur en fonction de son pseudo.
   *
   * @param pseudo : le pseudo de l'utilisateur
   * @return utilisateur : l'utilisateur possèdant l'id passé en paramètre
   * @throws BusinessException : est lancée si l'utilisateur avec le pseudo passé en paramètre n'est
   *                           pas trouvé
   * @throws FatalException    : est lancée si il y a un problème côté serveur
   */
  @Override
  public UtilisateurDTO rechercheParPseudo(String pseudo) throws FatalException, BusinessException {
    UtilisateurDTO utilisateur = utilisateurDAO.rechercheParPseudo(pseudo);
    if (utilisateur == null || utilisateur.getIdUtilisateur() < 1) {
      throw new BusinessException("L'utilisateur n'existe pas");
    }
    return utilisateur;
  }

  /**
   * Verifie si un utilisateur avec le pseudo passé en paramètre existe déjà lors de l'inscription.
   *
   * @param pseudo : le pseudo de l'utilisateur
   * @return utilisateur : l'utilisateur vide si aucun utilisateur correspond au pseudo
   * @throws BusinessException : est lancée si un utilisateur possède déjà le pseudo passé en
   *                           paramètre
   * @throws FatalException    : est lancée si il y a un problème côté serveur
   */
  @Override
  public UtilisateurDTO rechercheParPseudoInscription(String pseudo)
      throws FatalException, BusinessException {
    UtilisateurDTO utilisateur = utilisateurDAO.rechercheParPseudo(pseudo);
    if (utilisateur == null || utilisateur.getIdUtilisateur() > 0) {
      throw new BusinessException("Ce pseudo existe déjà");
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
   * @return utilisateur : l'utilisateur inscrit
   * @throws BusinessException : est lancée si un utilisateur possède déjà le pseudo
   * @throws BusinessException : est lancée si l'utilisateur n'a pas pu être ajouté
   * @throws FatalException    : est lancée si il y a un problème côté serveur
   */
  @Override
  public UtilisateurDTO inscription(String pseudo, String nom, String prenom, String mdp,
      int adresse) throws FatalException, BusinessException {
    if (utilisateurDAO.rechercheParPseudo(pseudo).getIdUtilisateur() > 0) {
      throw new BusinessException("Pseudo déjà utilisé");
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
      throw new BusinessException("L'utilisateur n'a pas pu être ajouté");
    }
    return utilisateur;
  }

  /**
   * Confirme l'inscription d'un utilisateur et met son statut en Admin si l'information a été
   * renseigné.
   *
   * @param id       :       l'id de l'utilisateur
   * @param estAdmin : si l'utilisateur est admin
   * @return utilisateurDTO : l'utilisateur avec son état d'inscription passé à "confirmé"
   * @throws BusinessException : est lancée si l'état d'inscription de l'utilisateur n'a pas pu être
   *                           confirmé
   * @throws FatalException    : est lancée si il y a un problème côté serveur
   */
  @Override
  public UtilisateurDTO confirmerInscription(int id, boolean estAdmin)
      throws FatalException, BusinessException {
    UtilisateurDTO utilisateurDTO = utilisateurDAO.confirmerInscription(id, estAdmin);
    if (utilisateurDTO == null || utilisateurDTO.getIdUtilisateur() < 1) {
      throw new BusinessException("L'inscription de l'utilisateur n'a pas pu être confirmé");
    }
    return utilisateurDTO;
  }

  /**
   * Refuse l'inscription d'un utilisateur accompagné d'un commentaire justifiant ce refus.
   *
   * @param id          : l'id de l'utilisateur
   * @param commentaire : le commentaire du refus
   * @return utilisateurDTO : l'utilisateur avec l'inscription refusée
   * @throws BusinessException : est lancée si l'état d'inscription de l'utilisateur n'a pas pu être
   *                           refusé
   * @throws FatalException    : est lancée si il y a un problème côté serveur
   */
  @Override
  public UtilisateurDTO refuserInscription(int id, String commentaire)
      throws FatalException, BusinessException {
    UtilisateurDTO utilisateurDTO = utilisateurDAO.refuserInscription(id, commentaire);
    if (utilisateurDTO == null || utilisateurDTO.getIdUtilisateur() < 1) {
      throw new BusinessException("L'inscription de l'utilisateur n'a pas pu être refusé");
    }
    return utilisateurDTO;
  }

  /**
   * Liste les utilisateurs en fonction de l'état de leur inscription.
   *
   * @param etatInscription : l'état de l'inscription
   * @return liste : la liste des utilisateurs avec l'état d'inscription passé en paramètre
   * @throws FatalException : est lancée si il y a un problème côté serveur
   */
  @Override
  public List<UtilisateurDTO> listerUtilisateursEtatsInscriptions(String etatInscription)
      throws FatalException {
    List<UtilisateurDTO> liste = utilisateurDAO.listerUtilisateursEtatsInscriptions(
        etatInscription);
    return liste;
  }

}

package be.vinci.pae.business.utilisateur;

import be.vinci.pae.business.adresse.AdresseDTO;
import be.vinci.pae.donnees.dao.adresse.AdresseDAO;
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import be.vinci.pae.utilitaires.exceptions.ConflitException;
import be.vinci.pae.utilitaires.exceptions.NonAutoriseException;
import jakarta.inject.Inject;
import java.util.List;

public class UtilisateurUCCImpl implements UtilisateurUCC {

  @Inject
  UtilisateurDAO utilisateurDAO;
  @Inject
  AdresseDAO adresseDAO;
  @Inject
  ServiceDAL serviceDAL;

  /**
   * Vérifie si le mot de passe de l'utilisateur est correct à sa connexion.
   *
   * @param pseudo : le pseudo de l'utilisateur
   * @param mdp    : le mot de passe de l'utilisateur
   * @return utilisateur : l'utilisateur si le mot de passe est bon
   * @throws NonAutoriseException : est lancée si le pseudo ou le mot de passe est incorrect
   */
  @Override
  public UtilisateurDTO connexion(String pseudo, String mdp) {
    serviceDAL.commencerTransaction();
    Utilisateur utilisateur = (Utilisateur) utilisateurDAO.rechercheParPseudo(pseudo);
    if (utilisateur == null || utilisateur.getIdUtilisateur() < 1 || !utilisateur.verifierMdp(
        mdp)) {
      serviceDAL.retourEnArriereTransaction();
      throw new NonAutoriseException("Pseudo ou mot de passe incorrect");
    }
    serviceDAL.commettreTransaction();
    return utilisateur;
  }

  /**
   * Renvoie un utilisateur en fonction de son id.
   *
   * @param id : l'id de l'utilisateur
   * @return utilisateur : l'utilisateur possèdant l'id passé en paramètre
   * @throws BusinessException : est lancée si l'utilisateur avec l'id passé en paramètre n'est pas
   *                           trouvé
   */
  @Override
  public UtilisateurDTO rechercheParId(int id) {
    serviceDAL.commencerTransaction();
    UtilisateurDTO utilisateur = utilisateurDAO.rechercheParId(id);
    if (utilisateur == null || utilisateur.getIdUtilisateur() < 1) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("L'utilisateur n'existe pas");
    }
    serviceDAL.commettreTransaction();
    return utilisateur;
  }

  /**
   * Renvoie un utilisateur en fonction de son pseudo.
   *
   * @param pseudo : le pseudo de l'utilisateur
   * @return utilisateur : l'utilisateur possèdant l'id passé en paramètre
   * @throws BusinessException : est lancée si l'utilisateur avec le pseudo passé en paramètre n'est
   *                           pas trouvé
   */
  @Override
  public UtilisateurDTO rechercheParPseudo(String pseudo) {
    serviceDAL.commencerTransaction();
    UtilisateurDTO utilisateur = utilisateurDAO.rechercheParPseudo(pseudo);
    if (utilisateur == null || utilisateur.getIdUtilisateur() < 1) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("L'utilisateur n'existe pas");
    }
    serviceDAL.commettreTransaction();
    return utilisateur;
  }

  /**
   * Permet l'inscription d'un utilisateur.
   *
   * @param utilisateurDTO : l'utilisateur à inscrire
   * @return utilisateur : l'utilisateur inscrit
   * @throws ConflitException  : est lancée si un utilisateur possède déjà le pseudo
   * @throws BusinessException : est lancée si l'utilisateur/adresse n'a pas pu être ajouté
   */
  @Override
  public UtilisateurDTO inscription(UtilisateurDTO utilisateurDTO) {
    serviceDAL.commencerTransaction();
    Utilisateur utilisateur = (Utilisateur) utilisateurDTO;
    utilisateur.setMdp(utilisateur.hashMdp(utilisateur.getMdp()));
    if (utilisateurDAO.rechercheParPseudo(utilisateurDTO.getPseudo()).getIdUtilisateur() > 0) {
      serviceDAL.retourEnArriereTransaction();
      throw new ConflitException("Ce pseudo est déjà utilisé");
    }
    AdresseDTO adresseDTO = adresseDAO.ajouterAdresse(utilisateurDTO.getAdresse());
    if (adresseDTO == null) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("L'adresse n'a pas pu être ajoutée.");
    }
    UtilisateurDTO utilisateurARenvoyer = utilisateurDAO.ajouterUtilisateur(utilisateurDTO);
    if (utilisateurARenvoyer == null) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("L'utilisateur n'a pas pu être ajouté");
    }
    serviceDAL.commettreTransaction();
    return utilisateurARenvoyer;
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
   */
  @Override
  public UtilisateurDTO confirmerInscription(int id, boolean estAdmin) {
    serviceDAL.commencerTransaction();
    UtilisateurDTO utilisateurDTO = utilisateurDAO.confirmerInscription(id, estAdmin);
    if (utilisateurDTO == null || utilisateurDTO.getIdUtilisateur() < 1) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("L'inscription de l'utilisateur n'a pas pu être confirmée");
    }
    serviceDAL.commettreTransaction();
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
   */
  @Override
  public UtilisateurDTO refuserInscription(int id, String commentaire) {
    serviceDAL.commencerTransaction();
    UtilisateurDTO utilisateurDTO = utilisateurDAO.refuserInscription(id, commentaire);
    if (utilisateurDTO == null || utilisateurDTO.getIdUtilisateur() < 1) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("L'inscription de l'utilisateur n'a pas pu être refusée");
    }
    serviceDAL.commettreTransaction();
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
    serviceDAL.commencerTransaction();
    List<UtilisateurDTO> liste = utilisateurDAO.listerUtilisateursEtatsInscriptions(
        etatInscription);
    serviceDAL.commettreTransaction();
    return liste;
  }

  /**
   * Met à jour les informations de l'utilisateur.
   *
   * @param utilisateur : l'utilisateur à mettre à jour
   * @return utilisateurDTO : l'utilisateur avec ses informations mises à jour
   */
  @Override
  public UtilisateurDTO miseAJourInfo(UtilisateurDTO utilisateur) {
    serviceDAL.commencerTransaction();
    UtilisateurDTO utilisateurDTO = utilisateurDAO.miseAJourInfo(utilisateur);
    if (utilisateurDTO == null) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("L'utilisateur n'existe pas");
    }
    serviceDAL.commettreTransaction();
    return utilisateurDTO;
  }

  /**
   * Liste tous les utilisateurs en fonction d'un critère de recherche (nom, code postal ou ville).
   *
   * @param recherche : le critère de recherche
   * @return liste : la liste des utilisateurs correspondant au critère de recherche passé en
   * paramètre
   */
  @Override
  public List<UtilisateurDTO> rechercherMembres(String recherche) {
    serviceDAL.commencerTransaction();
    List<UtilisateurDTO> liste = utilisateurDAO.rechercherMembres(recherche);
    serviceDAL.commettreTransaction();
    return liste;
  }

}

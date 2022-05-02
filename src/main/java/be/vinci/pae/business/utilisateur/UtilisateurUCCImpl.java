package be.vinci.pae.business.utilisateur;

import be.vinci.pae.business.adresse.AdresseDTO;
import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.offre.Offre;
import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.donnees.dao.adresse.AdresseDAO;
import be.vinci.pae.donnees.dao.interet.InteretDAO;
import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import be.vinci.pae.donnees.dao.offre.OffreDAO;
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import be.vinci.pae.utilitaires.exceptions.ConflitException;
import be.vinci.pae.utilitaires.exceptions.NonAutoriseException;
import be.vinci.pae.utilitaires.exceptions.OptimisticLockException;
import be.vinci.pae.utilitaires.exceptions.PasTrouveException;
import jakarta.inject.Inject;
import java.util.List;

public class UtilisateurUCCImpl implements UtilisateurUCC {

  @Inject
  UtilisateurDAO utilisateurDAO;
  @Inject
  AdresseDAO adresseDAO;
  @Inject
  OffreDAO offreDAO;
  @Inject
  ServiceDAL serviceDAL;
  @Inject
  ObjetDAO objetDAO;
  @Inject
  InteretDAO interetDAO;

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
    Utilisateur utilisateur;
    try {
      utilisateur = (Utilisateur) utilisateurDAO.rechercheParPseudo(pseudo);
      if (utilisateur == null || !utilisateur.verifierMdp(
          mdp)) {
        throw new NonAutoriseException("Pseudo ou mot de passe incorrect");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
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
    UtilisateurDTO utilisateur;
    try {
      utilisateur = utilisateurDAO.rechercheParId(id);
      if (utilisateur == null) {
        throw new BusinessException("L'utilisateur n'existe pas");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return utilisateur;
  }

  /**
   * Renvoie un utilisateur en fonction de son pseudo.
   *
   * @param pseudo : le pseudo de l'utilisateur à rechercher
   * @return utilisateur : l'utilisateur recherché
   * @throws PasTrouveException : est lancée si l'utilisateur avec le pseudo passé en paramètre
   *                            n'est pas trouvé
   */
  @Override
  public UtilisateurDTO rechercheParPseudo(String pseudo) {
    serviceDAL.commencerTransaction();
    UtilisateurDTO utilisateur;
    try {
      utilisateur = utilisateurDAO.rechercheParPseudo(pseudo);
      if (utilisateur == null) {
        throw new PasTrouveException("L'utilisateur n'existe pas");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return utilisateur;
  }

  /**
   * Permet l'inscription d'un utilisateur.
   *
   * @param utilisateurDTO : l'utilisateur à inscrire
   * @return utilisateurARenvoyer : l'utilisateur inscrit
   * @throws ConflitException  : est lancée si un utilisateur possède déjà le pseudo
   * @throws BusinessException : est lancée si l'utilisateur/adresse n'a pas pu être ajoutée
   */
  @Override
  public UtilisateurDTO inscription(UtilisateurDTO utilisateurDTO) {
    serviceDAL.commencerTransaction();
    UtilisateurDTO utilisateurARenvoyer;
    try {
      Utilisateur utilisateur = (Utilisateur) utilisateurDTO;
      utilisateur.setMdp(utilisateur.hashMdp(utilisateur.getMdp()));
      if (utilisateurDAO.rechercheParPseudo(utilisateurDTO.getPseudo()) != null) {
        throw new ConflitException("Ce pseudo est déjà utilisé");
      }
      AdresseDTO adresseDTO = adresseDAO.ajouterAdresse(utilisateurDTO.getAdresse());
      if (adresseDTO == null) {
        throw new BusinessException("L'adresse n'a pas pu être ajoutée");
      }
      if (!((Utilisateur) utilisateurDTO).mettreEnAttente()) {
        throw new BusinessException("L'utilisateur est déjà en attente");
      }
      utilisateurARenvoyer = utilisateurDAO.ajouterUtilisateur(utilisateurDTO);
      if (utilisateurARenvoyer == null) {
        throw new BusinessException("L'utilisateur n'a pas pu être ajouté");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return utilisateurARenvoyer;
  }

  /**
   * Confirme l'inscription d'un utilisateur et met son statut en Admin si l'information a été
   * renseigné.
   *
   * @param id       : l'utilisateur que l'on veut confirmer
   * @param estAdmin : si l'utilisateur est admin ou non
   * @return utilisateur : l'utilisateur avec son état d'inscription passé à "confirmé"
   * @throws BusinessException  : est lancée si l'état d'inscription de l'utilisateur n'a pas pu
   *                            être confirmé
   * @throws PasTrouveException : est lancée si l'utilisateur n'existe pas
   */
  @Override
  public UtilisateurDTO confirmerInscription(int id, boolean estAdmin) {
    serviceDAL.commencerTransaction();
    UtilisateurDTO utilisateur;
    try {
      UtilisateurDTO utilisateurDTO = utilisateurDAO.rechercheParId(id);
      if (utilisateurDTO == null) {
        throw new PasTrouveException("L'utilisateur n'existe pas");
      }
      if (!((Utilisateur) utilisateurDTO).confirmerInscription(estAdmin)) {
        throw new BusinessException("L'utilisateur est déjà confirmé");
      }
      utilisateur = utilisateurDAO.miseAJourUtilisateur(utilisateurDTO);
      if (utilisateur == null) {
        throw new BusinessException("L'inscription de l'utilisateur n'a pas pu être confirmé");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return utilisateur;
  }

  /**
   * Refuse l'inscription d'un utilisateur accompagné d'un commentaire justifiant ce refus.
   *
   * @param id          : l'id de l'utilisateur que l'on veut refuser
   * @param commentaire : le commentaire de refus
   * @return utilisateur : l'utilisateur avec l'inscription refusée
   * @throws BusinessException  : est lancée si l'état d'inscription de l'utilisateur n'a pas pu
   *                            être refusé
   * @throws PasTrouveException : est lancée si l'utilisateur n'existe pas
   */
  @Override
  public UtilisateurDTO refuserInscription(int id, String commentaire) {
    serviceDAL.commencerTransaction();
    UtilisateurDTO utilisateur;
    try {
      UtilisateurDTO utilisateurDTO = utilisateurDAO.rechercheParId(id);
      if (utilisateurDTO == null) {
        throw new PasTrouveException("L'utilisateur n'existe pas");
      }
      if (!((Utilisateur) utilisateurDTO).refuserInscription(commentaire)) {
        throw new BusinessException("L'utilisateur est déjà refusé");
      }
      utilisateur = utilisateurDAO.miseAJourUtilisateur(utilisateurDTO);
      if (utilisateur == null) {
        throw new BusinessException("L'inscription de l'utilisateur n'a pas pu être refusée");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return utilisateur;
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
    List<UtilisateurDTO> liste;
    try {
      liste = utilisateurDAO.listerUtilisateursEtatsInscriptions(
          etatInscription);
      if (etatInscription.equals("Confirmé")) {
        liste.addAll(utilisateurDAO.listerUtilisateursEtatsInscriptions("Empêché"));

      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return liste;
  }

  /**
   * Met à jour les informations de l'utilisateur.
   *
   * @param utilisateurDTO : l'utilisateur à mettre à jour
   * @return utilisateur : l'utilisateur avec ses informations mises à jour
   * @throws ConflitException        : est lancée si le pseudo existe déjà
   * @throws PasTrouveException      : est lancée si l'utilisateur ou l'adresse n'existe pas
   * @throws BusinessException       : est lancée si les données de l'utilisateur ou de l'adresse
   *                                 sont périmées
   * @throws OptimisticLockException : est lancée si les données sont périmées
   */
  @Override
  public UtilisateurDTO miseAJourUtilisateur(UtilisateurDTO utilisateurDTO) {
    serviceDAL.commencerTransaction();
    UtilisateurDTO utilisateur;
    try {
      UtilisateurDTO utilisateurDTO1 = utilisateurDAO.rechercheParPseudo(
          utilisateurDTO.getPseudo());
      if (utilisateurDTO1 != null
          && utilisateurDTO1.getIdUtilisateur() != utilisateurDTO.getIdUtilisateur()) {
        throw new ConflitException("Ce pseudo existe déjà");
      }
      AdresseDTO adresseDTO = adresseDAO.miseAJourAdresse(utilisateurDTO.getAdresse());
      if (adresseDTO == null) {
        if (adresseDAO.rechercheParId(utilisateurDTO.getAdresse().getIdAdresse()) == null) {
          throw new PasTrouveException("L'adresse n'existe pas");
        }
        throw new OptimisticLockException("Données de l'adresse sont périmées");
      }
      utilisateur = utilisateurDAO.miseAJourUtilisateur(utilisateurDTO);
      if (utilisateur == null) {
        if (utilisateurDAO.rechercheParId(utilisateurDTO.getIdUtilisateur()) == null) {
          throw new PasTrouveException("L'utilisateur n'existe pas");
        }
        throw new OptimisticLockException("Données de l'utilisateur sont périmées");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return utilisateur;
  }

  /**
   * Modifier l'état de l'utilisateur de empêcher à confirmer.
   *
   * @param utilisateurDTO : l'utilisateur dont on met à jour l'état.
   * @return utilisateurDTO : renvoit l'utilisateur avec son état changé.
   * @throws BusinessException       : si l'état spécifié n'est pas accepté.
   * @throws PasTrouveException      : si L'objet ou l'utilisateur ou l'offre n'existe pas.
   * @throws OptimisticLockException : si les données de l'objet, l'utilisateur ou l'offre sont
   *                                 périmées.
   */
  @Override
  public UtilisateurDTO confirmerUtilisateur(UtilisateurDTO utilisateurDTO) {
    serviceDAL.commencerTransaction();
    UtilisateurDTO utilisateur;

    try {

      //recupérer ses offres
      List<OffreDTO> listeMesOffres;
      listeMesOffres = offreDAO.mesOffres(utilisateurDTO.getIdUtilisateur());

      //réoffrir toutes les offres à l'état empéché
      for (OffreDTO offre : listeMesOffres) {

        if (!offre.getObjetDTO().getEtatObjet().equals("Annulé")
            && !offre.getObjetDTO().getEtatObjet().equals("Confirmé")) {

          if (interetDAO.listeDesPersonnesInteressees(offre.getObjetDTO()
              .getIdObjet()).isEmpty()) {

            ((Offre) offre).offrirObjet();
          } else {

            ((Offre) offre).interesseObjet();

          }

          ObjetDTO objet = objetDAO.miseAJourObjet(offre.getObjetDTO());

          if (objet == null) {
            ObjetDTO objetVerif = objetDAO.rechercheParId(offre.getObjetDTO());
            if (objetVerif == null) {
              throw new PasTrouveException("L'objet n'existe pas");
            }
            throw new OptimisticLockException("Données périmées");
          }
        }
      }

      ((Utilisateur) utilisateurDTO).confirmerUtilisateur();
      utilisateur = utilisateurDAO.miseAJourUtilisateur(utilisateurDTO);
      if (utilisateur == null) {
        if (utilisateurDAO.rechercheParId(utilisateurDTO.getIdUtilisateur()) == null) {
          throw new PasTrouveException("L'utilisateur n'existe pas");
        }
        throw new OptimisticLockException("Données de l'utilisateur sont périmées");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return utilisateur;
  }

  /**
   * Modifier l'état de l'utilisateur de confirmer à empêcher.
   *
   * @param utilisateurDTO : l'utilisateur dont on met à jour l'état.
   * @return utilisateurDTO : renvoit l'utilisateur avec son état changé.
   * @throws BusinessException       : si l'état spécifié n'est pas accepté.
   * @throws PasTrouveException      : si L'objet ou l'utilisateur ou l'offre n'existe pas.
   * @throws OptimisticLockException : si les données de l'objet, l'utilisateur ou l'offre sont
   *                                 périmées.
   */
  @Override
  public UtilisateurDTO empecherUtilisateur(UtilisateurDTO utilisateurDTO) {
    serviceDAL.commencerTransaction();
    UtilisateurDTO utilisateur;

    try {
      //recupérer ses offres
      List<OffreDTO> listeMesOffres;
      listeMesOffres = offreDAO.mesOffres(utilisateurDTO.getIdUtilisateur());

      //Prévenir tous les receveurs des offres à l'état confirmé
      //TO DO

      // passer toutes mes offres  (offert,intéréssé et PAS confirmé) à empecher
      for (OffreDTO offre : listeMesOffres) {

        if (!offre.getObjetDTO().getEtatObjet().equals("Annulé")
            && !offre.getObjetDTO().getEtatObjet().equals("Confirmé")) {

          ((Offre) offre).empecherOffre();

          ObjetDTO objet = objetDAO.miseAJourObjet(offre.getObjetDTO());

          if (objet == null) {
            ObjetDTO objetVerif = objetDAO.rechercheParId(offre.getObjetDTO());
            if (objetVerif == null) {
              throw new PasTrouveException("L'objet n'existe pas");
            }
            throw new OptimisticLockException("Données périmées");
          }
        }
      }

      ((Utilisateur) utilisateurDTO).empecherUtilisateur();
      utilisateur = utilisateurDAO.miseAJourUtilisateur(utilisateurDTO);
      if (utilisateur == null) {
        if (utilisateurDAO.rechercheParId(utilisateurDTO.getIdUtilisateur()) == null) {
          throw new PasTrouveException("L'utilisateur n'existe pas");
        }
        throw new OptimisticLockException("Données de l'utilisateur sont périmées");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return utilisateur;
  }

  /**
   * Change le mot de passe de l'utilisateur.
   *
   * @param id        : l'id de l'utilisateur à qui l'on veut changer le mot de passe
   * @param mdpActuel : le mot de passe actuel de l'utilisateur
   * @param nouvMdp   : le nouveau mot de passe de l'utilisateur
   * @return utilisateur : l'utilisateur avec le mot de passe modifié
   * @throws PasTrouveException      : est lancée si l'utilisateur n'existe pas
   * @throws BusinessException       : est lancée si le mot de passe est incorrect
   * @throws OptimisticLockException : est lancée si les données sont périmées
   */
  @Override
  public UtilisateurDTO modifierMdp(int id, String mdpActuel, String nouvMdp) {
    serviceDAL.commencerTransaction();
    UtilisateurDTO utilisateur;
    try {
      Utilisateur utilisateurActuel = (Utilisateur) utilisateurDAO.rechercheParId(id);
      if (utilisateurActuel == null) {
        throw new PasTrouveException("L'utilisateur n'existe pas");
      }
      if (!utilisateurActuel.verifierMdp(mdpActuel)) {
        throw new BusinessException("Mot de passe incorrect");
      }
      utilisateurActuel.setMdp(utilisateurActuel.hashMdp(nouvMdp));
      utilisateur = utilisateurDAO.modifierMdp(utilisateurActuel);
      if (utilisateur == null) {
        throw new OptimisticLockException("Données périmées");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return utilisateur;
  }

  /**
   * Liste tous les utilisateurs en fonction d'un critère de recherche (nom, code postal ou ville).
   *
   * @param recherche : le critère de recherche
   * @return liste : la liste des utilisateurs correspondante au critère de recherche
   */
  @Override
  public List<UtilisateurDTO> rechercherMembres(String recherche) {
    serviceDAL.commencerTransaction();
    List<UtilisateurDTO> liste;
    try {
      liste = utilisateurDAO.rechercherMembres(recherche);
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return liste;
  }

}

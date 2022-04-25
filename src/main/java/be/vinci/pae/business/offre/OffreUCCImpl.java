package be.vinci.pae.business.offre;

import be.vinci.pae.business.interet.Interet;
import be.vinci.pae.business.interet.InteretDTO;
import be.vinci.pae.business.objet.Objet;
import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.utilisateur.Utilisateur;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.donnees.dao.interet.InteretDAO;
import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import be.vinci.pae.donnees.dao.offre.OffreDAO;
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import be.vinci.pae.utilitaires.exceptions.OptimisticLockException;
import be.vinci.pae.utilitaires.exceptions.PasTrouveException;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OffreUCCImpl implements OffreUCC {

  @Inject
  OffreDAO offreDAO;
  @Inject
  ObjetDAO objetDAO;
  @Inject
  UtilisateurDAO utilisateurDAO;
  @Inject
  InteretDAO interetDAO;
  @Inject
  ServiceDAL serviceDAL;

  /**
   * Créer une offre et incrémente le nombre d'objet offerts de l'utilisateur.
   *
   * @param offreDTO : l'offre à créer
   * @return offre : l'offre créée
   * @throws BusinessException       : est lancée si l'objet ou l'offre n'a pas pu être créée
   * @throws OptimisticLockException : est lancée si les données sont périmées
   */
  @Override
  public OffreDTO creerOffre(OffreDTO offreDTO) {
    serviceDAL.commencerTransaction();
    OffreDTO offre;
    try {
      ObjetDTO objet = objetDAO.creerObjet(offreDTO.getObjetDTO());
      if (objet == null) {
        throw new BusinessException("L'objet n'a pas pu être crée");
      }
      ((Offre) offreDTO).offrirObjet();
      offre = offreDAO.creerOffre(offreDTO);
      if (offre == null) {
        throw new BusinessException("L'offre n'a pas pu être créée");
      }
      ((Utilisateur) objet.getOffreur()).incrementerNbObjetsOfferts();
      UtilisateurDTO utilisateur = utilisateurDAO.miseAJourUtilisateur(objet.getOffreur());
      if (utilisateur == null) {
        throw new OptimisticLockException("Données périmées");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return offre;
  }

  /**
   * Permet de réoffrir une offre.
   *
   * @param offreDTO : l'offre à réoffrir
   * @return offre : l'offre réoffert
   * @throws BusinessException       : est lancée si l'objet ou l'offre n'a pas pu être créée
   * @throws OptimisticLockException : est lancée si les données sont périmées
   */
  @Override
  public OffreDTO reoffrirObjet(OffreDTO offreDTO) {
    serviceDAL.commencerTransaction();
    OffreDTO offre;
    ObjetDTO objet;
    try {
      if (interetDAO.listeDesPersonnesInteressees(offreDTO.getObjetDTO().getIdObjet()).isEmpty()) {
        ((Offre) offreDTO).offrirObjet();
      } else {
        ((Offre) offreDTO).interesseObjet();
      }
      ((Offre) offreDTO).objetNonVu();
      objet = objetDAO.miseAJourObjet(offreDTO.getObjetDTO());
      if (objet == null) {
        if (objetDAO.rechercheParId(objet) == null) {
          throw new PasTrouveException("L'objet n'existe pas");
        }
        throw new OptimisticLockException("Données périmées");
      }
      offre = offreDAO.creerOffre(offreDTO);
      if (offre == null) {
        throw new BusinessException("L'offre n'a pas pu être créée");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return offre;
  }

  /**
   * Liste toutes les offres.
   *
   * @return liste : la liste de toutes les offres
   */
  public List<OffreDTO> listerOffres() {
    serviceDAL.commencerTransaction();
    List<OffreDTO> liste;
    try {
      liste = offreDAO.listerOffres();
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return liste;
  }

  /**
   * Liste les offres les plus récentes.
   *
   * @return liste : la liste des offres les plus récentes
   */
  public List<OffreDTO> listerOffresRecentes() {
    serviceDAL.commencerTransaction();
    List<OffreDTO> liste;
    try {
      liste = offreDAO.listerOffresRecentes();
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return liste;
  }

  /**
   * Annuler une offre.
   *
   * @param offreDTO : l'offre à annuler
   * @return offreDTO : l'offre annulée
   * @throws OptimisticLockException : est lancée si les données sont périmées
   * @throws PasTrouveException      : est lancée si l'objet n'existe pas
   */
  @Override
  public OffreDTO annulerOffre(OffreDTO offreDTO) {
    serviceDAL.commencerTransaction();
    try {
      ((Offre) offreDTO).annulerOffre();
      ObjetDTO objet = objetDAO.miseAJourObjet(offreDTO.getObjetDTO());
      if (objet == null) {
        ObjetDTO objetVerif = objetDAO.rechercheParId(offreDTO.getObjetDTO());
        if (objetVerif == null) {
          throw new PasTrouveException("L'objet n'existe pas");
        }
        throw new OptimisticLockException("Données périmées");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return offreDTO;
  }

  /**
   * Recherche une offre par son id.
   *
   * @param idOffre : l'id de l'offre recherché
   * @return offre : l'offre correspondante à l'id passé en paramètre
   * @throws PasTrouveException : est lancée si l'offre n'a pas pu être trouvée
   */
  @Override
  public OffreDTO rechercheParId(int idOffre) {
    serviceDAL.commencerTransaction();
    OffreDTO offre;
    try {
      offre = offreDAO.rechercheParId(idOffre);
      if (offre == null) {
        throw new PasTrouveException("L'offre n'a pas pu être trouvée");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return offre;
  }

  /**
   * Récupère les offres précédentes de l'objet avec l'id passé en paramètre.
   *
   * @param idObjet : l'id de l'objet à récupérer
   * @return liste : la liste des offres précédentes de l'objet avec l'id passé en paramètre
   * @throws BusinessException : est lancée si l'id de l'objet est incorrect
   */
  @Override
  public List<OffreDTO> offresPrecedentes(int idObjet) {
    serviceDAL.commencerTransaction();
    List<OffreDTO> liste;
    try {
      if (idObjet <= 0) {
        throw new BusinessException("L'id de l'objet est incorrect");
      }
      liste = offreDAO.offresPrecedentes(idObjet);
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return liste;
  }

  /**
   * Modifie une offre et son objet correspondant.
   *
   * @param offreAvecModification : l'offre contenant les modifications
   * @return offre : l'offre modifiée
   * @throws BusinessException       : est lancée si l'offre n'a pas pu être annulée
   * @throws PasTrouveException      : est lancée si l'objet/l'offre n'existe pas
   * @throws OptimisticLockException : est lancée si les données sont périmées
   */
  @Override
  public OffreDTO modifierOffre(OffreDTO offreAvecModification) {
    serviceDAL.commencerTransaction();
    OffreDTO offre;
    try {
      if (!((Objet) offreAvecModification.getObjetDTO()).verifierEtatPourModificationOffre()) {
        throw new BusinessException(
            "L'objet est dans un état ne lui permettant pas d'être modifié");
      }
      ObjetDTO objet = objetDAO.miseAJourObjet(offreAvecModification.getObjetDTO());
      if (objet == null) {
        if (objetDAO.rechercheParId(offreAvecModification.getObjetDTO()) == null) {
          throw new PasTrouveException("L'objet n'existe pas");
        }
        throw new OptimisticLockException("Données de l'objet sont périmées");
      }
      offre = offreDAO.modifierOffre(offreAvecModification);
      if (offre == null) {
        if (offreDAO.rechercheParId(offreAvecModification.getIdOffre()) == null) {
          throw new PasTrouveException("L'offre n'existe pas");
        }
        throw new OptimisticLockException("Données de l'offre sont périmées");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return offre;
  }

  /**
   * Liste toutes les offres en fonction d'un critère de recherche (nom du membre, type d'objet, ou
   * état d'objet).
   *
   * @param recherche : le critère de recherche
   * @param dateDebut : la date de début de la recherche
   * @param dateFin   : la date de fin de la recherche
   * @return liste : la liste des offres correspondantes au critère de recherche
   */
  @Override
  public List<OffreDTO> rechercherOffre(String recherche, LocalDate dateDebut, LocalDate dateFin) {
    serviceDAL.commencerTransaction();
    List<OffreDTO> liste;
    try {
      liste = offreDAO.rechercherOffres(recherche, dateDebut, dateFin);
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return liste;
  }

  /**
   * Permet d'indiquer à un membre en tant que receveur d'une offre.
   *
   * @param offreDTO : l'offre à confirmer
   * @return offre : l'offre confirmée
   * @throws PasTrouveException      : est lancée si l'objet n'est pas trouvé
   * @throws OptimisticLockException : est lancée si les données sont périmées
   */
  @Override
  public OffreDTO indiquerMembreReceveur(OffreDTO offreDTO) {
    serviceDAL.commencerTransaction();
    Offre offre;
    try {
      offre = (Offre) offreDTO;
      // offre.changerEtatObjet("Confirmé");
      ObjetDTO objet = objetDAO.miseAJourObjet(offreDTO.getObjetDTO());
      if (objet == null) {
        ObjetDTO objetVerif = objetDAO.rechercheParId(offre.getObjetDTO());
        if (objetVerif == null) {
          throw new PasTrouveException("L'objet n'existe pas");
        }
        throw new OptimisticLockException("Données périmées");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return offre;
  }

  /**
   * Permet de donner une offre.
   *
   * @param offreDTO : l'offre à donné
   * @return offre : l'offre donnée
   * @throws BusinessException       : est lancée si l'offre n'a pas pu être donnée ou l'id de
   *                                 l'objet est incorrect
   * @throws OptimisticLockException : est lancée si les données sont périmées
   * @throws PasTrouveException      : est lancée si l'objet n'existe pas
   */
  @Override
  public OffreDTO donnerOffre(OffreDTO offreDTO) {
    serviceDAL.commencerTransaction();
    try {
      ((Offre) offreDTO).donnerObjet();
      InteretDTO interetDTO = interetDAO.receveurActuel(offreDTO.getObjetDTO().getIdObjet());
      if (interetDTO == null) {
        throw new BusinessException("Pas d'intérêt");
      }
      ((Interet) interetDTO).venuChercher();
      if (interetDAO.miseAJourInteret(interetDTO) == null) {
        throw new OptimisticLockException("Données périmées");
      }
      ((Objet) offreDTO.getObjetDTO()).indiquerReceveur(interetDTO.getUtilisateur());
      ObjetDTO objet = objetDAO.miseAJourObjet(offreDTO.getObjetDTO());
      if (objet == null) {
        ObjetDTO objetVerif = objetDAO.rechercheParId(offreDTO.getObjetDTO());
        if (objetVerif == null) {
          throw new PasTrouveException("L'objet n'existe pas");
        }
        throw new OptimisticLockException("Données périmées");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return offreDTO;
  }

  /**
   * Liste les propres offres de l'utilisateur dont l'id est passé en paramètre.
   *
   * @param idUtilisateur : l'id de l'utilisateur à qui lister ses offres
   * @return liste : la liste de toutes ses propres offres
   */
  public List<OffreDTO> mesOffres(int idUtilisateur) {
    serviceDAL.commencerTransaction();
    List<OffreDTO> liste;
    try {
      liste = offreDAO.mesOffres(idUtilisateur);
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return liste;
  }

  /**
   * Liste les offres qui ont été attribuées à l'utilisateur dont l'id est passé en paramètre.
   *
   * @param idUtilisateur : l'id de l'utilisateur à qui lister ses offres attribuées
   * @return liste : la liste de toutes ses offres attribuées
   */
  public List<OffreDTO> voirOffreAttribuer(int idUtilisateur) {
    serviceDAL.commencerTransaction();
    List<OffreDTO> listeTemp;
    List<OffreDTO> liste = new ArrayList<>();
    try {
      listeTemp = offreDAO.voirOffreAttribuer(idUtilisateur);
      for (int i = 0; i < listeTemp.size(); i++) {
        listeTemp.get(i).getObjetDTO().setVue(true);
        ObjetDTO objet = objetDAO.miseAJourObjet(listeTemp.get(i).getObjetDTO());
        listeTemp.get(i).setObjetDTO(objet);
        liste.add(listeTemp.get(i));
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return liste;
  }

  /**
   * Récupère tous les objets que l'utilisateur doit évaluer.
   *
   * @param idReceveur : l'id de l'utilisateur concerné
   * @return objetsAEvalue : la liste des objets que l'utilisateur doit évaluer
   * @return null si il n'y a pas d'objet a évaluer
   * @throws PasTrouveException si un des objets n'existe pas.
   * @throws BusinessException  si les données sont périmées.
   */
  @Override
  public List<ObjetDTO> objetsAEvaluerParUtilisateur(int idReceveur) {
    serviceDAL.commencerTransaction();
    List<ObjetDTO> objetsAEvaluerTemp;
    List<ObjetDTO> objetsAEvaluer = new ArrayList<>();
    try {
      objetsAEvaluerTemp = objetDAO.rechercheObjetParReceveur(idReceveur);
      if (!objetsAEvaluerTemp.isEmpty()) {
        for (ObjetDTO objet : objetsAEvaluerTemp) {
          if (objet == null) {
            if (objetDAO.rechercheParId(objet) == null) {
              throw new PasTrouveException("L'objet n'existe pas");
            }
            throw new BusinessException("Données de l'objet sont périmées");
          }
          if (((Objet) objet).peutEtreEvalue()) {
            objetsAEvaluer.add(objet);
          }
        }
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return objetsAEvaluer;
  }

}

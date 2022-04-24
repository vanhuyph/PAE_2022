package be.vinci.pae.business.interet;

import be.vinci.pae.business.objet.Objet;
import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.utilisateur.Utilisateur;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.donnees.dao.interet.InteretDAO;
import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import be.vinci.pae.utilitaires.exceptions.PasTrouveException;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InteretUCCImpl implements InteretUCC {

  @Inject
  InteretDAO interetDAO;
  @Inject
  UtilisateurDAO utilisateurDAO;
  @Inject
  ServiceDAL serviceDAL;
  @Inject
  ObjetDAO objetDAO;

  /**
   * Créer un intérêt pour une offre.
   *
   * @param interetDTO : l'intérêt à créer
   * @return interet : interetDTO
   * @throws BusinessException  : est lancée s'il y a eu une erreur
   * @throws PasTrouveException : est lancée si l'utilisateur n'existe pas
   */
  @Override
  public InteretDTO creerUnInteret(InteretDTO interetDTO) {
    serviceDAL.commencerTransaction();
    InteretDTO interet;
    try {
      Date date = interetDTO.getDateRdv();
      Date now = new Date();
      if (date.before(now)) {
        throw new BusinessException("La date de rendez-vous ne peut pas être dans le passé");
      }
      if (!interetDTO.getUtilisateur().getGsm().isBlank()) {
        UtilisateurDTO utilisateur = utilisateurDAO.modifierGsm(interetDTO.getUtilisateur());
        if (utilisateur == null) {
          UtilisateurDTO utilisateurVerif = utilisateurDAO.rechercheParId(
              interetDTO.getUtilisateur().getIdUtilisateur());
          if (utilisateurVerif == null) {
            throw new PasTrouveException("L'utilisateur n'existe pas");
          }
          throw new BusinessException("Données périmées");
        }
      }
      ((Interet) interetDTO).marquerInteretObjet();
      objetDAO.miseAJourObjet(interetDTO.getObjet());
      interetDTO.setVue(false);
      interet = interetDAO.ajouterInteret(interetDTO);
      if (interet == null) {
        throw new BusinessException("L'intérêt n'a pas pu être créé");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return interet;
  }

  /**
   * Récupère le nombre de personnes intéressées pour une offre.
   *
   * @param id : l'id de l'offre dont les personnes sont intéressées
   * @return nbPersonnesInteressees : le nombre de personnes intéressées
   * @throws BusinessException : est lancée si l'id de l'offre est incorrect
   */
  @Override
  public int nbPersonnesInteressees(int id) {
    serviceDAL.commencerTransaction();
    int nbPersonnesInteressees;
    try {
      if (id <= 0) {
        throw new BusinessException("L'offre n'a pas pu être trouvée");
      }
      nbPersonnesInteressees = interetDAO.nbPersonnesInteressees(id);
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return nbPersonnesInteressees;
  }

  /**
   * Liste les personnes ayant marqué un intérêt pour l'objet dont l'id est passé en paramètre.
   *
   * @param idObjet : l'id de l'objet dont les personnes sont intéressées
   * @return listeInteret : la liste des personnes ayant marqué un intérêt pour l'objet
   * @throws BusinessException : est lancée si l'id de l'objet est incorrect
   */
  @Override
  public List<InteretDTO> listeDesPersonnesInteressees(int idObjet) {
    serviceDAL.commencerTransaction();
    List<InteretDTO> liste;
    try {
      if (idObjet <= 0) {
        throw new BusinessException("L'id de l'objet est incorrect");
      }
      liste = interetDAO.listeDesPersonnesInteressees(idObjet);
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return liste;
  }

  /**
   * Liste les personnes ayant vu un intérêt pour l'objet dont l'id est passé en paramètre.
   *
   * @param idObjet : l'id de l'objet dont les personnes sont intéressées
   * @return listeInteret : la liste de toutes les interets qu'on n'a pas encore vue
   * @throws BusinessException : est lancée si l'id de l'objet est incorrect
   */
  @Override
  public List<InteretDTO> listeDesPersonnesInteresseesVue(int idObjet) {
    serviceDAL.commencerTransaction();
    List<InteretDTO> listeTemp;
    List<InteretDTO> liste = new ArrayList<>();
    try {
      if (idObjet <= 0) {
        throw new BusinessException("L'id de l'objet est incorrect");
      }
      listeTemp = interetDAO.listeDesPersonnesInteresseesVue(idObjet);
      for (int i = 0; i < listeTemp.size(); i++) {
        listeTemp.get(i).setVue(true);
        InteretDTO interetDTO = interetDAO.miseAJourInteret(listeTemp.get(i));
        liste.add(interetDTO);
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return liste;
  }

  /**
   * Permet d'indiquer à une personne intéressée comme étant receveur de l'objet.
   *
   * @param interet : l'intérêt de la personne
   * @return interetDTO : interetDTO
   * @throws BusinessException : est lancée si le receveur n'a pas pu être indiqué ou données
   *                           périmées
   */
  @Override
  public InteretDTO indiquerReceveur(InteretDTO interet) {
    serviceDAL.commencerTransaction();
    InteretDTO interetDTO;
    try {
      ((Interet) interet).indiquerReceveur();
      interetDTO = interetDAO.miseAJourInteret(interet);
      if (interetDTO == null) {
        throw new BusinessException("Le receveur n'a pas pu être indiqué");
      }
      ((Objet) interetDTO.getObjet()).verifierEtatPourModificationOffre();
      ((Objet) interetDTO.getObjet()).confirmerObjet();
      ObjetDTO objet = objetDAO.miseAJourObjet(interetDTO.getObjet());
      if (objet == null) {
        throw new BusinessException("Données de l'objet périmées");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return interetDTO;
  }

  /**
   * Permet d'indiquer que l'objet a été non remis car le receveur n'est pas venu chercher l'objet.
   *
   * @param idObjet : l'id de l'objet à mettre en non remis
   * @return interetDTO : interetDTO
   * @throws PasTrouveException : est lancée si l'objet n'a pas de receveur actuellement
   * @throws BusinessException  : est lancée si les données de l'intérêt/utilisateur sont périmées
   */
  @Override
  public InteretDTO nonRemis(int idObjet) {
    serviceDAL.commencerTransaction();
    InteretDTO interetDTO;
    try {
      interetDTO = interetDAO.receveurActuel(idObjet);
      if (interetDTO == null) {
        throw new PasTrouveException("L'objet n'a pas de receveur actuellement");
      }
      ((Interet) interetDTO).pasVenuChercher();
      if (interetDAO.miseAJourInteret(interetDTO) == null) {
        throw new BusinessException("Données de l'intérêt périmées");
      }
      ((Utilisateur) interetDTO.getUtilisateur()).incrementerNbObjetAbandonne();
      if (utilisateurDAO.miseAJourUtilisateur(interetDTO.getUtilisateur()) == null) {
        throw new BusinessException("Données de l'utilisateur périmées");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return interetDTO;
  }

}

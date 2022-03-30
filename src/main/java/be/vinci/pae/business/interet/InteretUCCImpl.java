package be.vinci.pae.business.interet;

import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.donnees.dao.interet.InteretDAO;
import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import be.vinci.pae.utilitaires.exceptions.PasTrouveException;
import jakarta.inject.Inject;
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
   * @throws BusinessException : est lancée s'il y a eu une erreur
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
          throw new BusinessException("Données sont périmées");
        }
        interetDTO.setUtilisateur(utilisateur);
      }
      Interet interetChange = (Interet) interetDTO;
      interetChange.creerVersion();
      interetChange.changerEtatObjet("Intéressé");
      objetDAO.miseAJourObjet(interetChange.getObjet());
      interet = interetDAO.ajouterInteret(interetChange);
      if (interet == null) {
        throw new BusinessException("L'intérêt n'a pas pu être créé.");
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
   * @param idObjet : l'id de l'objet dont les personnes sont intéressées
   * @return nbPersonnesInteressees : le nombre de personnes intéressées
   * @throws BusinessException : est lancée si l'id de l'objet est incorrect
   */
  @Override
  public int nbPersonnesInteressees(int idObjet) {
    serviceDAL.commencerTransaction();
    int nbPersonnesInteressees;
    try {
      if (idObjet <= 0) {
        throw new BusinessException("L'offre n'a pas pu être trouvée");
      }
      nbPersonnesInteressees = interetDAO.nbPersonnesInteressees(idObjet);
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return nbPersonnesInteressees;
  }

  /**
   * Liste les interets.
   *
   * @param objetDTO : l'objet dont les personnes sont intéressées
   * @return liste : la liste de toutes les interets
   * @throws BusinessException : est lancée si l'id de l'objet est incorrect
   */
  @Override
  public List<InteretDTO> listeDesPersonnesInteressees(ObjetDTO objetDTO) {
    serviceDAL.commencerTransaction();
    if (objetDTO == null) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("l'objet est null");
    }
    List<InteretDTO> list = interetDAO.listeDesPersonnesInteressees(objetDTO);
    serviceDAL.commettreTransaction();
    return list;
  }

}

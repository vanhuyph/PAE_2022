package be.vinci.pae.business.interet;

import be.vinci.pae.donnees.dao.interet.InteretDAO;
import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
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
    Date date = interetDTO.getDateRdv();
    Date now = new Date();
    if (date.before(now)) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("La date de rendez-vous ne peut pas être dans le passé");
    }
    if (!interetDTO.getUtilisateur().getGsm().isBlank()) {
      interetDTO.setUtilisateur(utilisateurDAO.modifierGsm(interetDTO.getUtilisateur()));
    }
    interetDTO.getObjet().setEtatObjet("Intéressé");
    objetDAO.changeEtatObjet(interetDTO.getObjet());
    InteretDTO interet = interetDAO.ajouterInteret(interetDTO);
    if (interet == null || interet.getUtilisateur().getIdUtilisateur() <= 0 || interet.getObjet()
        .getIdObjet() <= 0) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("L'intérêt n'a pas pu être créé.");
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
    if (idObjet <= 0) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("L'objet n'a pas pu être trouvée");
    }
    int nbPersonnesInteressees = interetDAO.nbPersonnesInteressees(idObjet);
    serviceDAL.commettreTransaction();
    return nbPersonnesInteressees;
  }

  /**
   * Liste les interets.
   *
   * @param idObjet : l'id de l'objet dont les personnes sont intéressées
   * @return liste : la liste de toutes les interets
   * @throws BusinessException : est lancée si l'id de l'objet est incorrect
   */
  @Override
  public List<InteretDTO> listeDesPersonnesInteressees(int idObjet) {
    serviceDAL.commencerTransaction();
    if (idObjet <= 0) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("l'objet n'a pas pu être trouvée");
    }
    List<InteretDTO> list = interetDAO.listeDesPersonnesInteressees(idObjet);
    serviceDAL.commettreTransaction();
    return list;
  }

}

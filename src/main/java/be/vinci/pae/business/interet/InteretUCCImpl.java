package be.vinci.pae.business.interet;

import be.vinci.pae.donnees.dao.interet.InteretDAO;
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import jakarta.inject.Inject;
import java.util.Date;

public class InteretUCCImpl implements InteretUCC {

  @Inject
  InteretDAO interetDAO;
  @Inject
  UtilisateurDAO utilisateurDAO;
  @Inject
  ServiceDAL serviceDAL;

  @Override
  public InteretDTO creerUnInteret(InteretDTO interetDTO) {
    serviceDAL.commencerTransaction();
    Date date = interetDTO.getDateRdv();
    Date now = new Date();
    if (date.before(now)) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("La date de rendez-vous ne peut pas être dans le passé");
    }
    if (!interetDTO.getUtilisateur().getGsm().equals("")) {
      interetDTO.setUtilisateur(utilisateurDAO.modifierGsm(interetDTO.getUtilisateur()));
    }
    InteretDTO interet = interetDAO.ajouterInteret(interetDTO);
    if (interet == null || (interet.getUtilisateur().getIdUtilisateur() <= 0 || interet.getObjet()
        .getIdObjet() <= 0)) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("L'interet n'a pas pu être créé.");
    }
    serviceDAL.commettreTransaction();
    return interet;
  }
}

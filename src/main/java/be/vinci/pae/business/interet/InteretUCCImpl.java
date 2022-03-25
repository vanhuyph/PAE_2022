package be.vinci.pae.business.interet;

import be.vinci.pae.donnees.dao.interet.InteretDAO;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import jakarta.inject.Inject;
import java.util.Date;

public class InteretUCCImpl implements InteretUCC {

  @Inject
  InteretDAO interetDAO;

  @Override
  public InteretDTO creerUnInteret(int idUtilisateurInteresse, int idObjet, Date dateRdv) {
    InteretDTO interet = interetDAO.ajouterInteret(idUtilisateurInteresse, idObjet, dateRdv);
    if (interet == null) {
      throw new BusinessException("L'intérêt n'a pas pu être créé");
    }
    return interet;
  }

}

package be.vinci.pae.business.typeobjet;

import be.vinci.pae.donnees.dao.typeobjet.TypeObjetDAO;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import jakarta.inject.Inject;
import java.util.List;

public class TypeObjetUCCImpl implements TypeObjetUCC {

  @Inject
  ServiceDAL serviceDAL;
  @Inject
  private TypeObjetDAO typeObjetDAO;

  /**
   * Liste les types d'objet.
   *
   * @return la liste des types d'objet
   */
  @Override
  public List<TypeObjetDTO> listerTypeObjet() {
    serviceDAL.commencerTransaction();
    List<TypeObjetDTO> listTypeObjets = typeObjetDAO.listerTypeObjet();
    if (listTypeObjets == null) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("Il n'y a pas de type d'objet."); // vérifier statut de réponse
    }
    serviceDAL.commettreTransaction();
    return listTypeObjets;
  }
}

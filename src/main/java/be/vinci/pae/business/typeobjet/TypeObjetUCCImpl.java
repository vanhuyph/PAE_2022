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
   * @return liste : la liste des types d'objets
   */
  @Override
  public List<TypeObjetDTO> listerTypeObjet() {
    serviceDAL.commencerTransaction();
    List<TypeObjetDTO> liste = typeObjetDAO.listerTypeObjet();
    if (liste == null) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("Il n'y a pas de type d'objets");
    }
    serviceDAL.commettreTransaction();
    return liste;
  }

}

package be.vinci.pae.business.typeobjet;

import be.vinci.pae.donnees.dao.typeObjet.TypeObjetDAO;
import jakarta.inject.Inject;
import java.util.List;

public class TypeObjetUCCImpl implements TypeObjetUCC {

  @Inject
  private TypeObjetDAO typeObjetDAO;

  /**
   * Liste les types d'objet.
   *
   * @return la liste des types d'objet
   */
  @Override
  public List<TypeObjetDTO> listerTypeObjet() {
    return typeObjetDAO.listerTypeObjet();
  }
}

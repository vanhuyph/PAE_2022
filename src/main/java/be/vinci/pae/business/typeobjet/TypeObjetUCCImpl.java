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
    List<TypeObjetDTO> liste;
    try {
      liste = typeObjetDAO.listerTypeObjet();
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return liste;
  }

  /**
   * Permet de créer un nouveau type d'objet à ajouter dans la liste des types.
   *
   * @param typeObjetDTO : le type d'objet à créé
   * @return typeObjet : le type d'objet créé
   * @throws BusinessException : est lancée si le type d'objet existe déjà dans la liste
   */
  @Override
  public TypeObjetDTO creerTypeObjet(TypeObjetDTO typeObjetDTO) {
    serviceDAL.commencerTransaction();
    TypeObjetDTO typeObjet;
    try {
      typeObjet = typeObjetDAO.verifierUniqueTypeObjet(typeObjetDTO);
      if (typeObjet.getIdType() == 0) {
        typeObjet = typeObjetDAO.creerTypeObjet(typeObjetDTO);
      } else {
        throw new BusinessException("Le type d'objet existe déjà");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return typeObjet;
  }

}

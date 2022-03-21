package be.vinci.pae.business.objet;

import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import jakarta.inject.Inject;
import java.sql.SQLException;

public class ObjetUCCImpl implements ObjetUCC {

  @Inject
  ObjetDAO objetDAO; // vérifier injection de dépendances

  @Inject
  ServiceDAL serviceDAL;

  /**
   * Creer un objet.
   *
   * @param typeObjet   : le type de l'objet
   * @param description : description de l'objet
   * @param offreur     : id de l'utilisateur offrant l'objet
   * @param photo       : chemin de la photo , peut être null
   * @return l'objet créé
   */
  @Override
  public ObjetDTO creerUnObjet(int idOffreur, int typeObjet, String description, int offreur,
      String photo) {
    serviceDAL.commencerTransaction();
    ObjetDTO objet = objetDAO.creerObjet("offert", typeObjet, description, offreur, photo);
    if (objet == null) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("objet n'a pas pu être créé."); // vérifier statut de réponse
    }
    serviceDAL.commettreTransaction();
    return objet;
  }

  /**
   * Recherche un objet via un id dans la base de donnée.
   *
   * @param id : l'id de l'objet
   * @return ObjetDTO : l'objet, s'il trouve un objet qui possède ce id
   * @throws SQLException : est lancée s'il ne trouve pas l'objet
   */
  @Override
  public ObjetDTO rechercheParId(int id) {
    serviceDAL.commencerTransaction();
    ObjetDTO objetDTO = objetDAO.rechercheParId(id);
    if (objetDTO == null) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("objet n'a pas pu être trouvé."); // vérifier statut de réponse
    }
    serviceDAL.commettreTransaction();
    return objetDTO;
  }


}

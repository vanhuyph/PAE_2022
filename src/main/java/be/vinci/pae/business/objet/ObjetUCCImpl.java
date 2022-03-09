package be.vinci.pae.business.objet;

import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import be.vinci.pae.utilitaires.exceptions.ExceptionBusiness;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response.Status;

public class ObjetUCCImpl implements ObjetUCC {

  @Inject
  ObjetDAO objetDAO; // vérifier injection de dépendances


  /**
   * @param type_objet  : le type de l'objet
   * @param description : description de l'objet
   * @param offreur     : id de l'utilisateur offrant l'objet
   * @param photo       : chemin de la photo , peut être null
   * @return l'objet créé
   */
  @Override
  public ObjetDTO creerUnObjet(int id_offreur, String type_objet, String description, int offreur,
      String photo) {

    ObjetDTO nObjet = objetDAO.creerObjet("offert", type_objet, description, offreur, photo);
    if (nObjet == null) {
      throw new ExceptionBusiness("objet n'a pas pu être créé.",
          Status.INTERNAL_SERVER_ERROR); // vérifier statut de réponse
    }

    return nObjet;
  }
}

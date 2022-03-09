package be.vinci.pae.business.objet;

import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import be.vinci.pae.utilitaires.exceptions.ExceptionBusiness;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response.Status;

public class ObjetUCCImpl implements ObjetUCC {

  @Inject
  ObjetDAO objetDAO; // vérifier injection de dépendances


  /**
   * Methode qui créé un objet
   *
   * @param typeObjet   : le type de l'objet
   * @param description : description de l'objet
   * @param offreur     : id de l'utilisateur offrant l'objet
   * @param photo       : chemin de la photo , peut être null
   * @return l'objet créé
   */
  @Override
  public ObjetDTO creerUnObjet(int idOffreur, String typeObjet, String description, int offreur,
      String photo) {

    ObjetDTO objet = objetDAO.creerObjet("offert", typeObjet, description, offreur, photo);
    if (objet == null) {
      throw new ExceptionBusiness("objet n'a pas pu être créé.",
          Status.INTERNAL_SERVER_ERROR); // vérifier statut de réponse
    }

    return objet;
  }
}

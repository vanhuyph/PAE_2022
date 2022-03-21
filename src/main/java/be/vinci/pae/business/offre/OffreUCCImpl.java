package be.vinci.pae.business.offre;

import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import be.vinci.pae.donnees.dao.offre.OffreDAO;
import be.vinci.pae.utilitaires.exceptions.ExceptionBusiness;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response.Status;

public class OffreUCCImpl implements OffreUCC {

  @Inject
  OffreDAO offreDAO; // vérifier injection de dépendances
  @Inject
  ObjetDAO objetDAO;

  /**
   * Créer une offre.
   *
   * @param plageHoraire : disponibilité de l'offreur
   * @return offre : l'offre créée
   */
  @Override
  public OffreDTO creerUneOffre(int idObjet, String plageHoraire) {
    OffreDTO offre = offreDAO.creerOffre(idObjet, plageHoraire);
    if (offre == null) {
      throw new ExceptionBusiness("L'offre n'a pas pu être créée",
          Status.INTERNAL_SERVER_ERROR); // vérifier statut de réponse
    }
    return offre;
  }

  /**
   * Créer un objet.
   *
   * @param typeObjet   : le type de l'objet
   * @param description : description de l'objet
   * @param offreur     : id de l'utilisateur offrant l'objet
   * @param photo       : chemin de la photo, peut être null
   * @return objet : l'objet créé
   */
  @Override
  public ObjetDTO creerUnObjet(int idOffreur, int typeObjet, String description, int offreur,
      String photo) {
    ObjetDTO objet = objetDAO.creerObjet("offert", typeObjet, description, offreur, photo);
    if (objet == null) {
      throw new ExceptionBusiness("L'objet n'a pas pu être créé",
          Status.INTERNAL_SERVER_ERROR); // vérifier statut de réponse
    }
    return objet;
  }
}

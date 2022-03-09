package be.vinci.pae.business.offre;

import be.vinci.pae.donnees.dao.offre.OffreDAO;
import be.vinci.pae.utilitaires.exceptions.ExceptionBusiness;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response.Status;

public class OffreUCCImpl implements OffreUCC {

  @Inject
  OffreDAO offreDAO; // vérifier injection de dépendances

  /**
   * Creer une offre
   *
   * @param plageHoraire : disponibilité de l'offreur
   * @return l'offre créée
   */
  @Override
  public OffreDTO creerUneOffre(int idObjet, String plageHoraire) {
    OffreDTO offre = offreDAO.creerOffre(idObjet, plageHoraire);
    if (offre == null) {
      throw new ExceptionBusiness("l'offre n'a pas pu être créé.",
          Status.INTERNAL_SERVER_ERROR); // vérifier statut de réponse
    }

    return offre;
  }
}

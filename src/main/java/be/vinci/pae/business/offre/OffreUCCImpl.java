package be.vinci.pae.business.offre;

import be.vinci.pae.donnees.dao.offre.OffreDAO;
import be.vinci.pae.utilitaires.exceptions.ExceptionBusiness;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response.Status;

public class OffreUCCImpl implements OffreUCC {

  @Inject
  OffreDAO offreDAO; // vérifier injection de dépendances

  /**
   * @param plage_horaire : disponibilité de l'offreur
   * @return l'offre créée
   */
  @Override
  public OffreDTO creerUneOffre(int id_objet, String plage_horaire) {
    OffreDTO nOffre = offreDAO.creerOffre(id_objet, plage_horaire);
    if (nOffre == null) {
      throw new ExceptionBusiness("l'offre n'a pas pu être créé.",
          Status.INTERNAL_SERVER_ERROR); // vérifier statut de réponse
    }

    return nOffre;
  }
}

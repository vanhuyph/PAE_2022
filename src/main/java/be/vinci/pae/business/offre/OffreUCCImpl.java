package be.vinci.pae.business.offre;

import be.vinci.pae.donnees.dao.offre.OffreDAO;
import be.vinci.pae.utilitaires.exceptions.ExceptionBusiness;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response.Status;

public class OffreUCCImpl implements OffreUCC {

  @Inject
  OffreDAO offreDAO; // vérifier injection de dépendances

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
   *
   * @param idOffre : id de l'offre à annuler
   * @throws ExceptionBusiness : lance une exception business si l'offre n'a pas pu être annulée
   * @return l'offre annulée
   */
  @Override
  public OffreDTO annulerUneOffre(int idOffre){
    System.out.println("début annuler offre UccImpl");
    OffreDTO offre = offreDAO.annulerOffre(idOffre);
    if (offre == null) {
      throw new ExceptionBusiness("l'offre n'a pas pu être annulée.",
              Status.INTERNAL_SERVER_ERROR); // vérifier statut de réponse
    }
    System.out.println("fin annuler offre UccImpl");
    return offre;
  }

  /**
   * Recherche une offre par son id.
   *
   * @param idOffre id de l'offre recherchée
   * @return l'offre correspondante  l'id idS
   */
  @Override
  public OffreDTO rechercheParId(int idOffre) {
    OffreDTO offre = offreDAO.rechercheParId(idOffre);
    if (offre == null) {
      throw new ExceptionBusiness("L'offre n'a pas pu être trouvée",
          Status.INTERNAL_SERVER_ERROR); // vérifier statut de réponse
    }
    return offre;
  }
}

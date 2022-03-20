package be.vinci.pae.business.offre;

import be.vinci.pae.donnees.dao.offre.OffreDAO;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class OffreUCCImpl implements OffreUCC {

  @Inject
  OffreDAO offreDAO; // vérifier injection de dépendances

  @Inject
  ServiceDAL serviceDAL;

  /**
   * Creer une offre.
   *
   * @param plageHoraire : disponibilité de l'offreur
   * @return l'offre créée
   */
  @Override
  public OffreDTO creerUneOffre(int idObjet, String plageHoraire) {
    serviceDAL.commencerTransaction();
    OffreDTO offre = offreDAO.creerOffre(idObjet, plageHoraire);
    if (offre == null) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("l'offre n'a pas pu être créé."); // vérifier statut de réponse
    }
    serviceDAL.commettreTransaction();
    return offre;
  }

  /**
   * Lister les offres.
   *
   * @return Liste offre
   */
  public List<OffreDTO> listOffres() {
    serviceDAL.commencerTransaction();
    List<OffreDTO> listOffres = offreDAO.listOffres();
    if (listOffres == null) {
      throw new BusinessException("Il n'y a pas d'offre."); // vérifier statut de réponse
    }
    serviceDAL.commettreTransaction();
    return listOffres;
  }

  /**
   * Lister les offres les plus recentes.
   *
   * @return Liste offre recentes
   */
  public List<OffreDTO> listOffresRecent() {
    serviceDAL.commencerTransaction();
    List<OffreDTO> listOffresRecent = new ArrayList<>();

    for (int i = 0; i < 2; i++) {
      listOffresRecent.add(offreDAO.listOffres().get(i));
    }
    if (listOffresRecent == null) {
      throw new BusinessException("Il n'y a pas d'offre."); // vérifier statut de réponse
    }
    serviceDAL.commettreTransaction();
    return listOffresRecent;
  }


}

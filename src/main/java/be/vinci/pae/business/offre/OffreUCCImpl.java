package be.vinci.pae.business.offre;

import be.vinci.pae.donnees.dao.offre.OffreDAO;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.List;

public class OffreUCCImpl implements OffreUCC {

  @Inject
  OffreDAO offreDAO; // vérifier injection de dépendances

  /**
   * Creer une offre.
   *
   * @param plageHoraire : disponibilité de l'offreur
   * @return l'offre créée
   */
  @Override
  public OffreDTO creerUneOffre(int idObjet, String plageHoraire) {
    OffreDTO offre = offreDAO.creerOffre(idObjet, plageHoraire);
    if (offre == null) {
      throw new BusinessException("l'offre n'a pas pu être créé."); // vérifier statut de réponse
    }

    return offre;
  }

  /**
   * Lister les offres.
   *
   * @return Liste offre
   */
  public List<OffreDTO> listOffres() {

    List<OffreDTO> listOffres = offreDAO.listOffres();
    if (listOffres == null) {
      throw new BusinessException("Il n'y a pas d'offre."); // vérifier statut de réponse
    }
    return listOffres;
  }

  /**
   * Lister les offres les plus recentes.
   *
   * @return Liste offre recentes
   */
  public List<OffreDTO> listOffresRecent() {
    List<OffreDTO> listOffresRecent = new ArrayList<>();

    for (int i = 0; i < 2; i++) {
      listOffresRecent.add(offreDAO.listOffres().get(i));
    }
    if (listOffresRecent == null) {
      throw new BusinessException("Il n'y a pas d'offre."); // vérifier statut de réponse
    }
    return listOffresRecent;
  }


}

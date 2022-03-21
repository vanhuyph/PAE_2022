package be.vinci.pae.business.offre;

import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import be.vinci.pae.donnees.dao.offre.OffreDAO;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import jakarta.inject.Inject;
import java.util.List;

public class OffreUCCImpl implements OffreUCC {

  @Inject
  OffreDAO offreDAO; // vérifier injection de dépendances
  @Inject
  ObjetDAO objetDAO;
  @Inject
  ServiceDAL serviceDAL;

  /**
   * Créer une offre.
   *
   * @param plageHoraire : disponibilité de l'offreur
   * @return offre : l'offre créée
   */
  @Override
  public OffreDTO creerUneOffre(int idObjet, String plageHoraire) {
    serviceDAL.commencerTransaction();
    OffreDTO offre = offreDAO.creerOffre(idObjet, plageHoraire);
    if (offre == null) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("L'offre n'a pas pu être créée"); // vérifier statut de réponse
    }
    serviceDAL.commettreTransaction();
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
    serviceDAL.commencerTransaction();
    ObjetDTO objet = objetDAO.creerObjet("offert", typeObjet, description, offreur, photo);
    if (objet == null) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("L'objet n'a pas pu être créé"); // vérifier statut de réponse
    }
    serviceDAL.commettreTransaction();
    return objet;
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
      serviceDAL.retourEnArriereTransaction();
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
    List<OffreDTO> listOffresRecent = offreDAO.listOffresRecent();
    if (listOffresRecent == null) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("Il n'y a pas d'offre."); // vérifier statut de réponse
    }
    serviceDAL.commettreTransaction();
    return listOffresRecent;
  }


}

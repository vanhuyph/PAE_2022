package be.vinci.pae.business.offre;

import be.vinci.pae.business.objet.Objet;
import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import be.vinci.pae.donnees.dao.offre.OffreDAO;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import be.vinci.pae.utilitaires.exceptions.PasTrouveException;
import jakarta.inject.Inject;
import java.util.List;

public class OffreUCCImpl implements OffreUCC {

  @Inject
  OffreDAO offreDAO;
  @Inject
  ObjetDAO objetDAO;
  @Inject
  ServiceDAL serviceDAL;

  /**
   * Créer une offre.
   *
   * @param offreDTO : l'offre à créer
   * @return offre : l'offre créée
   * @throws BusinessException : est lancée si l'objet ou l'offre n'a pas pu être créée
   */
  @Override
  public OffreDTO creerOffre(OffreDTO offreDTO) {
    serviceDAL.commencerTransaction();
    OffreDTO offre = null;
    try {
      ((Objet) offreDTO.getObjetDTO()).premiereVersion();
      ObjetDTO objet = objetDAO.creerObjet(offreDTO.getObjetDTO());
      if (objet == null) {
        throw new BusinessException("L'objet n'a pas pu être créée");
      }
      ((Offre) offreDTO).premiereVersion();
      ((Offre) offreDTO).offrirObjet();
      offre = offreDAO.creerOffre(offreDTO);
      if (offre == null) {
        throw new BusinessException("L'offre n'a pas pu être créée");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return offre;
  }

  /**
   * Liste les offres.
   *
   * @return liste : la liste de toutes les offres
   */
  public List<OffreDTO> listerOffres() {
    serviceDAL.commencerTransaction();
    List<OffreDTO> liste = null;
    try {
      liste = offreDAO.listerOffres();
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return liste;
  }

  /**
   * Liste les offres les plus récentes.
   *
   * @return liste : la liste des offres les plus récentes
   */
  public List<OffreDTO> listerOffresRecentes() {
    serviceDAL.commencerTransaction();
    List<OffreDTO> liste = null;
    try {
      liste = offreDAO.listerOffresRecentes();
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return liste;
  }

  /**
   * Annuler une offre.
   *
   * @param offreDTO : id de l'offre à annuler
   * @return l'offre annulée
   * @throws BusinessException : lance une exception business si l'offre n'a pas pu être annulée
   */
  @Override
  public OffreDTO annulerOffre(OffreDTO offreDTO) {
    serviceDAL.commencerTransaction();
    try {
      ((Offre) offreDTO).annulerOffre();
      ObjetDTO objet = objetDAO.miseAJourObjet(offreDTO.getObjetDTO());
      if (objet == null) {
        ObjetDTO objetVerif = objetDAO.rechercheParId(offreDTO.getObjetDTO());
        if (objetVerif == null) {
          throw new PasTrouveException("L'objet n'existe pas");
        }
        throw new BusinessException("Données périmées");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return offreDTO;
  }

  /**
   * Recherche une offre par son id.
   *
   * @param idOffre id de l'offre recherchée
   * @return l'offre correspondante  l'id idS
   */
  @Override
  public OffreDTO rechercheParId(int idOffre) {
    serviceDAL.commencerTransaction();
    OffreDTO offre = null;
    try {
      offre = offreDAO.rechercheParId(idOffre);
      if (offre == null) {
        throw new BusinessException("L'offre n'a pas pu être trouvée.");
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return offre;
  }

  /**
   * Récupère les offres précédentes de l'objet avec l'id passé en paramètre.
   *
   * @param idObjet : l'id de l'objet à récupérer
   * @return liste : la liste des offres précédentes de l'objet avec l'id passé en paramètre
   */
  @Override
  public List<OffreDTO> offresPrecedentes(int idObjet) {
    serviceDAL.commencerTransaction();
    List<OffreDTO> liste = null;
    try {
      if (idObjet <= 0) {
        throw new BusinessException("L'id de l'objet est incorrect");
      }
      liste = offreDAO.offresPrecedentes(idObjet);
      if (liste.size() > 0) {
        liste.remove(0);
      }
    } catch (Exception e) {
      serviceDAL.retourEnArriereTransaction();
      throw e;
    }
    serviceDAL.commettreTransaction();
    return liste;
  }
}

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
  public OffreDTO creerUneOffre(OffreDTO offreDTO) {
    serviceDAL.commencerTransaction();
    ObjetDTO objet = objetDAO.creerObjet(offreDTO.getObjetDTO());
    if (objet == null) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("L'objet n'a pas pu être créée");
    }
    offreDTO.setObjetDTO(objet);
    OffreDTO offre = offreDAO.creerOffre(offreDTO);
    if (offre == null) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("L'offre n'a pas pu être créée");
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
    List<OffreDTO> liste = offreDAO.listerOffres();
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
    List<OffreDTO> liste = offreDAO.listerOffresRecentes();
    serviceDAL.commettreTransaction();
    return liste;
  }

  /**
   * Annuler une offre.
   *
   * @param idOffre : id de l'offre à annuler
   * @throws BusinessException : lance une exception business si l'offre n'a pas pu être annulée
   * @return l'offre annulée
   */
  @Override
  public OffreDTO annulerUneOffre(int idOffre) {
    System.out.println("début annuler offre UccImpl");
    OffreDTO offre = offreDAO.annulerOffre(idOffre);
    if (offre == null) {
      throw new BusinessException("l'offre n'a pas pu être annulée.");
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
      throw new BusinessException("l'offre n'a pas pu être annulée.");
    }
    return offre;
  }
}
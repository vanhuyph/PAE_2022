package be.vinci.pae.business.offre;

import be.vinci.pae.business.objet.Objet;
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
  public OffreDTO creerOffre(OffreDTO offreDTO) {
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
   * @param offreDTO : l'offre à annuler
   * @return l'offre annulée
   * @throws BusinessException : lance une exception business si l'offre n'a pas pu être annulée
   */
  @Override
  public OffreDTO annulerOffre(OffreDTO offreDTO) {
    serviceDAL.commencerTransaction();
    OffreDTO offreAnnulée = offreDAO.annulerOffre(offreDTO);
    if (offreAnnulée == null || offreAnnulée.getIdOffre() <= 0) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("L'offre n'a pas pu être annulée.");
    }
    serviceDAL.commettreTransaction();
    return offreAnnulée;
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
    OffreDTO offre = offreDAO.rechercheParId(idOffre);
    if (offre == null || offre.getIdOffre() <= 0) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("L'offre n'a pas pu être trouvée.");
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
    if (idObjet <= 0) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("L'id de l'objet est incorrect");
    }
    List<OffreDTO> liste = offreDAO.offresPrecedentes(idObjet);
    serviceDAL.commettreTransaction();
    return liste;
  }

  /**
   * Modifie une offre et son objet correspondant.
   *
   * @param offreAvecModification : l'offre contenant les modifications
   * @return l'offre modifiée
   * @throws BusinessException : lance une exception business si l'offre n'a pas pu être annulée
   */
  @Override
  public OffreDTO modifierOffre(OffreDTO offreAvecModification) {
    System.out.println("UCC Modifier Offre ");

    serviceDAL.commencerTransaction();
    Objet objet = (Objet) objetDAO.modifierObjet(offreAvecModification.getObjetDTO());
    if (objet == null) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("L'objet n'a pas pu être modifiée");
    }
    if (objet.verifierEtatPourModificationOffre()) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("L'objet est dans un état ne lui permettant pas d'être modifié");
    }
    OffreDTO offre = offreDAO.modifierOffre(offreAvecModification);
    if (offre == null) {
      serviceDAL.retourEnArriereTransaction();
      throw new BusinessException("L'offre n'a pas pu être modifiée");
    }
    serviceDAL.commettreTransaction();
    return offre;
  }

}

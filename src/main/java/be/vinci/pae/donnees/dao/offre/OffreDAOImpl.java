package be.vinci.pae.donnees.dao.offre;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.adresse.AdresseDTO;
import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.business.typeobjet.TypeObjetDTO;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.donnees.services.ServiceBackendDAL;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.utilitaires.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OffreDAOImpl implements OffreDAO {

  @Inject
  private DomaineFactory factory;
  @Inject
  private ServiceBackendDAL serviceBackendDAL;

  /**
   * Créer une offre.
   *
   * @param offreDTO : l'offre à créer
   * @return offreDTO : l'offre créée
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public OffreDTO creerOffre(OffreDTO offreDTO) {
    String requetePs = "INSERT INTO projet.offres VALUES (DEFAULT, ?, ?, ?) RETURNING *;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      Timestamp sqlDate = Timestamp.valueOf(LocalDateTime.now());
      ps.setInt(1, offreDTO.getObjetDTO().getIdObjet());
      ps.setTimestamp(2, sqlDate);
      ps.setString(3, offreDTO.getPlageHoraire());
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          offreDTO.setIdOffre(rs.getInt(1));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return offreDTO;
  }

  /**
   * Liste les offres.
   *
   * @return listeOffres : la liste d'offres
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  public List<OffreDTO> listerOffres() {
    String requetePs = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune, "
        + "u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, t.id_type, t.nom, o.id_objet, o.etat_objet, "
        + "o.description, o.photo, of.id_offre, of.date_offre, of.plage_horaire "
        + "FROM projet.offres of LEFT OUTER JOIN projet.objets o ON o.id_objet = of.id_objet "
        + "LEFT OUTER JOIN projet.utilisateurs u ON o.offreur = u.id_utilisateur "
        + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
        + "LEFT OUTER JOIN projet.types_objets t ON t.id_type = o.type_objet "
        + "WHERE o.etat_objet = 'Offert' OR o.etat_objet = 'Intéressé' "
        + "ORDER BY of.date_offre DESC";
    OffreDTO offreDTO = factory.getOffre();
    List<OffreDTO> liste;
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      liste = remplirListOffresDepuisResulSet(offreDTO, ps);
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return liste;
  }

  /**
   * Annule l'offre.
   *
   * @param id : est l'id de l'offre qu'on veut annulé
   * @return : un offreDTO avec seulement un id de l'offre annulé
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public OffreDTO annulerOffre(int id) {
    OffreDTO offreDTO = factory.getOffre();
    String requetePs = "UPDATE projet.objets SET etat_objet = 'Annulé' WHERE id_objet = ?;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setInt(1, id);
      ps.execute();
      offreDTO.setIdOffre(id);
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return offreDTO;
  }

  /**
   * Recherche une offre par son id.
   *
   * @param idOffre : l'id de l'objet correspondant à l'offre
   * @return offreDTO : l'offre
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public OffreDTO rechercheParId(int idOffre) {
    OffreDTO offreDTO = factory.getOffre();
    String requetePs = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune,"
        + "u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, t.id_type, t.nom, o.id_objet, o.etat_objet, "
        + "o.description, o.photo, of.id_offre, of.date_offre, of.plage_horaire "
        + "FROM projet.offres of LEFT OUTER JOIN projet.objets o ON o.id_objet = of.id_objet "
        + "LEFT OUTER JOIN projet.utilisateurs u ON o.offreur = u.id_utilisateur "
        + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
        + "LEFT OUTER JOIN projet.types_objets t ON t.id_type = o.type_objet "
        + "WHERE of.id_offre = ?;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setInt(1, idOffre);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          offreDTO = remplirOffreDepuisResultSet(offreDTO, rs);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return offreDTO;
  }

  /**
   * Liste les offres les plus récentes.
   *
   * @return liste : la liste des offres les plus récentes
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  public List<OffreDTO> listerOffresRecentes() {
    String requetePs = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune, "
        + "u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, t.id_type, t.nom, o.id_objet, o.etat_objet, "
        + "o.description, o.photo, of.id_offre, of.date_offre, of.plage_horaire "
        + "FROM projet.offres of LEFT OUTER JOIN projet.objets o ON o.id_objet = of.id_objet "
        + "LEFT OUTER JOIN projet.utilisateurs u ON o.offreur = u.id_utilisateur "
        + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
        + "LEFT OUTER JOIN projet.types_objets t ON t.id_type = o.type_objet "
        + "WHERE o.etat_objet = 'Offert' OR o.etat_objet = 'Intéressé' "
        + "ORDER BY of.date_offre DESC LIMIT 3;";
    OffreDTO offreDTO = factory.getOffre();
    List<OffreDTO> liste;
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      liste = remplirListOffresDepuisResulSet(offreDTO, ps);
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return liste;
  }

  @Override
  public List<OffreDTO> offresPrecedentes(int idObjet) {
    String requetePs = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune, "
        + "u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, t.id_type, t.nom, o.id_objet, o.etat_objet, "
        + "o.description, o.photo, of.id_offre, of.date_offre, of.plage_horaire "
        + "FROM projet.offres of LEFT OUTER JOIN projet.objets o ON o.id_objet = of.id_objet "
        + "LEFT OUTER JOIN projet.utilisateurs u ON o.offreur = u.id_utilisateur "
        + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
        + "LEFT OUTER JOIN projet.types_objets t ON t.id_type = o.type_objet "
        + "WHERE of.id_objet = ?;";
    OffreDTO offreDTO = factory.getOffre();
    List<OffreDTO> liste;
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setInt(1, idObjet);
      liste = remplirListOffresDepuisResulSet(offreDTO, ps);
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return liste;
  }

  /**
   * Rempli une liste d'offres depuis un ResultSet.
   *
   * @param offreDTO : l'offre vide, qui va être remplie
   * @param ps       : le PreparedStatement déjà mis en place
   * @return liste : la liste remplie
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  private List<OffreDTO> remplirListOffresDepuisResulSet(OffreDTO offreDTO, PreparedStatement ps) {
    List<OffreDTO> liste = new ArrayList<>();
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        offreDTO = remplirOffreDepuisResultSet(offreDTO, rs);
        liste.add(offreDTO);
        offreDTO = factory.getOffre();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return liste;
  }

  /**
   * Rempli les données de l'offre depuis un ResultSet.
   *
   * @param offreDTO : l'offre vide, qui va être rempli
   * @param rs       : le Result Statement déjà préparé
   * @return offreDTO : l'offre rempli
   */
  private OffreDTO remplirOffreDepuisResultSet(OffreDTO offreDTO, ResultSet rs) {
    ObjetDTO objetDTO = factory.getObjet();
    AdresseDTO adresseDTO = factory.getAdresse();
    UtilisateurDTO offreur = factory.getUtilisateur();
    TypeObjetDTO typeObjetDTO = factory.getTypeObjet();
    try {
      adresseDTO.setIdAdresse(rs.getInt(1));
      adresseDTO.setRue(rs.getString(2));
      adresseDTO.setNumero(rs.getInt(3));
      adresseDTO.setBoite(rs.getString(4));
      adresseDTO.setCodePostal(rs.getInt(5));
      adresseDTO.setCommune(rs.getString(6));

      offreur.setIdUtilisateur(rs.getInt(7));
      offreur.setPseudo(rs.getString(8));
      offreur.setNom(rs.getString(9));
      offreur.setPrenom(rs.getString(10));
      offreur.setMdp(rs.getString(11));
      offreur.setGsm(rs.getString(12));
      offreur.setEstAdmin(rs.getBoolean(13));
      offreur.setEtatInscription(rs.getString(14));
      offreur.setCommentaire(rs.getString(15));
      offreur.setAdresse(adresseDTO);

      typeObjetDTO.setIdType(rs.getInt(16));
      typeObjetDTO.setNom(rs.getString(17));

      objetDTO.setIdObjet(rs.getInt(18));
      objetDTO.setEtatObjet(rs.getString(19));
      objetDTO.setTypeObjet(typeObjetDTO);
      objetDTO.setDescription(rs.getString(20));
      objetDTO.setOffreur(offreur);
      objetDTO.setReceveur(null);
      objetDTO.setPhoto(rs.getString(21));

      offreDTO.setIdOffre(rs.getInt(22));
      offreDTO.setObjetDTO(objetDTO);
      offreDTO.setDateOffre(rs.getTimestamp(23).toLocalDateTime());
      offreDTO.setPlageHoraire(rs.getString(24));
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return offreDTO;
  }

}

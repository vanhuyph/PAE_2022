package be.vinci.pae.donnees.dao.offre;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.adresse.AdresseDTO;
import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.business.typeobjet.TypeObjetDTO;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.donnees.services.ServiceBackendDAL;
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
    String requetePs = "INSERT INTO projet.offres VALUES (DEFAULT, ?, ?, ?, ?) RETURNING *;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      Timestamp sqlDate = Timestamp.valueOf(LocalDateTime.now());
      ps.setInt(1, offreDTO.getObjetDTO().getIdObjet());
      ps.setTimestamp(2, sqlDate);
      ps.setString(3, offreDTO.getPlageHoraire());
      ps.setInt(4, offreDTO.getVersion());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          offreDTO.setIdOffre(rs.getInt(1));
          return offreDTO;
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
  }


  /**
   * Liste les offres.
   *
   * @return listeOffres : la liste d'offres
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  public List<OffreDTO> listerOffres() {
    String requetePs = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune,"
        + "a.version, u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, u.version, t.id_type, t.nom, o.id_objet, "
        + "o.etat_objet, o.description, o.photo, o.version, of.id_offre, of.date_offre, "
        + "of.plage_horaire, of.version "
        + "FROM projet.offres of LEFT OUTER JOIN projet.objets o ON o.id_objet = of.id_objet "
        + "LEFT OUTER JOIN projet.utilisateurs u ON o.offreur = u.id_utilisateur "
        + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
        + "LEFT OUTER JOIN projet.types_objets t ON t.id_type = o.type_objet "
        + "WHERE o.etat_objet = 'Offert' OR o.etat_objet = 'Intéressé' OR o.etat_objet = 'Annulé' "
        + "ORDER BY of.date_offre DESC";
    OffreDTO offreDTO = factory.getOffre();
    List<OffreDTO> liste;
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      liste = remplirListOffresDepuisResulSet(offreDTO, ps);
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return liste;
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
        + "a.version, u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, u.version, t.id_type, t.nom, o.id_objet, "
        + "o.etat_objet, o.description, o.photo, o.version, of.id_offre, of.date_offre, "
        + "of.plage_horaire, of.version "
        + "FROM projet.offres of LEFT OUTER JOIN projet.objets o ON o.id_objet = of.id_objet "
        + "LEFT OUTER JOIN projet.utilisateurs u ON o.offreur = u.id_utilisateur "
        + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
        + "LEFT OUTER JOIN projet.types_objets t ON t.id_type = o.type_objet "
        + "WHERE of.id_offre = ?;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setInt(1, idOffre);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          offreDTO = remplirOffreDepuisResultSet(offreDTO, rs);
          return offreDTO;
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
  }

  /**
   * Liste les offres les plus récentes.
   *
   * @return liste : la liste des offres les plus récentes
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  public List<OffreDTO> listerOffresRecentes() {
    String requetePs = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune,"
        + "a.version, u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, u.version, t.id_type, t.nom, o.id_objet, "
        + "o.etat_objet, o.description, o.photo, o.version, of.id_offre, of.date_offre, "
        + "of.plage_horaire, of.version "
        + "FROM projet.offres of LEFT OUTER JOIN projet.objets o ON o.id_objet = of.id_objet "
        + "LEFT OUTER JOIN projet.utilisateurs u ON o.offreur = u.id_utilisateur "
        + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
        + "LEFT OUTER JOIN projet.types_objets t ON t.id_type = o.type_objet "
        + "WHERE o.etat_objet = 'Offert' OR o.etat_objet = 'Intéressé' OR o.etat_objet = 'Annulé' "
        + "ORDER BY of.date_offre DESC LIMIT 3;";
    OffreDTO offreDTO = factory.getOffre();
    List<OffreDTO> liste;
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      liste = remplirListOffresDepuisResulSet(offreDTO, ps);
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return liste;
  }

  @Override
  public List<OffreDTO> offresPrecedentes(int idObjet) {
    String requetePs = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune,"
        + "a.version, u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, u.version, t.id_type, t.nom, o.id_objet, "
        + "o.etat_objet, o.description, o.photo, o.version, of.id_offre, of.date_offre, "
        + "of.plage_horaire, of.version "
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
      throw new FatalException(e.getMessage(), e);
    }
    return liste;
  }

  @Override
  public List<OffreDTO> mesOffres(int idUtilisateur) {
    String requetePs = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune,"
        + "a.version, u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, u.version, t.id_type, t.nom, o.id_objet, "
        + "o.etat_objet, o.description, o.photo, o.version, of.id_offre, of.date_offre, "
        + "of.plage_horaire, of.version "
        + "FROM projet.offres of LEFT OUTER JOIN projet.objets o ON o.id_objet = of.id_objet "
        + "LEFT OUTER JOIN projet.utilisateurs u ON o.offreur = u.id_utilisateur "
        + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
        + "LEFT OUTER JOIN projet.types_objets t ON t.id_type = o.type_objet "
        + "WHERE u.id_utilisateur = ? AND (o.etat_objet = 'Offert' OR o.etat_objet = 'Intéressé' OR "
        + "o.etat_objet = 'Annulé' OR o.etat_objet = 'Confirmé' ) ORDER BY of.date_offre DESC";

    OffreDTO offreDTO = factory.getOffre();
    List<OffreDTO> liste;
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setInt(1, idUtilisateur);
      liste = remplirListOffresDepuisResulSet(offreDTO, ps);
    } catch (SQLException e) {
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
      adresseDTO.setVersion(rs.getInt(7));

      offreur.setIdUtilisateur(rs.getInt(8));
      offreur.setPseudo(rs.getString(9));
      offreur.setNom(rs.getString(10));
      offreur.setPrenom(rs.getString(11));
      offreur.setMdp(rs.getString(12));
      offreur.setGsm(rs.getString(13));
      offreur.setEstAdmin(rs.getBoolean(14));
      offreur.setEtatInscription(rs.getString(15));
      offreur.setCommentaire(rs.getString(16));
      offreur.setAdresse(adresseDTO);
      offreur.setVersion(rs.getInt(17));

      typeObjetDTO.setIdType(rs.getInt(18));
      typeObjetDTO.setNom(rs.getString(19));

      objetDTO.setIdObjet(rs.getInt(20));
      objetDTO.setEtatObjet(rs.getString(21));
      objetDTO.setTypeObjet(typeObjetDTO);
      objetDTO.setDescription(rs.getString(22));
      objetDTO.setOffreur(offreur);
      objetDTO.setReceveur(null);
      objetDTO.setPhoto(rs.getString(23));
      objetDTO.setVersion(rs.getInt(24));

      offreDTO.setIdOffre(rs.getInt(25));
      offreDTO.setObjetDTO(objetDTO);
      offreDTO.setDateOffre(rs.getTimestamp(26).toLocalDateTime());
      offreDTO.setPlageHoraire(rs.getString(27));
      offreDTO.setVersion(rs.getInt(28));
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return offreDTO;
  }

}

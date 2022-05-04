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
import java.time.LocalDate;
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
        + "o.etat_objet, o.description, o.photo, o.version, o.vue, of.id_offre, of.date_offre, "
        + "of.plage_horaire, of.version "
        + "FROM projet.offres of LEFT OUTER JOIN projet.objets o ON o.id_objet = of.id_objet "
        + "LEFT OUTER JOIN projet.utilisateurs u ON o.offreur = u.id_utilisateur "
        + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
        + "LEFT OUTER JOIN projet.types_objets t ON t.id_type = o.type_objet "
        + "WHERE (o.etat_objet = 'Offert' OR o.etat_objet = 'Intéressé' OR o.etat_objet = 'Annulé' "
        + "OR o.etat_objet = 'Confirmé')"
        + " AND of.date_offre = (SELECT max(of2.date_offre) "
        + "FROM projet.offres of2, projet.objets o2 "
        + "WHERE of2.id_objet = o.id_objet)"
        + "ORDER BY of.date_offre DESC";
    OffreDTO offreDTO = factory.getOffre();
    List<OffreDTO> liste;
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      liste = remplirListeOffresDepuisResulSet(offreDTO, ps);
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
        + "o.etat_objet, o.description, o.photo, o.version, o.vue, of.id_offre, of.date_offre, "
        + "of.plage_horaire, of.version "
        + "FROM projet.offres of LEFT OUTER JOIN projet.objets o ON o.id_objet = of.id_objet "
        + "LEFT OUTER JOIN projet.utilisateurs u ON o.offreur = u.id_utilisateur "
        + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
        + "LEFT OUTER JOIN projet.types_objets t ON t.id_type = o.type_objet "
        + "WHERE of.id_offre = ?"
        + "AND of.date_offre = (SELECT max(of2.date_offre) "
        + "FROM projet.offres of2, projet.objets o2 "
        + "WHERE of2.id_objet = o.id_objet)"
        + ";";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setInt(1, idOffre);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return remplirOffreDepuisResultSet(offreDTO, rs);
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
        + "o.etat_objet, o.description, o.photo, o.version, o.vue, of.id_offre, of.date_offre, "
        + "of.plage_horaire, of.version "
        + "FROM projet.offres of LEFT OUTER JOIN projet.objets o ON o.id_objet = of.id_objet "
        + "LEFT OUTER JOIN projet.utilisateurs u ON o.offreur = u.id_utilisateur "
        + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
        + "LEFT OUTER JOIN projet.types_objets t ON t.id_type = o.type_objet "
        + "WHERE (o.etat_objet = 'Offert' OR o.etat_objet = 'Intéressé' "
        + "OR o.etat_objet = 'Confirmé') "
        + "AND of.date_offre = (SELECT max(of2.date_offre) "
        + "FROM projet.offres of2, projet.objets o2 "
        + "WHERE of2.id_objet = o.id_objet)"
        + "ORDER BY of.date_offre DESC LIMIT 3;";
    OffreDTO offreDTO = factory.getOffre();
    List<OffreDTO> liste;
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      liste = remplirListeOffresDepuisResulSet(offreDTO, ps);
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return liste;
  }

  /**
   * Récupère les offres précédentes de l'objet avec l'id passé en paramètre.
   *
   * @param idObjet : l'id de l'objet à récupérer
   * @return liste : la liste des offres précédentes de l'objet avec l'id passé en paramètre
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public List<OffreDTO> offresPrecedentes(int idObjet) {
    String requetePs = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune,"
        + "a.version, u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, u.version, t.id_type, t.nom, o.id_objet, "
        + "o.etat_objet, o.description, o.photo, o.version, o.vue, of.id_offre, of.date_offre, "
        + "of.plage_horaire, of.version "
        + "FROM projet.offres of LEFT OUTER JOIN projet.objets o ON o.id_objet = of.id_objet "
        + "LEFT OUTER JOIN projet.utilisateurs u ON o.offreur = u.id_utilisateur "
        + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
        + "LEFT OUTER JOIN projet.types_objets t ON t.id_type = o.type_objet "
        + "WHERE of.id_objet = ? "
        + "AND of.date_offre <> (SELECT max(of2.date_offre) "
        + "FROM projet.offres of2, projet.objets o2 "
        + "WHERE of2.id_objet = of.id_objet)"
        + "ORDER BY of.date_offre DESC;";
    OffreDTO offreDTO = factory.getOffre();
    List<OffreDTO> liste;
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setInt(1, idObjet);
      liste = remplirListeOffresDepuisResulSet(offreDTO, ps);
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return liste;
  }

  /**
   * Modifie une offre.
   *
   * @param offreAvecModification : : l'offre contenant les modifications
   * @return l'offre modifiée
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public OffreDTO modifierOffre(OffreDTO offreAvecModification) {
    String requetePs = "UPDATE projet.offres SET plage_horaire = ?, version = ? "
        + "WHERE id_offre = ? AND version = ?"
        + "RETURNING id_offre, date_offre, plage_horaire, version;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setString(1, offreAvecModification.getPlageHoraire());
      ps.setInt(2, offreAvecModification.getVersion() + 1);
      ps.setInt(3, offreAvecModification.getIdOffre());
      ps.setInt(4, offreAvecModification.getVersion());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return remplirOffrePourUpdate(offreAvecModification, rs);
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e.getMessage(), e);
    }
  }

  /**
   * Récupère tous les offres en fonction d'un critère de recherche (nom du membre, type d'objet ou
   * état d'objet.
   *
   * @param recherche : le critère de recherche
   * @param dateDebut : la date de début pour la recherche
   * @param dateFin   : la date de fin pour la recherche
   * @return liste : la liste des offres correspondante au critère de recherche
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public List<OffreDTO> rechercherOffres(String recherche, LocalDate dateDebut, LocalDate dateFin) {
    String requetePs;
    if (!recherche.equals("")) {
      requetePs =
          "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune,"
              + "a.version, u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin,"
              + "u.etat_inscription, u.commentaire, u.version, t.id_type, t.nom, o.id_objet, "
              + "o.etat_objet, o.description, o.photo, o.version, o.vue, of.id_offre, "
              + "of.date_offre, of.plage_horaire, of.version "
              + "FROM projet.offres of LEFT OUTER JOIN projet.objets o ON o.id_objet = of.id_objet "
              + "LEFT OUTER JOIN projet.utilisateurs u ON o.offreur = u.id_utilisateur "
              + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
              + "LEFT OUTER JOIN projet.types_objets t ON t.id_type = o.type_objet "
              + "WHERE (o.etat_objet = 'Offert' OR o.etat_objet = 'Intéressé' "
              + "OR o.etat_objet = 'Annulé' OR o.etat_objet = 'Confirmé') "
              + "AND (lower(u.nom) LIKE lower(?) "
              + "OR lower(t.nom) LIKE lower(?) OR lower(o.etat_objet) "
              + "LIKE lower(?)) "
              + "AND (of.date_offre BETWEEN ? AND ?) "
              + "AND of.date_offre = (SELECT max(of2.date_offre) "
              + "FROM projet.offres of2, projet.objets o2 "
              + "WHERE of2.id_objet = o.id_objet)"
              + "ORDER BY of.date_offre DESC";
    } else {
      requetePs =
          "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune,"
              + "a.version, u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin,"
              + "u.etat_inscription, u.commentaire, u.version, t.id_type, t.nom, o.id_objet, "
              + "o.etat_objet, o.description, o.photo, o.version, o.vue, of.id_offre, "
              + "of.date_offre, of.plage_horaire, of.version "
              + "FROM projet.offres of LEFT OUTER JOIN projet.objets o ON o.id_objet = of.id_objet "
              + "LEFT OUTER JOIN projet.utilisateurs u ON o.offreur = u.id_utilisateur "
              + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
              + "LEFT OUTER JOIN projet.types_objets t ON t.id_type = o.type_objet "
              + "WHERE (of.date_offre BETWEEN ? AND ?) "
              + "AND of.date_offre = (SELECT max(of2.date_offre) "
              + "FROM projet.offres of2, projet.objets o2 "
              + "WHERE of2.id_objet = o.id_objet)"
              + "ORDER BY of.date_offre DESC";
    }
    OffreDTO offreDTO = factory.getOffre();
    List<OffreDTO> liste;
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      if (!recherche.equals("")) {
        recherche = "%" + recherche + "%";
        ps.setString(1, recherche);
        ps.setString(2, recherche);
        ps.setString(3, recherche);
        ps.setTimestamp(4, Timestamp.valueOf(dateDebut.atStartOfDay()));
        ps.setTimestamp(5, Timestamp.valueOf(dateFin.atTime(23, 59)));
      } else {
        ps.setTimestamp(1, Timestamp.valueOf(dateDebut.atStartOfDay()));
        ps.setTimestamp(2, Timestamp.valueOf(dateFin.atTime(23, 59)));
      }
      liste = remplirListeOffresDepuisResulSet(offreDTO, ps);
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return liste;
  }

  /**
   * Liste les propres offres de l'utilisateur dont l'id est passé en paramètre.
   *
   * @param idUtilisateur : l'id de l'utilisateur à qui lister ses offres
   * @return liste : la liste de toutes ses propres offres
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public List<OffreDTO> mesOffres(int idUtilisateur) {
    String requetePs = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune,"
        + "a.version, u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, u.version, t.id_type, t.nom, o.id_objet, "
        + "o.etat_objet, o.description, o.photo, o.version, o.vue, of.id_offre, of.date_offre, "
        + "of.plage_horaire, of.version "
        + "FROM projet.offres of LEFT OUTER JOIN projet.objets o ON o.id_objet = of.id_objet "
        + "LEFT OUTER JOIN projet.utilisateurs u ON o.offreur = u.id_utilisateur "
        + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
        + "LEFT OUTER JOIN projet.types_objets t ON t.id_type = o.type_objet "
        + "WHERE u.id_utilisateur = ? AND (o.etat_objet = 'Offert' OR o.etat_objet = 'Intéressé' "
        + "OR o.etat_objet = 'Annulé' OR o.etat_objet = 'Confirmé' OR o.etat_objet = 'Empêché' ) "
        + "AND of.date_offre = (SELECT max(of2.date_offre) "
        + "FROM projet.offres of2, projet.objets o2 "
        + "WHERE of2.id_objet = o.id_objet)"
        + "ORDER BY of.date_offre DESC";
    OffreDTO offreDTO = factory.getOffre();
    List<OffreDTO> liste;
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setInt(1, idUtilisateur);
      liste = remplirListeOffresDepuisResulSet(offreDTO, ps);
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return liste;
  }

  /**
   * Liste les propres offres offertes et interessées de l'utilisateur dont l'id est passé en
   * paramètre.
   *
   * @param idUtilisateur : l'id de l'utilisateur à qui lister ses offres offertes et interessées
   * @return liste : la liste de toutes ses propres offres offertes et interessées
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public List<OffreDTO> mesOffresAEmpecher(int idUtilisateur) {
    String requetePs = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune,"
        + "a.version, u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, u.version, t.id_type, t.nom, o.id_objet, "
        + "o.etat_objet, o.description, o.photo, o.version, o.vue, of.id_offre, of.date_offre, "
        + "of.plage_horaire, of.version "
        + "FROM projet.offres of LEFT OUTER JOIN projet.objets o ON o.id_objet = of.id_objet "
        + "LEFT OUTER JOIN projet.utilisateurs u ON o.offreur = u.id_utilisateur "
        + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
        + "LEFT OUTER JOIN projet.types_objets t ON t.id_type = o.type_objet "
        + "WHERE u.id_utilisateur = ? AND (o.etat_objet = 'Offert' OR o.etat_objet = 'Intéressé') "
        + "AND of.date_offre = (SELECT max(of2.date_offre) "
        + "FROM projet.offres of2, projet.objets o2 "
        + "WHERE of2.id_objet = o.id_objet)"
        + "ORDER BY of.date_offre DESC";
    OffreDTO offreDTO = factory.getOffre();
    List<OffreDTO> liste;
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setInt(1, idUtilisateur);
      liste = remplirListeOffresDepuisResulSet(offreDTO, ps);
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return liste;
  }

  /**
   * Liste les propres offres empêchées de l'utilisateur dont l'id est passé en paramètre.
   *
   * @param idUtilisateur : l'id de l'utilisateur à qui lister ses offres empêchées
   * @return liste : la liste de toutes ses propres offres empêchées
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public List<OffreDTO> mesOffresEmpecher(int idUtilisateur) {
    String requetePs = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune,"
        + "a.version, u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, u.version, t.id_type, t.nom, o.id_objet, "
        + "o.etat_objet, o.description, o.photo, o.version, o.vue, of.id_offre, of.date_offre, "
        + "of.plage_horaire, of.version "
        + "FROM projet.offres of LEFT OUTER JOIN projet.objets o ON o.id_objet = of.id_objet "
        + "LEFT OUTER JOIN projet.utilisateurs u ON o.offreur = u.id_utilisateur "
        + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
        + "LEFT OUTER JOIN projet.types_objets t ON t.id_type = o.type_objet "
        + "WHERE u.id_utilisateur = ? AND o.etat_objet = 'Empêché' AND "
        + "of.date_offre = (SELECT max(of2.date_offre) FROM projet.offres of2, projet.objets o2 "
        + "WHERE of2.id_objet = o.id_objet) "
        + "ORDER BY of.date_offre DESC";
    OffreDTO offreDTO = factory.getOffre();
    List<OffreDTO> liste;
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setInt(1, idUtilisateur);
      liste = remplirListeOffresDepuisResulSet(offreDTO, ps);
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return liste;
  }

  /**
   * Liste les offres qui ont été attribuées à l'utilisateur dont l'id est passé en paramètre.
   *
   * @param idUtilisateur : l'id de l'utilisateur à qui lister ses offres attribuées
   * @return liste : la liste de toutes ses offres attribuées
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public List<OffreDTO> voirOffreAttribuer(int idUtilisateur) {
    String requetePs = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune,"
        + "a.version, u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, u.version, t.id_type, t.nom, o.id_objet, "
        + "o.etat_objet, o.description, o.photo, o.version, o.vue, of.id_offre, of.date_offre, "
        + "of.plage_horaire, of.version "
        + "FROM projet.offres of LEFT OUTER JOIN projet.objets o ON o.id_objet = of.id_objet "
        + "LEFT OUTER JOIN projet.interets i ON i.objet = o.id_objet "
        + "LEFT OUTER JOIN projet.utilisateurs u ON o.offreur = u.id_utilisateur "
        + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
        + "LEFT OUTER JOIN projet.types_objets t ON t.id_type = o.type_objet "
        + "WHERE i.utilisateur = ? AND i.receveur_choisi = true AND o.etat_objet = 'Confirmé' "
        + "AND o.vue = 'false' "
        + "ORDER BY of.date_offre DESC";
    OffreDTO offreDTO = factory.getOffre();
    List<OffreDTO> liste;
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setInt(1, idUtilisateur);
      liste = remplirListeOffresDepuisResulSet(offreDTO, ps);
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return liste;
  }

  /**
   * Liste les objets offerts de l'utilisateur dont l'id est passé en paramètre.
   *
   * @param idUtilisateur : l'id de l'utilisateur à qui lister ses objets offerts
   * @return liste : la liste de ses objets offerts
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public List<OffreDTO> objetsOffertsUtilisateur(int idUtilisateur) {
    String requetePs = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune, "
        + "a.version, u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, u.version, t.id_type, t.nom, o.id_objet, "
        + "o.etat_objet, o.description, o.photo, o.version, o.vue, of.id_offre, of.date_offre, "
        + "of.plage_horaire, of.version "
        + "FROM projet.offres of LEFT OUTER JOIN projet.objets o ON o.id_objet = of.id_objet "
        + "LEFT OUTER JOIN projet.utilisateurs u ON o.offreur = u.id_utilisateur "
        + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
        + "LEFT OUTER JOIN projet.types_objets t ON t.id_type = o.type_objet "
        + "WHERE u.id_utilisateur = ? AND o.etat_objet = 'Offert' "
        + "AND of.date_offre = (SELECT MAX(of2.date_offre) "
        + "FROM projet.offres of2, projet.objets o2 "
        + "WHERE of2.id_objet = o.id_objet)";
    return listeObjetsUtilisateur(idUtilisateur, requetePs);
  }

  /**
   * Liste les objets reçus de l'utilisateur dont l'id est passé en paramètre.
   *
   * @param idUtilisateur : l'id de l'utilisateur à qui lister ses objets reçus
   * @return liste : la liste de ses objets reçus
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public List<OffreDTO> objetsRecusUtilisateur(int idUtilisateur) {
    String requetePs = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune,"
        + "a.version, u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, u.version, t.id_type, t.nom, o.id_objet, "
        + "o.etat_objet, o.description, o.photo, o.version, o.vue, of.id_offre, of.date_offre, "
        + "of.plage_horaire, of.version "
        + "FROM projet.offres of LEFT OUTER JOIN projet.objets o ON o.id_objet = of.id_objet "
        + "LEFT OUTER JOIN projet.utilisateurs u ON o.receveur = u.id_utilisateur "
        + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
        + "LEFT OUTER JOIN projet.types_objets t ON t.id_type = o.type_objet "
        + "WHERE o.receveur = ? AND (o.etat_objet = 'Donné' OR o.etat_objet = 'Evalué') "
        + "AND of.date_offre = (SELECT MAX(of2.date_offre) "
        + "FROM projet.offres of2, projet.objets o2 "
        + "WHERE of2.id_objet = o.id_objet)";
    return listeObjetsUtilisateur(idUtilisateur, requetePs);
  }

  /**
   * Liste les objets de l'utilisateur dont l'id est passé en paramètre.
   *
   * @param idUtilisateur : l'id de l'utilisateur à qui lister ses objets
   * @param requetePs     : la requête
   * @return liste : la liste remplie
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  private List<OffreDTO> listeObjetsUtilisateur(int idUtilisateur, String requetePs) {
    List<OffreDTO> liste;
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setInt(1, idUtilisateur);
      OffreDTO offreDTO = factory.getOffre();
      liste = remplirListeOffresDepuisResulSet(offreDTO, ps);
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
  private List<OffreDTO> remplirListeOffresDepuisResulSet(OffreDTO offreDTO, PreparedStatement ps) {
    List<OffreDTO> liste = new ArrayList<>();
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        remplirOffreDepuisResultSet(offreDTO, rs);
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
   * @param offreDTO : l'offre vide, qui va être remplie
   * @param rs       : le ResultSet
   * @return offreDTO : l'offre remplie
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  private OffreDTO remplirOffreDepuisResultSet(OffreDTO offreDTO, ResultSet rs) {
    ObjetDTO objetDTO = factory.getObjet();
    AdresseDTO adresseDTO = factory.getAdresse();
    UtilisateurDTO offreurDTO = factory.getUtilisateur();
    TypeObjetDTO typeObjetDTO = factory.getTypeObjet();
    try {
      adresseDTO.setIdAdresse(rs.getInt(1));
      adresseDTO.setRue(rs.getString(2));
      adresseDTO.setNumero(rs.getInt(3));
      adresseDTO.setBoite(rs.getString(4));
      adresseDTO.setCodePostal(rs.getInt(5));
      adresseDTO.setCommune(rs.getString(6));
      adresseDTO.setVersion(rs.getInt(7));

      offreurDTO.setIdUtilisateur(rs.getInt(8));
      offreurDTO.setPseudo(rs.getString(9));
      offreurDTO.setNom(rs.getString(10));
      offreurDTO.setPrenom(rs.getString(11));
      offreurDTO.setMdp(rs.getString(12));
      offreurDTO.setGsm(rs.getString(13));
      offreurDTO.setEstAdmin(rs.getBoolean(14));
      offreurDTO.setEtatInscription(rs.getString(15));
      offreurDTO.setCommentaire(rs.getString(16));
      offreurDTO.setAdresse(adresseDTO);
      offreurDTO.setVersion(rs.getInt(17));

      typeObjetDTO.setIdType(rs.getInt(18));
      typeObjetDTO.setNom(rs.getString(19));

      objetDTO.setIdObjet(rs.getInt(20));
      objetDTO.setEtatObjet(rs.getString(21));
      objetDTO.setTypeObjet(typeObjetDTO);
      objetDTO.setDescription(rs.getString(22));
      objetDTO.setOffreur(offreurDTO);
      objetDTO.setReceveur(null);
      objetDTO.setPhoto(rs.getString(23));
      objetDTO.setVersion(rs.getInt(24));
      objetDTO.setVue(rs.getBoolean(25));

      offreDTO.setIdOffre(rs.getInt(26));
      offreDTO.setObjetDTO(objetDTO);
      offreDTO.setDateOffre(rs.getTimestamp(27).toLocalDateTime());
      offreDTO.setPlageHoraire(rs.getString(28));
      offreDTO.setVersion(rs.getInt(29));
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return offreDTO;
  }

  /**
   * Rempli les données de l'offre pour une mise à jour depuis un ResultSet.
   *
   * @param offreDTO : l'offre vide, qui va être remplie
   * @param rs       : le ResultSet
   * @return offreDTO : l'offre remplie
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  private OffreDTO remplirOffrePourUpdate(OffreDTO offreDTO, ResultSet rs) {
    try {
      offreDTO.setIdOffre(rs.getInt(1));
      offreDTO.setDateOffre(rs.getTimestamp(2).toLocalDateTime());
      offreDTO.setPlageHoraire(rs.getString(3));
      offreDTO.setVersion(rs.getInt(4));
      return offreDTO;
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
  }

}

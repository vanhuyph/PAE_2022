package be.vinci.pae.donnees.dao.interet;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.adresse.AdresseDTO;
import be.vinci.pae.business.interet.InteretDTO;
import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.typeobjet.TypeObjetDTO;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.donnees.services.ServiceBackendDAL;
import be.vinci.pae.utilitaires.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class InteretDAOImpl implements InteretDAO {

  @Inject
  private DomaineFactory factory;
  @Inject
  private ServiceBackendDAL serviceBackendDAL;

  /**
   * Ajoute un intérêt à l'objet.
   *
   * @param interetDTO : l'intérêt à ajouter
   * @return interetDTO : l'interetDTO rempli
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public InteretDTO ajouterInteret(InteretDTO interetDTO) {
    String requetePs =
        "INSERT INTO projet.interets VALUES (?, ?, ?, ?, ?, false, NULL, false, false) "
            + "RETURNING *;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      Date dateRdvSQL = new Date(interetDTO.getDateRdv().getTime());
      ps.setInt(1, interetDTO.getUtilisateur().getIdUtilisateur());
      ps.setInt(2, interetDTO.getObjet().getIdObjet());
      ps.setDate(3, dateRdvSQL);
      ps.setInt(4, interetDTO.getVersion());
      ps.setBoolean(5, interetDTO.isVue());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return interetDTO;
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
  }

  /**
   * Récupère le nombre de personnes intéressées de l'objet avec l'id passé en paramètre.
   *
   * @param idObjet : l'id de l'objet dont les personnes sont intéressées
   * @return nbPers : le nombre de personnes intéressées
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public int nbPersonnesInteressees(int idObjet) {
    String requetePS = "SELECT COUNT(i.utilisateur) FROM projet.interets i WHERE i.objet = ? "
        + "AND i.receveur_choisi = false AND i.venu_chercher IS NULL;";
    int nbPers = 0;
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePS)) {
      ps.setInt(1, idObjet);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          nbPers = rs.getInt(1);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return nbPers;
  }

  /**
   * Récupère l'intérêt de l'utilisateur pour un objet.
   *
   * @param idObjet       : l'id de l'objet
   * @param idUtilisateur : l'id de l'utilisateur
   * @return interet : l'intérêt trouvé, sinon null
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public InteretDTO interetUtilisateurPourObjet(int idObjet, int idUtilisateur) {
    String requetePs = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune, "
        + "u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, u.version, u.nb_objet_offert, u.nb_objet_donne, "
        + "u.nb_objet_recu, u.nb_objet_abandonne, a2.id_adresse, a2.rue, a2.numero, a2.boite, "
        + "a2.code_postal, a2.commune, u2.id_utilisateur, u2.pseudo, u2.nom, u2.prenom, u2.mdp, "
        + "u2.gsm, u2.est_admin, u2.etat_inscription, u2.commentaire, u2.version, "
        + "u2.nb_objet_offert, u2.nb_objet_donne, u2.nb_objet_recu, u2.nb_objet_abandonne, "
        + "t.id_type, t.nom, o.id_objet, o.etat_objet, o.description, o.photo, o.version, o.vue, "
        + "i.vue, i.date, i.receveur_choisi, i.venu_chercher, i.version, i.vue_empecher, "
        + "i.vue_reoffert "
        + "FROM projet.interets i, "
        + "projet.utilisateurs u, projet.utilisateurs u2, "
        + "projet.types_objets t, projet.adresses a, projet.adresses a2, projet.objets o "
        + "WHERE o.id_objet = ? AND i.objet = o.id_objet AND i.utilisateur = u.id_utilisateur AND "
        + "u.id_utilisateur = ? AND "
        + "a.id_adresse = u.adresse AND u2.id_utilisateur = o.offreur AND t.id_type = o.type_objet "
        + "AND a2.id_adresse = u2.adresse;";
    InteretDTO interetDTO = factory.getInteret();
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setInt(1, idObjet);
      ps.setInt(2, idUtilisateur);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return remplirInteretDepuisResulSet(interetDTO, rs);
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
   * Liste les intérêts pour l'objet dont l'id est passé en paramètre.
   *
   * @param idObjet : l'id de l'objet dont les personnes sont intéressées
   * @return liste : la liste des intérêts pour l'objet
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public List<InteretDTO> listeDesPersonnesInteressees(int idObjet) {
    String requetePS = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune, "
        + "u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, u.version, u.nb_objet_offert, u.nb_objet_donne, "
        + "u.nb_objet_recu, u.nb_objet_abandonne, a2.id_adresse, a2.rue, a2.numero, a2.boite, "
        + "a2.code_postal, a2.commune, u2.id_utilisateur, u2.pseudo, u2.nom, u2.prenom, u2.mdp, "
        + "u2.gsm, u2.est_admin, u2.etat_inscription, u2.commentaire, u2.version, "
        + "u2.nb_objet_offert, u2.nb_objet_donne, u2.nb_objet_recu, u2.nb_objet_abandonne, "
        + "t.id_type, t.nom, o.id_objet, o.etat_objet, o.description, o.photo, o.version, o.vue, "
        + "i.vue, i.date, i.receveur_choisi, i.venu_chercher, i.version, i.vue_empecher, "
        + "i.vue_reoffert "
        + "FROM projet.interets i, "
        + "projet.utilisateurs u, projet.utilisateurs u2, "
        + "projet.types_objets t, projet.adresses a, projet.adresses a2, projet.objets o "
        + "WHERE o.id_objet = ? AND i.objet = o.id_objet AND i.utilisateur = u.id_utilisateur AND "
        + "a.id_adresse = u.adresse AND u2.id_utilisateur = o.offreur AND t.id_type = o.type_objet "
        + "AND a2.id_adresse = u2.adresse AND u.etat_inscription = 'Confirmé' AND "
        + "i.receveur_choisi = false AND i.venu_chercher IS NULL "
        + "ORDER BY i.date, u.nom, u.prenom, u.pseudo;";
    InteretDTO interetDTO = factory.getInteret();
    List<InteretDTO> liste;
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePS)) {
      ps.setInt(1, idObjet);
      liste = remplirListeInteretDepuisResulSet(interetDTO, ps);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e.getMessage(), e);
    }
    return liste;
  }

  /**
   * Liste les intérêts non-vues pour les objets d'un utilisateur dont l'id est passé en paramètre.
   *
   * @param idOffreur : l'id de l'offreur dont les personnes sont intéressées ses objets
   * @return liste : la liste des intérêts non-vues
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public List<InteretDTO> listeDesPersonnesInteresseesVue(int idOffreur) {
    String requetePS = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune, "
        + "u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, u.version, u.nb_objet_offert, u.nb_objet_donne, "
        + "u.nb_objet_recu, u.nb_objet_abandonne, a2.id_adresse, a2.rue, a2.numero, a2.boite, "
        + "a2.code_postal, a2.commune, u2.id_utilisateur, u2.pseudo, u2.nom, u2.prenom, u2.mdp, "
        + "u2.gsm, u2.est_admin, u2.etat_inscription, u2.commentaire, u2.version, "
        + "u2.nb_objet_offert, u2.nb_objet_donne, u2.nb_objet_recu, u2.nb_objet_abandonne, "
        + "t.id_type, t.nom, o.id_objet, o.etat_objet, o.description, o.photo, o.version, o.vue, "
        + "i.vue, i.date, i.receveur_choisi, i.venu_chercher, i.version, i.vue_empecher, "
        + "i.vue_reoffert "
        + "FROM projet.interets i, "
        + "projet.utilisateurs u, projet.utilisateurs u2, "
        + "projet.types_objets t, projet.adresses a, projet.adresses a2, projet.objets o "
        + "WHERE o.offreur = ? AND i.objet = o.id_objet AND i.utilisateur = u.id_utilisateur AND "
        + "a.id_adresse = u.adresse AND u2.id_utilisateur = o.offreur AND t.id_type = o.type_objet "
        + "AND a2.id_adresse = u2.adresse AND i.vue = false AND u.etat_inscription = 'Confirmé' "
        + "AND i.receveur_choisi = false AND i.venu_chercher IS NULL "
        + "ORDER BY o.description, u.nom, u.prenom, u.pseudo;";
    InteretDTO interetDTO = factory.getInteret();
    List<InteretDTO> listeDesPersonnesInteressees;
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePS)) {
      ps.setInt(1, idOffreur);
      listeDesPersonnesInteressees = remplirListeInteretDepuisResulSet(interetDTO, ps);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e.getMessage(), e);
    }
    return listeDesPersonnesInteressees;
  }

  /**
   * Met à jour l'intérêt.
   *
   * @param interetDTO : l'intérêt à mettre à jour
   * @return interetDTO : l'intérêt mise à jour
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public InteretDTO miseAJourInteret(InteretDTO interetDTO) {
    String requetePs = "UPDATE projet.interets SET date = ?, vue = ?, "
        + "version = ?, receveur_choisi = ?, venu_chercher = ?, vue_empecher = ?, vue_reoffert = ? "
        + "WHERE version = ? AND utilisateur = ? AND objet = ? "
        + "RETURNING utilisateur, objet, version;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setDate(1, new Date(interetDTO.getDateRdv().getTime()));
      ps.setBoolean(2, interetDTO.isVue());
      ps.setInt(3, interetDTO.getVersion() + 1);
      ps.setBoolean(4, interetDTO.isReceveurChoisi());
      if (interetDTO.isVenuChercher() == null) {
        ps.setNull(5, Types.BOOLEAN);
      } else {
        ps.setBoolean(5, interetDTO.isVenuChercher());
      }
      ps.setBoolean(6, interetDTO.isVueEmpecher());
      ps.setBoolean(7, interetDTO.isVueReoffert());
      ps.setInt(8, interetDTO.getVersion());
      ps.setInt(9, interetDTO.getUtilisateur().getIdUtilisateur());
      ps.setInt(10, interetDTO.getObjet().getIdObjet());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          interetDTO.getUtilisateur().setIdUtilisateur(rs.getInt(1));
          interetDTO.getObjet().setIdObjet(rs.getInt(2));
          interetDTO.setVersion(rs.getInt(3));
          return interetDTO;
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
   * Permet de récupérer le receveur actuel de l'objet dont l'id est passé en paramètre.
   *
   * @param idObjet : l'id de l'objet
   * @return interetDTO : interetDTO
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public InteretDTO receveurActuel(int idObjet) {
    String requetePs = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune, "
        + "u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, u.version, u.nb_objet_offert, u.nb_objet_donne, "
        + "u.nb_objet_recu, u.nb_objet_abandonne, a2.id_adresse, a2.rue, a2.numero, a2.boite, "
        + "a2.code_postal, a2.commune, u2.id_utilisateur, u2.pseudo, u2.nom, u2.prenom, u2.mdp, "
        + "u2.gsm, u2.est_admin, u2.etat_inscription, u2.commentaire, u2.version, "
        + "u2.nb_objet_offert, u2.nb_objet_donne, u2.nb_objet_recu, u2.nb_objet_abandonne, "
        + "t.id_type, t.nom, o.id_objet, o.etat_objet, o.description, o.photo, o.version, o.vue, "
        + "i.vue, i.date, i.receveur_choisi, i.venu_chercher, i.version, i.vue_empecher, "
        + "i.vue_reoffert "
        + "FROM projet.interets i, "
        + "projet.utilisateurs u, projet.utilisateurs u2, projet.types_objets t, "
        + "projet.adresses a, projet.adresses a2, projet.objets o "
        + "WHERE o.id_objet = ? AND i.objet = o.id_objet AND i.utilisateur = u.id_utilisateur "
        + "AND a.id_adresse = u.adresse AND u2.id_utilisateur = o.offreur "
        + "AND t.id_type = o.type_objet AND a2.id_adresse = u2.adresse "
        + "AND i.receveur_choisi = true AND i.venu_chercher IS NULL;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setInt(1, idObjet);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          InteretDTO interetDTO = factory.getInteret();
          return remplirInteretDepuisResulSet(interetDTO, rs);
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
  }

  /**
   * Notifie le receveur actuel de l'objet que l'offreur a eu un empêchement.
   *
   * @param idUtilisateur : l'id du receveur qui va recevoir la notification
   * @return liste : la liste des notifications d'empêchements
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public List<InteretDTO> notifierReceveurEmpecher(int idUtilisateur) {
    String requetePS = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune, "
        + "u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, u.version, u.nb_objet_offert, u.nb_objet_donne, "
        + "u.nb_objet_recu, u.nb_objet_abandonne, a2.id_adresse, a2.rue, a2.numero, a2.boite, "
        + "a2.code_postal, a2.commune, u2.id_utilisateur, u2.pseudo, u2.nom, u2.prenom, u2.mdp, "
        + "u2.gsm, u2.est_admin, u2.etat_inscription, u2.commentaire, u2.version, "
        + "u2.nb_objet_offert, u2.nb_objet_donne, u2.nb_objet_recu, u2.nb_objet_abandonne, "
        + "t.id_type, t.nom, o.id_objet, o.etat_objet, o.description, o.photo, o.version, o.vue, "
        + "i.vue, i.date, i.receveur_choisi, i.venu_chercher, i.version, i.vue_empecher, "
        + "i.vue_reoffert "
        + "FROM projet.interets i, "
        + "projet.utilisateurs u, projet.utilisateurs u2, "
        + "projet.types_objets t, projet.adresses a, projet.adresses a2, projet.objets o "
        + "WHERE u.id_utilisateur = ? AND i.objet = o.id_objet AND i.utilisateur = u.id_utilisateur"
        + " AND a.id_adresse = u.adresse AND u2.id_utilisateur = o.offreur "
        + "AND t.id_type = o.type_objet AND a2.id_adresse = u2.adresse "
        + "AND u.etat_inscription = 'Confirmé' AND u2.etat_inscription = 'Empêché' "
        + "AND i.receveur_choisi = true AND i.vue_empecher = false AND i.venu_chercher IS NULL;";
    InteretDTO interetDTO = factory.getInteret();
    List<InteretDTO> liste;
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePS)) {
      ps.setInt(1, idUtilisateur);
      liste = remplirListeInteretDepuisResulSet(interetDTO, ps);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e.getMessage(), e);
    }
    return liste;
  }

  /**
   * Notifie le receveur qui n'est pas venu chercher l'objet que ce dernier est à nouveau réoffert.
   *
   * @param idUtilisateur : l'id du receveur qui va recevoir la notification
   * @return liste : la liste des notifications des objets reofferts
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public List<InteretDTO> objetANouveauOffert(int idUtilisateur) {
    String requetePS = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune, "
        + "u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, u.version, u.nb_objet_offert, u.nb_objet_donne, "
        + "u.nb_objet_recu, u.nb_objet_abandonne, a2.id_adresse, a2.rue, a2.numero, a2.boite, "
        + "a2.code_postal, a2.commune, u2.id_utilisateur, u2.pseudo, u2.nom, u2.prenom, u2.mdp, "
        + "u2.gsm, u2.est_admin, u2.etat_inscription, u2.commentaire, u2.version, "
        + "u2.nb_objet_offert, u2.nb_objet_donne, u2.nb_objet_recu, u2.nb_objet_abandonne, "
        + "t.id_type, t.nom, o.id_objet, o.etat_objet, o.description, o.photo, o.version, o.vue, "
        + "i.vue, i.date, i.receveur_choisi, i.venu_chercher, i.version, i.vue_empecher, "
        + "i.vue_reoffert "
        + "FROM projet.interets i, "
        + "projet.utilisateurs u, projet.utilisateurs u2, "
        + "projet.types_objets t, projet.adresses a, projet.adresses a2, projet.objets o "
        + "WHERE u.id_utilisateur = ? AND i.objet = o.id_objet AND i.utilisateur = u.id_utilisateur"
        + " AND a.id_adresse = u.adresse AND u2.id_utilisateur = o.offreur "
        + "AND t.id_type = o.type_objet AND a2.id_adresse = u2.adresse "
        + "AND i.receveur_choisi = true AND i.venu_chercher = false AND i.vue_reoffert = false;";
    InteretDTO interetDTO = factory.getInteret();
    List<InteretDTO> liste;
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePS)) {
      ps.setInt(1, idUtilisateur);
      liste = remplirListeInteretDepuisResulSet(interetDTO, ps);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e.getMessage(), e);
    }
    return liste;
  }

  /**
   * Rempli une liste d'intérêts depuis un PreparedStatement.
   *
   * @param interetDTO : l'intérêt vide, qui va être rempli
   * @param ps         : le PreparedStatement déjà mis en place
   * @return liste : la liste remplie
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  private List<InteretDTO> remplirListeInteretDepuisResulSet(InteretDTO interetDTO,
      PreparedStatement ps) {
    List<InteretDTO> liste = new ArrayList<>();
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        interetDTO = remplirInteretDepuisResulSet(interetDTO, rs);
        liste.add(interetDTO);
        interetDTO = factory.getInteret();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e.getMessage(), e);
    }
    return liste;
  }

  /**
   * Rempli les données de l'intérêt depuis un ResultSet.
   *
   * @param interetDTO : l'intérêt vide, qui va être rempli
   * @param rs         : le ResultSet déjà préparé
   * @return interetDTO : l'intérêt rempli
   */
  private InteretDTO remplirInteretDepuisResulSet(InteretDTO interetDTO, ResultSet rs) {
    AdresseDTO adresseInteresse = factory.getAdresse();
    AdresseDTO adresseOffreur = factory.getAdresse();
    UtilisateurDTO interesse = factory.getUtilisateur();
    UtilisateurDTO offreur = factory.getUtilisateur();
    TypeObjetDTO typeObjetDTO = factory.getTypeObjet();
    ObjetDTO objetDTO = factory.getObjet();
    try {
      adresseInteresse.setIdAdresse(rs.getInt(1));
      adresseInteresse.setRue(rs.getString(2));
      adresseInteresse.setNumero(rs.getInt(3));
      adresseInteresse.setBoite(rs.getString(4));
      adresseInteresse.setCodePostal(rs.getInt(5));
      adresseInteresse.setCommune(rs.getString(6));

      interesse.setIdUtilisateur(rs.getInt(7));
      interesse.setPseudo(rs.getString(8));
      interesse.setNom(rs.getString(9));
      interesse.setPrenom(rs.getString(10));
      interesse.setMdp(rs.getString(11));
      interesse.setGsm(rs.getString(12));
      interesse.setEstAdmin(rs.getBoolean(13));
      interesse.setEtatInscription(rs.getString(14));
      interesse.setCommentaire(rs.getString(15));
      interesse.setVersion(rs.getInt(16));
      interesse.setNbObjetOfferts(rs.getInt(17));
      interesse.setNbObjetDonnees(rs.getInt(18));
      interesse.setNbObjetRecus(rs.getInt(19));
      interesse.setNbObjetAbandonnes(rs.getInt(20));
      interesse.setAdresse(adresseInteresse);

      adresseOffreur.setIdAdresse(rs.getInt(21));
      adresseOffreur.setRue(rs.getString(22));
      adresseOffreur.setNumero(rs.getInt(23));
      adresseOffreur.setBoite(rs.getString(24));
      adresseOffreur.setCodePostal(rs.getInt(25));
      adresseOffreur.setCommune(rs.getString(26));

      offreur.setIdUtilisateur(rs.getInt(27));
      offreur.setPseudo(rs.getString(28));
      offreur.setNom(rs.getString(29));
      offreur.setPrenom(rs.getString(30));
      offreur.setMdp(rs.getString(31));
      offreur.setGsm(rs.getString(32));
      offreur.setEstAdmin(rs.getBoolean(33));
      offreur.setEtatInscription(rs.getString(34));
      offreur.setCommentaire(rs.getString(35));
      offreur.setVersion(rs.getInt(36));
      offreur.setNbObjetOfferts(rs.getInt(37));
      offreur.setNbObjetDonnees(rs.getInt(38));
      offreur.setNbObjetRecus(rs.getInt(39));
      offreur.setNbObjetAbandonnes(rs.getInt(40));
      offreur.setAdresse(adresseOffreur);

      typeObjetDTO.setIdType(rs.getInt(41));
      typeObjetDTO.setNom(rs.getString(42));

      objetDTO.setIdObjet(rs.getInt(43));
      objetDTO.setEtatObjet(rs.getString(44));
      objetDTO.setTypeObjet(typeObjetDTO);
      objetDTO.setDescription(rs.getString(45));
      objetDTO.setOffreur(offreur);
      objetDTO.setReceveur(null);
      objetDTO.setPhoto(rs.getString(46));
      objetDTO.setVersion(rs.getInt(47));
      objetDTO.setVue(rs.getBoolean(48));

      interetDTO.setUtilisateur(interesse);
      interetDTO.setVue(rs.getBoolean(49));
      interetDTO.setDateRdv(rs.getDate(50));
      interetDTO.setReceveurChoisi(rs.getBoolean(51));
      if (rs.getObject(52) == null) {
        interetDTO.setVenuChercher(null);
      } else {
        interetDTO.setVenuChercher(rs.getBoolean(52));
      }
      interetDTO.setVersion(rs.getInt(53));
      interetDTO.setObjet(objetDTO);
      interetDTO.setVueEmpecher(rs.getBoolean(54));
      interetDTO.setVueReoffert(rs.getBoolean(55));
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e.getMessage(), e);
    }
    return interetDTO;
  }

}

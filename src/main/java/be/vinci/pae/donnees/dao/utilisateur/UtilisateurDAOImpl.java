package be.vinci.pae.donnees.dao.utilisateur;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.adresse.AdresseDTO;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.donnees.services.ServiceBackendDAL;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.utilitaires.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAOImpl implements UtilisateurDAO {

  @Inject
  private DomaineFactory factory;
  @Inject
  private ServiceBackendDAL serviceBackendDAL;

  /**
   * Recherche un utilisateur via un pseudo unique dans la base de données.
   *
   * @param pseudo : le pseudo de l'utilisateur
   * @return utilisateurDTO : l'utilisateur, s'il trouve un utilisateur qui possède ce pseudo
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public UtilisateurDTO rechercheParPseudo(String pseudo) {
    UtilisateurDTO utilisateurDTO = factory.getUtilisateur();
    String requetePs =
        "SELECT u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
            + "u.etat_inscription, u.commentaire, a.id_adresse, a.rue, a.numero, a.boite, "
            + "a.code_postal, a.commune FROM projet.utilisateurs u "
            + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
            + "WHERE u.pseudo = ?;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setString(1, pseudo);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          utilisateurDTO = remplirUtilisateursDepuisRS(rs, utilisateurDTO);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return utilisateurDTO;
  }

  /**
   * Recherche un utilisateur via un id dans la base de données.
   *
   * @param id : l'id de l'utilisateur
   * @return utilisateurDTO : l'utilisateur, s'il trouve un utilisateur qui possède ce id
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public UtilisateurDTO rechercheParId(int id) {
    UtilisateurDTO utilisateurDTO = factory.getUtilisateur();
    String requetePs =
        "SELECT u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
            + "u.etat_inscription, u.commentaire,  a.id_adresse, a.rue, a.numero, a.boite, "
            + "a.code_postal, a.commune FROM projet.utilisateurs u "
            + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
            + "WHERE u.id_utilisateur = ?;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          utilisateurDTO = remplirUtilisateursDepuisRS(rs, utilisateurDTO);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return utilisateurDTO;
  }

  /**
   * Ajoute un utilisateur dans la base de données.
   *
   * @param utilisateur : l'utilisateur que l'on va ajouter
   * @return utilisateur : l'utilisateur qui a été ajouté
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public UtilisateurDTO ajouterUtilisateur(UtilisateurDTO utilisateur) {
    String requetePs = "INSERT INTO projet.utilisateurs "
        + "VALUES (DEFAULT, ?, ?, ?, ?, NULL, false, ?, 'En attente', NULL) RETURNING "
        + "id_utilisateur, etat_inscription, commentaire;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setString(1, utilisateur.getPseudo());
      ps.setString(2, utilisateur.getNom());
      ps.setString(3, utilisateur.getPrenom());
      ps.setString(4, utilisateur.getMdp());
      ps.setInt(5, utilisateur.getAdresse().getIdAdresse());
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          utilisateur.setIdUtilisateur(rs.getInt(1));
          utilisateur.setEtatInscription(rs.getString(2));
          utilisateur.setCommentaire(rs.getString(3));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return utilisateur;
  }

  /**
   * Met à jour l'état de l'inscription d'un utilisateur à "Confirmé".
   *
   * @param id       : l'id de l'utilisateur
   * @param estAdmin : si l'utilisateur est admin
   * @return utilisateurDTO : l'utilisateur avec l'état de son inscription à "Confirmé"
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public UtilisateurDTO confirmerInscription(int id, boolean estAdmin) {
    UtilisateurDTO utilisateurDTO = factory.getUtilisateur();
    String requetePs = "UPDATE projet.utilisateurs SET etat_inscription = ?, est_admin = ? "
        + "WHERE id_utilisateur = ? "
        + "RETURNING id_utilisateur, pseudo, nom, prenom, mdp, gsm, est_admin, "
        + "etat_inscription, commentaire, adresse;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setString(1, "Confirmé");
      ps.setBoolean(2, estAdmin);
      ps.setInt(3, id);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          utilisateurDTO = remplirUtilisateursDepuisRSSansAdresse(rs, utilisateurDTO);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return utilisateurDTO;
  }

  /**
   * Met à jour le commentaire et l'état de l'inscription d'un utilisateur à "Refusé".
   *
   * @param id          : l'id de l'utilisateur
   * @param commentaire : le commentaire que l'on va ajouter
   * @return utilisateurDTO : l'utilisateur mis à jour
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public UtilisateurDTO refuserInscription(int id, String commentaire) {
    UtilisateurDTO utilisateurDTO = factory.getUtilisateur();
    String requetePs = "UPDATE projet.utilisateurs SET etat_inscription = ?, commentaire = ? "
        + "WHERE id_utilisateur = ? "
        + "RETURNING id_utilisateur, pseudo, nom, prenom, mdp, gsm, est_admin, "
        + "etat_inscription, commentaire, adresse;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setString(1, "Refusé");
      ps.setString(2, commentaire);
      ps.setInt(3, id);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          utilisateurDTO = remplirUtilisateursDepuisRSSansAdresse(rs, utilisateurDTO);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return utilisateurDTO;
  }

  @Override
  public UtilisateurDTO modifierGsm(UtilisateurDTO utilisateurDTO) {
    String requetePs = "UPDATE projet.utilisateurs SET gsm = ? WHERE id_utilisateur = ? "
        + "RETURNING *;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setString(1, utilisateurDTO.getGsm());
      ps.setInt(2, utilisateurDTO.getIdUtilisateur());
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          utilisateurDTO = remplirUtilisateursDepuisRSSansAdresse(rs, utilisateurDTO);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return utilisateurDTO;
  }

  /**
   * Récupère tous les utilisateurs avec un certain état d'inscription et les placent dans une
   * liste.
   *
   * @param etatInscription : l'état de l'inscription
   * @return liste : la liste des utilisateurs avec l'état d'inscription passé en paramètre
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  public List<UtilisateurDTO> listerUtilisateursEtatsInscriptions(String etatInscription) {
    String requetePs =
        "SELECT u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
            + "u.etat_inscription, u.commentaire, a.id_adresse, a.rue, a.numero, a.boite, "
            + "a.code_postal, a.commune FROM projet.utilisateurs u "
            + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
            + "WHERE u.etat_inscription = ? ORDER BY u.pseudo;";
    List<UtilisateurDTO> liste = new ArrayList<>();
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setString(1, etatInscription);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          UtilisateurDTO utilisateurDTO = factory.getUtilisateur();
          remplirUtilisateursDepuisRS(rs, utilisateurDTO);
          liste.add(utilisateurDTO);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return liste;
  }

  /**
   * Rempli les données de l'utilisateur depuis un ResultSet.
   *
   * @param rs             : le ResultSet
   * @param utilisateurDTO : l'utilisateur vide, qui va être rempli
   * @return utilisateurDTO : l'utilisateur rempli
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  private UtilisateurDTO remplirUtilisateursDepuisRS(ResultSet rs, UtilisateurDTO utilisateurDTO) {
    try {
      utilisateurDTO = remplirUtilisateurSansAdresse(utilisateurDTO, rs.getInt(1),
          rs.getString(2), rs.getString(3), rs.getString(4),
          rs.getString(5), rs.getString(6), rs.getBoolean(7),
          rs.getString(8), rs.getString(9));
      AdresseDTO adresseDTO = factory.getAdresse();
      adresseDTO.setIdAdresse(rs.getInt(10));
      adresseDTO.setRue(rs.getString(11));
      adresseDTO.setNumero(rs.getInt(12));
      adresseDTO.setBoite(rs.getString(13));
      adresseDTO.setCodePostal(rs.getInt(14));
      adresseDTO.setCommune(rs.getString(15));
      utilisateurDTO.setAdresse(adresseDTO);
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return utilisateurDTO;
  }

  /**
   * Rempli les données de l'utilisateur depuis un ResultSet qui ne possède pas d'adresse.
   *
   * @param rs             : le ResultSet
   * @param utilisateurDTO : l'utilisateur vide, qui va être rempli
   * @return utilisateurDTO : l'utilisateur rempli
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  private UtilisateurDTO remplirUtilisateursDepuisRSSansAdresse(ResultSet rs,
      UtilisateurDTO utilisateurDTO) {
    try {
      utilisateurDTO = remplirUtilisateurSansAdresse(utilisateurDTO, rs.getInt(1),
          rs.getString(2), rs.getString(3), rs.getString(4),
          rs.getString(5), rs.getString(6), rs.getBoolean(7),
          rs.getString(8), rs.getString(9));
      AdresseDTO adresseDTO = factory.getAdresse();
      String requetePs = "SELECT id_adresse, rue, numero, boite, code_postal, commune "
          + "FROM projet.adresses WHERE id_adresse = ?;";
      try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
        ps.setInt(1, rs.getInt(10));
        try (ResultSet rs1 = ps.executeQuery()) {
          while (rs1.next()) {
            adresseDTO = remplirAdresseDepuisRS(rs1, adresseDTO);
          }
        }
      } catch (SQLException e) {
        e.printStackTrace();
        ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
        throw new FatalException(e.getMessage(), e);
      }
      utilisateurDTO.setAdresse(adresseDTO);
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return utilisateurDTO;
  }

  /**
   * Rempli les données de l'adresse depuis un ResultSet.
   *
   * @param rs         : le ResultSet
   * @param adresseDTO : l'adresse vide, qui va être rempli
   * @return adresseDTO : l'adresse rempli
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  private AdresseDTO remplirAdresseDepuisRS(ResultSet rs, AdresseDTO adresseDTO) {
    try {
      adresseDTO.setIdAdresse(rs.getInt(1));
      adresseDTO.setRue(rs.getString(2));
      adresseDTO.setNumero(rs.getInt(3));
      adresseDTO.setBoite(rs.getString(4));
      adresseDTO.setCodePostal(rs.getInt(5));
      adresseDTO.setCommune(rs.getString(6));
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return adresseDTO;
  }

  /**
   * Rempli les données de l'utilisateur sans adresse.
   *
   * @param utilisateurDTO  : l'utilisateur vide, qui va être rempli
   * @param id              : l'id de l'utilisateur
   * @param pseudo          : le pseudo de l'utilisateur
   * @param nom             : le nom de l'utilisateur
   * @param prenom          : le prenom de l'utilisateur
   * @param mdp             : le mot de passe de l'utilisateur
   * @param gsm             : le gsm de l'utilisateur
   * @param estAdmin        : si l'utilisateur est admin ou non
   * @param etatInscription : l'etat d'inscription de l'utilisateur
   * @param commentaire     : le commentaire de refus concernant l'inscription de l'utilisateur
   * @return utilisateurDTO  : l'utilisateur rempli
   */
  private UtilisateurDTO remplirUtilisateurSansAdresse(UtilisateurDTO utilisateurDTO, int id,
      String pseudo, String nom, String prenom, String mdp, String gsm, boolean estAdmin,
      String etatInscription, String commentaire) {
    utilisateurDTO.setIdUtilisateur(id);
    utilisateurDTO.setPseudo(pseudo);
    utilisateurDTO.setNom(nom);
    utilisateurDTO.setPrenom(prenom);
    utilisateurDTO.setMdp(mdp);
    utilisateurDTO.setGsm(gsm);
    utilisateurDTO.setEstAdmin(estAdmin);
    utilisateurDTO.setEtatInscription(etatInscription);
    utilisateurDTO.setCommentaire(commentaire);
    return utilisateurDTO;
  }

}
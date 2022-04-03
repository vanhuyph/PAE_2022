package be.vinci.pae.donnees.dao.utilisateur;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.adresse.AdresseDTO;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.donnees.services.ServiceBackendDAL;
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
            + "u.etat_inscription, u.commentaire, u.version, a.id_adresse, a.rue, a.numero, "
            + "a.boite, a.code_postal, a.commune, a.version FROM projet.utilisateurs u "
            + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
            + "WHERE u.pseudo = ?;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setString(1, pseudo);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          utilisateurDTO = remplirUtilisateursDepuisRS(rs, utilisateurDTO);
          return utilisateurDTO;
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
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
            + "u.etat_inscription, u.commentaire, u.version, a.id_adresse, a.rue, a.numero, "
            + "a.boite, a.code_postal, a.commune, a.version FROM projet.utilisateurs u "
            + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
            + "WHERE u.id_utilisateur = ?;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          utilisateurDTO = remplirUtilisateursDepuisRS(rs, utilisateurDTO);
          return utilisateurDTO;
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
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
        + "VALUES (DEFAULT, ?, ?, ?, ?, NULL, false, ?, ?, NULL, ?) RETURNING "
        + "id_utilisateur, commentaire;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setString(1, utilisateur.getPseudo());
      ps.setString(2, utilisateur.getNom());
      ps.setString(3, utilisateur.getPrenom());
      ps.setString(4, utilisateur.getMdp());
      ps.setInt(5, utilisateur.getAdresse().getIdAdresse());
      ps.setString(6, utilisateur.getEtatInscription());
      ps.setInt(7, utilisateur.getVersion());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          utilisateur.setIdUtilisateur(rs.getInt(1));
          utilisateur.setCommentaire(rs.getString(2));
          return utilisateur;
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
  }


  @Override
  public UtilisateurDTO miseAJourUtilisateur(UtilisateurDTO utilisateurDTO) {
    String requetePs =
        "UPDATE projet.utilisateurs SET pseudo = ?, nom = ?, prenom = ?, gsm = ?, est_admin = ?,"
            + " etat_inscription = ?, commentaire = ?, adresse = ?, version = ? "
            + "WHERE id_utilisateur = ? AND version = ? "
            + "RETURNING id_utilisateur, pseudo, nom, prenom, mdp, gsm, est_admin, adresse"
            + ", etat_inscription, commentaire, version;";

    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setString(1, utilisateurDTO.getPseudo());
      ps.setString(2, utilisateurDTO.getNom());
      ps.setString(3, utilisateurDTO.getPrenom());
      ps.setString(4, utilisateurDTO.getGsm());
      ps.setBoolean(5, utilisateurDTO.isEstAdmin());
      ps.setString(6, utilisateurDTO.getEtatInscription());
      ps.setString(7, utilisateurDTO.getCommentaire());
      ps.setInt(8, utilisateurDTO.getAdresse().getIdAdresse());
      ps.setInt(9, utilisateurDTO.getVersion() + 1);
      ps.setInt(10, utilisateurDTO.getIdUtilisateur());
      ps.setInt(11, utilisateurDTO.getVersion());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          utilisateurDTO = remplirUtilisateursDepuisRSSansAdresse(rs, utilisateurDTO);
          return utilisateurDTO;
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
  }


  @Override
  public UtilisateurDTO modifierGsm(UtilisateurDTO utilisateurDTO) {
    String requetePs =
        "UPDATE projet.utilisateurs SET gsm = ?, version = ? WHERE id_utilisateur = ? "
            + "AND version = ? "
            + "RETURNING *;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setString(1, utilisateurDTO.getGsm());
      ps.setInt(2, utilisateurDTO.getVersion() + 1);
      ps.setInt(3, utilisateurDTO.getIdUtilisateur());
      ps.setInt(4, utilisateurDTO.getVersion());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          utilisateurDTO = remplirUtilisateursDepuisRSSansAdresse(rs, utilisateurDTO);
          return utilisateurDTO;
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
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
            + "u.etat_inscription, u.commentaire, u.version, a.id_adresse, a.rue, a.numero, "
            + "a.boite, a.code_postal, a.commune, a.version FROM projet.utilisateurs u "
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
          rs.getString(8), rs.getString(9), rs.getInt(10));
      AdresseDTO adresseDTO = factory.getAdresse();
      adresseDTO.setIdAdresse(rs.getInt(11));
      adresseDTO.setRue(rs.getString(12));
      adresseDTO.setNumero(rs.getInt(13));
      adresseDTO.setBoite(rs.getString(14));
      adresseDTO.setCodePostal(rs.getInt(15));
      adresseDTO.setCommune(rs.getString(16));
      adresseDTO.setVersion(rs.getInt(17));
      utilisateurDTO.setAdresse(adresseDTO);
    } catch (SQLException e) {
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
          rs.getString(9), rs.getString(10), rs.getInt(11));
      AdresseDTO adresseDTO = factory.getAdresse();
      String requetePs = "SELECT id_adresse, rue, numero, boite, code_postal, commune "
          + "FROM projet.adresses WHERE id_adresse = ?;";
      try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
        ps.setInt(1, rs.getInt(8));
        try (ResultSet rs1 = ps.executeQuery()) {
          while (rs1.next()) {
            adresseDTO = remplirAdresseDepuisRS(rs1, adresseDTO);
          }
        }
      } catch (SQLException e) {
        throw new FatalException(e.getMessage(), e);
      }
      utilisateurDTO.setAdresse(adresseDTO);
    } catch (SQLException e) {
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
      String etatInscription, String commentaire, int version) {
    utilisateurDTO.setIdUtilisateur(id);
    utilisateurDTO.setPseudo(pseudo);
    utilisateurDTO.setNom(nom);
    utilisateurDTO.setPrenom(prenom);
    utilisateurDTO.setMdp(mdp);
    utilisateurDTO.setGsm(gsm);
    utilisateurDTO.setEstAdmin(estAdmin);
    utilisateurDTO.setEtatInscription(etatInscription);
    utilisateurDTO.setCommentaire(commentaire);
    utilisateurDTO.setVersion(version);
    return utilisateurDTO;
  }

}

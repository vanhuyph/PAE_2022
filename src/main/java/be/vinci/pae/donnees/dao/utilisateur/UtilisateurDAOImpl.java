package be.vinci.pae.donnees.dao.utilisateur;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
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
  private ServiceDAL serviceDAL;

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
    PreparedStatement ps = serviceDAL.getPs(
        "SELECT u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
            + "u.etat_inscription, u.commentaire, u.adresse"
            + " FROM projet.utilisateurs u WHERE u.pseudo = ?;");
    try {
      ps.setString(1, pseudo);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          utilisateurDTO = remplirUtilisateursDepuisRS(rs, utilisateurDTO);
        }
      }
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
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
    PreparedStatement ps = serviceDAL.getPs(
        "SELECT u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
            + "u.etat_inscription, u.commentaire, u.adresse"
            + " FROM projet.utilisateurs u WHERE u.id_utilisateur = ?;");
    try {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          utilisateurDTO = remplirUtilisateursDepuisRS(rs, utilisateurDTO);
        }
      }
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
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
    PreparedStatement ps = serviceDAL.getPs(
        "INSERT INTO projet.utilisateurs "
            + "VALUES (DEFAULT, ?, ?, ?, ?, NULL, false, ?, 'en attente', NULL) RETURNING "
            + "id_utilisateur;");
    try {
      ps.setString(1, utilisateur.getPseudo());
      ps.setString(2, utilisateur.getNom());
      ps.setString(3, utilisateur.getPrenom());
      ps.setString(4, utilisateur.getMdp());
      ps.setInt(5, utilisateur.getAdresse());
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          utilisateur.setIdUtilisateur(rs.getInt(1));
        }
      }
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e.getMessage(), e);
    }
    return utilisateur;
  }

  /**
   * Met à jour l'état de l'inscription d'un utilisateur à "confirmé".
   *
   * @param id       : l'id de l'utilisateur
   * @param estAdmin : si l'utilisateur est admin
   * @return utilisateurDTO : l'utilisateur avec l'état de son inscription à "confirmé"
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public UtilisateurDTO confirmerInscription(int id, boolean estAdmin) {
    UtilisateurDTO utilisateurDTO = factory.getUtilisateur();
    PreparedStatement ps = serviceDAL.getPs(
        "UPDATE projet.utilisateurs SET etat_inscription = ?, est_admin = ? "
            + "WHERE id_utilisateur = ? "
            + "RETURNING id_utilisateur, pseudo, nom, prenom, mdp, gsm, est_admin, "
            + "etat_inscription, commentaire, adresse;");
    try {
      ps.setString(1, "confirmé");
      ps.setBoolean(2, estAdmin);
      ps.setInt(3, id);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          utilisateurDTO = remplirUtilisateursDepuisRS(rs, utilisateurDTO);
        }
      }
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e.getMessage(), e);
    }
    return utilisateurDTO;
  }

  /**
   * Met à jour le commentaire et l'état de l'inscription d'un utilisateur à "refusé".
   *
   * @param id          : l'id de l'utilisateur
   * @param commentaire : le commentaire que l'on va ajouter
   * @return utilisateurDTO : l'utilisateur mis à jour
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public UtilisateurDTO refuserInscription(int id, String commentaire) {
    UtilisateurDTO utilisateurDTO = factory.getUtilisateur();
    PreparedStatement ps = serviceDAL.getPs(
        "UPDATE projet.utilisateurs SET etat_inscription = ?, commentaire = ? "
            + "WHERE id_utilisateur = ? "
            + "RETURNING id_utilisateur, pseudo, nom, prenom, mdp, gsm, est_admin, "
            + "etat_inscription, commentaire, adresse;");
    try {
      ps.setString(1, "refusé");
      ps.setString(2, commentaire);
      ps.setInt(3, id);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          utilisateurDTO = remplirUtilisateursDepuisRS(rs, utilisateurDTO);
        }
      }
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
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
    PreparedStatement ps = serviceDAL.getPs(
        "SELECT u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
            + "u.etat_inscription, u.commentaire, u.adresse "
            + "FROM projet.utilisateurs u WHERE u.etat_inscription = ? "
            + "ORDER BY u.id_utilisateur");
    List<UtilisateurDTO> liste = new ArrayList<>();
    try {
      ps.setString(1, etatInscription);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        UtilisateurDTO utilisateurDTO = factory.getUtilisateur();
        remplirUtilisateursDepuisRS(rs, utilisateurDTO);
        liste.add(utilisateurDTO);
      }
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
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
   * @throws FatalException : est lancée s'il y a un problème côté serveur
   */
  private UtilisateurDTO remplirUtilisateursDepuisRS(ResultSet rs, UtilisateurDTO utilisateurDTO) {
    try {
      utilisateurDTO.setIdUtilisateur(rs.getInt(1));
      utilisateurDTO.setPseudo(rs.getString(2));
      utilisateurDTO.setNom(rs.getString(3));
      utilisateurDTO.setPrenom(rs.getString(4));
      utilisateurDTO.setMdp(rs.getString(5));
      utilisateurDTO.setGsm(rs.getString(6));
      utilisateurDTO.setEstAdmin(rs.getBoolean(7));
      utilisateurDTO.setEtatInscription(rs.getString(8));
      utilisateurDTO.setCommentaire(rs.getString(9));
      utilisateurDTO.setAdresse(rs.getInt(10));
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e.getMessage(), e);
    }
    return utilisateurDTO;
  }

}




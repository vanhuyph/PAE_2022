package be.vinci.pae.donnees.dao.utilisateur;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.donnees.services.ServiceDAL;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilisateurDAOImpl implements UtilisateurDAO {

  @Inject
  private DomaineFactory factory;
  @Inject
  private ServiceDAL serviceDAL;

  /**
   * Recherche un utilisateur via un pseudo unique dans la base de donnée.
   *
   * @param pseudo : le pseudo de l'utilisateur
   * @return utilisateurDTO : l'utilisateur, s'il trouve un utilisateur qui possède ce pseudo
   */
  @Override
  public UtilisateurDTO rechercheParPseudo(String pseudo) {
    UtilisateurDTO utilisateurDTO = factory.getUtilisateur();
    PreparedStatement ps = serviceDAL.getPs(
        "SELECT id_utilisateur, pseudo, nom, prenom, mdp, gsm, est_admin, "
            + "etat_inscription, commentaire, adresse"
            + " FROM projet.utilisateurs WHERE pseudo = ?;");
    try {
      ps.setString(1, pseudo);
      return remplirUtilisateurDepuisResulSet(utilisateurDTO, ps);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return utilisateurDTO;
  }

  /**
   * Recherche un utilisateur via un id dans la base de donnée.
   *
   * @param id : l'id de l'utilisateur
   * @return utilisateurDTO : l'utilisateur, s'il trouve un utilisateur qui possède ce id
   */
  @Override
  public UtilisateurDTO rechercheParId(int id) {

    UtilisateurDTO utilisateurDTO = factory.getUtilisateur();
    PreparedStatement ps = serviceDAL.getPs(
        "SELECT * "
            + "FROM projet.utilisateurs WHERE id_utilisateur = ?;");
    try {
      ps.setInt(1, id);
      return remplirUtilisateurDepuisResulSet(utilisateurDTO, ps);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return utilisateurDTO;
  }


  /**
   * Ajoute un utilisateur dans la base de données.
   *
   * @param utilisateur : l'utilisateur que l'on va ajouter
   * @return utilisateur : l'utilisateur qui a été ajouté
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
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return utilisateur;
  }

  /**
   * Rempli les données de l'utilisateur depuis un ResultSet.
   *
   * @param utilisateurDTO : l'utilisateur vide, qui va être rempli
   * @param ps             : le PreparedStatement déjà mis en place
   * @return utilisateurDTO : l'utilisateur rempli
   * @throws SQLException : est lancée si il y a un problème
   */
  private UtilisateurDTO remplirUtilisateurDepuisResulSet(UtilisateurDTO utilisateurDTO,
      PreparedStatement ps) throws SQLException {
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        utilisateurDTO.setIdUtilisateur(rs.getInt(1));
        utilisateurDTO.setPseudo(rs.getString(2));
        utilisateurDTO.setNom(rs.getString(3));
        utilisateurDTO.setPrenom(rs.getString(4));
        utilisateurDTO.setMdp(rs.getString(5));
        if (rs.getString(6) != null) {
          utilisateurDTO.setGsm(rs.getString(6));
        }
        utilisateurDTO.setEstAdmin(rs.getBoolean(7));
        utilisateurDTO.setEtatInscription(rs.getString(8));
        if (rs.getString(9) != null) {
          utilisateurDTO.setCommentaire(rs.getString(9));
        }
        utilisateurDTO.setAdresse(rs.getInt(10));
      }
    }
    return utilisateurDTO;
  }

}




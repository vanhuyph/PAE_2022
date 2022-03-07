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
   * @throws SQLException : est lancée s'il ne trouve pas l'utilisateur
   */
  @Override
  public UtilisateurDTO rechercheParPseudo(String pseudo) {
    UtilisateurDTO utilisateurDTO = factory.getUtilisateur();
    PreparedStatement ps = serviceDAL.getPs(
        "SELECT u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
            + "u.etat_inscription, u.commentaire FROM projet.utilisateurs u WHERE u.pseudo = ?;");
    try {
      ps.setString(1, pseudo);
      utilisateurDTO = remplirUtilisateurDepuisResulSet(utilisateurDTO, ps);
      ps.close();
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
   * @throws SQLException : est lancée s'il ne trouve pas l'utilisateur
   */
  @Override
  public UtilisateurDTO rechercheParId(int id) {
    UtilisateurDTO utilisateurDTO = factory.getUtilisateur();
    PreparedStatement ps = serviceDAL.getPs(
        "SELECT u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
            + "u.etat_inscription, u.commentaire"
            + "FROM projet.utilisateurs u WHERE u.id_utilisateur = ?;");
    try {
      ps.setInt(1, id);
      utilisateurDTO = remplirUtilisateurDepuisResulSet(utilisateurDTO, ps);
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return utilisateurDTO;
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
        utilisateurDTO.setGsm(rs.getString(6));
        utilisateurDTO.setEstAdmin(rs.getBoolean(7));
        utilisateurDTO.setEtatInscription(rs.getString(8));
        utilisateurDTO.setCommentaire(rs.getString(9));
      }
    }
    return utilisateurDTO;
  }

}




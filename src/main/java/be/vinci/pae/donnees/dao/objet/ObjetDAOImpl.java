package be.vinci.pae.donnees.dao.objet;

import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.donnees.services.ServiceBackendDAL;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjetDAOImpl implements ObjetDAO {

  @Inject
  private ServiceBackendDAL serviceBackendDAL;


  /**
   * Créer un objet.
   *
   * @param objetDTO : l'objet à créer
   * @return objetDTO : l'objet créé
   * @throws SQLException : est lancé si il ne sait pas insérer l'objet dans la db
   */
  @Override
  public ObjetDTO creerObjet(ObjetDTO objetDTO) {
    PreparedStatement ps = serviceBackendDAL.getPs(
        "INSERT INTO projet.objets VALUES (DEFAULT, 'offert', ?, ?, ?, null, ?) RETURNING *;");
    try {
      ps.setInt(1, objetDTO.getTypeObjet().getIdType());
      ps.setString(2, objetDTO.getDescription());
      ps.setInt(3, objetDTO.getOffreur().getIdUtilisateur());
      ps.setString(4, objetDTO.getPhoto());
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          objetDTO.setIdObjet(rs.getInt(1));
        }

      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return objetDTO;
  }
}

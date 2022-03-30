package be.vinci.pae.donnees.dao.objet;

import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.donnees.services.ServiceBackendDAL;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.utilitaires.exceptions.FatalException;
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
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public ObjetDTO creerObjet(ObjetDTO objetDTO) {
    String requetePs = "INSERT INTO projet.objets VALUES (DEFAULT, 'Offert', ?, ?, ?, null, ?) "
        + "RETURNING *;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
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
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return objetDTO;
  }

  /**
   * Change l'état de l'objet.
   *
   * @param objetDTO : l'objet à changer d'état
   * @return objetDTO : l'objet avec son état changé
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public ObjetDTO changeEtatObjet(ObjetDTO objetDTO) {
    String requetePs = "UPDATE projet.objets SET etat_objet = ? WHERE id_objet = ?;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setString(1, objetDTO.getEtatObjet());
      ps.setInt(2, objetDTO.getIdObjet());
      ps.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return objetDTO;
  }

}

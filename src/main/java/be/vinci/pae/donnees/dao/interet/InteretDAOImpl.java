package be.vinci.pae.donnees.dao.interet;

import be.vinci.pae.business.interet.InteretDTO;
import be.vinci.pae.donnees.services.ServiceBackendDAL;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.utilitaires.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InteretDAOImpl implements InteretDAO {

  @Inject
  private ServiceBackendDAL serviceBackendDAL;

  /**
   * Ajoute un intérêt à l'objet.
   *
   * @param interetDTO : interet
   * @return interetDTO : interetDTO rempli
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public InteretDTO ajouterInteret(InteretDTO interetDTO) {
    String requetePs = "INSERT INTO projet.interets VALUES (?, ?, ?) RETURNING *;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      java.sql.Date dateRdvSQL = new java.sql.Date(interetDTO.getDateRdv().getTime());
      ps.setInt(1, interetDTO.getUtilisateur().getIdUtilisateur());
      ps.setInt(2, interetDTO.getObjet().getIdObjet());
      ps.setDate(3, dateRdvSQL);
      remplirInteretDepuisResultSet(interetDTO, ps);
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return interetDTO;
  }

  /**
   * Rempli les données de l'intérêt.
   *
   * @param interet : l'intérêt vide, qui va être rempli
   * @param ps      : le PreparedStatement
   * @return interet : l'intérêt rempli
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  private InteretDTO remplirInteretDepuisResultSet(InteretDTO interet, PreparedStatement ps) {
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        //interet.set(rs.getInt(1));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return interet;
  }

}
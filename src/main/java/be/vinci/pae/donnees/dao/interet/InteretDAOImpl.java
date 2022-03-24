package be.vinci.pae.donnees.dao.interet;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.interet.InteretDTO;
import be.vinci.pae.donnees.services.ServiceBackendDAL;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.utilitaires.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class InteretDAOImpl implements InteretDAO {

  @Inject
  private DomaineFactory factory;
  @Inject
  private ServiceBackendDAL serviceBackendDAL;

  /**
   * Ajoute un intérêt à l'objet.
   *
   * @param idUtilisateurInteresse : l'utilisateur qui est intéressé par l'objet
   * @param idObjet                : l'id de l'objet dont l'utilisateur est intéressé
   * @param dateRdv                : la date de RDV pour venir chercher l'objet
   * @return interetDTO : interetDTO rempli
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public InteretDTO ajouterInteret(int idUtilisateurInteresse, int idObjet, Date dateRdv) {
    String requetePs = "INSERT INTO projet.interets VALUES (?, ?, ?) RETURNING *;";
    InteretDTO interetDTO = factory.getInteret();
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      java.sql.Timestamp dateRdvSQL = new java.sql.Timestamp(dateRdv.getTime());
      ps.setInt(1, idUtilisateurInteresse);
      ps.setInt(2, idObjet);
      ps.setTimestamp(3, dateRdvSQL);
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
        interet.setIdUtilisateur(rs.getInt(1));
        interet.setIdObjet(rs.getInt(2));
        interet.setDateRdv(rs.getDate(3));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return interet;
  }

}
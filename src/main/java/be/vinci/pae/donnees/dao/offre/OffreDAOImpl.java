package be.vinci.pae.donnees.dao.offre;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import be.vinci.pae.donnees.services.ServiceDAL;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OffreDAOImpl implements OffreDAO {

  @Inject
  private DomaineFactory factory;
  @Inject
  private ServiceDAL serviceDAL;
  @Inject
  private ObjetDAO objetDAO;

  /**
   * Creer une offre.
   *
   * @param idObjet      : l'id de l'objet correspondant à l'offre
   * @param plageHoraire : plage horaire des disponibilité de l'offreur
   * @return
   */
  @Override
  public OffreDTO creerOffre(Integer idObjet, String plageHoraire) {
    OffreDTO offreDTO = factory.getOffre();
    PreparedStatement ps = serviceDAL.getPs("INSERT INTO projet.offres VALUES (DEFAULT, ?, ?, ?);");

    try {
      java.util.Date date = new java.util.Date();
      java.sql.Date sqlDate = new java.sql.Date(date.getTime());
      ps.setDate(1, sqlDate);
      ps.setInt(2, idObjet);
      ps.setString(3, plageHoraire);

      offreDTO = remplirOffreDepuisResultSet(offreDTO, ps);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return offreDTO;
  }

  /**
   * Rempli les données de l'offre depuis un ResultSet.
   *
   * @param offreDTO : l'offre vide, qui va être rempli
   * @param ps       : le PreparedStatement déjà mis en place
   * @return OffreDTO : l'offre rempli
   * @throws SQLException : est lancée si il y a un problème
   */
  private OffreDTO remplirOffreDepuisResultSet(OffreDTO offreDTO,
      PreparedStatement ps) throws SQLException {
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        offreDTO.setIdOffre(rs.getInt(1));
        offreDTO.setDateOffre(rs.getDate(2));
        ObjetDTO objetDTO = objetDAO.rechercheParId(rs.getInt(3));
        offreDTO.setObjet(objetDTO);
        offreDTO.setPlageHoraire(rs.getString(4));
      }
    }
    return offreDTO;
  }
}
// Ne pas oublier de fermer le preparedStatement quand on aura décidé où le faire
package be.vinci.pae.donnees.dao.offre;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import be.vinci.pae.donnees.services.ServiceBackendDAL;
import jakarta.inject.Inject;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import net.bytebuddy.asm.Advice.Local;

public class OffreDAOImpl implements OffreDAO {

  @Inject
  private DomaineFactory factory;
  @Inject
  private ServiceBackendDAL serviceBackendDAL;
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
    PreparedStatement ps = serviceBackendDAL.getPs("INSERT INTO projet.offres VALUES (DEFAULT, ?, ?, ?);");

    try {
      java.util.Date date = new java.util.Date();
      java.sql.Date sqlDate = new java.sql.Date(date.getTime());
      ps.setInt(1, idObjet);
      ps.setDate(2, sqlDate);
      ps.setString(3, plageHoraire);

      offreDTO = remplirOffreDepuisResultSet(offreDTO, ps);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return offreDTO;
  }

  /**
   * Rempli une liste d'offre.
   *
   * @return List : liste d'offre
   * @throws SQLException : est lancé si il y a un problème"
   */
  public List<OffreDTO> listOffres() {
    OffreDTO offreDTO = factory.getOffre();
    PreparedStatement ps = serviceBackendDAL.getPs(
        "SELECT of.id_offre, of.id_objet, of.date_offre, of.plage_horaire"
            + " FROM projet.objets o, projet.offres of WHERE o.id_objet = of.id_objet AND"
            + " (o.etat_objet = 'offert' OR o.etat_objet = 'interrese')"
            + " ORDER BY of.date_offre DESC;");
    List<OffreDTO> listOffres = null;
    try {

      listOffres = remplirListOffresDepuisResulSet(offreDTO, ps);
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return listOffres;
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
        offreDTO.setObjetDTO(objetDAO.rechercheParId(rs.getInt(2)));
        offreDTO.setDateOffre(convertirDateSQLEnLocalDateTime(rs.getDate(3)));
        offreDTO.setPlageHoraire(rs.getString(4));
      }
    }
    return offreDTO;
  }

  /**
   * Rempli une liste d'offre depuis un ResultSet.
   *
   * @param offreDTO : l'offre vide, qui va être rempli
   * @param ps       : le PreparedStatement déjà mis en place
   * @return List : liste remplie
   * @throws SQLException : est lancée si il y a un problème
   */
  private List<OffreDTO> remplirListOffresDepuisResulSet(OffreDTO offreDTO,
      PreparedStatement ps) throws SQLException {
    List<OffreDTO> listOffres = new ArrayList<>();
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {

        offreDTO.setIdOffre(rs.getInt(1));
        offreDTO.setObjetDTO(objetDAO.rechercheParId(rs.getInt(2)));
        offreDTO.setDateOffre(convertirDateSQLEnLocalDateTime(rs.getDate(3)));
        offreDTO.setPlageHoraire(rs.getString(4));
        listOffres.add(offreDTO);
        offreDTO = factory.getOffre();
      }
    }
    return listOffres;
  }

  public LocalDateTime convertirDateSQLEnLocalDateTime(Date dateToConvert) {
    return Instant.ofEpochMilli(dateToConvert.getTime())
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
  }
}
// Ne pas oublier de fermer le preparedStatement quand on aura décidé où le faire
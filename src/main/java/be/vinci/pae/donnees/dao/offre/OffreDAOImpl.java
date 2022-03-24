package be.vinci.pae.donnees.dao.offre;

import be.vinci.pae.business.DomaineFactory;
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
   * Créer une offre.
   *
   * @param idObjet      : l'id de l'objet correspondant à l'offre
   * @param plageHoraire : plage horaire des disponibilité de l'offreur
   * @return offreDTO : l'offre
   */
  @Override
  public OffreDTO creerOffre(int idObjet, String plageHoraire) {
    OffreDTO offreDTO = factory.getOffre();
    PreparedStatement ps = serviceDAL.getPs(
            "INSERT INTO projet.offres VALUES (DEFAULT, ?, ?, ?) RETURNING *;");
    try {
      java.util.Date date = new java.util.Date();
      java.sql.Date sqlDate = new java.sql.Date(date.getTime());
      ps.setInt(1, idObjet);
      ps.setDate(2, sqlDate);
      ps.setString(3, plageHoraire);
      return remplirOffreDepuisResultSet(offreDTO, ps);

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return offreDTO;
  }

  /**
  * Annuler une offre via un id.
  *
  * @param idOffre : est l'id de l'offre qu'on veut annulé
  * @return : un offreDTO avec seulement un id de l'offre annulé
  */
  @Override
  public OffreDTO annulerOffre(int idOffre) {
    System.out.println("idOffre annulerOffreDAO :" + idOffre);
    OffreDTO offreDTO = factory.getOffre();
    PreparedStatement ps = serviceDAL.getPs(
            "UPDATE projet.objets SET etat_objet = 'annulé' WHERE id_objet = ?;");

    try {
      ps.setInt(1, idOffre);
      ps.execute();


      offreDTO.setIdOffre(idOffre);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return offreDTO;
  }

  /**
   * Recherche une offre par son id.
   *
   * @param idOffre : l'id de l'objet correspondant à l'offre
   * @return offreDTO : l'offre
   */
  @Override
  public OffreDTO rechercheParId(int idOffre) {
    System.out.println("RechercheParId DAO :" + idOffre);
    OffreDTO offreDTO = factory.getOffre();
    PreparedStatement ps = serviceDAL.getPs(
            "SELECT * FROM projet.offres WHERE id_offre= ? ;");
    try {
      ps.setInt(1, idOffre);


      return remplirOffreDepuisResultSet(offreDTO, ps);


    } catch (SQLException e) {
      e.printStackTrace();
    }

    return offreDTO;
  }

  /**
   * Rempli les données de l'offre depuis un ResultSet.
   *
   * @param offreDTO : l'offre vide, qui va être rempli
   * @param ps       : le Prepared Statement déjà préparé
   * @return offreDTO : l'offre rempli
   * @throws SQLException : est lancée si il y a un problème
   */
  private OffreDTO remplirOffreDepuisResultSet(OffreDTO offreDTO,
                                               PreparedStatement ps) throws SQLException {
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        offreDTO.setIdOffre(rs.getInt(1));
        offreDTO.setObjet(
                objetDAO.rechercheParId(rs.getInt(2))); //voir si possible en sql
        offreDTO.setDateOffre(rs.getDate(3));
        offreDTO.setPlageHoraire(rs.getString(4));

      }
    } catch (SQLException e) {
      e.printStackTrace();
    }



    return offreDTO;
  }
}

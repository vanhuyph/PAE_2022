package be.vinci.pae.donnees.dao.objet;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.donnees.services.ServiceDAL;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjetDAOImpl implements ObjetDAO {

  @Inject
  private DomaineFactory factory;
  @Inject
  private ServiceDAL serviceDAL;


  /**
   * Creer un objet.
   *
   * @param etatObjet   : l'état de l'objet
   * @param typeObjet   : le type de l'objet
   * @param description : la description de l'objet
   * @param offreur     : l'id de l'offeur de l'objet
   * @param photo       : chemin de la photo
   * @return l'objet créé
   * @throws SQLException : est lancé si il ne sait pas insérer l'objet dans la db
   */
  @Override
  public ObjetDTO creerObjet(String etatObjet, int typeObjet, String description,
      int offreur, String photo) {
    {
      ObjetDTO objetDTO = factory.getObjet();
      PreparedStatement ps = serviceDAL.getPs(
          "INSERT INTO projet.objets VALUES (DEFAULT, ?, ?, ?, ?, null, ?)RETURNING *;");

      try {
        ps.setString(1, etatObjet);
        ps.setInt(2, typeObjet);
        ps.setString(3, description);
        ps.setInt(4, offreur);
        ps.setString(5, photo);

        objetDTO = remplirObjetDepuisResultSet(objetDTO, ps);
      } catch (SQLException e) {
        e.printStackTrace();
      }
      return objetDTO;
    }
  }

  /**
   * Recherche un objet via un id dans la base de donnée.
   *
   * @param id : l'id de l'objet
   * @return ObjetDTO : l'objet, s'il trouve un objet qui possède ce id
   * @throws SQLException : est lancée s'il ne trouve pas l'objet
   */
  @Override
  public ObjetDTO rechercheParId(int id) {
    ObjetDTO objetDTO = factory.getObjet();
    PreparedStatement ps = serviceDAL.getPs(
        "SELECT o.id_objet, o.etat_objet, o.type_objet, o.description,"
            + " o.offreur, o.receveur, o.photo"
            + " FROM projet.objets o WHERE o.id_objet = ?;");
    try {
      ps.setInt(1, id);

      objetDTO = remplirObjetDepuisResultSet(objetDTO, ps);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return objetDTO;
  }


  /**
   * Rempli les données de l'objet depuis un ResultSet.
   *
   * @param objetDTO : l'objet vide
   * @param ps       : le PreparedStatement déjà mis en place
   * @return objetDTO : l'objet rempli
   * @throws SQLException : est lancé si il y a un problème"
   */
  private ObjetDTO remplirObjetDepuisResultSet(ObjetDTO objetDTO, PreparedStatement ps) {
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        objetDTO.setIdObjet(rs.getInt(1));
        objetDTO.setEtatObjet(rs.getString(2));
        objetDTO.setTypeObjet(rs.getInt(3));
        objetDTO.setDescription(rs.getString(4));
        objetDTO.setOffreur(rs.getInt(5));
        if (rs.getInt(6) != 0) {

          objetDTO.setReceveur(rs.getInt(6));
        }
        if (rs.getString(7) != null) {
          objetDTO.setPhoto(rs.getString(7));
        }

      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return objetDTO;
  }
}

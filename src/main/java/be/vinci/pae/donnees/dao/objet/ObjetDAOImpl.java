package be.vinci.pae.donnees.dao.objet;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
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
  @Inject
  private UtilisateurDAO utilisateurDAO;


  /**
   * @param etat_objet  : l'état de l'objet
   * @param type_objet  : le type de l'objet
   * @param description : la description de l'objet
   * @param offreur     : l'id de l'offeur de l'objet
   * @param photo       : chemin de la photo
   * @return
   * @throws SQLException : est lancé si il ne sait pas insérer l'objet dans la db
   */
  @Override
  public ObjetDTO creerObjet(String etat_objet, String type_objet, String description,
      Integer offreur, String photo) {
    {
      ObjetDTO objetDTO = factory.getObjet();
      PreparedStatement ps = serviceDAL.getPs(
          "INSERT INTO projet.objets VALUES (DEFAULT, ?, ?, ?, ?, null, ?);");

      try {
        ps.setString(1, etat_objet);
        ps.setString(2, type_objet);
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
   * @param objetDTO : l'objet vide
   * @param ps       : le PreparedStatement déjà mis en place
   * @return objetDTO : l'objet rempli
   * @throws SQLException
   */
  private ObjetDTO remplirObjetDepuisResultSet(ObjetDTO objetDTO, PreparedStatement ps) {
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        objetDTO.setId_objet(rs.getInt(1));
        objetDTO.setEtat_objet(rs.getString(2));
        objetDTO.setType_objet(rs.getString(3));
        objetDTO.setDescription(rs.getString(4));
        UtilisateurDTO offreur = utilisateurDAO.rechercheParId(
            rs.getInt(5)); //vérifier le resultat ?
        objetDTO.setOffreur(offreur);
        if (rs.getInt(6) != 0) {
          UtilisateurDTO receveur = utilisateurDAO.rechercheParId(
              rs.getInt(6));//vérifier le resultat ?
          objetDTO.setReceveur(receveur);
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

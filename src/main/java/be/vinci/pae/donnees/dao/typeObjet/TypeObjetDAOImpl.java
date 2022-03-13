package be.vinci.pae.donnees.dao.typeObjet;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.typeObjet.TypeObjetDTO;
import be.vinci.pae.donnees.services.ServiceDAL;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TypeObjetDAOImpl implements TypeObjetDAO {

  @Inject
  private DomaineFactory factory;
  @Inject
  private ServiceDAL serviceDAL;

  /**
   * liste les types d'objet
   *
   * @return la liste des types d'objet
   */
  @Override
  public List<TypeObjetDTO> listerTypeObjet() {
    List<TypeObjetDTO> typesObjet = new ArrayList<TypeObjetDTO>();
    TypeObjetDTO typeObjetCourrant = factory.getTypeObjet();
    PreparedStatement ps = serviceDAL.getPs(
        "SELECT u.id_type, u.nom FROM projet.types_Objet_ u ;");
    try {
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {

        typeObjetCourrant = remplirTypeObjetDepuisResulSet(typeObjetCourrant, ps);
        typesObjet.add(typeObjetCourrant);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return typesObjet;
  }

  /**
   * Rempli les données du type d'objet depuis un ResultSet.
   *
   * @param TypeObjetDTO : le type d'objet vide, qui va être rempli
   * @param ps           : le PreparedStatement déjà mis en place
   * @return TypeObjetDTO : le type d'objet rempli rempli
   * @throws SQLException : est lancée si il y a un problème
   */
  private TypeObjetDTO remplirTypeObjetDepuisResulSet(TypeObjetDTO typeObjetDTO,
      PreparedStatement ps) throws SQLException {
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        typeObjetDTO.setIdType(rs.getInt(1));
        typeObjetDTO.setNom(rs.getString(2));
      }
    }
    return typeObjetDTO;
  }
}

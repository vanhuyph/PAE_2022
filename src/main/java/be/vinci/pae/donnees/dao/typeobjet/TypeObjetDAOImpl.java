package be.vinci.pae.donnees.dao.typeobjet;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.typeobjet.TypeObjetDTO;
import be.vinci.pae.donnees.services.ServiceBackendDAL;
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
  private ServiceBackendDAL serviceBackendDAL;

  /**
   * Liste les types d'objet.
   *
   * @return typesObjet : la liste des types d'objet
   */
  @Override
  public List<TypeObjetDTO> listerTypeObjet() {
    List<TypeObjetDTO> typesObjet = new ArrayList<>();
    PreparedStatement ps = serviceBackendDAL.getPs(
        "SELECT * FROM projet.types_Objets  ;");
    try {
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        TypeObjetDTO typeObjetCourrant = factory.getTypeObjet();
        typeObjetCourrant = remplirTypeObjetDepuisResulSet(typeObjetCourrant, rs);
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
   * @param typeObjetDTO : le type d'objet vide, qui va être rempli
   * @param rs           : le PreparedStatement déjà mis en place
   * @return typeObjetDTO : le type d'objet rempli rempli
   * @throws SQLException : est lancée si il y a un problème
   */
  private TypeObjetDTO remplirTypeObjetDepuisResulSet(TypeObjetDTO typeObjetDTO,
      ResultSet rs) throws SQLException {
    try {
      typeObjetDTO.setIdType(rs.getInt(1));
      typeObjetDTO.setNom(rs.getString(2));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return typeObjetDTO;
  }
}

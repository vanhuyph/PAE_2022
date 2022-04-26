package be.vinci.pae.donnees.dao.typeobjet;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.typeobjet.TypeObjetDTO;
import be.vinci.pae.donnees.services.ServiceBackendDAL;
import be.vinci.pae.utilitaires.exceptions.FatalException;
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
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public List<TypeObjetDTO> listerTypeObjet() {
    String requetePs = "SELECT * FROM projet.types_objets;";
    List<TypeObjetDTO> liste = new ArrayList<>();
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          TypeObjetDTO typeObjetCourant = factory.getTypeObjet();
          remplirTypeObjetDepuisResulSet(typeObjetCourant, rs);
          liste.add(typeObjetCourant);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return liste;
  }

  @Override
  public TypeObjetDTO creerTypeObjet(TypeObjetDTO typeObjetDTO) {
    String requetePs = "INSERT INTO projet.types_objets"
        + " VALUES (DEFAULT, ?, ?) RETURNING *";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setString(1, typeObjetDTO.getNom());
      ps.setInt(2, typeObjetDTO.getVersion());
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          remplirTypeObjetDepuisResulSet(typeObjetDTO, rs);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return typeObjetDTO;
  }

  @Override
  public TypeObjetDTO verifierUniqueTypeObjet(TypeObjetDTO typeObjetDTO) {
    String requetePs = "SELECT * FROM projet.types_objets WHERE nom = ? AND version = ?";

    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setString(1, typeObjetDTO.getNom());
      ps.setInt(2, typeObjetDTO.getVersion());
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          remplirTypeObjetDepuisResulSet(typeObjetDTO, rs);
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return typeObjetDTO;
  }

  /**
   * Rempli les données du type d'objet depuis un ResultSet.
   *
   * @param typeObjetDTO : le type d'objet vide, qui va être rempli
   * @param rs           : le ResultSet
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  private void remplirTypeObjetDepuisResulSet(TypeObjetDTO typeObjetDTO, ResultSet rs) {
    try {
      typeObjetDTO.setIdType(rs.getInt(1));
      typeObjetDTO.setNom(rs.getString(2));
      typeObjetDTO.setVersion(rs.getInt(3));
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
  }

}

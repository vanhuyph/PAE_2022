package be.vinci.pae.donnees.dao.adresse;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.adresse.AdresseDTO;
import be.vinci.pae.donnees.services.ServiceBackendDAL;
import be.vinci.pae.utilitaires.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class AdresseDAOImpl implements AdresseDAO {

  @Inject
  private ServiceBackendDAL serviceBackendDAL;
  @Inject
  private DomaineFactory domaineFactory;


  /**
   * Recherche une adresse via un id dans la base de données.
   *
   * @param id : l'id de l'adresse
   * @return adresseDTO : l'adresse récupérée
   * @throws FatalException : est lancée s'il y a un problème côté serveur
   */
  @Override
  public AdresseDTO rechercheParId(int id) {
    AdresseDTO adresseDTO = domaineFactory.getAdresse();
    String requetePs = "SELECT id_adresse, rue, numero, boite, code_postal, commune, version "
        + "FROM projet.adresses WHERE id_adresse = ?;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return remplirAdresseDepuisResultSet(adresseDTO, rs);
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
  }

  /**
   * Ajoute une adresse dans la base de données.
   *
   * @param adresseDTO : l'adresse à ajouter
   * @return adresseDTO : l'adresse ajoutée
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public AdresseDTO ajouterAdresse(AdresseDTO adresseDTO) {
    String requetePs = "INSERT INTO projet.adresses VALUES (DEFAULT, ?, ?, ?, ?, ?, ?) "
        + "RETURNING id_adresse, rue, numero, boite, code_postal, commune, version;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setString(1, adresseDTO.getRue());
      ps.setInt(2, adresseDTO.getNumero());
      if (adresseDTO.getBoite() == null || adresseDTO.getBoite().isBlank()) {
        ps.setNull(3, Types.INTEGER);
      } else {
        ps.setString(3, adresseDTO.getBoite());
      }
      ps.setInt(4, adresseDTO.getCodePostal());
      ps.setString(5, adresseDTO.getCommune());
      ps.setInt(6, adresseDTO.getVersion());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          adresseDTO = remplirAdresseDepuisResultSet(adresseDTO, rs);
          return adresseDTO;
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
  }

  /**
   * Met à jour l'adresse.
   *
   * @param adresseDTO : l'adresse à mettre à jour
   * @return adresseDTO : l'adresse mise à jour
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public AdresseDTO miseAJourAdresse(AdresseDTO adresseDTO) {
    String requetePs = "UPDATE projet.adresses SET rue = ?, numero = ?, boite = ?, "
        + "code_postal = ?, commune = ?, version = ? WHERE id_adresse = ? AND version = ? "
        + "RETURNING id_adresse, rue, numero, boite, code_postal, commune, version;";

    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      return recupAdresseDTODepuisPs(adresseDTO, ps);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e.getMessage(), e);
    }
  }

  /**
   * Rempli les données de l'adresse depuis un ResultSet.
   *
   * @param rs      : le ResultSet
   * @param adresse : l'adresse vide, qui va être rempli
   * @return adresse : l'adresse rempli
   * @throws FatalException : est lancée s'il y a un problème côté serveur
   */
  private AdresseDTO remplirAdresseDepuisResultSet(AdresseDTO adresse, ResultSet rs) {
    try {
      adresse.setIdAdresse(rs.getInt(1));
      adresse.setRue(rs.getString(2));
      adresse.setNumero(rs.getInt(3));
      adresse.setBoite(rs.getString(4));
      adresse.setCodePostal(rs.getInt(5));
      adresse.setCommune(rs.getString(6));
      adresse.setVersion(rs.getInt(7));
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
    return adresse;
  }

  /**
   * Récupère une adresseDTO depuis un PreparedStatement.
   *
   * @param adresseDTO : l'adresse à récupérer
   * @param ps         : le PreparedStatement
   * @return adresseDTO : l'adresse récupéré
   * @throws FatalException : est lancée s'il y a un problème côté serveur
   */
  private AdresseDTO recupAdresseDTODepuisPs(AdresseDTO adresseDTO, PreparedStatement ps) {
    try {
      ps.setString(1, adresseDTO.getRue());
      ps.setInt(2, adresseDTO.getNumero());
      if (adresseDTO.getBoite() == null || adresseDTO.getBoite().isBlank()) {
        ps.setNull(3, Types.INTEGER);
      } else {
        ps.setString(3, adresseDTO.getBoite());
      }
      ps.setInt(4, adresseDTO.getCodePostal());
      ps.setString(5, adresseDTO.getCommune());
      ps.setInt(6, adresseDTO.getVersion() + 1);
      ps.setInt(7, adresseDTO.getIdAdresse());
      ps.setInt(8, adresseDTO.getVersion());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          adresseDTO = remplirAdresseDepuisResultSet(adresseDTO, rs);
          return adresseDTO;
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e.getMessage(), e);
    }
  }
}

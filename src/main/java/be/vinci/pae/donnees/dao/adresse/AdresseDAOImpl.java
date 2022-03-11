package be.vinci.pae.donnees.dao.adresse;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.adresse.AdresseDTO;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.utilitaires.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class AdresseDAOImpl implements AdresseDAO {

  @Inject
  private DomaineFactory factory;
  @Inject
  private ServiceDAL serviceDAL;

  /**
   * Ajoute une adresse dans la base de données.
   *
   * @param rue        : la rue de l'adresse
   * @param numero     : le numero de l'adresse
   * @param boite      : la boite de l'adresse
   * @param codePostal : le code postal de l'adresse
   * @param commune    : la commune de l'adresse
   * @return adresseDTO : l'adresse ajoutée
   * @throws SQLException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public AdresseDTO ajouterAdresse(String rue, int numero, int boite, int codePostal,
      String commune) {
    AdresseDTO adresseDTO = factory.getAdresse();
    PreparedStatement ps = serviceDAL.getPs(
        "INSERT INTO projet.adresses VALUES (DEFAULT, ?, ?, ?, ?, ?)  RETURNING id_adresse, "
            + "rue, numero, boite, code_postal, commune;");
    try {
      ps.setString(1, rue);
      ps.setInt(2, numero);
      if (boite == 0) {
        ps.setNull(3, Types.INTEGER);
      } else {
        ps.setInt(3, boite);
      }
      ps.setInt(4, codePostal);
      ps.setString(5, commune);
      adresseDTO = remplirAdresseDepuisResultSet(adresseDTO, ps);
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e.getMessage(), e);
    }
    return adresseDTO;
  }

  /**
   * Rempli les données de l'adresse depuis un ResultSet.
   *
   * @param ps      : le PreparedStatement
   * @param adresse : l'adresse vide, qui va être rempli
   * @return adresse : l'adresse rempli
   * @throws SQLException : est lancée s'il y a un problème côté serveur
   */
  private AdresseDTO remplirAdresseDepuisResultSet(AdresseDTO adresse, PreparedStatement ps) {
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        adresse.setIdAdresse(rs.getInt(1));
        adresse.setRue(rs.getString(2));
        adresse.setNumero(rs.getInt(3));
        adresse.setBoite(rs.getInt(4));
        adresse.setCodePostal(rs.getInt(5));
        adresse.setCommune(rs.getString(6));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e.getMessage(), e);
    }
    return adresse;
  }

}

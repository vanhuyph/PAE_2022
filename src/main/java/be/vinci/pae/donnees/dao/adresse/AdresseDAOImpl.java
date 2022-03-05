package be.vinci.pae.donnees.dao.adresse;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.adresse.AdresseDTO;
import be.vinci.pae.donnees.services.ServiceDAL;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdresseDAOImpl implements AdresseDAO {

  @Inject
  private DomaineFactory factory;

  @Inject
  private ServiceDAL serviceDAL;


  /**
   * Ajoute une adresse dans la base de donnée.
   *
   * @param rue        : la rue de l'adresse
   * @param numero     : le numero de l'adresse
   * @param boite      : la boite de l'adresse
   * @param codePostal : le code postal de l'adresse
   * @param commune    : la commune de l'adresse
   * @return : l'adresse ajoutée
   * @throws SQLException : est lancé si l'adresse n'a pas pu être ajoutée
   */
  @Override
  public AdresseDTO ajouterAdresse(String rue, int numero, int boite, int codePostal,
      String commune) {

    AdresseDTO adresseDTO = factory.getAdresse();

    PreparedStatement ps = serviceDAL.getPs(
        "INSERT INTO projet.adresses VALUES (?, ?, ?, ?, ?, ?);");
    adresseDTO.setIdAdresse(prochaineIdAdresse());
    adresseDTO.setRue(rue);
    adresseDTO.setNumero(numero);
    adresseDTO.setBoite(boite);
    adresseDTO.setCodePostal(codePostal);
    adresseDTO.setCommune(commune);

    try {
      ps.setInt(1, adresseDTO.getIdAdresse());
      ps.setString(2, adresseDTO.getRue());
      ps.setInt(3, adresseDTO.getNumero());
      ps.setInt(4, adresseDTO.getBoite());
      ps.setInt(5, adresseDTO.getCodePostal());
      ps.setString(6, adresseDTO.getCommune());
      ps.execute();

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return adresseDTO;
  }


  /**
   * Recupere le prochain id dans la table adresses
   *
   * @return prochainId + 1: le prochain id
   */
  private int prochaineIdAdresse() {
    int prochainId = 0;
    PreparedStatement ps = serviceDAL.getPs(
        "SELECT MAX(id_adresse) FROM projet.adresses");
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        prochainId = rs.getInt(1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return prochainId + 1;
  }

}

package be.vinci.pae.donnees.dao.objet;

import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.donnees.services.ServiceBackendDAL;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.utilitaires.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class ObjetDAOImpl implements ObjetDAO {

  @Inject
  private ServiceBackendDAL serviceBackendDAL;

  /**
   * Créer un objet.
   *
   * @param objetDTO : l'objet à créer
   * @return objetDTO : l'objet créé
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public ObjetDTO creerObjet(ObjetDTO objetDTO) {
    String requetePs = "INSERT INTO projet.objets VALUES (DEFAULT, 'Offert', ?, ?, ?, null, ?) "
        + "RETURNING *;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setInt(1, objetDTO.getTypeObjet().getIdType());
      ps.setString(2, objetDTO.getDescription());
      ps.setInt(3, objetDTO.getOffreur().getIdUtilisateur());
      ps.setString(4, objetDTO.getPhoto());
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          objetDTO.setIdObjet(rs.getInt(1));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return objetDTO;
  }


  /**
   * Recherche l'objet en fonction de son id.
   *
   * @param objetDTO : l'objet que l'on veut rechercher
   * @return objetDTO : l'objet retrouvé, null sinon
   * @throws FatalException : est lancée s'il y a un problème côté serveur
   */
  @Override
  public ObjetDTO rechercheParId(ObjetDTO objetDTO) {
    String requetePs = "SELECT id_objet FROM projet.objets WHERE id_objet = ?;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setInt(1, objetDTO.getIdObjet());
      objetDTO.setIdObjet(0);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          objetDTO.setIdObjet(rs.getInt(1));
          return objetDTO;
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
  }


  /**
   * Met à jour l'objet.
   *
   * @param objetDTO : l'objet à mettre a jour
   * @return objetDTO : l'objet mit à jour
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public ObjetDTO miseAJourObjet(ObjetDTO objetDTO) {
    String requetePs = "UPDATE projet.objets SET etat_objet = ?, type_objet = ?, description = ?, "
        + "offreur = ?, receveur = ?, photo = ?, version = ? "
        + "WHERE id_objet = ? AND version = ? RETURNING id_objet, version;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setString(1, objetDTO.getEtatObjet());
      ps.setInt(2, objetDTO.getTypeObjet().getIdType());
      ps.setString(3, objetDTO.getDescription());
      ps.setInt(4, objetDTO.getOffreur().getIdUtilisateur());
      if (objetDTO.getReceveur() == null) {
        ps.setNull(5, Types.INTEGER);
      } else {
        ps.setInt(5, objetDTO.getReceveur().getIdUtilisateur());
      }
      ps.setString(6, objetDTO.getPhoto());
      ps.setInt(7, objetDTO.getVersion() + 1);
      ps.setInt(8, objetDTO.getIdObjet());
      ps.setInt(9, objetDTO.getVersion());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          objetDTO.setIdObjet(rs.getInt(1));
          objetDTO.setVersion(rs.getInt(2));
          return objetDTO;
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
  }

}

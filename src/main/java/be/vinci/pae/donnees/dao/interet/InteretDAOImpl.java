package be.vinci.pae.donnees.dao.interet;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.adresse.AdresseDTO;
import be.vinci.pae.business.interet.InteretDTO;
import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.donnees.services.ServiceBackendDAL;
import be.vinci.pae.donnees.services.ServiceDAL;
import be.vinci.pae.utilitaires.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InteretDAOImpl implements InteretDAO {

  @Inject
  private DomaineFactory factory;
  @Inject
  private ServiceBackendDAL serviceBackendDAL;


  /**
   * Ajoute un intérêt à l'objet.
   *
   * @param interetDTO : interet
   * @return interetDTO : interetDTO rempli
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public InteretDTO ajouterInteret(InteretDTO interetDTO) {
    String requetePs = "INSERT INTO projet.interets VALUES (?, ?, ?) RETURNING *;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      java.sql.Date dateRdvSQL = new java.sql.Date(interetDTO.getDateRdv().getTime());
      ps.setInt(1, interetDTO.getUtilisateur().getIdUtilisateur());
      ps.setInt(2, interetDTO.getObjet().getIdObjet());
      ps.setDate(3, dateRdvSQL);
      ps.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return interetDTO;
  }

  /**
   * nombre de personnes intéressées pour une offre.
   *
   * @param idObjet : l'id de l'objet dont les personnes sont intéressées
   * @return nbPers : le nombre de personnes intéressées
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public int nbPersonnesInteressees(int idObjet) {
    String requetePS = "SELECT COUNT(i.utilisateur) FROM projet.interets i WHERE i.objet = ?;";
    int nbPers = 0;
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePS)) {
      ps.setInt(1, idObjet);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          nbPers = rs.getInt(1);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
    return nbPers;
  }

  /**
   * Liste les interet de sa propre offre.
   *
   * @return liste : la liste des offres les plus récentes
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public List<InteretDTO> listeDesPersonnesInteressees(ObjetDTO objetDTO) {
    String requetePS = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune, "
        + "u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, i.date FROM projet.interets i, "
        + "projet.utilisateurs u, projet.adresses a WHERE "
        + "i.objet = ? AND i.utilisateur = u.id_utilisateur AND a.id_adresse = u.adresse;";
    InteretDTO interetDTO = factory.getInteret();
    interetDTO.setObjet(objetDTO);
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePS)) {
      ps.setInt(1, objetDTO.getIdObjet());
      List<InteretDTO> listeDesPersonnesInteressees =
          remplirListInteretDepuisResulSet(interetDTO, ps);

      return listeDesPersonnesInteressees;
    } catch (SQLException e) {
      e.printStackTrace();
      ((ServiceDAL) serviceBackendDAL).retourEnArriereTransaction();
      throw new FatalException(e.getMessage(), e);
    }
  }

  /**
   * Rempli une liste d'interet depuis un ResultSet.
   *
   * @param interetDTO : l'interet vide, qui va être remplie
   * @param ps         : le PreparedStatement déjà mis en place
   * @return liste : la liste remplie
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  private List<InteretDTO> remplirListInteretDepuisResulSet(InteretDTO interetDTO,
      PreparedStatement ps) {
    List<InteretDTO> liste = new ArrayList<>();
    ObjetDTO objetDTO = interetDTO.getObjet();
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        interetDTO = remplirInteretDepuisResulSet(interetDTO, rs);
        liste.add(interetDTO);
        interetDTO = factory.getInteret();
        interetDTO.setObjet(objetDTO);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e.getMessage(), e);
    }
    return liste;
  }

  /**
   * Rempli les données de l'interet depuis un ResultSet.
   *
   * @param interetDTO : l'interet vide, qui va être rempli
   * @param rs         : le Result Statement déjà préparé
   * @return interetDTO : l'interet rempli
   */
  private InteretDTO remplirInteretDepuisResulSet(InteretDTO interetDTO, ResultSet rs) {
    AdresseDTO adresseDTO = factory.getAdresse();
    UtilisateurDTO interesse = factory.getUtilisateur();
    try {
      adresseDTO.setIdAdresse(rs.getInt(1));
      adresseDTO.setRue(rs.getString(2));
      adresseDTO.setNumero(rs.getInt(3));
      adresseDTO.setBoite(rs.getString(4));
      adresseDTO.setCodePostal(rs.getInt(5));
      adresseDTO.setCommune(rs.getString(6));

      interesse.setIdUtilisateur(rs.getInt(7));
      interesse.setPseudo(rs.getString(8));
      interesse.setNom(rs.getString(9));
      interesse.setPrenom(rs.getString(10));
      interesse.setMdp(rs.getString(11));
      interesse.setGsm(rs.getString(12));
      interesse.setEstAdmin(rs.getBoolean(13));
      interesse.setEtatInscription(rs.getString(14));
      interesse.setCommentaire(rs.getString(15));
      interesse.setAdresse(adresseDTO);

      interetDTO.setUtilisateur(interesse);
      interetDTO.setDateRdv(rs.getDate(16));

    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e.getMessage(), e);
    }
    return interetDTO;
  }
}

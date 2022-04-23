package be.vinci.pae.donnees.dao.interet;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.adresse.AdresseDTO;
import be.vinci.pae.business.interet.InteretDTO;
import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.donnees.services.ServiceBackendDAL;
import be.vinci.pae.utilitaires.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.Date;
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
   * @param interetDTO : l'intérêt à ajouter
   * @return interetDTO : l'interetDTO rempli
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public InteretDTO ajouterInteret(InteretDTO interetDTO) {
    String requetePs = "INSERT INTO projet.interets VALUES (?, ?, ?, ?, ?, ?) RETURNING *;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      Date dateRdvSQL = new Date(interetDTO.getDateRdv().getTime());
      ps.setInt(1, interetDTO.getUtilisateur().getIdUtilisateur());
      ps.setInt(2, interetDTO.getObjet().getIdObjet());
      ps.setDate(3, dateRdvSQL);
      ps.setInt(4, interetDTO.getVersion());
      ps.setBoolean(5, interetDTO.isVue());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return interetDTO;
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
  }

  /**
   * supprimer les intérêts de l'objet.
   *
   * @param idObjet : l'id de l'objet pour lequel on va supprimé les interets
   * @return interetDTO : interetDTO rempli
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public int supprimerInteret(int idObjet) {
    String requetePs = "DELETE FROM interets WHERE objet = ? ";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setInt(1, idObjet);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return idObjet;
        } else {
          return 0;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e.getMessage(), e);
    }
  }

  /**
   * Récupère le nombre de personnes intéressées de l'objet avec l'id passé en paramètre.
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
      throw new FatalException(e.getMessage(), e);
    }
    return nbPers;
  }

  /**
   * Liste les interet de sa propre offre.
   *
   * @param idObjet : l'id de l'objet dont les personnes sont intéressées
   * @return liste : la liste de toutes les interets qu'on n'a pas encore vue
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public List<InteretDTO> listeDesPersonnesInteresseesVue(int idObjet) {
    String requetePS = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune, "
        + "u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, i.vue, i.date FROM projet.interets i, "
        + "projet.utilisateurs u, projet.adresses a WHERE "
        + "i.objet = ? AND i.utilisateur = u.id_utilisateur AND a.id_adresse = u.adresse "
        + "AND i.vue = 'false';";
    InteretDTO interetDTO = factory.getInteret();
    ObjetDTO objetDTO = factory.getObjet();
    objetDTO.setIdObjet(idObjet);
    List<InteretDTO> listeDesPersonnesInteressees;
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePS)) {
      ps.setInt(1, idObjet);
      interetDTO.setObjet(objetDTO);
      listeDesPersonnesInteressees =
          remplirListInteretDepuisResulSet(interetDTO, ps);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e.getMessage(), e);
    }
    return listeDesPersonnesInteressees;
  }

  /**
   * Liste les interet de sa propre offre.
   *
   * @param idObjet : l'id de l'objet dont les personnes sont intéressées
   * @return liste : la liste de toutes les interets
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public List<InteretDTO> listeDesPersonnesInteressees(int idObjet) {
    String requetePS = "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune, "
        + "u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
        + "u.etat_inscription, u.commentaire, i.vue, i.date FROM projet.interets i, "
        + "projet.utilisateurs u, projet.adresses a WHERE "
        + "i.objet = ? AND i.utilisateur = u.id_utilisateur AND a.id_adresse = u.adresse;";
    InteretDTO interetDTO = factory.getInteret();
    ObjetDTO objetDTO = factory.getObjet();
    objetDTO.setIdObjet(idObjet);
    List<InteretDTO> listeDesPersonnesInteressees;
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePS)) {
      ps.setInt(1, idObjet);
      interetDTO.setObjet(objetDTO);
      listeDesPersonnesInteressees =
          remplirListInteretDepuisResulSet(interetDTO, ps);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e.getMessage(), e);
    }
    return listeDesPersonnesInteressees;
  }

  /**
   * Met à jour l'interet.
   *
   * @param interetDTO : l'interet à mettre a jour
   * @return interetDTO : l'interet mit à jour
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public InteretDTO miseAJourInteret(InteretDTO interetDTO) {
    String requetePs = "UPDATE projet.interets SET date = ?, vue = ?, "
        + "version = ? WHERE version = ? AND utilisateur = ? AND objet = ? "
        + "RETURNING utilisateur, objet, version;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setDate(1, (Date) interetDTO.getDateRdv());
      ps.setBoolean(2, interetDTO.isVue());
      ps.setInt(3, interetDTO.getVersion() + 1);
      ps.setInt(4, interetDTO.getVersion());
      ps.setInt(5, interetDTO.getUtilisateur().getIdUtilisateur());
      ps.setInt(6, interetDTO.getObjet().getIdObjet());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          interetDTO.getUtilisateur().setIdUtilisateur(rs.getInt(1));
          interetDTO.getObjet().setIdObjet(rs.getInt(2));
          interetDTO.setVersion(rs.getInt(3));
          return interetDTO;
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
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
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        interetDTO = remplirInteretDepuisResulSet(interetDTO, rs);
        liste.add(interetDTO);
        interetDTO = factory.getInteret();
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
      interetDTO.setVue(rs.getBoolean(16));
      interetDTO.setDateRdv(rs.getDate(17));

    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException(e.getMessage(), e);
    }
    return interetDTO;
  }

}

package be.vinci.pae.donnees.dao.objet;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.donnees.services.ServiceBackendDAL;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjetDAOImpl implements ObjetDAO {

  @Inject
  private DomaineFactory factory;
  @Inject
  private ServiceBackendDAL serviceBackendDAL;


  /**
   * Créer un objet.
   *
   * @param objetDTO : l'objet à créer
   * @return objetDTO : l'objet créé
   * @throws SQLException : est lancé si il ne sait pas insérer l'objet dans la db
   */
  @Override
  public ObjetDTO creerObjet(ObjetDTO objetDTO) {
    PreparedStatement ps = serviceBackendDAL.getPs(
        "INSERT INTO projet.objets VALUES (DEFAULT, 'offert', ?, ?, ?, null, ?) RETURNING *;");
    try {
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
    }
    return objetDTO;
  }

  /*
   * Recherche un objet via un id dans la base de données.
   *
   * @param id : l'id de l'objet
   * @return objetDTO : l'objet, s'il trouve un objet qui possède ce id
   * @throws SQLException : est lancée s'il ne trouve pas l'objet
   *
  @Override
  public ObjetDTO rechercheParId(int id) {
    ObjetDTO objetDTO = factory.getObjet();
    PreparedStatement ps = serviceBackendDAL.getPs(
        "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune,"
            + "u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
            + "u.etat_inscription, u.commentaire, t.id_type, t.nom, o.id_objet, o.etat_objet,  "
            + "o.description, o.photo"
            + "FROM projet.objets o LEFT OUTER JOIN projet.utilisateurs u ON"
            + " o.offreur = u.id_utilisateurLEFT OUTER JOIN projet.adresses a ON"
            + "u.adresse = a.id_adresseLEFT OUTER JOIN projet.types_objets t ON"
            + "t.id_type = o.type_objet WHERE o.id_objet = ?;");
    try {
      ps.setInt(1, id);
      objetDTO = remplirObjetDepuisResultSet(objetDTO, ps);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return objetDTO;
  }

  /**
   * Rempli les données de l'objet depuis un ResultSet.
   *
   * @param objetDTO : l'objet vide
   * @param ps       : le PreparedStatement déjà mis en place
   * @return objetDTO : l'objet rempli
   * @throws SQLException : est lancé si il y a un problème"
   *
  private ObjetDTO remplirObjetDepuisResultSet(ObjetDTO objetDTO, PreparedStatement ps) {
    TypeObjetDTO typeObjetDTO;
    AdresseDTO adresseDTO;
    UtilisateurDTO offreur;
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        typeObjetDTO = factory.getTypeObjet();
        adresseDTO = factory.getAdresse();
        offreur = factory.getUtilisateur();

        adresseDTO.setIdAdresse(rs.getInt(1));
        adresseDTO.setRue(rs.getString(2));
        adresseDTO.setNumero(rs.getInt(3));
        adresseDTO.setBoite(rs.getInt(4));
        adresseDTO.setCodePostal(rs.getInt(5));
        adresseDTO.setCommune(rs.getString(6));

        offreur.setIdUtilisateur(rs.getInt(7));
        offreur.setPseudo(rs.getString(8));
        offreur.setNom(rs.getString(9));
        offreur.setPrenom(rs.getString(10));
        offreur.setMdp(rs.getString(11));
        offreur.setGsm(rs.getString(12));
        offreur.setEstAdmin(rs.getBoolean(13));
        offreur.setEtatInscription(rs.getString(14));
        offreur.setCommentaire(rs.getString(15));
        offreur.setAdresse(adresseDTO);

        typeObjetDTO.setIdType(rs.getInt(16));
        typeObjetDTO.setNom(rs.getString(17));

        objetDTO.setIdObjet(rs.getInt(18));
        objetDTO.setEtatObjet(rs.getString(19));
        objetDTO.setTypeObjet(typeObjetDTO);
        objetDTO.setDescription(rs.getString(20));
        objetDTO.setOffreur(offreur);
        objetDTO.setReceveur(null);
        objetDTO.setPhoto(rs.getString(21));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return objetDTO;
  }*/
}

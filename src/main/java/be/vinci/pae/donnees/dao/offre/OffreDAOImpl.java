package be.vinci.pae.donnees.dao.offre;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.adresse.AdresseDTO;
import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.business.typeobjet.TypeObjetDTO;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.donnees.services.ServiceBackendDAL;
import jakarta.inject.Inject;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class OffreDAOImpl implements OffreDAO {

  @Inject
  private DomaineFactory factory;
  @Inject
  private ServiceBackendDAL serviceBackendDAL;

  /**
   * Créer une offre.
   *
   * @param offreDTO : l'offre à créer
   * @return offre : l'offre créée
   */
  @Override
  public OffreDTO creerOffre(OffreDTO offreDTO) {
    PreparedStatement ps = serviceBackendDAL.getPs(
        "INSERT INTO projet.offres VALUES (DEFAULT, ?, ?, ?) RETURNING *;");
    try {
      java.util.Date date = new java.util.Date();
      java.sql.Date sqlDate = new java.sql.Date(date.getTime());
      ps.setInt(1, offreDTO.getObjetDTO().getIdObjet());
      ps.setDate(2, sqlDate);
      ps.setString(3, offreDTO.getPlageHoraire());
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          offreDTO.setIdOffre(rs.getInt(1));
        }

      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return offreDTO;
  }

  /**
   * Rempli une liste d'offre.
   *
   * @return List : liste d'offre
   * @throws SQLException : est lancé si il y a un problème"
   */
  public List<OffreDTO> listOffres() {
    OffreDTO offreDTO = factory.getOffre();
    PreparedStatement ps = serviceBackendDAL.getPs(
        "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune, "
            + "u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
            + "u.etat_inscription, u.commentaire, t.id_type, t.nom, o.id_objet, o.etat_objet,  "
            + "o.description, o.photo, of.id_offre, of.date_offre, of.plage_horaire "
            + "FROM projet.offres of LEFT OUTER JOIN projet.objets o ON o.id_objet = of.id_objet "
            + "LEFT OUTER JOIN projet.utilisateurs u ON o.offreur = u.id_utilisateur "
            + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
            + "LEFT OUTER JOIN projet.types_objets t ON t.id_type = o.type_objet "
            + "WHERE o.etat_objet = 'offert' OR o.etat_objet = 'interrese' "
            + "ORDER BY of.date_offre DESC ");
    List<OffreDTO> listOffres = null;
    try {
      listOffres = remplirListOffresDepuisResulSet(offreDTO, ps);
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return listOffres;
  }

  /**
   * Rempli une liste d'offre récente.
   *
   * @return List : liste d'offre récente
   * @throws SQLException : est lancé si il y a un problème"
   */
  public List<OffreDTO> listOffresRecent() {
    OffreDTO offreDTO = factory.getOffre();
    PreparedStatement ps = serviceBackendDAL.getPs(
        "SELECT a.id_adresse, a.rue, a.numero, a.boite, a.code_postal, a.commune, "
            + "u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin, "
            + "u.etat_inscription, u.commentaire, t.id_type, t.nom, o.id_objet, o.etat_objet,  "
            + "o.description, o.photo, of.id_offre, of.date_offre, of.plage_horaire "
            + "FROM projet.offres of LEFT OUTER JOIN projet.objets o ON o.id_objet = of.id_objet "
            + "LEFT OUTER JOIN projet.utilisateurs u ON o.offreur = u.id_utilisateur "
            + "LEFT OUTER JOIN projet.adresses a ON u.adresse = a.id_adresse "
            + "LEFT OUTER JOIN projet.types_objets t ON t.id_type = o.type_objet "
            + "WHERE o.etat_objet = 'offert' OR o.etat_objet = 'interrese' "
            + "ORDER BY of.date_offre DESC "
            + "LIMIT 2;");
    List<OffreDTO> listOffresRecent = null;
    try {

      listOffresRecent = remplirListOffresDepuisResulSet(offreDTO, ps);
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return listOffresRecent;
  }


  /**
   * Rempli une liste d'offre depuis un ResultSet.
   *
   * @param offreDTO : l'offre vide, qui va être rempli
   * @param ps       : le PreparedStatement déjà mis en place
   * @return List : liste remplie
   * @throws SQLException : est lancée si il y a un problème
   */
  private List<OffreDTO> remplirListOffresDepuisResulSet(OffreDTO offreDTO,
      PreparedStatement ps) throws SQLException {
    ObjetDTO objetDTO;
    AdresseDTO adresseDTO;
    UtilisateurDTO offreur;
    TypeObjetDTO typeObjetDTO;
    List<OffreDTO> listOffres = new ArrayList<>();
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        typeObjetDTO = factory.getTypeObjet();
        objetDTO = factory.getObjet();
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

        offreDTO.setIdOffre(rs.getInt(22));
        offreDTO.setObjetDTO(objetDTO);
        offreDTO.setDateOffre(convertirDateSQLEnLocalDateTime(rs.getDate(23)));
        offreDTO.setPlageHoraire(rs.getString(24));

        listOffres.add(offreDTO);

        offreDTO = factory.getOffre();
      }
    }
    return listOffres;
  }

  public LocalDateTime convertirDateSQLEnLocalDateTime(Date dateToConvert) {
    return Instant.ofEpochMilli(dateToConvert.getTime())
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
  }
}
// Ne pas oublier de fermer le preparedStatement quand on aura décidé où le faire
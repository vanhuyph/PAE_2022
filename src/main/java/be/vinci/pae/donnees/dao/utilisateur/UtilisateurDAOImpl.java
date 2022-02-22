package be.vinci.pae.donnees.dao.utilisateur;

import be.vinci.pae.business.domaine.DomaineFactory;
import be.vinci.pae.business.domaine.UtilisateurDTO;
import be.vinci.pae.donnees.services.ServicesDAL;
import jakarta.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilisateurDAOImpl implements UtilisateurDAO {

  @Inject
  private DomaineFactory factory;
  @Inject
  private ServicesDAL servicesDAL;

  @Override
  public UtilisateurDTO rechercheParPseudo(String pseudo) {
    UtilisateurDTO utilisateurDTO = factory.getUtilisateur();
    try {
      ResultSet rs = servicesDAL.getPs(
              "SELECT u.id_utilisateur, u.pseudo, u.nom, u.prenom, u.mdp, u.gsm, u.est_admin"
                  + "FROM projet.utilisateurs u WHERE u.pseudo="
                  + pseudo + ";")
          .executeQuery();
      utilisateurDTO.setIdUtilisateur(rs.getInt(1));
      utilisateurDTO.setPseudo(rs.getString(2));
      utilisateurDTO.setNom(rs.getString(3));
      utilisateurDTO.setPrenom(rs.getString(4));
      utilisateurDTO.setMdp(rs.getString(5));
      utilisateurDTO.setGsm(rs.getString(6));
      utilisateurDTO.setEstAdmin(rs.getBoolean(7));

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return utilisateurDTO;
  }

}




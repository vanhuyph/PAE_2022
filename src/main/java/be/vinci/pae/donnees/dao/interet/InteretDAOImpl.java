package be.vinci.pae.donnees.dao.interet;


import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.business.interet.InteretDTO;
import be.vinci.pae.donnees.services.ServiceDAL;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


public class InteretDAOImpl implements InteretDAO {

  @Inject
  private DomaineFactory factory;

  @Inject
  private ServiceDAL serviceDAL;


  /**
  *
  * @param idUtilisateurInteresse : l'id de l'utilisateur qui veut marquer son interet sur une offre
  * @param idObjet : l'id de l'objet dont l'utilisateur est interessé
  * @param dateRdv : la date de RDV pour venir chercher l'objet en format ?????
  * @return InteretDTO : interetDTO remplit
  */
  @Override
  public InteretDTO ajouterInteret(int idUtilisateurInteresse, int idObjet , Date dateRdv ) {
    InteretDTO interetDTO = factory.getInteret();
    PreparedStatement ps = serviceDAL.getPs("INSERT INTO projet.interets "
            + "VALUES (?, ?, ?) RETURNING *;");

    try {

      java.sql.Timestamp dateRdvSQL = new java.sql.Timestamp(dateRdv.getTime());

       ps.setInt(1, idUtilisateurInteresse);
       ps.setInt(2, idObjet);
       ps.setTimestamp(3, dateRdvSQL);

         return remplirInteretDepuisResultSet(interetDTO, ps);
       } catch(SQLException e){
         e.printStackTrace();
       }
       return interetDTO;
  }


  /**
  *
  * @param interet : l'interetDTO qu'on va remplir grace au preparedStatement
  * @param ps : le preparedStatement qu'on va executer
  * @return un interetDTO complété
  * @throws SQLException : est lancée si il y a un problème
  */
  private InteretDTO remplirInteretDepuisResultSet(InteretDTO interet, PreparedStatement ps) throws SQLException {
    try (ResultSet rs = ps.executeQuery()) {
      while(rs.next()) {
        interet.setIdUtilisateur(rs.getInt(1));
        interet.setIdObjet(rs.getInt(2));
        interet.setDateRdv(rs.getDate(3));
      }
    }
  return interet;
  }
}

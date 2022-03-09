package be.vinci.pae.donnees.dao.objet;

import be.vinci.pae.business.DomaineFactory;
import be.vinci.pae.donnees.services.ServiceDAL;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjetDAO {

  @Inject
  private DomaineFactory factory;

  @Inject
  private ServiceDAL serviceDAL;

  public List<ObjetDTO> listObjetOffert(){
    ObjetDTO objetDTO = factory.getObjet();
    PreparedStatement ps = serviceDAL.getPs(
        "SELECT o.id_objet, o.etat_objet, o.type_objet, o.description, o.offreur, o.receveur,"
            + "o.photo  FROM projet.objets o, projet.offres of WHERE o.receveur == 0 ORDER BY o.id_objet;");
    ArrayList<ObjetDTO> listObjetDTO = null;
    try{
        listObjetDTO = remplirObjetDepuisResulSet(objetDTO, ps);
        ps.close();
    } catch (SQLException e){
      e.printStackTrace();
    }

    return listObjetDTO;
  }

  private List<ObjetDTO> remplirObjetDepuisResulSet(ObjetDTO objetDTO,
      PreparedStatement ps) throws SQLException {
    ArrayList<ObjetDTO> listObjet = new ArrayList<ObjetDTO>();
    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        objetDTO.setIdObjet(rs.getInt(1));
        objetDTO.setEtatObjet(rs.getString(2));
        objetDTO.setTypeObjet(rs.getInt(3));
        objetDTO.setDescription(rs.getString(4));
        objetDTO.setOffreur(rs.getString(5));
        objetDTO.setReceveur(rs.getString(6));
        objetDTO.setPhoto(rs.getBoolean(7));
        listObjet.add(objetDTO);
      }
    }
    return listObjet;
  }

}

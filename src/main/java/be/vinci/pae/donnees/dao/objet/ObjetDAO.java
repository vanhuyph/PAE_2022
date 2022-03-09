package be.vinci.pae.donnees.dao.objet;

import be.vinci.pae.business.objet.ObjetDTO;
import java.util.List;

public interface ObjetDAO {


  ObjetDTO creerObjet(String etatObjet, String typeObjet, String description,
      Integer offreur, String photo);

  List<ObjetDTO> listObjetOffert();

}

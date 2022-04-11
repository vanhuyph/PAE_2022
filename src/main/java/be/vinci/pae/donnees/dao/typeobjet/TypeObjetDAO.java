package be.vinci.pae.donnees.dao.typeobjet;

import be.vinci.pae.business.typeobjet.TypeObjetDTO;
import java.util.List;

public interface TypeObjetDAO {

  List<TypeObjetDTO> listerTypeObjet();

  String creerTypeObjet(String nom);

}

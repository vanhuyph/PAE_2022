package be.vinci.pae.business.typeobjet;

import java.util.List;

public interface TypeObjetUCC {

  List<TypeObjetDTO> listerTypeObjet();

  TypeObjetDTO creerTypeObjet(TypeObjetDTO typeObjetDTO);

}

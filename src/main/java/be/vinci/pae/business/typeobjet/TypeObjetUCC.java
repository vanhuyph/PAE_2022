package be.vinci.pae.business.typeobjet;

import java.util.List;

public interface TypeObjetUCC {

  List<TypeObjetDTO> listerTypeObjet();

  String creerTypeObjet(String nom);

}

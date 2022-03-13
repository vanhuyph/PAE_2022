package be.vinci.pae.business.typeObjet;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = TypeObjetImpl.class)
public interface TypeObjetDTO {

  int getIdType();

  String getNom();

  void setNom(String nom);

}

package be.vinci.pae.business.typeobjet;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = TypeObjetImpl.class)
public interface TypeObjetDTO {

  int getIdType();

  void setIdType(int idType);

  String getNom();

  void setNom(String nom);
}

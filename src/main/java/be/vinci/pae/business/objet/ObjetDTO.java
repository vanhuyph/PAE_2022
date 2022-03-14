package be.vinci.pae.business.objet;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ObjetImpl.class)
public interface ObjetDTO {


  int getIdObjet();

  void setIdObjet(int idObjet);

  String getEtatObjet();

  void setEtatObjet(String etatObjet);

  int getTypeObjet();

  void setTypeObjet(int typeObjet);

  String getDescription();

  void setDescription(String decsription);

  int getOffreur();

  void setOffreur(int offreur);

  int getReceveur();

  void setReceveur(int receveur);

  String getPhoto();

  void setPhoto(String photo);
}

package be.vinci.pae.business.objet;


import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ObjetImpl.class)
public interface ObjetDTO {


  int getId_objet();

  void setId_objet(int id_objet);

  String getEtat_objet();

  void setEtat_objet(String etat_objet);

  String getType_objet();

  void setType_objet(String type_objet);

  String getDescription();

  void setDescription(String decsription);

  UtilisateurDTO getOffreur();

  void setOffreur(UtilisateurDTO offreur);

  UtilisateurDTO getReceveur();

  void setReceveur(UtilisateurDTO receveur);

  String getPhoto();

  void setPhoto(String photo);
}

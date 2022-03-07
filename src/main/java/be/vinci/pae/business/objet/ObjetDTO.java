package be.vinci.pae.business.objet;

import be.vinci.pae.business.utilisateur.Utilisateur;

public interface ObjetDTO {

  String getEtat_objet();

  void setEtat_objet(String etat_objet);

  String getType_objet();

  void setType_objet(String type_objet);

  String getDescription();

  void setDescription(String decsription);

  Utilisateur getOffreur();

  void setOffreur(Utilisateur offreur);

  Utilisateur getReceveur();

  void setReceveur(Utilisateur receveur);

  String getPhoto();

  void setPhoto(String photo);
}

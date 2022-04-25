package be.vinci.pae.business.objet;

import be.vinci.pae.business.typeobjet.TypeObjetDTO;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ObjetImpl.class)
public interface ObjetDTO {

  int getIdObjet();

  void setIdObjet(int idObjet);

  String getEtatObjet();

  void setEtatObjet(String etatObjet);

  TypeObjetDTO getTypeObjet();

  void setTypeObjet(TypeObjetDTO typeObjet);

  String getDescription();

  void setDescription(String decsription);

  UtilisateurDTO getOffreur();

  void setOffreur(UtilisateurDTO offreur);

  UtilisateurDTO getReceveur();

  void setReceveur(UtilisateurDTO receveur);

  String getPhoto();

  void setPhoto(String photo);

  int getVersion();

  void setVersion(int version);

  boolean isVue();

  void setVue(boolean vue);

}

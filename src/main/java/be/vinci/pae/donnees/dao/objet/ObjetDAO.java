package be.vinci.pae.donnees.dao.objet;

import be.vinci.pae.business.objet.ObjetDTO;

public interface ObjetDAO {


  ObjetDTO creerObjet(String etat_objet, String type_objet, String description,
      Integer offreur, String photo);
}

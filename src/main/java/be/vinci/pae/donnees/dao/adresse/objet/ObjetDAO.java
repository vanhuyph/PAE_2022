package be.vinci.pae.donnees.dao.adresse.objet;

import be.vinci.pae.business.objet.ObjetDTO;

public interface ObjetDAO {


  ObjetDTO creerObjet(String etatObjet, int typeObjet, String description,
      int offreur, String photo);

  ObjetDTO rechercheParId(int id);
}

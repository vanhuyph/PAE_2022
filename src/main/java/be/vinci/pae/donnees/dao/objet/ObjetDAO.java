package be.vinci.pae.donnees.dao.objet;

import be.vinci.pae.business.objet.ObjetDTO;

public interface ObjetDAO {

  ObjetDTO creerObjet(ObjetDTO objetDTO);

  ObjetDTO changeEtatObjet(ObjetDTO objetDTO);

  ObjetDTO modifierObjet(ObjetDTO objetAvecModification);
}

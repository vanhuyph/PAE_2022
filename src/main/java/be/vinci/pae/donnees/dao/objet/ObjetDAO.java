package be.vinci.pae.donnees.dao.objet;

import be.vinci.pae.business.objet.ObjetDTO;
import java.util.List;

public interface ObjetDAO {

  ObjetDTO creerObjet(ObjetDTO objetDTO);

  ObjetDTO rechercheParId(ObjetDTO objetDTO);

  List<ObjetDTO> rechercheObjetParReceveur(int idReceveur);

  ObjetDTO miseAJourObjet(ObjetDTO objetDTO);

  void remplirOffreurDepuisObjet(ObjetDTO objetDTO);

}

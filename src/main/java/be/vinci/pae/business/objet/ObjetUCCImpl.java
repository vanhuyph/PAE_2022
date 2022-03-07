package be.vinci.pae.business.objet;

import be.vinci.pae.donnees.dao.objet.ObjetDAO;
import jakarta.inject.Inject;

public class ObjetUCCImpl implements ObjetUCC {

  @Inject
  ObjetDAO objetDAO;


  /**
   * @param type_objet  : le type de l'objet
   * @param description : description de l'objet
   * @param offreur     : id de l'utilisateur offrant l'objet
   * @param photo       : chemin de la photo , peut être null
   * @return l'objet créé
   */
  @Override
  public ObjetDTO creerUnObjet(String type_objet, String description, int offreur, String photo) {

    return null;
  }
}

package be.vinci.pae.business.objet;

public interface ObjetUCC {

  ObjetDTO creerUnObjet(int idOffreur, int typeObjet, String description, int offreur,
      String photo);

  ObjetDTO rechercheObjetParId(int id);

}

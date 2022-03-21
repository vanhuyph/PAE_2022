package be.vinci.pae.business.offre;

import be.vinci.pae.business.objet.ObjetDTO;

public interface OffreUCC {

  OffreDTO creerUneOffre(int idObjet, String plageHoraire);
  
  ObjetDTO creerUnObjet(int idOffreur, int typeObjet, String description, int offreur,
      String photo);
}

package be.vinci.pae.business.offre;

import be.vinci.pae.business.objet.ObjetDTO;
import java.util.List;

public interface OffreUCC {

  ObjetDTO creerUnObjet(int idOffreur, int typeObjet, String description, int offreur,
      String photo);

  OffreDTO creerUneOffre(int idObjet, String plageHoraire);

  List<OffreDTO> listOffres();

  List<OffreDTO> listOffresRecent();

}

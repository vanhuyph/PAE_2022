package be.vinci.pae.business.offre;

import java.util.List;

public interface OffreUCC {

  OffreDTO creerUneOffre(int idObjet, String plageHoraire);

  List<OffreDTO> listOffres();

}

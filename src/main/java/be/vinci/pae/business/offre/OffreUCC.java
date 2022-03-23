package be.vinci.pae.business.offre;

import java.util.List;

public interface OffreUCC {


  OffreDTO creerUneOffre(OffreDTO offreDTO);

  List<OffreDTO> listOffres();

  List<OffreDTO> listOffresRecent();

}

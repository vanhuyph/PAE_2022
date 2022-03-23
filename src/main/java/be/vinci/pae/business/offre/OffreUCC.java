package be.vinci.pae.business.offre;

import java.util.List;

public interface OffreUCC {

  OffreDTO creerUneOffre(OffreDTO offreDTO);

  List<OffreDTO> listerOffres();

  List<OffreDTO> listerOffresRecentes();
}

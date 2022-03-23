package be.vinci.pae.donnees.dao.offre;

import be.vinci.pae.business.offre.OffreDTO;
import java.util.List;

public interface OffreDAO {

  OffreDTO creerOffre(OffreDTO offreDTO);

  List<OffreDTO> listOffres();

  List<OffreDTO> listOffresRecent();

}

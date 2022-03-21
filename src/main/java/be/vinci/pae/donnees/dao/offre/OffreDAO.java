package be.vinci.pae.donnees.dao.offre;

import be.vinci.pae.business.offre.OffreDTO;
import java.util.List;

public interface OffreDAO {

  OffreDTO creerOffre(Integer idObjet, String plageHoraire);

  List<OffreDTO> listOffres();

  List<OffreDTO> listOffresRecent();

}

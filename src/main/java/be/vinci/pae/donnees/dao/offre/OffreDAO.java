package be.vinci.pae.donnees.dao.offre;

import be.vinci.pae.business.offre.OffreDTO;
import java.util.List;

public interface OffreDAO {

  OffreDTO creerOffre(int idObjet, String plageHoraire);

  List<OffreDTO> listOffres();

  List<OffreDTO> listOffresRecent();

}

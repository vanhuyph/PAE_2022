package be.vinci.pae.donnees.dao.offre;

import be.vinci.pae.business.offre.OffreDTO;

public interface OffreDAO {

  OffreDTO creerOffre(Integer id_objet, String plage_horaire);

}

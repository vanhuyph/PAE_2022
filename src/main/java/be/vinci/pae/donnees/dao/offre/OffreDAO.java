package be.vinci.pae.donnees.dao.offre;

import be.vinci.pae.business.offre.OffreDTO;

public interface OffreDAO {

  OffreDTO creerOffre(int idObjet, String plageHoraire);

  OffreDTO annulerOffre(int idOffre);

  OffreDTO rechercheParId(int id);

}

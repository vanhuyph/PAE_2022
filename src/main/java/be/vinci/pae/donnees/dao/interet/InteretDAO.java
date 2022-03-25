package be.vinci.pae.donnees.dao.interet;

import be.vinci.pae.business.interet.InteretDTO;

public interface InteretDAO {

  InteretDTO ajouterInteret(InteretDTO interetDTO);

  int nbPersonnesInteressees(int idObjet);
}

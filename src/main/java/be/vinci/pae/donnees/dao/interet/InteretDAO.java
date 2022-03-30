package be.vinci.pae.donnees.dao.interet;

import be.vinci.pae.business.interet.InteretDTO;
import be.vinci.pae.business.objet.ObjetDTO;
import java.util.List;

public interface InteretDAO {

  InteretDTO ajouterInteret(InteretDTO interetDTO);

  int nbPersonnesInteressees(int idObjet);

  List<InteretDTO> listeDesPersonnesInteressees(ObjetDTO objetDTO);

}

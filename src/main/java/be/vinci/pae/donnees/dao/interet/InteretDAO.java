package be.vinci.pae.donnees.dao.interet;

import be.vinci.pae.business.interet.InteretDTO;
import java.util.List;

public interface InteretDAO {

  InteretDTO ajouterInteret(InteretDTO interetDTO);

  int nbPersonnesInteressees(int idObjet);

  List<InteretDTO> listeDesPersonnesInteressees(int idObjet);

  InteretDTO miseAJourInteret(InteretDTO interetDTO);

}

package be.vinci.pae.business.interet;

import be.vinci.pae.business.objet.ObjetDTO;
import java.util.List;

public interface InteretUCC {

  InteretDTO creerUnInteret(InteretDTO interetDTO);

  int nbPersonnesInteressees(int id);

  List<InteretDTO> listeDesPersonnesInteressees(ObjetDTO objetDTO);

}

package be.vinci.pae.business.interet;

import java.util.List;

public interface InteretUCC {

  InteretDTO creerUnInteret(InteretDTO interetDTO);

  int nbPersonnesInteressees(int id);

  List<InteretDTO> listeDesPersonnesInteressees(int idObjet);

  List<InteretDTO> listeDesPersonnesInteresseesVue(int idObjet);

}

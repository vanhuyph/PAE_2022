package be.vinci.pae.business.interet;

import java.util.List;

public interface InteretUCC {

  InteretDTO creerUnInteret(InteretDTO interetDTO);

  int nbPersonnesInteressees(int id);

  InteretDTO interetUtilisateurPourObjet(int idObjet, int idUtilisateur);

  List<InteretDTO> listeDesPersonnesInteressees(int idObjet);

  List<InteretDTO> listeDesPersonnesInteresseesVue(int idObjet);

  InteretDTO indiquerReceveur(InteretDTO interet);

  InteretDTO nonRemis(int idObjet);

  InteretDTO notifierReceveurEmpecher(int idUtilisateur);
}

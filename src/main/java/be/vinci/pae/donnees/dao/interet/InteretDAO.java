package be.vinci.pae.donnees.dao.interet;

import be.vinci.pae.business.interet.InteretDTO;
import java.util.List;

public interface InteretDAO {

  InteretDTO ajouterInteret(InteretDTO interetDTO);

  int nbPersonnesInteressees(int idObjet);

  InteretDTO interetUtilisateurPourObjet(int idObjet, int idUtilisateur);

  List<InteretDTO> listeDesPersonnesInteressees(int idObjet);

  List<InteretDTO> listeDesPersonnesInteresseesVue(int idOffreur);

  InteretDTO miseAJourInteret(InteretDTO interetDTO);

  InteretDTO receveurActuel(int idObjet);

  InteretDTO notifierReceveurEmpecher(int idUtilisateur);

}

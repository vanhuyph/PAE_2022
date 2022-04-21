package be.vinci.pae.business.offre;

import be.vinci.pae.business.objet.ObjetDTO;
import java.util.List;

public interface OffreUCC {

  OffreDTO creerOffre(OffreDTO offreDTO);

  List<OffreDTO> listerOffres();

  List<OffreDTO> listerOffresRecentes();

  OffreDTO annulerOffre(OffreDTO offreDTO);

  OffreDTO rechercheParId(int id);

  List<OffreDTO> offresPrecedentes(int idObjet);

  OffreDTO modifierOffre(OffreDTO offreModifiee);

  List<OffreDTO> rechercherOffre(String recherche);

  List<ObjetDTO> objetsAEvalueParUtilisateur(int idUtilisateur);
}

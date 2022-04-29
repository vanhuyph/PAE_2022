package be.vinci.pae.business.offre;

import be.vinci.pae.business.objet.ObjetDTO;
import java.time.LocalDate;
import java.util.List;

public interface OffreUCC {

  OffreDTO creerOffre(OffreDTO offreDTO);

  OffreDTO reoffrirObjet(OffreDTO offreDTO);

  List<OffreDTO> listerOffres();

  List<OffreDTO> listerOffresRecentes();

  OffreDTO annulerOffre(OffreDTO offreDTO);

  OffreDTO rechercheParId(int id);

  List<OffreDTO> offresPrecedentes(int idObjet);

  OffreDTO modifierOffre(OffreDTO offreModifiee);

  List<OffreDTO> rechercherOffre(String recherche, LocalDate dateDebut, LocalDate dateFin);

  OffreDTO donnerOffre(OffreDTO offreDTO);

  List<OffreDTO> mesOffres(int idUtilisateur);

  List<OffreDTO> voirOffreAttribuer(int idUtilisateur);

  List<ObjetDTO> objetsAEvaluerParUtilisateur(int idUtilisateur);

  List<OffreDTO> objetsOffertsUtilisateur(int idUtilisateur);

  List<OffreDTO> objetsRecusUtilisateur(int idUtilisateur);

}

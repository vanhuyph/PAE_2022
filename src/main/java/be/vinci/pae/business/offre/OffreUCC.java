package be.vinci.pae.business.offre;

import java.time.LocalDate;
import java.util.List;

public interface OffreUCC {

  OffreDTO creerOffre(OffreDTO offreDTO);

  List<OffreDTO> listerOffres();

  List<OffreDTO> listerOffresRecentes();

  OffreDTO annulerOffre(OffreDTO offreDTO);

  OffreDTO rechercheParId(int id);

  List<OffreDTO> offresPrecedentes(int idObjet);

  OffreDTO modifierOffre(OffreDTO offreModifiee);

  List<OffreDTO> rechercherOffre(String recherche, LocalDate dateDebut, LocalDate dateFin);

  OffreDTO indiquerMembreReceveur(OffreDTO offreDTO);

  OffreDTO donnerOffre(OffreDTO offreDTO);

  List<OffreDTO> mesOffres(int idUtilisateur);

  List<OffreDTO> voirOffreAttribuer(int idUtilisateur);

}

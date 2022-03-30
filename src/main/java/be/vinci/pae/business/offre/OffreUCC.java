package be.vinci.pae.business.offre;

import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import java.util.List;

public interface OffreUCC {

  OffreDTO creerOffre(OffreDTO offreDTO);

  List<OffreDTO> listerOffres();

  List<OffreDTO> listerOffresRecentes();

  OffreDTO annulerOffre(int idOffre);

  OffreDTO rechercheParId(int id);

  List<OffreDTO> offresPrecedentes(int idObjet);

  OffreDTO indiquerMembreReceveur(OffreDTO offreDTO, UtilisateurDTO utilisateurDTO);

}

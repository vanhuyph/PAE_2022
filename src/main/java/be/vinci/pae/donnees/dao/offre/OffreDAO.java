package be.vinci.pae.donnees.dao.offre;

import be.vinci.pae.business.offre.OffreDTO;
import java.util.List;

public interface OffreDAO {

  OffreDTO creerOffre(OffreDTO offreDTO);

  List<OffreDTO> listerOffres();

  List<OffreDTO> listerOffresRecentes();

  OffreDTO rechercheParId(int id);

  List<OffreDTO> offresPrecedentes(int idObjet);

  OffreDTO modifierOffre(OffreDTO offreAvecModification);

  List<OffreDTO> rechercherOffres(String recherche);

}

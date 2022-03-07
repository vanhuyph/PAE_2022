package be.vinci.pae.business.offre;

import be.vinci.pae.business.objet.ObjetDTO;
import java.util.Date;

public interface OffreDTO {

  int getId_offre();

  void setId_offre(int id_offre);

  Date getDate_offre();

  void setDate_offre(Date date_offre);

  ObjetDTO getObjet();

  void setObjet(ObjetDTO objet);

  String getPlage_horaire();

  void setPlage_horaire(String plage_horaire);
}

package be.vinci.pae.business.offre;

import be.vinci.pae.business.objet.ObjetDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;

@JsonDeserialize(as = OffreImpl.class)
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

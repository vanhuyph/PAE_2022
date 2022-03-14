package be.vinci.pae.business.offre;

import be.vinci.pae.business.objet.ObjetDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;

@JsonDeserialize(as = OffreImpl.class)
public interface OffreDTO {

  int getIdOffre();

  void setIdOffre(int idOffre);

  Date getDateOffre();

  void setDateOffre(Date dateOffre);

  ObjetDTO getObjet();

  void setObjet(ObjetDTO objet);

  String getPlageHoraire();

  void setPlageHoraire(String plageHoraire);
}

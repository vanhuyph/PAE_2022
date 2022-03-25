package be.vinci.pae.business.offre;

import be.vinci.pae.business.objet.ObjetDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDateTime;

@JsonDeserialize(as = OffreImpl.class)
public interface OffreDTO {

  int getIdOffre();

  void setIdOffre(int idOffre);

  LocalDateTime getDateOffre();

  void setDateOffre(LocalDateTime dateOffre);

  ObjetDTO getObjetDTO();

  void setObjetDTO(ObjetDTO objetDTO);

  String getPlageHoraire();

  void setPlageHoraire(String plageHoraire);

}

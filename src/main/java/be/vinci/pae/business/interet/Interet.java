package be.vinci.pae.business.interet;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = InteretImpl.class)
public interface Interet extends InteretDTO {

  void marquerInteretObjet();

  void indiquerReceveur();

  void pasVenuChercher();

  void venuChercher();

  void estVu();

  void estVuEmpecher();

}

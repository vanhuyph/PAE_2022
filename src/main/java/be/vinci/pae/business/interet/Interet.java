package be.vinci.pae.business.interet;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = InteretImpl.class)
public interface Interet extends InteretDTO {

  // Changer l'état de l'objet.
  void marquerInteretObjet();

}

package be.vinci.pae.business.offre;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = OffreImpl.class)
public interface Offre extends OffreDTO {

  void offrirObjet();

  void annulerOffre();

  void reoffriObjet();

}

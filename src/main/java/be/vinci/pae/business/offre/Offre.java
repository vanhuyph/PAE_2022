package be.vinci.pae.business.offre;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = OffreImpl.class)
public interface Offre extends OffreDTO {

  void offrirObjet();

  void interesseObjet();

  void annulerOffre();

  void objetNonVu();

  void donnerObjet();

  void empecherOffre();

}

package be.vinci.pae.business.objet;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ObjetImpl.class)
public interface Objet extends ObjetDTO {

  boolean verifierEtatPourModificationOffre();

  void confirmerObjet();
}

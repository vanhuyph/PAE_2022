package be.vinci.pae.business.objet;

import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ObjetImpl.class)
public interface Objet extends ObjetDTO {

  boolean verifierEtatPourModificationOffre();

  void confirmerObjet();

  boolean verifierEtatPourReoffrirObjet();

  void indiquerReceveur(UtilisateurDTO utilisateur);

  boolean peutEtreEvalue();

  void estEvalue();

  void estVu();

}

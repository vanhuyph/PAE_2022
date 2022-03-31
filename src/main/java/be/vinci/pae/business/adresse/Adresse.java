package be.vinci.pae.business.adresse;

import be.vinci.pae.business.utilisateur.UtilisateurImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UtilisateurImpl.class)
public interface Adresse extends AdresseDTO {

  void premiereVersion();

}

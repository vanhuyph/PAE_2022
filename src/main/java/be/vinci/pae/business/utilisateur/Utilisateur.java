package be.vinci.pae.business.utilisateur;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UtilisateurImpl.class)
public interface Utilisateur extends UtilisateurDTO {

  boolean verifierMdp(String mdp);

  String hashMdp(String mdp);

  boolean mettreEnAttente();

  boolean confirmerInscription(boolean estAdmin);

  boolean refuserInscription(String commentaire);

  void incrementerNbObjetOffert();

  void incrementerNbObjetDonne();

  void incrementerNbObjetRecu();

  void incrementerNbObjetAbandonne();

}

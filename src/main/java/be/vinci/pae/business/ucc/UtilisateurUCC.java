package be.vinci.pae.business.ucc;

import be.vinci.pae.business.domaine.UtilisateurDTO;

public interface UtilisateurUCC {
  
  UtilisateurDTO connexion(String pseudo, String mdp);

  UtilisateurDTO rechercheParId(int id);
}

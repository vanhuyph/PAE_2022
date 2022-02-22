package be.vinci.pae.business.ucc;

import be.vinci.pae.business.domaine.Utilisateur;
import be.vinci.pae.business.domaine.UtilisateurDTO;
import be.vinci.pae.donnees.dao.utilisateur.UtilisateurDAO;
import jakarta.inject.Inject;

public class UtilisateurUCC {

  @Inject
  UtilisateurDAO utilisateurDAO;

  public UtilisateurDTO connexion(String pseudo, String mdp) {

    Utilisateur utilisateur = (Utilisateur) utilisateurDAO.rechercheParPseudo(pseudo);

    if (utilisateur.verifierMdp(mdp)) {
      return utilisateur;
    } else {
      throw new IllegalStateException("Non autorisé");
    }
  }
  
}

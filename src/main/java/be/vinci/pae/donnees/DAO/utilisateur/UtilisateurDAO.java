package be.vinci.pae.donnees.DAO.utilisateur;

import be.vinci.pae.business.domaine.UtilisateurDTO;

public interface UtilisateurDAO {

  UtilisateurDTO rechercheParPseudo(String pseudo);
}

package be.vinci.pae.donnees.dao.utilisateur;

import be.vinci.pae.business.utilisateur.UtilisateurDTO;

public interface UtilisateurDAO {

  UtilisateurDTO rechercheParPseudo(String pseudo);

  UtilisateurDTO rechercheParId(int id);
}

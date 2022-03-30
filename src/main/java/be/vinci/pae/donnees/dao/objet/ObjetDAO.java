package be.vinci.pae.donnees.dao.objet;

import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;

public interface ObjetDAO {

  ObjetDTO creerObjet(ObjetDTO objetDTO);

  ObjetDTO changeEtatObjet(ObjetDTO objetDTO);

  ObjetDTO indiquerMembreReceveur(UtilisateurDTO utilisateurDTO, ObjetDTO objetDTO);

}

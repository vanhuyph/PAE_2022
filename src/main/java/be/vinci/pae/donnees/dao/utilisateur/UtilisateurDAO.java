package be.vinci.pae.donnees.dao.utilisateur;

import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import java.util.List;

public interface UtilisateurDAO {

  UtilisateurDTO rechercheParPseudo(String pseudo);

  UtilisateurDTO rechercheParId(int id);

  UtilisateurDTO ajouterUtilisateur(UtilisateurDTO utilisateur);

  UtilisateurDTO miseAJourUtilisateur(UtilisateurDTO utilisateurDTO);

  UtilisateurDTO modifierGsm(UtilisateurDTO utilisateurDTO);

  UtilisateurDTO modifierMdp(UtilisateurDTO utilisateurDTO);

  List<UtilisateurDTO> listerUtilisateursEtatsInscriptions(String etatInscription);

  List<UtilisateurDTO> rechercherMembres(String recherche);

  int nbreObjets(int idUtilisateur, String etatObjet);

  UtilisateurDTO incrementerObjetOffert(UtilisateurDTO utilisateurDTO);

}

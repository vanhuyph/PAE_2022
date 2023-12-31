package be.vinci.pae.business;

import be.vinci.pae.business.adresse.AdresseDTO;
import be.vinci.pae.business.evaluation.EvaluationDTO;
import be.vinci.pae.business.interet.InteretDTO;
import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.business.typeobjet.TypeObjetDTO;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;

public interface DomaineFactory {

  UtilisateurDTO getUtilisateur();

  OffreDTO getOffre();

  ObjetDTO getObjet();

  AdresseDTO getAdresse();

  TypeObjetDTO getTypeObjet();

  InteretDTO getInteret();

  EvaluationDTO getEvaluation();

}

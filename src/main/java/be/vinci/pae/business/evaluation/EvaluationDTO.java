package be.vinci.pae.business.evaluation;

import be.vinci.pae.business.objet.ObjetDTO;

public interface EvaluationDTO {

  int getIdEvaluation();

  void setIdEvaluation(int idEvaluation);

  ObjetDTO getObjet();

  void setObjet(ObjetDTO objet);

  String getCommentaire();

  void setCommmentaire(String commmentaire);
}

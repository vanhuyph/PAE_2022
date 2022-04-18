package be.vinci.pae.business.evaluation;

import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.vue.Vues;
import com.fasterxml.jackson.annotation.JsonView;

public class EvaluationImpl implements Evaluation {

  @JsonView(Vues.Public.class)
  private int idEvaluation;
  @JsonView(Vues.Public.class)
  private ObjetDTO objet;
  @JsonView(Vues.Public.class)
  private String commentaire;

  @Override
  public int getIdEvaluation() {
    return this.idEvaluation;
  }

  @Override
  public void setIdEvaluation(int idEvaluation) {
    this.idEvaluation = idEvaluation;
  }

  @Override
  public ObjetDTO getObjet() {
    return this.objet;
  }

  @Override
  public void setObjet(ObjetDTO objet) {
    this.objet = objet;
  }

  @Override
  public String getCommentaire() {
    return this.commentaire;
  }

  @Override
  public void setCommmentaire(String commmentaire) {
    this.commentaire = commmentaire;
  }

  @Override
  public boolean peutEtreEvaluer() {
    if (this.objet.getEtatObjet().equals("donné")) {
      return true;
    }
    return false;
  }
}

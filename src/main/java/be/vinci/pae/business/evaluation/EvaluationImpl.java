package be.vinci.pae.business.evaluation;

import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.vue.Vues;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EvaluationImpl implements Evaluation {

  @JsonView(Vues.Public.class)
  private int idEvaluation;
  @JsonView(Vues.Public.class)
  private int note;
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
  public int getNote() {
    return this.note;
  }

  @Override
  public void setNote(int note) {
    this.note = note;
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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EvaluationImpl that = (EvaluationImpl) o;
    return idEvaluation == that.idEvaluation && Objects.equals(objet, that.objet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idEvaluation, objet);
  }

  @Override
  public String toString() {
    return "EvaluationImpl{"
        + "idEvaluation=" + idEvaluation
        + ", note=" + note
        + ", objet=" + objet
        + ", commentaire='" + commentaire + '\''
        + '}';
  }

}

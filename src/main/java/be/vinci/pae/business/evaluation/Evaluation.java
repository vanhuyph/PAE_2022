package be.vinci.pae.business.evaluation;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = EvaluationImpl.class)
public interface Evaluation extends EvaluationDTO {

}

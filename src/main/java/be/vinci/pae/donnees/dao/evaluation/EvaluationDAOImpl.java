package be.vinci.pae.donnees.dao.evaluation;

import be.vinci.pae.business.evaluation.EvaluationDTO;
import be.vinci.pae.donnees.services.ServiceBackendDAL;
import be.vinci.pae.utilitaires.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EvaluationDAOImpl implements EvaluationDAO {

  @Inject
  private ServiceBackendDAL serviceBackendDAL;

  /**
   * Creer une evaluation.
   *
   * @param evaluationDTO : l'évaluation a créé
   * @return l'évaluation créée
   */
  @Override
  public EvaluationDTO creerEvaluation(EvaluationDTO evaluationDTO) {
    //ajouter Notes
    String requetePs = "INSERT INTO projet.evaluations VALUES (DEFAULT, ?, ?) RETURNING *;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setInt(1, evaluationDTO.getObjet().getIdObjet());
      ps.setString(2, evaluationDTO.getCommentaire());

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return evaluationDTO;
        } else {
          return null;
        }
      }
    } catch (SQLException e) {
      throw new FatalException(e.getMessage(), e);
    }
  }
}

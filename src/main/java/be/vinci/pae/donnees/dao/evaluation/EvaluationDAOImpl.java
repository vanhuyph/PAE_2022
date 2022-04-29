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
   * Ajoute une évaluation dans la base de données.
   *
   * @param evaluationDTO : l'évaluation à créé
   * @return evaluationDTO : l'évaluation créée
   * @throws FatalException : est lancée s'il y a eu un problème côté serveur
   */
  @Override
  public EvaluationDTO creerEvaluation(EvaluationDTO evaluationDTO) {
    String requetePs = "INSERT INTO projet.evaluations VALUES (DEFAULT, ?,?, ?) RETURNING *;";
    try (PreparedStatement ps = serviceBackendDAL.getPs(requetePs)) {
      ps.setInt(1, evaluationDTO.getObjet().getIdObjet());
      ps.setInt(2, evaluationDTO.getNote());
      ps.setString(3, evaluationDTO.getCommentaire());
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

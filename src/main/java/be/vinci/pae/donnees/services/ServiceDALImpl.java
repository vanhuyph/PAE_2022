package be.vinci.pae.donnees.services;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ServiceDALImpl implements ServiceDAL {

  private String url = null;
  private Connection conn = null;

  /**
   * Le constructeur va nous servir à la connexion à la DB.
   *
   * @throws SQLException : est lancée si la connexion n'a pas pu aboutir
   */
  public ServiceDALImpl() {
    url = "jdbc:postgresql://coursinfo.vinci.be:5432/dbabdenour_didi";
    try {
      conn = DriverManager.getConnection(url, "abdenour_didi", "batbat123");
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Impossible de joindre le serveur !");
      System.exit(1);
    }

  }

  /**
   * Précompile l'instruction SQL.
   *
   * @param query : instruction SQL sous format String
   * @return : une instruction SQL precompile
   * @throws SQLException : est lancée si l'instruction SQL n'a pas su se precompile
   */
  @Override
  public PreparedStatement getPs(String query) {
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = conn.prepareStatement(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return preparedStatement;
  }

}

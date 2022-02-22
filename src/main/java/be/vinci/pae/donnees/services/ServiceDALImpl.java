package be.vinci.pae.donnees.services;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ServiceDALImpl implements ServiceDAL {

  private String url = null;
  private Connection conn = null;

  public ServiceDALImpl() {
    url = "jdbc:postgresql://coursinfo.vinci.be:5432/dbabdenour_didi";
    try {
      conn = DriverManager.getConnection(url, "abdenour_didi", "batbat123");
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("impossible de joindre le server!");
      System.exit(1);
    }

  }

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

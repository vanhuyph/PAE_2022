package be.vinci.pae.donnees.DAO.services;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ServicesDAL {
    private String url = null;
    private Connection conn = null;
    private PreparedStatement ps;
    public ServicesDAL(){
        url = "jdbc:postgresql://coursinfo.vinci.be:5432/dbabdenour_didi";
        try{
            conn = DriverManager.getConnection(url, "abdenour_didi", "batbat123");
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("impossible de joindre le server!");
            System.exit(1);
        }

    }

    public PreparedStatement getPs(String query){
        try {
            return conn.prepareStatement(query);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

}

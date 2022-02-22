package be.vinci.pae.donnees.services;

import java.sql.PreparedStatement;

public interface ServicesDAL {

  PreparedStatement getPs(String query);
}

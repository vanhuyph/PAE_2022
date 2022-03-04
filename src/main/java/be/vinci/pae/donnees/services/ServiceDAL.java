package be.vinci.pae.donnees.services;

import java.sql.PreparedStatement;

public interface ServiceDAL {

  PreparedStatement getPs(String query);
}

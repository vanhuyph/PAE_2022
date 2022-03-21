package be.vinci.pae.donnees.services;

import java.sql.PreparedStatement;

public interface ServiceBackendDAL {

  PreparedStatement getPs(String requete);
}

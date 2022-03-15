package be.vinci.pae.donnees.services;

import be.vinci.pae.utilitaires.Config;
import be.vinci.pae.utilitaires.exceptions.FatalException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ServiceDALImpl implements ServiceDAL, ServiceBackendDAL {

  private ThreadLocal<Connection> threadConnexion = new ThreadLocal<>();
  private String url = null;
  private Connection conn = null;

  /**
   * Le constructeur va nous servir à la connexion à la DB.
   *
   * @throws SQLException : est lancée si la connexion n'a pas pu aboutir
   */
  public ServiceDALImpl() {
    url = Config.getPropriete("PostgresCheminDB");
    try {
      conn = DriverManager.getConnection(url, Config.getPropriete("PostgresUtilisateur"),
          Config.getPropriete("PostgresMdp"));
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Impossible de joindre le serveur !");
      System.exit(1);
    }
  }

  /**
   * Précompile l'instruction SQL.
   *
   * @param requete : instruction SQL sous format String
   * @return : une instruction SQL precompile
   * @throws FatalException : est lancée si l'instruction SQL n'a pas su se precompile
   */
  @Override
  public PreparedStatement getPs(String requete) {
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = conn.prepareStatement(requete);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new FatalException("Erreur de ps", e);
    }
    return preparedStatement;
  }

  /**
   * Commmence une transaction SQL.
   *
   * @throws FatalException : est lancée s'il y a déjà une transaction active ou un problème lors de
   *                        la transaction
   */
  @Override
  public void commencerTransaction() {
    if (threadConnexion.get() != null) {
      throw new FatalException("Il y a déjà une transaction active");
    }
    try {
      conn.setAutoCommit(false);
      threadConnexion.set(conn);
    } catch (SQLException e) {
      throw new FatalException("Erreur de transaction", e);
    }
  }

  /**
   * Commet une transaction SQL.
   *
   * @throws FatalException : est lancée s'il y a déjà une transaction active ou un problème lors de
   *                        la transaction
   */
  @Override
  public void commettreTransaction() {
    Connection c = threadConnexion.get();
    if (c == null) {
      throw new FatalException("Pas de connexion: la transaction n'a pas été commencé");
    }
    try {
      c.commit();
      conn.setAutoCommit(true);
      threadConnexion.remove();
      c.close();
    } catch (SQLException e) {
      throw new FatalException("Erreur de commit", e);
    }
  }

  /**
   * Retour en arriève  d'une transaction SQL.
   *
   * @throws FatalException : est lancée s'il y a déjà une transaction active ou un problème lors de
   *                        la transaction
   */
  @Override
  public void retourEnArriereTransaction() {
    Connection c = threadConnexion.get();
    if (c == null) {
      throw new FatalException("Pas de connexion: la transaction n'a pas été commencé");
    }
    try {
      c.rollback();
      conn.setAutoCommit(true);
      threadConnexion.remove();
      c.close();
    } catch (SQLException e) {
      throw new FatalException("Erreur de retour en arriere", e);
    }
  }
}

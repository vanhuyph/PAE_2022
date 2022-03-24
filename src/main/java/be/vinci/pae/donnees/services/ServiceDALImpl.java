package be.vinci.pae.donnees.services;

import be.vinci.pae.utilitaires.Config;
import be.vinci.pae.utilitaires.exceptions.FatalException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

public class ServiceDALImpl implements ServiceDAL, ServiceBackendDAL {

  private ThreadLocal<Connection> threadConnexion = new ThreadLocal<>();
  private BasicDataSource bds;

  /**
   * Le constructeur va nous servir à la connexion à la DB.
   */
  public ServiceDALImpl() {
    bds = new BasicDataSource();
    bds.setUrl(Config.getPropriete("PostgresCheminDB"));
    bds.setDriverClassName(Config.getPropriete("PostgresDriver"));
    bds.setUsername(Config.getPropriete("PostgresUtilisateur"));
    bds.setPassword(Config.getPropriete("PostgresMdp"));
    bds.setMaxTotal(5);
  }

  /**
   * Précompile l'instruction SQL.
   *
   * @param requete : instruction SQL sous format String
   * @return : une instruction SQL précompilée
   * @throws FatalException : est lancée si l'instruction SQL n'a pas su se précompilé
   */
  @Override
  public PreparedStatement getPs(String requete) {
    if (threadConnexion.get() == null) {
      throw new FatalException("Erreur connexion introuvable");
    }
    PreparedStatement ps;
    try {
      ps = threadConnexion.get().prepareStatement(requete);
      return ps;
    } catch (SQLException e) {
      throw new FatalException("Erreur lors de la création du ps", e);
    }
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
      bds.setDefaultAutoCommit(false);
      Connection c = bds.getConnection();
      threadConnexion.set(c);
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
    try (c) {
      if (c == null) {
        throw new FatalException("Pas de connexion : la transaction n'a pas été commencée");
      }
      c.commit();
      bds.setDefaultAutoCommit(true);
      threadConnexion.remove();
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
    try (c) {
      if (c == null) {
        throw new FatalException("Pas de connexion : la transaction n'a pas été commencée");
      }
      c.rollback();
      bds.setDefaultAutoCommit(true);
      threadConnexion.remove();
    } catch (SQLException e) {
      throw new FatalException("Erreur de retour en arrière", e);
    }
  }

}

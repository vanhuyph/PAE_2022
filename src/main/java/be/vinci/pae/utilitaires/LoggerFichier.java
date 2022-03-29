package be.vinci.pae.utilitaires;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerFichier {

  protected static final Logger logger = Logger.getLogger("MonLogger");

  static {
    FileHandler fh;
    try {
      fh = new FileHandler("logger.log", 1000000, 5, true);
      logger.addHandler(fh);
      fh.setFormatter(new SimpleFormatter());
      logger.setLevel(Level.ALL);
    } catch (IOException | SecurityException ex1) {
      logger.log(Level.ALL, null, ex1);
    }
  }

  /**
   * Méthode permettant de logger la stacktrace de toutes les exceptions et de préciser le niveau du
   * message dans un fichier logger.log.
   *
   * @param niveau    : le niveau définit du message
   * @param exception : l'exception à logger
   */
  public static void log(Level niveau, Exception exception) {
    logger.log(niveau, exception, exception::getMessage);
  }

  /**
   * Surcharge de la méthode log permettant de logger un message définit et de préciser le niveau du
   * message dans un fichier logger.log.
   *
   * @param niveau  : le niveau définit du message
   * @param message : le message à logger
   */
  public static void log(Level niveau, String message) {
    logger.log(niveau, message);
  }

}

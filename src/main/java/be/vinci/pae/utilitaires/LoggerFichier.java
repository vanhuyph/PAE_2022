package be.vinci.pae.utilitaires;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerFichier {

  protected static final Logger logger = Logger.getLogger("MonLogger");

  /**
   * Méthode permettant de logger la stacktrace de toutes les exceptions dans un fichier
   * logger.log.
   *
   * @param exception : exception à logger
   */
  public static void log(Exception exception) {
    FileHandler fh = null;
    try {
      fh = new FileHandler("logger.log", 1000000, 5, true);
      logger.addHandler(fh);
      SimpleFormatter formatter = new SimpleFormatter();
      fh.setFormatter(formatter);
      logger.setLevel(Level.ALL);
      logger.log(Level.ALL, exception, exception::getMessage);
    } catch (SecurityException | IOException e) {
      logger.log(Level.SEVERE, null, e);
    } finally {
      if (fh != null) {
        fh.close();
      }
    }
  }

}

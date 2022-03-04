package be.vinci.pae.utilitaires;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

  private static Properties props;

  /**
   * Charge les propriétés contenu dans fichier.
   *
   * @param fichier : le fichier .properties
   */
  public static void charger(String fichier) {
    props = new Properties();
    try (InputStream input = new FileInputStream(fichier)) {
      props.load(input);
    } catch (IOException e) {
      throw new WebApplicationException(
          Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage())
              .type("text/plain")
              .build());
    }
  }

  public static String getPropriete(String cle) {
    return props.getProperty(cle);
  }

  public static Integer getIntPropriete(String cle) {
    return Integer.parseInt(props.getProperty(cle));
  }

  public static boolean getBoolPropriete(String cle) {
    return Boolean.parseBoolean(props.getProperty(cle));
  }

}
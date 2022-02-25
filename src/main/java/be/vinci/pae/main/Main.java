package be.vinci.pae.main;

import be.vinci.pae.utilitaires.Config;
import java.io.IOException;
import java.net.URI;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class Main {

  /**
   * Démarre le serveur Grizzly.
   *
   * @return Grizzly HTTP server
   */
  public static HttpServer startServer() {
    final ResourceConfig rc = new ResourceConfig().packages("be.vinci.pae")
        .register(JacksonFeature.class);
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(Config.getPropriete("BaseURI")),
        rc);
  }

  /**
   * Méthode main.
   *
   * @throws IOException : est lancée si le serveur n'a pas pu se lancer
   */
  public static void main(String[] args) throws IOException {
    Config.charger("dev.properties");
    final HttpServer server = startServer();
    System.out.println(
        String.format("Jersey app disponible sur " + Config.getPropriete("BaseURI")));
    System.in.read();
    server.stop();
  }
}



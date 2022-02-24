package be.vinci.pae.main;


import be.vinci.pae.utils.Config;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class Main {
  static{
    Config.load("dev.properties");
  }
  public static final String BASE_URI = Config.getProperty("BaseURI");

  /**
   *  demarre le server Grizzly
   * @return Grizzly HTTP server.
   */
  public static HttpServer startServer() {

    final ResourceConfig rc = new ResourceConfig().packages("be.vinci.pae").register(JacksonFeature.class);



    return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
  }

  /**
   * Main method.
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    final HttpServer server = startServer();
    System.out.println(String.format("Jersey app started with endpoints available at "
            + "%s%nHit Ctrl-C to stop it...", BASE_URI));
    System.in.read();
    server.stop();
  }
}



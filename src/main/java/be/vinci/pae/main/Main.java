package be.vinci.pae.main;


import be.vinci.pae.utils.Config;
import java.io.IOException;
import java.net.URI;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;


/**
 * Main class.
 */
public class Main {

  public static final String BASE_URI = Config.getProperty("BaseURI");

  static {
    Config.load("dev.properties");
  }

  /**
   * demarre le server Grizzly
   *
   * @return Grizzly HTTP server.
   */
  public static HttpServer startServer() {

    final ResourceConfig rc = new ResourceConfig().packages("be.vinci.pae")
        .register(JacksonFeature.class);
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
  }

  /**
   * Main method.
   *
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {

    final HttpServer server = startServer();
    System.out.println(String.format("Jersey app disponible sur ", BASE_URI));
    System.in.read();
    server.stop();


  }
}



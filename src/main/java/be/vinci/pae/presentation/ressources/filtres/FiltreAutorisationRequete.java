package be.vinci.pae.presentation.ressources.filtres;

import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Singleton
@Provider
@Autorisation
public class FiltreAutorisationRequete implements ContainerRequestFilter {

  //a refactor en fonction de UtilisateurUCC

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final JWTVerifier jwtVerifier = JWT.require(this.jwtAlgorithm).withIssuer("auth0")
      .build();
  private Object utilisateurUCC;

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String token = requestContext.getHeaderString("Authorization");
    if (token == null) {
      requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
          .entity("un token est nécessaire pour accéder à cette ressource").build());
    } else {
      DecodedJWT tokenDecode = null;
      try {
        tokenDecode = this.jwtVerifier.verify(token);
      } catch (Exception e) {
        throw new WebApplicationException(Response.status(Status.UNAUTHORIZED)
            .entity("token malformé: " + e.getMessage()).type("text/plain").build());
      }

      Object authenticatedUser = tokenDecode;
      //utilisateurUCC.getUtilisateur(tokenDecode.getClaim("user").asInt());
      if (authenticatedUser == null) {
        requestContext.abortWith(Response.status(Status.FORBIDDEN)
            .entity("Vous ne pouvez pas accéder a cette ressource").build());
      }

      requestContext.setProperty("user",
          utilisateurUCC);//.getUtilisateur(tokenDecode.getClaim("user").asInt()));
    }
  }

}

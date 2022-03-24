package be.vinci.pae.presentation.ressources.filtres;

import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.business.utilisateur.UtilisateurUCC;
import be.vinci.pae.utilitaires.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;

@Singleton
@Provider
@Autorisation
public class FiltreAutorisationRequete implements ContainerRequestFilter {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getPropriete("JWTSecret"));
  private final JWTVerifier jwtVerifier = JWT.require(this.jwtAlgorithm).withIssuer("auth0")
      .build();
  @Inject
  private UtilisateurUCC utilisateurUCC;

  @Override
  public void filter(ContainerRequestContext requestContext)  {
    String token = requestContext.getHeaderString("Authorization");
    if (token == null) {
      requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
          .entity("un token est nécessaire pour accéder à cette ressource").build());
    } else {

      try {
        this.jwtVerifier.verify(token);

      } catch (Exception e) {
        throw new WebApplicationException(Response.status(Status.UNAUTHORIZED)
            .entity("token malformé: " + e.getMessage()).type("text/plain").build());
      }

      int idUtilisateur = Integer.parseInt(requestContext.getHeaderString("user"));


      UtilisateurDTO authenticatedUser = utilisateurUCC.rechercheParId(idUtilisateur);
      if (authenticatedUser == null) {
        requestContext.abortWith(Response.status(Status.FORBIDDEN)
            .entity("Vous ne pouvez pas accéder a cette ressource").build());
      }
      requestContext.setProperty("user", authenticatedUser);
    }
  }

}

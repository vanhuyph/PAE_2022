package be.vinci.pae.presentation.ressources.filtres;

import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Singleton
@Provider
@AutorisationAdmin
public class FiltreAutorisationRequeteAdmin extends AutorisationAbstraite implements
    ContainerRequestFilter {

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    UtilisateurDTO utilisateurAuthentifie = tokenDecode(requestContext);
    if (utilisateurAuthentifie == null || !utilisateurAuthentifie.isEstAdmin()) {
      requestContext.abortWith(Response.status(Status.FORBIDDEN)
          .entity("Vous ne pouvez pas accéder a cette ressource").build());
    }
    requestContext.setProperty("utilisateur", utilisateurAuthentifie);

  }
}

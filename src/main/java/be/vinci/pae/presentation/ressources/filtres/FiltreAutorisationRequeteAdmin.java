package be.vinci.pae.presentation.ressources.filtres;

import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import be.vinci.pae.utilitaires.exceptions.FatalException;
import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;

@Singleton
@Provider
@AutorisationAdmin
public class FiltreAutorisationRequeteAdmin extends AutorisationAbstraite implements
    ContainerRequestFilter {

  @Override
  public void filter(ContainerRequestContext requestContext) {
    UtilisateurDTO utilisateurAuthentifie = null;
    try {
      utilisateurAuthentifie = tokenDecode(requestContext);
    } catch (BusinessException e) {
      requestContext.abortWith(Response.status(Status.BAD_REQUEST)
          .entity(e.getMessage()).build());
    } catch (FatalException e) {
      requestContext.abortWith(Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity(e.getMessage()).build());
    }
    if (utilisateurAuthentifie == null || !utilisateurAuthentifie.isEstAdmin()) {
      requestContext.abortWith(Response.status(Status.FORBIDDEN)
          .entity("Vous ne pouvez pas accéder à cette ressource").build());
    }
    requestContext.setProperty("utilisateur", utilisateurAuthentifie);
  }

}

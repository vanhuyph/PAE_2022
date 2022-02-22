package be.vinci.pae.utilitaires;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class WebExceptionMapper implements ExceptionMapper<Throwable> {

  @Override
  public Response toResponse(Throwable exception) {
    exception.printStackTrace();
    if (exception instanceof WebApplicationException) {
      return ((WebApplicationException) exception).getResponse();
    }
    if (exception instanceof IllegalStateException && exception.getMessage()
        .equals("Non autorisé")) {
      return Response.status(Response.Status.UNAUTHORIZED).entity("Vous êtes non autorisé.")
          .build();
    }
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage())
        .build();
  }
}

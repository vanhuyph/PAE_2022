package be.vinci.pae.utilitaires;

import be.vinci.pae.utilitaires.exceptions.BusinessException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class WebExceptionMapper implements ExceptionMapper<Throwable> {

  @Override
  public Response toResponse(Throwable exception) {
    LoggerFichier.log((Exception) exception);
    if (exception instanceof BusinessException) {
      return Response.status(((BusinessException) exception).getResponse().getStatus())
          .entity(exception.getMessage()).build();
    }
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage())
        .build();
  }

}

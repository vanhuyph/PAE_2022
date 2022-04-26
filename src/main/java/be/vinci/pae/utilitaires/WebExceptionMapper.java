package be.vinci.pae.utilitaires;

import be.vinci.pae.utilitaires.exceptions.BusinessException;
import be.vinci.pae.utilitaires.exceptions.ConflitException;
import be.vinci.pae.utilitaires.exceptions.InterdictionException;
import be.vinci.pae.utilitaires.exceptions.NonAutoriseException;
import be.vinci.pae.utilitaires.exceptions.OptimisticLockException;
import be.vinci.pae.utilitaires.exceptions.PasTrouveException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class WebExceptionMapper implements ExceptionMapper<Throwable> {

  @Override
  public Response toResponse(Throwable exception) {
    LoggerFichier.log(Level.WARNING, (Exception) exception);
    if (exception instanceof WebApplicationException) {
      return Response.status(((WebApplicationException) exception).getResponse().getStatus())
          .entity(exception.getMessage()).build();
    }
    if (exception instanceof NonAutoriseException) {
      return Response.status(Status.UNAUTHORIZED).entity(exception.getMessage()).build();
    }
    if (exception instanceof PasTrouveException) {
      return Response.status(Status.NOT_FOUND).entity(exception.getMessage()).build();
    }
    if (exception instanceof ConflitException || exception instanceof OptimisticLockException) {
      return Response.status(Status.CONFLICT).entity(exception.getMessage()).build();
    }
    if (exception instanceof InterdictionException) {
      return Response.status(Status.FORBIDDEN).entity(exception.getMessage()).build();
    }
    if (exception instanceof BusinessException) {
      return Response.status(Status.PRECONDITION_FAILED).entity(exception.getMessage()).build();
    }
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage())
        .build();
  }

}

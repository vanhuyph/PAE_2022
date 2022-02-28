package be.vinci.pae.utilitaires;

import be.vinci.pae.utilitaires.exceptions.ExceptionBusiness;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class WebExceptionMapper implements ExceptionMapper<Throwable> {

  @Override
  public Response toResponse(Throwable exception) {
    // exception.printStackTrace();
    if (exception instanceof ExceptionBusiness) {
      return Response.status(((ExceptionBusiness) exception).getResponse().getStatus())
          .entity(exception.getMessage()).build();
    }
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage())
        .build();
  }
}

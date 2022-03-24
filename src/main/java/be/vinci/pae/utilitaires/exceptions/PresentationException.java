package be.vinci.pae.utilitaires.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

public class PresentationException extends WebApplicationException {

  public PresentationException(String message, Status status) {
    super(message, status);
  }
  
}

package be.vinci.pae.utilitaires.exceptions;

import jakarta.ws.rs.WebApplicationException;

public class FatalException extends WebApplicationException {

  public FatalException(String message, Throwable cause) {
    super(message, cause);
  }

}

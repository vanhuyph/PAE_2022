package be.vinci.pae.utilitaires.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

public class BusinessException extends WebApplicationException {

  public BusinessException(String message, Status statut) {
    super(message, statut);
  }

}

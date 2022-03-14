package be.vinci.pae.utilitaires.exceptions;

public class FatalException extends RuntimeException {

  public FatalException(String message, Throwable cause) {
    super(message, cause);
  }

}

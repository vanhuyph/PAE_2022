package be.vinci.pae.utilitaires.exceptions;

public class OptimisticLockException extends RuntimeException {

  public OptimisticLockException(String message) {
    super(message);
  }

}

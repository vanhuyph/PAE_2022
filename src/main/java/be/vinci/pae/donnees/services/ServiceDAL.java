package be.vinci.pae.donnees.services;

public interface ServiceDAL {

  void commencerTransaction();

  void commettreTransaction();

  void retourEnArriereTransaction();

}

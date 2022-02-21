package be.vinci.pae.business.domaine;

public class DomaineFactoryImpl implements DomaineFactory {

  @Override
  public Utilisateur getUtilisateur() {
    return new UtilisateurImpl();
  }

}

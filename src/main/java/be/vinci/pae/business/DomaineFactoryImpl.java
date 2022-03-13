package be.vinci.pae.business;

import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.objet.ObjetImpl;
import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.business.offre.OffreImpl;
import be.vinci.pae.business.typeObjet.TypeObjetDTO;
import be.vinci.pae.business.typeObjet.TypeObjetImpl;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.business.utilisateur.UtilisateurImpl;

public class DomaineFactoryImpl implements DomaineFactory {

  @Override
  public TypeObjetDTO getTypeObjet() {
    return new TypeObjetImpl();
  }

  @Override
  public UtilisateurDTO getUtilisateur() {
    return new UtilisateurImpl();
  }

  @Override
  public OffreDTO getOffre() {
    return new OffreImpl();
  }

  @Override
  public ObjetDTO getObjet() {
    return new ObjetImpl();
  }
}

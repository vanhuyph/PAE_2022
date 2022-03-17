package be.vinci.pae.business.offre;

public interface OffreUCC {

  OffreDTO creerUneOffre(int idObjet, String plageHoraire);

  OffreDTO annulerUneOffre(int idOffre);

  OffreDTO rechercheParId(int id);
}

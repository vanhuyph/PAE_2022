package be.vinci.pae.business.adresse;

import be.vinci.pae.donnees.dao.adresse.AdresseDAO;
import be.vinci.pae.utilitaires.exceptions.BusinessException;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response.Status;

public class AdresseUCCImpl implements AdresseUCC {


  @Inject
  AdresseDAO adresseDAO;

  /**
   * Vérifie si l'adresse a bien été ajoutée.
   *
   * @param rue        : la rue de l'adresse
   * @param numero     : le numero de l'adresse
   * @param boite      : la boite de l'adresse
   * @param codePostal : le code postal de l'adresse
   * @param commune    : la commune de l'adresse
   * @return : l'adresse ajoutée
   * @throws BusinessException : est lancé si l'adresse n'a pas pu être ajoutée
   */
  @Override
  public AdresseDTO ajouterAdresse(String rue, int numero, int boite, int codePostal,
      String commune) {

    AdresseDTO adresseDTO = adresseDAO.ajouterAdresse(rue, numero, boite, codePostal, commune);

    if (adresseDTO == null) {
      throw new BusinessException("L'adresse n'a pas pu être ajoutée.", Status.BAD_REQUEST);
    }
    return adresseDTO;
  }
}

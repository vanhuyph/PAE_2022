package be.vinci.pae.donnees.dao.interet;

import be.vinci.pae.business.interet.InteretDTO;
import java.util.Date;

public interface InteretDAO {

  InteretDTO ajouterInteret(int idUtilisateurInteresse, int idObjet, Date dateRdv);
}

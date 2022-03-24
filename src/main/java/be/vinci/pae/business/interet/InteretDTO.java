package be.vinci.pae.business.interet;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;

@JsonDeserialize(as = InteretImpl.class)
public interface InteretDTO {

  int getIdUtilisateur();

  void setIdUtilisateur(int idUtilisateurInteresse);

  int getIdObjet();

  void setIdObjet(int idObjet);

  Date getDateRdv();

  void setDateRdv(Date dateRdv);


}

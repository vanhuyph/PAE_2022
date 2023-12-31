package be.vinci.pae.business.interet;

import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;

@JsonDeserialize(as = InteretImpl.class)
public interface InteretDTO {

  UtilisateurDTO getUtilisateur();

  void setUtilisateur(UtilisateurDTO idUtilisateurInteresse);

  ObjetDTO getObjet();

  void setObjet(ObjetDTO idObjet);

  Date getDateRdv();

  void setDateRdv(Date dateRdv);

  boolean isVue();

  void setVue(boolean vue);

  int getVersion();

  void setVersion(int version);

  boolean isReceveurChoisi();

  void setReceveurChoisi(boolean receveurChoisi);

  Boolean isVenuChercher();

  void setVenuChercher(Boolean venuChercher);

  boolean isVueEmpecher();

  void setVueEmpecher(boolean vueEmpecher);

  boolean isVueReoffert();

  void setVueReoffert(boolean vueReoffert);

}
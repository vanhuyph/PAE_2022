package be.vinci.pae.business.offre;

import be.vinci.pae.business.objet.ObjetDTO;
import java.util.Date;

public class OffreImpl implements Offre {

  private int id_offre;
  private Date date_offre;
  private ObjetDTO objet;
  private String plage_horaire;

  public int getId_offre() {
    return id_offre;
  }

  public void setId_offre(int id_offre) {
    this.id_offre = id_offre;
  }

  public Date getDate_offre() {
    return date_offre;
  }

  public void setDate_offre(Date date_offre) {
    this.date_offre = date_offre;
  }

  public ObjetDTO getObjet() {
    return objet;
  }

  public void setObjet(ObjetDTO objet) {
    this.objet = objet;
  }

  public String getPlage_horaire() {
    return plage_horaire;
  }

  public void setPlage_horaire(String plage_horaire) {
    this.plage_horaire = plage_horaire;
  }
}

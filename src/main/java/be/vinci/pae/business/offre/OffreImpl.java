package be.vinci.pae.business.offre;

import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.vue.Vues;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.Date;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OffreImpl implements Offre {

  @JsonView(Vues.Public.class)
  private int idOffre;
  @JsonView(Vues.Public.class)
  private Date dateOffre;
  @JsonView(Vues.Public.class)
  private ObjetDTO objet;
  @JsonView(Vues.Public.class)
  private String plageHoraire;

  public int getIdOffre() {
    return idOffre;
  }

  public void setIdOffre(int idOffre) {
    this.idOffre = idOffre;
  }

  public Date getDateOffre() {
    return dateOffre;
  }

  public void setDateOffre(Date dateOffre) {
    this.dateOffre = dateOffre;
  }

  public ObjetDTO getObjet() {
    return objet;
  }

  public void setObjet(ObjetDTO objet) {
    this.objet = objet;
  }

  public String getPlageHoraire() {
    return plageHoraire;
  }

  public void setPlageHoraire(String plageHoraire) {
    this.plageHoraire = plageHoraire;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OffreImpl offre = (OffreImpl) o;
    return idOffre == offre.idOffre && dateOffre.equals(offre.dateOffre);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idOffre, dateOffre);
  }

  @Override
  public String toString() {
    return "Offre{"
        + "id offre= " + idOffre
        + ", date offre= " + dateOffre
        + ", objet= " + objet.toString()
        + ", plage horaire= " + plageHoraire
        + '}';
  }
}

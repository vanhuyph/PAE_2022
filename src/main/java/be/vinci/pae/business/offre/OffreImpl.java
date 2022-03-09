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
  private int id_offre;
  @JsonView(Vues.Public.class)
  private Date date_offre;
  @JsonView(Vues.Public.class)
  private ObjetDTO objet;
  @JsonView(Vues.Public.class)
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OffreImpl offre = (OffreImpl) o;
    return id_offre == offre.id_offre && date_offre.equals(offre.date_offre);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id_offre, date_offre);
  }

  @Override
  public String toString() {
    return "Offre{"
        + "id offre= " + id_offre
        + ", date offre= " + date_offre
        + ", objet= " + objet.toString()
        + ", plage horaire= " + plage_horaire
        + '}';
  }
}

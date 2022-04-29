package be.vinci.pae.business.offre;

import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.vue.Vues;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import java.time.LocalDateTime;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OffreImpl implements Offre {

  @JsonView(Vues.Public.class)
  private int idOffre;
  @JsonView(Vues.Public.class)
  private LocalDateTime dateOffre;
  @JsonView(Vues.Public.class)
  private ObjetDTO objetDTO;
  @JsonView(Vues.Public.class)
  private String plageHoraire;
  @JsonView(Vues.Public.class)
  private int version;

  public int getIdOffre() {
    return idOffre;
  }

  public void setIdOffre(int idOffre) {
    this.idOffre = idOffre;
  }

  public LocalDateTime getDateOffre() {
    return dateOffre;
  }

  public void setDateOffre(LocalDateTime dateOffre) {
    this.dateOffre = dateOffre;
  }

  public ObjetDTO getObjetDTO() {
    return objetDTO;
  }

  public void setObjetDTO(ObjetDTO objetDTO) {
    this.objetDTO = objetDTO;
  }

  public String getPlageHoraire() {
    return plageHoraire;
  }

  public void setPlageHoraire(String plageHoraire) {
    this.plageHoraire = plageHoraire;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
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
    return idOffre == offre.idOffre;
  }

  @Override
  public int hashCode() {
    return Objects.hash(idOffre, dateOffre);
  }

  @Override
  public void offrirObjet() {
    this.getObjetDTO().setEtatObjet("Offert");
  }

  @Override
  public void interesseObjet() {
    this.getObjetDTO().setEtatObjet("Intéressé");
  }

  @Override
  public void annulerOffre() {
    this.getObjetDTO().setEtatObjet("Annulé");
  }

  @Override
  public void objetNonVu() {
    this.getObjetDTO().setVue(false);
  }

  @Override
  public void donnerObjet() {
    this.getObjetDTO().setEtatObjet("Donné");
  }

  @Override
  public String toString() {
    return "Offre{"
        + "id offre= " + idOffre
        + ", date offre= " + dateOffre
        + ", objetDTO= " + objetDTO
        + ", plage horaire= " + plageHoraire
        + ", version= " + version
        + '}';
  }

}

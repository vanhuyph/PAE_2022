package be.vinci.pae.business.adresse;

import be.vinci.pae.vue.Vues;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdresseImpl implements AdresseDTO, Adresse {

  @JsonView(Vues.Public.class)
  private int idAdresse;
  @JsonView(Vues.Public.class)
  private String rue;
  @JsonView(Vues.Public.class)
  private int numero;
  @JsonView(Vues.Public.class)
  private String boite;
  @JsonView(Vues.Public.class)
  private int codePostal;
  @JsonView(Vues.Public.class)
  private String commune;
  @JsonView(Vues.Public.class)
  private int version;

  @Override
  public int getIdAdresse() {
    return idAdresse;
  }

  @Override
  public void setIdAdresse(int idAdresse) {
    this.idAdresse = idAdresse;
  }

  @Override
  public String getRue() {
    return rue;
  }

  @Override
  public void setRue(String rue) {
    this.rue = rue;
  }

  @Override
  public int getNumero() {
    return numero;
  }

  @Override
  public void setNumero(int numero) {
    this.numero = numero;
  }

  @Override
  public String getBoite() {
    return boite;
  }

  @Override
  public void setBoite(String boite) {
    this.boite = boite;
  }

  @Override
  public int getCodePostal() {
    return codePostal;
  }

  @Override
  public void setCodePostal(int codePostal) {
    this.codePostal = codePostal;
  }

  @Override
  public String getCommune() {
    return commune;
  }

  @Override
  public void setCommune(String commune) {
    this.commune = commune;
  }

  @Override
  public int getVersion() {
    return version;
  }

  @Override
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
    AdresseImpl adresse = (AdresseImpl) o;
    return idAdresse == adresse.idAdresse && numero == adresse.numero && boite == adresse.boite
        && codePostal == adresse.codePostal && Objects.equals(rue, adresse.rue)
        && Objects.equals(commune, adresse.commune);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idAdresse, rue, numero, boite, codePostal, commune);
  }

  @Override
  public String toString() {
    return "Adresse{"
        + "idAdresse=" + idAdresse
        + ", rue='" + rue + '\''
        + ", numero=" + numero
        + ", boite=" + boite
        + ", codePostal=" + codePostal
        + ", commune='" + commune + '\''
        + ", version='" + version + '\''
        + '}';
  }

  @Override
  public void premiereVersion() {
    this.setVersion(1);
  }
}

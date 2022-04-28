package be.vinci.pae.business.typeobjet;

import be.vinci.pae.vue.Vues;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TypeObjetImpl implements TypeObjet {

  @JsonView(Vues.Public.class)
  private int idType;
  @JsonView(Vues.Public.class)
  private String nom;

  public int getIdType() {
    return idType;
  }

  public void setIdType(int idType) {
    this.idType = idType;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TypeObjetImpl typeObjet = (TypeObjetImpl) o;
    return idType == typeObjet.idType && nom.equals(typeObjet.nom);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idType, nom);
  }

  @Override
  public String toString() {
    return "TypeObjet{"
        + "id type= " + idType
        + ", nom= " + nom
        + '}';
  }

}

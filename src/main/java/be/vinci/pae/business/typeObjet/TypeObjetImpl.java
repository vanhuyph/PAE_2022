package be.vinci.pae.business.typeObjet;

import be.vinci.pae.vue.Vues;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TypeObjetImpl implements TypeObjetDTO {

  @JsonView(Vues.Public.class)
  private int idType;
  @JsonView(Vues.Public.class)
  private String nom;

  @Override
  public int getIdType() {
    return idType;
  }

  @Override
  public void setIdType(int idType) {
    this.idType = idType;
  }

  @Override
  public String getNom() {
    return nom;
  }

  @Override
  public void setNom(String nom) {
    this.nom = nom;
  }
}

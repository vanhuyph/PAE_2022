package be.vinci.pae.business.objet;

import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.vue.Vues;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjetImpl implements Objet {

  @JsonView(Vues.Public.class)
  private int idObjet;
  @JsonView(Vues.Public.class)
  private String etatObjet;
  @JsonView(Vues.Public.class)
  private String typeObjet;
  @JsonView(Vues.Public.class)
  private String description;
  @JsonView(Vues.Public.class)
  private UtilisateurDTO offreur;
  private UtilisateurDTO receveur;
  @JsonView(Vues.Public.class)//vérifier type d'objet
  private String photo;


  public int getIdObjet() {
    return idObjet;
  }

  public void setIdObjet(int idObjet) {
    this.idObjet = idObjet;
  }

  public String getEtatObjet() {
    return etatObjet;
  }

  public void setEtatObjet(String etatObjet) {
    this.etatObjet = etatObjet;
  }

  public String getTypeObjet() {
    return typeObjet;
  }

  public void setTypeObjet(String typeObjet) {
    this.typeObjet = typeObjet;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String decsription) {
    this.description = decsription;
  }

  public UtilisateurDTO getOffreur() {
    return offreur;
  }

  public void setOffreur(UtilisateurDTO offreur) {
    this.offreur = offreur;
  }

  public UtilisateurDTO getReceveur() {
    return receveur;
  }

  public void setReceveur(UtilisateurDTO receveur) {
    this.receveur = receveur;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ObjetImpl objet = (ObjetImpl) o;
    return idObjet == objet.idObjet && typeObjet.equals(objet.typeObjet) && offreur.equals(
        objet.offreur);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idObjet, typeObjet, offreur);
  }

  @Override
  public String toString() {
    return "Objet{"
        + "id objet= " + idObjet
        + ", etat objet= " + etatObjet
        + ", type objet= " + typeObjet
        + ", description= " + description
        + ", offreur= " + offreur.toString()
        + ", receveur= " + receveur.toString()
        + ", photo= " + photo
        + '}';
  }
}

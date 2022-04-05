package be.vinci.pae.business.objet;

import be.vinci.pae.business.typeobjet.TypeObjetDTO;
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
  private TypeObjetDTO typeObjet;
  @JsonView(Vues.Public.class)
  private String description;
  @JsonView(Vues.Public.class)
  private UtilisateurDTO offreur;
  @JsonView(Vues.Public.class)
  private UtilisateurDTO receveur;
  @JsonView(Vues.Public.class)//vérifier type d'objet
  private String photo;
  @JsonView(Vues.Public.class)
  private int version;

  /**
   * Verifie si l'état de l'objet permet de le modifier ainsi que son offre
   *
   * @return true si l'objet peut être modifié false si non.
   */
  @Override
  public boolean verifierEtatPourModificationOffre() {
    if (this.etatObjet.equals("annulé")) {
      return false;
    }
    if (this.etatObjet.equals("donné")) {
      return false;
    }

    return true;
  }

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

  public TypeObjetDTO getTypeObjet() {
    return typeObjet;
  }

  public void setTypeObjet(TypeObjetDTO typeObjet) {
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
  public int getVersion() {
    return this.version;
  }

  @Override
  public void setVersion(int nVersion) {
    this.version = nVersion;
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
    return idObjet == objet.idObjet && offreur == objet.offreur;
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
        + ", offreur= " + offreur
        + ", receveur= " + receveur
        + ", photo= " + photo
        + ",version= " + version
        + '}';
  }
}

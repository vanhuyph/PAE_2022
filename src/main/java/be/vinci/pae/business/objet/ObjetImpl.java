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
  @JsonView(Vues.Public.class)
  private String photo;
  @JsonView(Vues.Public.class)
  private boolean vue;
  @JsonView(Vues.Public.class)
  private int version;

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

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public boolean isVue() {
    return vue;
  }

  public void setVue(boolean vue) {
    this.vue = vue;
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
  public boolean verifierEtatPourModificationOffre() {
    return this.etatObjet != null && !this.etatObjet.equals("Annulé") && !this.etatObjet.equals(
        "Donné") && !this.etatObjet.equals("Evalué");
  }

  @Override
  public boolean peutEtreEvalue() {
    return this.etatObjet != null && this.etatObjet.equals("Donné");
  }

  @Override
  public void estEvalue() {
    this.setEtatObjet("Evalué");
  }

  @Override
  public void indiquerReceveur(UtilisateurDTO utilisateur) {
    this.setReceveur(utilisateur);
  }

  @Override
  public void confirmerObjet() {
    this.setEtatObjet("Confirmé");
  }

  public void estVu() {
    this.setVue(true);
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
        + ", vue= " + vue
        + ", version= " + version
        + '}';
  }

}

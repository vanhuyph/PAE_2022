package be.vinci.pae.business.objet;

import be.vinci.pae.business.utilisateur.Utilisateur;
import be.vinci.pae.vue.Vues;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjetImpl implements Objet {

  @JsonView(Vues.Public.class)
  private int id_objet;
  @JsonView(Vues.Public.class)
  private String etat_objet;
  @JsonView(Vues.Public.class)
  private String type_objet;
  @JsonView(Vues.Public.class)
  private String description;
  @JsonView(Vues.Public.class)
  private Utilisateur offreur;
  private Utilisateur receveur;
  @JsonView(Vues.Public.class)//vérifier type d'objet
  private String photo;


  public int getId_objet() {
    return id_objet;
  }

  public void setId_objet(int id_objet) {
    this.id_objet = id_objet;
  }

  public String getEtat_objet() {
    return etat_objet;
  }

  public void setEtat_objet(String etat_objet) {
    this.etat_objet = etat_objet;
  }

  public String getType_objet() {
    return type_objet;
  }

  public void setType_objet(String type_objet) {
    this.type_objet = type_objet;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String decsription) {
    this.description = decsription;
  }

  public Utilisateur getOffreur() {
    return offreur;
  }

  public void setOffreur(Utilisateur offreur) {
    this.offreur = offreur;
  }

  public Utilisateur getReceveur() {
    return receveur;
  }

  public void setReceveur(Utilisateur receveur) {
    this.receveur = receveur;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }
}

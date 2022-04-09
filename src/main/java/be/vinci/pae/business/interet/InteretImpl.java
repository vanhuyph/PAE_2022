package be.vinci.pae.business.interet;

import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.vue.Vues;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.Date;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InteretImpl implements Interet {

  @JsonView(Vues.Public.class)
  private UtilisateurDTO utilisateur;
  @JsonView(Vues.Public.class)
  private ObjetDTO objet;
  @JsonView(Vues.Public.class)
  private Date dateRdv;
  @JsonView(Vues.Public.class)
  private boolean vue;
  @JsonView(Vues.Public.class)
  private int version;

  public UtilisateurDTO getUtilisateur() {
    return utilisateur;
  }

  public void setUtilisateur(UtilisateurDTO utilisateur) {
    this.utilisateur = utilisateur;
  }

  public ObjetDTO getObjet() {
    return objet;
  }

  public void setObjet(ObjetDTO objet) {
    this.objet = objet;
  }

  public Date getDateRdv() {
    return dateRdv;
  }

  public void setDateRdv(Date dateRdv) {
    this.dateRdv = dateRdv;
  }

  public boolean isVue() {
    return vue;
  }

  public void setVue(boolean vue) {
    this.vue = vue;
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
    InteretImpl interet = (InteretImpl) o;
    return Objects.equals(utilisateur, interet.utilisateur) && Objects.equals(
        objet, interet.objet) && Objects.equals(dateRdv, interet.dateRdv);
  }

  @Override
  public int hashCode() {
    return Objects.hash(utilisateur, objet, dateRdv);
  }

  @Override
  public String toString() {
    return "InteretImpl{"
        + "utilisateur=" + utilisateur
        + ", objet=" + objet
        + ", dateRdv=" + dateRdv
        + ", vue =" + vue
        + ", version=" + version
        + '}';
  }

  @Override
  public InteretDTO changerEtatObjet(String etat) {
    this.getObjet().setEtatObjet(etat);
    return this;
  }

  @Override
  public void creerVersion() {
    this.setVersion(1);
  }
}

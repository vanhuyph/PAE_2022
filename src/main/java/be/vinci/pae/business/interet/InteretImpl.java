package be.vinci.pae.business.interet;




import be.vinci.pae.vue.Vues;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.Date;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InteretImpl implements Interet {

  @JsonView(Vues.Public.class)
  private int idUtilisateurInteresse;
  @JsonView(Vues.Public.class)
  private int idObjet;
  @JsonView(Vues.Public.class)
  private Date dateRdv;

  public int getIdUtilisateur()  {
    return idUtilisateurInteresse;
  }

  public void setIdUtilisateur(int idUtilisateurInteresse) {
    this.idUtilisateurInteresse = idUtilisateurInteresse;
  }

  public int getIdObjet() {
    return idObjet;
  }

  public void setIdObjet(int idObjet) {
    this.idObjet = idObjet;
  }

  public Date getDateRdv() {
    return dateRdv;
  }

  public void setDateRdv(Date dateRdv) {
    this.dateRdv = dateRdv;
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
    return idObjet == interet.idObjet && idUtilisateurInteresse == interet.idUtilisateurInteresse;
  }

  @Override
  public int hashCode() {
    return Objects.hash(idObjet, idUtilisateurInteresse);
  }

  @Override
  public String toString() {
    return "Interet{"
                + "id utilisateur interesse= " + idUtilisateurInteresse
                + "id objet= " + idObjet
                + "date de rendez vous =" + dateRdv
                + "}";
  }
}

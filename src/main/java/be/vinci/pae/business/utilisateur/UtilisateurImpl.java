package be.vinci.pae.business.utilisateur;

import be.vinci.pae.business.adresse.AdresseDTO;
import be.vinci.pae.vue.Vues;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.Objects;
import org.mindrot.jbcrypt.BCrypt;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UtilisateurImpl implements Utilisateur {

  @JsonView(Vues.Public.class)
  private int idUtilisateur;
  @JsonView(Vues.Public.class)
  private String pseudo;
  @JsonView(Vues.Internal.class)
  private String nom;
  @JsonView(Vues.Internal.class)
  private String prenom;
  @JsonView(Vues.Internal.class)
  private String mdp;
  @JsonView(Vues.Internal.class)
  private String gsm;
  @JsonView(Vues.Public.class)
  private boolean estAdmin;
  @JsonView(Vues.Public.class)
  private String etatInscription;
  @JsonView(Vues.Public.class)
  private String commentaire;
  @JsonView(Vues.Internal.class)
  private AdresseDTO adresse;
  @JsonView(Vues.Public.class)
  private int version;

  @Override
  public int getIdUtilisateur() {
    return idUtilisateur;
  }

  @Override
  public void setIdUtilisateur(int idUtilisateur) {
    this.idUtilisateur = idUtilisateur;
  }

  @Override
  public String getPseudo() {
    return pseudo;
  }

  @Override
  public void setPseudo(String pseudo) {
    this.pseudo = pseudo;
  }

  @Override
  public String getNom() {
    return nom;
  }

  @Override
  public void setNom(String nom) {
    this.nom = nom;
  }

  @Override
  public String getPrenom() {
    return prenom;
  }

  @Override
  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  @Override
  public String getMdp() {
    return mdp;
  }

  @Override
  public void setMdp(String mdp) {
    this.mdp = mdp;
  }

  @Override
  public String getGsm() {
    return gsm;
  }

  @Override
  public void setGsm(String gsm) {
    this.gsm = gsm;
  }

  @Override
  public boolean isEstAdmin() {
    return estAdmin;
  }

  @Override
  public void setEstAdmin(boolean estAdmin) {
    this.estAdmin = estAdmin;
  }

  @Override
  public String getEtatInscription() {
    return etatInscription;
  }

  @Override
  public void setEtatInscription(String etatInscription) {
    this.etatInscription = etatInscription;
  }

  @Override
  public String getCommentaire() {
    return commentaire;
  }

  @Override
  public void setCommentaire(String commentaire) {
    this.commentaire = commentaire;
  }

  @Override
  public AdresseDTO getAdresse() {
    return adresse;
  }

  @Override
  public void setAdresse(AdresseDTO adresse) {
    this.adresse = adresse;
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
    UtilisateurImpl that = (UtilisateurImpl) o;
    return idUtilisateur == that.idUtilisateur && Objects.equals(pseudo, that.pseudo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idUtilisateur, pseudo);
  }

  @Override
  public boolean verifierMdp(String mdp) {
    return BCrypt.checkpw(mdp, this.mdp);
  }

  @Override
  public String hashMdp(String mdp) {
    return BCrypt.hashpw(mdp, BCrypt.gensalt());
  }

  @Override
  public String toString() {
    return "UtilisateurImpl{"
        + "idUtilisateur=" + idUtilisateur
        + ", pseudo='" + pseudo + '\''
        + ", nom='" + nom + '\''
        + ", prenom='" + prenom + '\''
        + ", mdp='" + mdp + '\''
        + ", gsm='" + gsm + '\''
        + ", estAdmin=" + estAdmin
        + ", etatInscription='" + etatInscription + '\''
        + ", commentaire='" + commentaire + '\''
        + ", adresse=" + adresse.toString()
        + '}';
  }

}

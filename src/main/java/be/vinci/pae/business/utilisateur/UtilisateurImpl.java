package be.vinci.pae.business.utilisateur;

import java.util.Objects;
import org.mindrot.jbcrypt.BCrypt;

public class UtilisateurImpl implements Utilisateur {

  private int idUtilisateur;
  private String pseudo;
  private String nom;
  private String prenom;
  private String mdp;
  private String gsm;
  private boolean estAdmin;

  public UtilisateurImpl(int idUtilisateur, String pseudo, String nom, String prenom,
      String mdp, String gsm, boolean estAdmin) {
    this.idUtilisateur = idUtilisateur;
    this.pseudo = pseudo;
    this.nom = nom;
    this.prenom = prenom;
    this.mdp = mdp;
    this.gsm = gsm;
    this.estAdmin = estAdmin;
  }

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
    return "Utilisateur{" +
        "idUtilisateur=" + idUtilisateur +
        ", pseudo='" + pseudo + '\'' +
        ", nom='" + nom + '\'' +
        ", prenom='" + prenom + '\'' +
        ", mdp='" + mdp + '\'' +
        ", gsm='" + gsm + '\'' +
        ", estAdmin=" + estAdmin +
        '}';
  }
}

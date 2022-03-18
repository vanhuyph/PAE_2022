import {recupUtilisateurDonneesSession} from '../../utilitaires/session'
import {Redirect} from "../Router/Router";

const PageProfil = () => {
  let session = recupUtilisateurDonneesSession()

  if (!session) {
    Redirect('/connexion')
  }

  let pseudo = session.utilisateur.pseudo;
  fetch("/api/utilisateurs/" + pseudo, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: session.token
    },
  })
  .then((response) => {
    if (!response.ok) {
      throw new Error(
          "Error code : " + response.status + " : " + response.statusText
      );
    }
    return response.json();
  })
  .then((data) => surProfilUtilisateur(data))
}

const surProfilUtilisateur = (data) => {
  let boite = ""
  let gsm = "Pas de numéro"
  if(data.adresse.boite !== 0){
    boite = data.adresse.boite
  }
  if(data.adresse.gsm){
    gsm = data.adresse.gsm
  }

  let profil = `
  <div class="page-profil">
    <h2>Profil</h2>
    <div class="ui form">
      <div class="formulaire-profil">
        <div class="two fields">
          <div class="field">
            <div class="two fields">
              <div class="field">
                <label>Nom</label>
                <p>${data.nom}</p>
              </div>
              <div class="field">
                <label>Prenom</label>
                <p>${data.prenom}</p>
              </div>
            </div>
            <div class="two fields">
              <div class="field">
                <label>Pseudo</label>
                <p>${data.pseudo}</p>
              </div>
              <div class="field">
                <label>GSM</label>
                <p>${gsm}</p>
              </div>
            </div>
            <div class="field">
              <label>Mot de passe</label>
              <input id="mdp-profil" type="password" disabled name="profil[mdp]" value="password">
            </div>
            <div class="field">
                <button id="modifier-mdp-profil" class="ui button inverted secondary">Changer mot de passe</button>
            </div>
          </div>
          <div class="field">
            <div class="rue">
            <div class="field">
              <label>Rue</label>
              <p>${data.adresse.rue}</p>
              </div>
            </div>
            <div class="two fields">
              <div class="field">
                <label>Numéro</label>
                <p>${data.adresse.numero}</p>
              </div>
              <div class="field">
                <label>Boite</label>
                <p>${boite}</p>
              </div>
            </div>
            <div class="two fields">
              <div class="field">
                <label>Code postal</label>
                <p>${data.adresse.codePostal}</p>
              </div>
              <div class="field">
                <label>Commune</label>
                <p>${data.adresse.commune}</p>
              </div>
            </div>
            <div class="field">
                <button id="modifier-profil" class="ui button inverted secondary">Modifier le profil</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  `
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = profil;

  document.getElementById("modifier-profil").addEventListener("click", (e) => {
    e.preventDefault()
    surModifierProfilUtilisateur(data)
  })

}

const surModifierProfilUtilisateur = (data) => {
  let boite = ""
  let gsm = ""
  if(data.adresse.boite !== 0){
    boite = data.adresse.boite
  }
  if(data.adresse.gsm){
    gsm = data.adresse.gsm
  }

  let profilModifier = `
  <div class="page-profil">
    <h2>Profil</h2>
    <form id="formulaire-profil" class="ui form">
      <div>
        <div class="two fields">
          <div class="field">
            <div class="two fields">
              <div class="field">
                <label>Nom</label>
                <input id="nom" type="text" name="profil[nom]" value=${data.nom}>
              </div>
              <div class="field">
                <label>Prenom</label>
                <input id="prenom" type="text" name="profil[Prenom]" value=${data.prenom}>
              </div>
            </div>
            <div class="two fields">
              <div class="field">
                <label>Pseudo</label>
                <input id="pseudo" type="text" name="profil[pseudo]" value=${data.pseudo}>
              </div>
              <div class="field">
                <label>GSM</label>
                <input id="gsm" type="text" name="profil[gsm]" value=${gsm}>
              </div>
            </div>
            <div class="field">
              <label>Mot de passe</label>
              <input type="password" disabled name="profil[mdp]" value="password">
            </div>
          </div>
          <div class="field">
            <div class="rue">
            <div class="field">
              <label>Rue</label>
              <input id="rue" type="text" name="profil[rue]" value=${data.adresse.rue}>
              </div>
            </div>
            <div class="two fields">
              <div class="field">
                <label>Numéro</label>
                <input id="numero" type="number" name="profil[numero]" value=${data.adresse.numero}>
              </div>
              <div class="field">
                <label>Boite</label>
                <input id="boite" type="number" name="profil[boite]" value=${boite}>
              </div>
            </div>
            <div class="two fields">
              <div class="field">
                <label>Code postal</label>
                <input id="code-postal" type="number" name="profil[codepostal]" value=${data.adresse.codePostal}>
              </div>
              <div class="field">
                <label>Commune</label>
                <input id="commune" type="text" name="profil[commune]" value=${data.adresse.commune}>
              </div>
            </div>
            <div class="field">
              <div class="ui buttons">
                  <button id="confirmer-modifier" type="submit" class="ui green button">Modifier</button>
                  <button id="annuler-modifier" class="ui red button">Annuler</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </form>
  </div>
  `

  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = profilModifier;

  document.getElementById('annuler-modifier').addEventListener('click', () => {
    surProfilUtilisateur(data)
  })

  let nouveauGsm = document.getElementById("gsm").value

  if (nouveauGsm === ""){
    nouveauGsm = null
  }

  let nouvelleBoite = document.getElementById("boite").value

  if(nouvelleBoite === ""){
    nouvelleBoite = 0
  }

  let nouvelleAdresse = {
    rue: document.getElementById("rue").value,
    numero: document.getElementById("numero").value,
    boite: nouvelleBoite,
    codePostal: document.getElementById("code-postal").value,
    commune: document.getElementById("commune").value
  }

  let utilisateur = {
    pseudo: document.getElementById("pseudo").value,
    nom: document.getElementById("nom").value,
    prenom: document.getElementById("prenom").value,
    gsm: nouveauGsm,
    adresse: nouvelleAdresse
  }

  document.getElementById('formulaire-profil').addEventListener('submit', () => {
    let session = recupUtilisateurDonneesSession()
    fetch("/api/utilisateurs/", {
      method: "PUT",
      body: JSON.stringify(utilisateur),
      headers: {
        "Content-Type": "application/json",
        Authorization: session.token,
      }
    })
    .then((response) => {
      if (!response.ok) {
        throw new Error(
            "Error code : " + response.status + " : " + response.statusText + " : " + response.text())
      }
      console.log(response)
      return response.json()
    })
    .then((donnee) => surProfilUtilisateur(donnee))
  })
}

export default PageProfil
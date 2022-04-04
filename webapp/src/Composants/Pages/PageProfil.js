import {recupUtilisateurDonneesSession} from '../../utilitaires/session'
import {Redirect} from "../Router/Router";

const PageProfil = () => {
    let session = recupUtilisateurDonneesSession()

    if(!session){
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
    <form class="ui form">
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
              <input type="password" disabled value="password" id="mdp-profil">
            </div>
            <div class="field">
                <button id="changer-mdp-profil" class="ui button inverted secondary">Changer mot de passe</button>
            </div>
            <div class="changer-mdp">
             <div class="field">
              <label>Mot de passe actuel</label>
              <input type="password" name="profil[mdp]" id="mdp-actuel">
             </div> 
             <div class="field">
              <label>Nouveau mot de passe</label>
              <input type="password" name="nv-mdp" id="nv-mdp"> 
             </div> 
             <div class="field">
              <label>Confirmer nouveau mot de passe</label>  
              <input type="password" name="conf-mdp" id="conf-mdp">
             </div>
              <div class="two fields">
              <div class="field">
              </div>
              <div class="field">
                <button id="modifier-mdp" class="ui positive button">Modifier mot de passe</button>
              </div>
            </div>
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
    </form>
  </div>
  `
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = profil;

  document.getElementById("changer-mdp-profil").addEventListener("click", (e) => {
    e.preventDefault()
    document.querySelector(".changer-mdp").classList.toggle("montrer-block")

  })

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
                <input id="nom" type="text" name="nom" value="${data.nom}" onchange="console.log('lol')">
              </div>
              <div class="field">
                <label>Prenom</label>
                <input id="prenom" type="text" name="prenom" value="${data.prenom}">
              </div>
            </div>
            <div class="two fields">
              <div class="field">
                <label>Pseudo</label>
                <input id="pseudo" type="text" name="pseudo" value="${data.pseudo}">
              </div>
              <div class="field">
                <label>GSM</label>
                <input id="gsm" type="text" name="gsm" value="${gsm}">
              </div>
            </div>
            <div class="field">
              <label>Mot de passe</label>
              <input type="password" disabled name="mdp" value="password">
            </div>
          </div>
          <div class="field">
            <div class="rue">
            <div class="field">
              <label>Rue</label>
              <input id="rue" type="text" name="rue" value="${data.adresse.rue}">
              </div>
            </div>
            <div class="two fields">
              <div class="field">
                <label>Numéro</label>
                <input id="numero" type="number" name="numero" value=${data.adresse.numero}>
              </div>
              <div class="field">
                <label>Boite</label>
                <input id="boite" type="number" name="boite" value="${boite}">
              </div>
            </div>
            <div class="two fields">
              <div class="field">
                <label>Code postal</label>
                <input id="code-postal" type="number" name="codepostal" value=${data.adresse.codePostal}>
              </div>
              <div class="field">
                <label>Commune</label>
                <input id="commune" type="text" name="commune" value="${data.adresse.commune}">
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

  console.log(data)
  document.getElementById('annuler-modifier').addEventListener('click', () => {
    surProfilUtilisateur(data)
  })



  document.getElementById('formulaire-profil').addEventListener('submit', () => {
    let nouveauGsm = document.getElementById("gsm").value

    if (nouveauGsm === ""){
      nouveauGsm = null
    }

    let nouvelleAdresse = {
      idAdresse: data.adresse.idAdresse,
      rue: document.getElementById("rue").value,
      numero: document.getElementById("numero").value,
      boite: document.getElementById("boite").value,
      codePostal: document.getElementById("code-postal").value,
      commune: document.getElementById("commune").value,
      version: data.adresse.version
    }

    let utilisateur = {
      idUtilisateur: data.idUtilisateur,
      pseudo: document.getElementById("pseudo").value,
      nom: document.getElementById("nom").value,
      prenom: document.getElementById("prenom").value,
      gsm: nouveauGsm,
      estAdmin: data.estAdmin,
      etatInscription: data.etatInscription,
      commentaire: data.commentaire,
      adresse: nouvelleAdresse,
      version: data.version
    }
    console.log(document.getElementById("nom").value)

    let session = recupUtilisateurDonneesSession()
    fetch("/api/utilisateurs", {
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
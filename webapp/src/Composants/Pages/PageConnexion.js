import {
  creationDonneeSessionUtilisateur,
  recupUtilisateurDonneesSession
} from "../../utilitaires/session"
import {Redirect} from "../Router/Router";
import Navbar from "../Navbar/Navbar";

let pageCon = `
    <div class="page-connexion">
        <h2>Connexion</h2>
        <form id="formulaire-connexion" class="ui form">
        
            <div class="field">
              <label for="pseudo">Pseudo</label>
              <div class="pseudo-conteneur">
                  <input type="text" id="pseudo" class="pseudo">
                  <p class="message-erreur erreur-pseudo"></p>
              </div>
            </div>
            
            <div class="field">
              <label for="mdp">Mot de passe</label>
              <div class="mdp-conteneur">
                  <input type="password" id="mdp" class="mdp">
                  <p class="message-erreur erreur-mdp"></p>
              </div>
            </div>
            <div class="field">
              <div class="ui checkbox"><input type="checkbox" id="souvenir" name="souvenir"><label for="souvenir">Se souvenir de moi</label></div>
            </div>
            <div id="messageErreur" class="message-erreur"></div>
            <button class="ui secondary inverted button" type="submit">Connexion</button>
            
        </form>
        <p class="separateur-ou">ou</p>
        <button class="ui secondary inverted button">S'inscrire</button>
    </div>
  `;

const PageConnexion = () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = pageCon;
  let formCon = document.querySelector("#formulaire-connexion")
  const utilisateur = recupUtilisateurDonneesSession()
  if (utilisateur) {
    Navbar()
    Redirect("/")
  } else {
    formCon.addEventListener("submit", surConnexion)
  }
}

const surConnexion = (e) => {
  e.preventDefault()
  let pseudo = document.querySelector("#pseudo")
  let mdp = document.querySelector("#mdp")
  document.querySelector(".erreur-pseudo").innerHTML = ""
  document.querySelector(".erreur-mdp").innerHTML = ""
  document.querySelector("#messageErreur").innerHTML = ""
  if (pseudo.value === "") {
    document.querySelector(".erreur-pseudo").innerHTML = "Votre pseudo est vide"
  }
  if (mdp.value === "") {
    document.querySelector(
        ".erreur-mdp").innerHTML = "Votre mot de passe est vide"
  }
  if (pseudo.value !== "" && mdp.value !== "") {
    let utilisateur = {
      pseudo: pseudo.value,
      mdp: mdp.value
    }
    let souvenir = document.querySelector("#souvenir").checked
    fetch("/api/utilisateurs/connexion", {
      method: "POST",
      body: JSON.stringify(utilisateur),
      headers: {
        "Content-Type": "application/json",
      }
    })
    .then((reponse) => {
      if (!reponse.ok) {
        throw new Error(
            "Error code : " + reponse.status + " : " + reponse.statusText)
      }
      return reponse.json();
    })
    .then((donnee) => surConUtilisateur(donnee, souvenir))
    .catch(err => surErreur(err))
  }
}

const surConUtilisateur = (donnee, souvenir) => {
  const utilisateur = {...donnee, isAutenticated: true}
  creationDonneeSessionUtilisateur(utilisateur, souvenir)
  Navbar()
  Redirect("/")
}

const surErreur = (err) => {
  let messageErreur = document.querySelector("#messageErreur");
  let erreurMessage = "";
  console.log(err)
  if (err.message.includes(
      "401") || err.message.includes(
      "500")) {
    erreurMessage = "Pseudo ou mot de passe incorrect.";
  } else {
    erreurMessage = err.message;
  }
  messageErreur.innerText = erreurMessage;
  document.querySelector("#pseudo").value = ""
  document.querySelector("#mdp").value = ""
}

export default PageConnexion
import {recupUtilisateurDonneesSession, creationDonneeSessionUtilisateur} from "../../utils/session"
import {Redirect} from "../Router/Router";
import Navbar from "../Navbar/Navbar";
let pageCon = `
    <div class="page-connexion">
        <h2>Connexion</h2>
        <form class="formulaire-connexion">
            <label for="pseudo">Pseudo</label>
            <div class="pseudo-conteneur">
                <input type="text" id="pseudo" class="pseudo">
            </div>
            <label for="mdp">Mot de passe</label>
            <div class="mdp-conteneur">
                <input type="password" id="mdp" class="mdp">
                
            </div>
             <div id="messageErreur"></div>
            <button class="connexion" type="submit">CONNEXION</button>
            <p class="separateur-ou">ou</p>
            <button class="insciption">S'INSCRIRE</button>
        </form>
    </div>
  `;

const PageConnexion = () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = pageCon;

  let formCon = document.querySelector(".formulaire-connexion")

  const utilisateur = recupUtilisateurDonneesSession()

  if(utilisateur){
    Navbar()
    Redirect("/")
  }else{
    formCon.addEventListener("submit", surConnexion)
  }

}

const surConnexion = (e) => {
  e.preventDefault()

  let utilisateur = {
    pseudo: document.querySelector("#pseudo").value,
    mdp: document.querySelector("#mdp").value
  }

  fetch("/api/utilisateurs/connexion", {
    method:"POST",
    body:JSON.stringify(utilisateur),
    headers:{
      "Content-Type": "application/json",
    }
  })
  .then((reponse) => {
    if(!reponse.ok){
      throw new Error("Error code : " + reponse.status + " : " + reponse.statusText)
    }
    return reponse.json();
  })
  .then((donnee) => surConUtilisateur(donnee))
  .catch(err => surErreur(err))

}

const surConUtilisateur = (donnee) => {
  const utilisateur = {...donnee, is}
  creationDonneeSessionUtilisateur(utilisateur)
  Navbar()
  Redirect("/")
}

const surErreur = (err) => {
  let messageErreur = document.querySelector("#messageErreur");
  let erreurMessage = "";
  if (err.message.includes("401")) erreurMessage = "Pseudo/mot de passe incorrect.";
  else erreurMessage = err.message;
  messageErreur.innerText = erreurMessage;
}
export default PageConnexion
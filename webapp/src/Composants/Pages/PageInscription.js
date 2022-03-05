import {
  creationDonneeSessionUtilisateur,
  recupUtilisateurDonneesSession
} from "../../utilitaires/session";
import Navbar from "../Navbar/Navbar";
import {Redirect} from "../Router/Router";

let inscription = `
<div class="page-inscription">
  <h2>Inscription</h2>
  <form class="ui form" id="formulaire-inscription">
    <div class="field">
      <label for="pseudo">Pseudo <span>*</span></label>
      <div class="pseudo-conteneur">
        <input type="text" id="pseudo">
        <p class="message-erreur erreur-pseudo"></p>
      </div>
    </div>
    <div class="field">
      <label for="nom">Nom <span>*</span></label>
      <div class="nom-conteneur">
        <input type="text" id="nom">
      </div>
    </div>
    <div class="field">
      <label for="prenom">Prenom <span>*</span></label>
      <div class="prenom-conteneur">
        <input type="text" id="prenom">
      </div>
    </div>
    <div class="field">
      <label for="mdp">Mot de passe <span>*</span></label>
      <div class="mdp-conteneur">
        <input type="password" id="mdp">
      </div>
    </div>
    
    <h3>Adresse</h3>
    <div class="field">
      <label for="rue">Rue <span>*</span></label>
      <div class="rue-conteneur">
        <input type="text" id="rue">
      </div>
    </div>
    
    <div class="field">
      <div class="two fields">
        <div class="field">
          <label for="numero">Numero <span>*</span></label>
          <div class="numero-conteneur">
            <input type="number" id="numero">
          </div>
        </div>
        <div class="field">
          <label for="boite">Boite</label>
          <div class="boite-conteneur">
            <input type="number" id="boite">
          </div>
        </div>
      </div>
    </div>
    
    <div class="field">
      <label for="code-postal">Code postal <span>*</span></label>
      <div class="code-postal-conteneur">
        <input type="number" id="code-postal">
      </div>
    </div>
    <div class="field">
      <label for="commune">Commune <span>*</span></label>
      <div class="commune-conteneur">
        <input type="text" id="commune">
      </div>
    </div>
    <p class="obligatoire">* champ obligatoire</p>
    <div id="messageErreur" class="message-erreur"></div>
    <div class="field">
      <button class="ui button inverted secondary">S'inscrire</button>
    </div>
  </form>
  <p class="separateur-ou">ou</p>
   <a href="/connexion">
     <button class="ui button inverted secondary">Connexion</button>
   </a>
  
</div>
`

const PageInscription = () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = inscription;

  let form = document.querySelector("#formulaire-inscription");
  const utilisateur = recupUtilisateurDonneesSession()
  if (utilisateur) {
    Navbar()
    Redirect("/")
  } else {
    form.addEventListener("submit", surInscription)
  }

}

const surInscription = (e) => {
  e.preventDefault()

  let nouvelUtilisateur = {
    pseudo: document.querySelector("#pseudo").value,
    nom: document.querySelector("#nom").value,
    prenom: document.querySelector("#prenom").value,
    mdp: document.querySelector("#mdp").value,
    rue: document.querySelector("#rue").value,
    numero: document.querySelector("#numero").value,
    boite: document.querySelector("#boite").value,
    code_postal: document.querySelector("#code-postal").value,
    commune: document.querySelector("#commune").value
  }

  fetch("/api/utilisateurs/inscription", {
    method: "POST",
    body: JSON.stringify(nouvelUtilisateur),
    headers: {
      "Content-Type": "application/json",
    }
  })
  .then((response) => {
    if(!response.ok){
      throw new Error("Error code : " + response.status + " : " + response.statusText)
    }
    console.log(response)
    return response.json()
  })
  .then((donnee) => surInscrUtilisateur(donnee))
  .catch(err => surErreur(err))

}

const surInscrUtilisateur = (donnee) => {
  const utilisateur = {...donnee, isAutenticated: true}
  creationDonneeSessionUtilisateur(utilisateur, false)
  Redirect("/")
}

const surErreur = (err) => {
  let messageErreur = document.querySelector("#messageErreur");
  let erreurMessage = "";
  console.log(err)
  /*if (err.message.includes(
      "401") || err.message.includes(
      "500")) {
    erreurMessage = "(Faut vérifier les status d'erreur)";
  } else {*/
    erreurMessage = err.message;
  //}
  messageErreur.innerText = erreurMessage;
}

export default PageInscription;
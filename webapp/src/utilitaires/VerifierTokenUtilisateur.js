import Navbar from "../Composants/Navbar/Navbar";
import {API_URL} from "./serveur";
import {recupUtilisateurDonneesSession, enleverDonneeSession} from "./session";

let tokenLocal = null

const VerifierTokenUtilisateur = (id, itemLocal) => {

  tokenLocal = itemLocal
  fetch(API_URL+"utilisateurs/moi",{
    method:"GET",
    headers : {
      "Content-Type":"application/json",
      Authorization:id
    }
  })
  .then((reponse) => {
    if(!reponse.ok){
      throw new Error(
          "Error code : " + reponse.status + " : " + reponse.statusText
      );
    }
    return reponse.json()
  })
  .then((donnee) => surUtilisateurTrouve(donnee))
  .catch(err => surErreur(err))

}
const surUtilisateurTrouve = (donneeUtilisateur) => {
  const utilisateur = {...donneeUtilisateur, isAutenticated:true}
  recupUtilisateurDonneesSession(utilisateur, tokenLocal)
  Navbar()
}

const surErreur = (err) => {
  let messageErreur = document.querySelector("#messageErreur");
  let erreurMessage = "";
  console.log(err)
  if (err.message.includes(
      "401")) {
    erreurMessage = "Pseudo ou mot de passe incorrect";
  } else {
    erreurMessage = err.message;
  }
  messageErreur.innerText = erreurMessage;
}


export default VerifierTokenUtilisateur;
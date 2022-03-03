import {
  enleverDonneeSession,
  recupUtilisateurDonneesSession
} from "../../utilitaires/session";
import Navbar from "../Navbar/Navbar";

const PageAccueil = () => {
  const pageDiv = document.querySelector("#page");
  const utilisateur = recupUtilisateurDonneesSession()
  let etatInscription
  let commetaire
  if(utilisateur){
    if(utilisateur.utilisateur.etatInscription !== "confirmé"){
      etatInscription = utilisateur.utilisateur.etatInscription
      if(utilisateur.utilisateur.etatInscription === "en attente"){
        commetaire = "Vous pourrez acceder aux fonctionnalités quand un administrateur aura accepté votre inscription"
      }else{
        commetaire = utilisateur.utilisateur.commentaire
      }
    }
  }

  let pageAccueil = `
  <div class="conteneur-modal">
    <div class="overlay declancheur-modal"></div>
    <div class="ui modal active">
      <button class="fermer-modal declancheur-modal">X</button>
      <div class="header">Votre inscription est "${etatInscription}"</div>
      <div class="content">
        <p>${commetaire}</p>
      </div>
    </div>
  </div>
  <div class="offres">
    <h2>Offres récentes</h2>
  </div>
  `


  pageDiv.innerHTML = pageAccueil;
  const conteneurModal = document.querySelector(".conteneur-modal")
  const declancheurModal = document.querySelectorAll(".declancheur-modal")

  if(utilisateur){
    if(utilisateur.utilisateur.etatInscription !== "confirmé"){
      conteneurModal.classList.add('active')
      enleverDonneeSession()
      Navbar()
    }
  }

  declancheurModal.forEach(decl => decl.addEventListener("click", () => {
    conteneurModal.classList.toggle("active")
  }))
};

export default PageAccueil;

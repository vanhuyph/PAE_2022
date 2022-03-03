import {
  enleverDonneeSession,
  recupUtilisateurDonneesSession
} from "../../utilitaires/session";
import Navbar from "../Navbar/Navbar";

const PageAccueil = () => {
  const pageDiv = document.querySelector("#page");
  const utilisateur = recupUtilisateurDonneesSession()
  let etatInscription
  let commentaire
  if (utilisateur) {
    if (utilisateur.utilisateur.etatInscription !== "confirmé") {
      etatInscription = utilisateur.utilisateur.etatInscription
      if (utilisateur.utilisateur.etatInscription === "en attente") {
        commentaire = "Vous pourrez accéder aux fonctionnalités lorsqu'un administrateur aura confirmé votre inscription."
      } else {
        commentaire = utilisateur.utilisateur.commentaire
      }
    }
  }

  let pageAccueil = `
  <div class="conteneur-modal">
    <div class="overlay declencheur-modal"></div>
    <div class="ui modal active">
      <button class="fermer-modal declencheur-modal">X</button>
      <div class="header">Votre inscription est "${etatInscription}"</div>
      <div class="content">
        <p>${commentaire}</p>
      </div>
    </div>
  </div>
  <div class="offres">
    <h2>Offres récentes</h2>
  </div>
  `

  pageDiv.innerHTML = pageAccueil;
  const conteneurModal = document.querySelector(".conteneur-modal")
  const declencheurModal = document.querySelectorAll(".declencheur-modal")

  if (utilisateur) {
    if (utilisateur.utilisateur.etatInscription !== "confirmé") {
      conteneurModal.classList.add('active')
      enleverDonneeSession()
      Navbar()
    }
  }

  declencheurModal.forEach(decl => decl.addEventListener("click", () => {
    conteneurModal.classList.toggle("active")
  }))
};

export default PageAccueil;

import {
  enleverDonneeSession,
  recupUtilisateurDonneesSession
} from "../../utilitaires/session";
import Navbar from "../Navbar/Navbar";

// Page d'accueil
const PageAccueil = () => {
  const pageDiv = document.querySelector("#page");
  const session = recupUtilisateurDonneesSession()
  let etatInscription
  let commentaire
  if (session) {
    if (session.utilisateur.etatInscription !== "confirmé") {
      etatInscription = session.utilisateur.etatInscription
      if (session.utilisateur.etatInscription === "en attente") {
        commentaire = "Vous pourrez accéder aux fonctionnalités lorsqu'un administrateur aura confirmé votre inscription."
      } else {
        commentaire = session.utilisateur.commentaire
      }
    }
  }

  let pageAccueil = `
  <div class="conteneur-modal">
    <div class="overlay declencheur-modal"></div>
    <div class="ui modal active">
      <button class="fermer-modal declencheur-modal">X</button>
      <div class="header">Statut de votre inscription : "${etatInscription}"</div>
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
  Navbar()
  const conteneurModal = document.querySelector(".conteneur-modal")
  const declencheurModal = document.querySelectorAll(".declencheur-modal")

  if (session) {
    if (session.utilisateur.etatInscription !== "confirmé") {
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

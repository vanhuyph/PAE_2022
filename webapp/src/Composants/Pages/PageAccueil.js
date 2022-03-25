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
    if (session.utilisateur.etatInscription !== "Confirmé") {
      etatInscription = session.utilisateur.etatInscription
      if (session.utilisateur.etatInscription === "En attente") {
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
    <div id="offreListRecent"> </div>
  </div>
  `;

  pageDiv.innerHTML = pageAccueil;
  Navbar()
  const conteneurModal = document.querySelector(".conteneur-modal")
  const declencheurModal = document.querySelectorAll(".declencheur-modal")

  // Récupération de la liste des offres récentes
  fetch("/api/offres/listerOffresRecentes", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  }).then((response) => {
    if (!response.ok) {
      throw new Error(
          "Error code : " + response.status + " : " + response.statusText);
    }
    return response.json();
  }).then((data) => onOffreRecentListpage(data))
  .catch((err) => onError(err));

  if (session) {
    if (session.utilisateur.etatInscription !== "Confirmé") {
      conteneurModal.classList.add('active')
      enleverDonneeSession()
      Navbar()
    }else{
      pageAccueil += `
      <div class="offres">
        <h2>Toutes les offres</h2>
        <div id="offreList"> </div>
      </div>
      
      `;
        pageDiv.innerHTML = pageAccueil;
        fetch("/api/offres/listerOffres", {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: session.token
          },
        }).then((response) => {
          if (!response.ok) {
            throw new Error(
                "Error code : " + response.status + " : " + response.statusText
            );
          }
          return response.json();
        }).then((data) => onOffreListpage(data))
        .catch((err) => onError(err));
    }
  }

  declencheurModal.forEach(decl => decl.addEventListener("click", () => {
    conteneurModal.classList.toggle("active")
  }))
};
//check la taille des images
const onOffreRecentListpage = (data) => {
  const listOffreRecent = document.getElementById("offreListRecent");
  const session = recupUtilisateurDonneesSession()
  let listRecent = `<div class="ui cards">`;
  data.forEach((offre) => {
    if (session) {
      listRecent += `
      <a class="card">
        <div class="image">
          <img src="/api/offres/photos/${offre.objetDTO.photo}">
        </div>
        <div class="content">
          <h4 class="header">Description</h4>
           <p>${offre.objetDTO.description}</p>
        </div>
      </a>
    `;
    }else{
      listRecent += `
      <div class="card">
        <div class="image">
          <img src="/api/offres/photos/${offre.objetDTO.photo}">
        </div>
        <div class="content">
          <h4 class="header">Description</h4>
           <p>${offre.objetDTO.description}</p>
        </div>
      </div>
    `;
    }
  })
  listRecent += `</div>`;
  listOffreRecent.innerHTML = listRecent;
};

const onOffreListpage = (data) => {
  const listOffre = document.getElementById("offreList");
  let list = `<div class="ui link cards">`;
  data.forEach((offre) => {
    list += `
      <a class="card">
        <div class="image">
          <img src="/api/offres/photos/${offre.objetDTO.photo}">
        </div>
        <div class="content">
          <h4 class="header">Description</h4>
           <p>${offre.objetDTO.description}</p>
        </div>
      </a>`;
  })
  list += `</div>`;
  listOffre.innerHTML = list;
};

export default PageAccueil;

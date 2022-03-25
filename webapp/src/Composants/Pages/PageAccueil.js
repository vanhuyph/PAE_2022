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
    <div id="liste-offres-recentes"> </div>
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
          "Code d'erreur : " + response.status + " : " + response.statusText);
    }
    return response.json();
  }).then((data) => surListeOffresRecentes(data))

  if (session) {
    if (session.utilisateur.etatInscription !== "Confirmé") {
      conteneurModal.classList.add('active')
      enleverDonneeSession()
      Navbar()
    } else {
      pageAccueil += `
      <div class="offres">
        <h2>Toutes les offres</h2>
        <div id="liste-offres"> </div>
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
              "Code d'erreur : " + response.status + " : " + response.statusText
          );
        }
        return response.json();
      }).then((data) => surListeOffres(data))
    }
  }
  declencheurModal.forEach(decl => decl.addEventListener("click", () => {
    conteneurModal.classList.toggle("active")
  }))
};

// Affichage d'une liste des offres les plus récentes
const surListeOffresRecentes = (data) => {
  const listeOffresRecentes = document.getElementById("liste-offres-recentes");
  const session = recupUtilisateurDonneesSession()
  let listeRecente = `<div class="ui cards">`;
  data.forEach((offre) => {
    if (session) {
      listeRecente += `
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
    } else {
      listeRecente += `
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
  listeRecente += `</div>`;
  listeOffresRecentes.innerHTML = listeRecente;
};

// Affichage d'une liste de toutes les offres
const surListeOffres = (data) => {
  const listeOffres = document.getElementById("liste-offres");
  let liste = `<div class="ui link cards">`;
  data.forEach((offre) => {
    liste += `
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
  liste += `</div>`;
  listeOffres.innerHTML = liste;
};

export default PageAccueil;

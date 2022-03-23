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
  <div id="offreListRecent"> </div>
  <br>`;




 
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

  fetch("/api/offres/listOffresRecent", {
    method: "GET",
    headers:{
      "Content-Type": "application/json",
    },
  }).then((response)=>{
    if (!response.ok)
    throw new Error(
      "Error code : " + response.status + " : " + response.statusText
    );
    return response.json();
  }).then((data)=>onOffreRecentListpage(data))
    .catch((err) => onError(err));
  
  if(utilisateur){
    pageAccueil +=  `
    <div class="offres">
      <h2>Toutes les offres</h2>
    </div>
    <div id="offreList"> </div>
    <br>`;
    pageDiv.innerHTML = pageAccueil;
    fetch("/api/offres/listOffres", {
      method: "GET",
      headers:{
        "Content-Type": "application/json",
        Authorization : utilisateur.token      
      },
    }).then((response)=>{
      if (!response.ok)
      throw new Error(
        "Error code : " + response.status + " : " + response.statusText
      );
      return response.json();
    }).then((data)=>onOffreListpage(data))
      .catch((err) => onError(err));
  }
};

const onOffreRecentListpage = (data) =>{
  const listOffreRecent = document.getElementById("offreListRecent");
  let listRecent = ``;
  data.forEach((offre)=>{
    const date = `${offre.dateOffre[2]}\\${offre.dateOffre[1]}\\${offre.dateOffre[0]}`
    listRecent += `<article>
      <h4>${offre.objetDTO.photo}</h4>
      Description : ${offre.objetDTO.description}
      Date de création : ${date}
      </article>`;
  })

  listOffreRecent.innerHTML = listRecent;
};

const onOffreListpage = (data)=>{
  const listOffre = document.getElementById("offreList");
  let list = ``;
  data.forEach((offre)=>{
    const date = `${offre.dateOffre[2]}\\${offre.dateOffre[1]}\\${offre.dateOffre[0]}`
    list += `<article>
      <h4>${offre.objetDTO.photo}</h4>
      Description : ${offre.objetDTO.description}
      Date de création : ${date}
      </article>`;
  })
  listOffre.innerHTML = list;
};

export default PageAccueil;

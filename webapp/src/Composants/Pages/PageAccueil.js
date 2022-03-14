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
      <div class="header">Statut de votre inscription : "${etatInscription}"</div>
      <div class="content">
        <p>${commentaire}</p>
      </div>
    </div>
  </div>
  <div class="offres">
    <h2>Offres récentes</h2>
  </div>
  <div id="offreListRecent"></div>
  <br>
  <div id ="offreList"></div>
  `;




 
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
    fetch("/api/offres/listOffres", {
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
    }).then((data)=>onOffreListpage(data))
      .catch((err) => onError(err));
  }
};

const onOffreRecentListpage = (data) =>{
  const listOffreRecent = document.getElementById("offreListRecent");
  const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
  let listRecent = data.forEach((offre)=>{
    console.log(offre.idOffre);
    console.log(offre.photo);
    console.log(offre.objetDTO.description);
    console.log(offre.dateOffre.toLocaleTimeString());
    listRecent+= `<article>
      <h4>${offre.objetDTO.photo}</h4>
      Description : ${offre.objetDTO.description}
      Date de création : ${offre.dateOffre.toLocaleTimeString()}
      </article>
      `;
  })

  listOffreRecent.innerHTML = listRecent;
};

const onOffreListpage=(data)=>{
  const listOffre = document.getElementById("offreList");
  const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };

  let list = data.forEach((offre)=>{
    console.log(offre.idOffre);
    console.log(offre.photo);
    console.log(offre.objetDTO.description);
    console.log(offre.dateOffre.toLocaleTimeString());
    list+= `<article>
      <h4>${offre.objetDTO.photo}</h4>
      Description : ${offre.objetDTO.description}
      Date de création : ${offre.dateOffre.toLocaleTimeString()}
      </article>
      `;
  })
  listOffre.innerHTML = list;
};

export default PageAccueil;

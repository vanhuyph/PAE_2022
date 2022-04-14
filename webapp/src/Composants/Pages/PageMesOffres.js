import {
    enleverDonneeSession,
    recupUtilisateurDonneesSession
  } from "../../utilitaires/session";
  import Navbar from "../Navbar/Navbar";
  import {Redirect} from "../Router/Router";


  const PageMesOffres = () =>{
    const pageDiv = document.querySelector("#page");
    const session = recupUtilisateurDonneesSession();
    let pageMesOffres = `<div class="offres">
    <h2>Mes offres</h2>
    <div id="mesOffres"></div>
    <h2>Mes offres avec receveur</h2>
    <div id="mesOffresReceveur"></div>
    <h2>Mes offres annulées</h2>
    <div id="mesOffresAnnulees"></div>
  </div>`;
  pageDiv.innerHTML = pageMesOffres;
  const id = session.utilisateur.idUtilisateur;
  fetch("/api/offres/mesOffres/" + id, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      "Authorization": session.token,
    },
  }).then((response) => {
    if (!response.ok) {
      throw new Error(
          "Code d'erreur : " + response.status + " : " + response.statusText);
    }
    return response.json();
  }).then((data) => surListeMesOffres(data))


  };

  const surListeMesOffres = (data) =>{
      const mesOffres = document.getElementById("mesOffres");
      const mesOffresReceveur = document.getElementById("mesOffresReceveur");
      const mesOffresAnnulees = document.getElementById("mesOffresAnnulees");
      let listeMesOffres = `<div class="ui cards">`;
      let listeMesOffresConfirmer = `<div class="ui cards">`;
      let listeMesOffresAnnuler = `<div class="ui cards">`;
      data.forEach((offre) => {
          if(offre.objetDTO.etatObjet === "Offert" || offre.objetDTO.etatObjet === "Intéressé"){
            listeMesOffres +=`<div id="of" class="card" data-of="${offre.idOffre}">
            <div id=${offre.idOffre}></div>
            <div class="image">
              <img src="/api/offres/photos/${offre.objetDTO.photo}">
            </div>
            <div class="content">
              <h4 class="header">Description</h4>
               <p>${offre.objetDTO.description}</p>
            </div>
            <div class="ui two buttons">
            <button id="interessees" class="ui primary button">Voir les personnes intéressées</button>
            <button id="annuler-offre" class="ui basic red button">Annuler offre</button>
            </div>
          </div>`;
          } else if(offre.objetDTO.etatObjet === "Confirmé"){
            listeMesOffresConfirmer += `<a id="of" class="card" data-of="${offre.idOffre}">
            <div id=${offre.idOffre}></div>
            <div class="image">
              <img src="/api/offres/photos/${offre.objetDTO.photo}">
            </div>
            <div class="content">
              <h4 class="header">Description</h4>
               <p>${offre.objetDTO.description}</p>
            </div>
            <button id="objetRemis" class="ui green button" >Objet remis</button>
            <button id="nonRemis" class="ui primary button" >Non remis</button>
            <button id="annuler-offre" class="ui basic red button">Annuler offre</button>
          </a>`;
          } else if(offre.objetDTO.etatObjet === "Annulé"){
            listeMesOffresAnnuler += `<a id="of" class="card" data-of="${offre.idOffre}">
            <div id=${offre.idOffre}></div>
            <div class="image">
              <img src="/api/offres/photos/${offre.objetDTO.photo}">
            </div>
            <div class="content">
              <h4 class="header">Description</h4>
               <p>${offre.objetDTO.description}</p>
            </div>
            <button id="offrir" class="ui green button" >Offrir l'objet</button>
          </a>`;
          }
      })

     
      listeMesOffres += `</div>`;
      listeMesOffresConfirmer += `</div>`;
      listeMesOffresAnnuler += `</div>`;
      mesOffres.innerHTML = listeMesOffres;
      mesOffresReceveur.innerHTML = listeMesOffresConfirmer;
      mesOffresAnnulees.innerHTML = listeMesOffresAnnuler;
      document.querySelectorAll("#annuler-offre").forEach(offre => {
      offre.addEventListener("click", () =>{
        fetch("/api/offres/annulerOffre", {
            method: "PUT",
            body:JSON.stringify(data),
            headers: {
              "Content-Type": "application/json",
              "Authorization": session.token,
            },
          })
          .then((response) => {
            if (!response.ok) {
              throw new Error(
                  "Code d'erreur : " + response.status + " : " + response.statusText
              );
            }
            return response.json();
          })
          .then(() => Redirect("/mesOffres"))
        })
      })
  };
  export default PageMesOffres;
import {
    enleverDonneeSession,
    recupUtilisateurDonneesSession
  } from "../../utilitaires/session";
  import Navbar from "../Navbar/Navbar";
  import {Redirect} from "../Router/Router";
import {API_URL} from "../../utilitaires/serveur";
import Swal from "sweetalert2";


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
    let session = recupUtilisateurDonneesSession()
      const mesOffres = document.getElementById("mesOffres");
      const mesOffresReceveur = document.getElementById("mesOffresReceveur");
      const mesOffresAnnulees = document.getElementById("mesOffresAnnulees");
      let listeMesOffres = `<div class="mes-offres">`;
      let listeMesOffresConfirmer = `<div class="mes-offres-conf">`;
      let listeMesOffresAnnuler = `<div class="mes-offres-ann">`;
      data.forEach((offre) => {
          if(offre.objetDTO.etatObjet === "Offert" || offre.objetDTO.etatObjet === "Intéressé"){
            listeMesOffres +=`
            <div class="mon-offre">
            <div>
              <input id="id-offre" type="hidden" value="${offre.idOffre}">
              <input id="id-objet" type="hidden" value="${offre.objetDTO.idObjet}">
              <input id="etat-objet" type="hidden" value="${offre.objetDTO.etatObjet}">
              <input id="type-objet" type="hidden" value="${offre.objetDTO.typeObjet.idType}">
              <input id="desc-objet" type="hidden" value="${offre.objetDTO.description}">
              <input id="offreur-objet" type="hidden" value="${offre.objetDTO.offreur.idUtilisateur}">
              <input id="photo-objet" type="hidden" value="${offre.objetDTO.photo}">
              <input id="version-objet" type="hidden" value="${offre.objetDTO.version}">
              <input id="vue-objet" type="hidden" value="${offre.objetDTO.vue}">
            </div>
              <div class="ui three column grid">
                <div class="row">
                  <div class="column">
                    <img src="/api/offres/photos/${offre.objetDTO.photo}" height="170px">
                  </div>
                  <div class="column descri">
                    <p>${offre.objetDTO.description}</p>
                  </div>
                  <div class="column actions">
                    <button id="liste-interessees" class="ui primary button">Voir les personnes intéressées</button>
                    <button id="annuler-offre" class="ui basic red button">Annuler offre</button>
                  </div>
                </div>
              </div>
            </div>`;
          } else if(offre.objetDTO.etatObjet === "Confirmé"){
            listeMesOffresConfirmer += `
            <div class="mon-offre">
            <div>
              <input id="id-offre" type="hidden" value="${offre.idOffre}">
              <input id="id-objet" type="hidden" value="${offre.objetDTO.idObjet}">
              <input id="etat-objet" type="hidden" value="${offre.objetDTO.etatObjet}">
              <input id="type-objet" type="hidden" value="${offre.objetDTO.typeObjet.idType}">
              <input id="desc-objet" type="hidden" value="${offre.objetDTO.description}">
              <input id="offreur-objet" type="hidden" value="${offre.objetDTO.offreur.idUtilisateur}">
              <input id="photo-objet" type="hidden" value="${offre.objetDTO.photo}">
              <input id="version-objet" type="hidden" value="${offre.objetDTO.version}">
              <input id="vue-objet" type="hidden" value="${offre.objetDTO.vue}">
            </div>
              <div class="ui three column grid">
                <div class="row">
                  <div class="column">
                    <img src="/api/offres/photos/${offre.objetDTO.photo}" height="170px">
                  </div>
                  <div class="column descri">
                     <p>${offre.objetDTO.description}</p>
                  </div>
                  <div class="column actions">
                    <button id="objetRemis" class="ui green button" >Objet remis</button>
                    <button id="nonRemis" class="ui yellow button" >Non remis</button>
                    <button id="annuler-offre" class="ui basic red button">Annuler offre</button>
                  </div>
                </div>
              </div>
            </div>
            `;
          } else if(offre.objetDTO.etatObjet === "Annulé"){
            listeMesOffresAnnuler += ` 
              <div class="mon-offre">
              <div>
              <input id="id-offre" type="hidden" value="${offre.idOffre}">
              <input id="id-objet" type="hidden" value="${offre.objetDTO.idObjet}">
              <input id="etat-objet" type="hidden" value="${offre.objetDTO.etatObjet}">
              <input id="type-objet" type="hidden" value="${offre.objetDTO.typeObjet.idType}">
              <input id="desc-objet" type="hidden" value="${offre.objetDTO.description}">
              <input id="offreur-objet" type="hidden" value="${offre.objetDTO.offreur.idUtilisateur}">
              <input id="photo-objet" type="hidden" value="${offre.objetDTO.photo}">
              <input id="version-objet" type="hidden" value="${offre.objetDTO.version}">
              <input id="vue-objet" type="hidden" value="${offre.objetDTO.vue}">
            </div>
                <div class="ui three column grid">
                  <div class="row">
                    <div class="column">
                      <img src="/api/offres/photos/${offre.objetDTO.photo}" height="170px">
                    </div>
                    <div class="column descri">
                       <p>${offre.objetDTO.description}</p>
                    </div>
                    <div class="column actions">
                      <button id="offrir" class="ui green button" >Offrir l'objet</button>
                    </div>
                  </div>
                </div>
              </div>`;
          }
      })
      listeMesOffres += `</div>`;
      listeMesOffresConfirmer += `</div>`;
      listeMesOffresAnnuler += `</div>`;
      mesOffres.innerHTML = listeMesOffres;
      mesOffresReceveur.innerHTML = listeMesOffresConfirmer;
      mesOffresAnnulees.innerHTML = listeMesOffresAnnuler;

      document.querySelectorAll(".mon-offre").forEach(offre => {
        let idObj = document.getElementById("id-objet").value
        let etatObjet = document.getElementById("etat-objet").value
        let type = document.getElementById("type-objet").value
        let desc = document.getElementById("desc-objet").value
        let offreur = document.getElementById("offreur-objet").value
        let photo = document.getElementById("photo-objet").value
        let version = document.getElementById("version-objet").value
        let vue = document.getElementById("vue-objet").value
        let of = {
          idOffre: document.getElementById("id-offre").value,
          objetDTO:{
            idObjet: idObj,
            etatObjet : etatObjet,
            typeObjet : {idType: type},
            description : desc,
            offreur: {idUtilisateur: offreur},
            photo : photo,
            version:version,
            vue:vue
          }
        }



        offre.querySelector("#liste-interessees").addEventListener("click", () => {
          fetch(API_URL+"interets/listeDesPersonnesInteressees/"+idObj, {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
              Authorization: session.token,
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
          .then((donnees) => {
            let listeInteret = `<div class="interets">`;
            donnees.forEach((interet) => {
              listeInteret += `
                <div class="interet">
                  <input type="hidden" id="ut" value="${interet.utilisateur.idUtilisateur}">
                  <input type="hidden" id="version" value="${interet.version}">
                  <p>${interet.utilisateur.nom}</p>
                  <p>${interet.utilisateur.prenom}</p>
                  <p>${interet.utilisateur.pseudo}</p>
                  <p>Horaire: ${interet.dateRdv}</p>
                </div>`
            })
            listeInteret += `</div>`

            let intReceveur = {
              objet: {idObjet: idObj}
            }
            Swal.fire({
              title: `<strong>Liste d'intérêts</strong>`,
              html:`${listeInteret}`,
              showCloseButton: true,
              showCancelButton: true,
              focusConfirm: false,
              confirmButtonText:
                  'Soumettre',
              confirmButtonAriaLabel: 'Thumbs up, great!',
              cancelButtonText:
                  'Retour',
              cancelButtonAriaLabel: 'Thumbs down'
            }).then((r) => {
              console.log(intReceveur)
              fetch(API_URL+"interets/indiquerReceveur", {
                method: "PUT",
                body: JSON.stringify(intReceveur),
                headers: {
                  "Content-Type": "application/json",
                  "Authorization": session.token,
                },
              }).then((reponse) => {
                if (!reponse.ok) {
                  throw new Error(
                      "Code d'erreur : " + reponse.status + " : " + reponse.statusText);
                }
                return reponse.json();
              }).then((donnee) => {

                Redirect("/mesOffres")
              })
            })
            document.querySelectorAll(".interet").forEach((inte) => {
              inte.addEventListener("click", () => {
                let retirer = document.querySelector(".choisi")
                if(retirer) {
                  retirer.classList.remove("choisi")
                }
                inte.classList.add("choisi")
                intReceveur = {
                  ...intReceveur,
                  utilisateur: {idUtilisateur: inte.querySelector("#ut").value},
                  version: inte.querySelector("#version").value,
                }
              })
            })
          })
        })

        offre.querySelector("#annuler-offre").addEventListener("click", () =>{
          fetch(API_URL+"offres/annulerOffre", {
            method: "PUT",
            body:JSON.stringify(of),
            headers: {
              "Content-Type": "application/json",
              Authorization: session.token,
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
          .then(() => {

            Redirect("/mesOffres")
          })
        })
      })
  };
  export default PageMesOffres;
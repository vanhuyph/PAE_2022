import {recupUtilisateurDonneesSession} from "../../utilitaires/session";
import {Redirect} from "../Router/Router";
import {API_URL} from "../../utilitaires/serveur";
import Swal from "sweetalert2";


  const PageMesOffres = () =>{
    const pageDiv = document.querySelector("#page");
    const session = recupUtilisateurDonneesSession();
  pageDiv.innerHTML = `<div class="offres">
    <h2>Mes offres</h2>
    <div id="mesOffres"></div>
    <h2>Mes offres avec receveur</h2>
    <div id="mesOffresReceveur"></div>
    <h2>Mes offres annulées</h2>
    <div id="mesOffresAnnulees"></div>
  </div>`;
  const id = session.utilisateur.idUtilisateur;
  fetch("/api/offres/mesOffres/" + id, {
    method: "GET",
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
  }).then((data) => surListeMesOffres(data))
};

// Affichage des listes d'offres, des listes d'offres avec receveur et des listes d'offres annulées
const surListeMesOffres = (data) => {
  let session = recupUtilisateurDonneesSession()
  const mesOffres = document.getElementById("mesOffres");
  const mesOffresReceveur = document.getElementById("mesOffresReceveur");
  const mesOffresAnnulees = document.getElementById("mesOffresAnnulees");
  let listeMesOffres = `<div class="mes-offres">`;
  let listeMesOffresConfirmees = `<div class="mes-offres-conf">`;
  let listeMesOffresAnnulees = `<div class="mes-offres-ann">`;
  data.forEach((offre) => {
    if (offre.objetDTO.etatObjet === "Offert" || offre.objetDTO.etatObjet
        === "Intéressé") {
      listeMesOffres += `
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
                    <button id="annuler-offre" class="ui basic red button">Annuler mon offre</button>
                  </div>
                </div>
              </div>
            </div>`;
    } else if (offre.objetDTO.etatObjet === "Confirmé") {
      listeMesOffresConfirmees += `
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
                    <button id="annuler-offre" class="ui basic red button">Annuler mon offre</button>
                  </div>
                </div>
              </div>
            </div>
            `;
    } else if (offre.objetDTO.etatObjet === "Annulé") {
      listeMesOffresAnnulees += ` 
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
              <input id="plage-horaire-offre" type="hidden" value="${offre.plageHoraire}">
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
                      <button id="offrir" class="ui green button" >Offrir à nouveau</button>
                    </div>
                  </div>
                </div>
              </div>`;
    }
  })
  listeMesOffres += `</div>`;
  listeMesOffresConfirmees += `</div>`;
  listeMesOffresAnnulees += `</div>`;
  mesOffres.innerHTML = listeMesOffres;
  mesOffresReceveur.innerHTML = listeMesOffresConfirmees;
  mesOffresAnnulees.innerHTML = listeMesOffresAnnulees;

  document.querySelectorAll(".mon-offre").forEach(offre => {
    let idObj = offre.querySelector("#id-objet").value
    let etatObjet = offre.querySelector("#etat-objet").value
    let type = offre.querySelector("#type-objet").value
    let desc = offre.querySelector("#desc-objet").value
    let offreur = offre.querySelector("#offreur-objet").value
    let photo = offre.querySelector("#photo-objet").value
    let version = offre.querySelector("#version-objet").value
    let vue = offre.querySelector("#vue-objet").value
    let plageHoraire;
    if(offre.querySelector("#plage-horaire-offre")){
      plageHoraire = offre.querySelector("#plage-horaire-offre").value;
    }

    let offreurObjet = {
      idUtilisateur: offreur,
    }

    let objet = {
      idObjet: idObj,
      etatObjet : etatObjet,
      typeObjet : {idType: type},
      description : desc,
      offreur: offreurObjet,
      photo : photo,
      version:version,
      vue:vue
    }

    let of = {
      idOffre: offre.querySelector("#id-offre").value,
      objetDTO: objet,
    }

    if(plageHoraire){
      of = {
        ...of,
        plageHoraire: plageHoraire
      }
    }

    let reoffrir = offre.querySelector("#offrir");
    if(reoffrir){
      reoffrir.addEventListener("click", () => {
        reoffrirObjet(of)
      })
    }

    let offreListeInteressees = offre.querySelector("#liste-interessees");
    if (offreListeInteressees) {
      offreListeInteressees.addEventListener("click", () => {
        listeInteresse(objet)
      })
    }

    let offreAnnulee = offre.querySelector("#annuler-offre");
    if (offreAnnulee) {
        offreAnnulee.addEventListener("click", () => {
          annulerOffre(of)
        })
    }

    let offreNonRemis = offre.querySelector("#nonRemis")
    if (offreNonRemis) {
      offreNonRemis.addEventListener("click", () => {
        Swal.fire({
          title: `<strong>Objet non remis</strong>`,
          html: `
            <button id="liste-interessees-non-remis" class="ui primary button">Choisir un nouveau receveur</button> 
            <button id="offrir-non-remis" class="ui green button" >Offrir à nouveau l'objet</button> 
            <button id="annuler-offre-non-remis" class="ui basic red button">Annuler mon offre</button>
          `,
          showConfirmButton: false,
        })
        let listeInteresseNR = document.querySelector(
            "#liste-interessees-non-remis")
        console.log(listeInteresseNR)
        if (listeInteresseNR) {
          listeInteresseNR.addEventListener("click", () => {
            listeInteresseNonRemis(objet)
          })
        }

        let annulerOffreNonRemis = document.querySelector(
            "#annuler-offre-non-remis")
        if (annulerOffreNonRemis) {
          annulerOffreNonRemis.addEventListener("click", () => {
            nonRemis(objet)
            annulerOffre(of)
          })
        }
        let reoffrirNonRemis = document.querySelector("#offrir-non-remis");
        if(reoffrirNonRemis){
          reoffrirNonRemis.addEventListener("click", () => {
            nonRemis(objet)
            reoffrirObjet(of)
          })
        }
      })
    }
  })
};

const reoffrirObjet = (offre) => {
  console.log(offre)
  let session = recupUtilisateurDonneesSession()
  fetch(API_URL+ "offres/reoffrirObjet", {
    method: "POST",
    body: JSON.stringify(offre),
    headers: {
      "Content-Type": "application/json",
      Authorization: session.token,
    },
  })
  .then((reponse) => {
    if (!reponse.ok) {
      throw new Error(
          "Code d'erreur : " + reponse.status + " : "
          + reponse.statusText
      );
    }
    return reponse.json();
  })
  .then((donnee) => {
    Redirect("/mesOffres")
  })
}

const nonRemis = (objet) => {
  let session = recupUtilisateurDonneesSession()
  fetch(API_URL + "interets/nonRemis/" + objet.idObjet, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: session.token,
    },
  })
  .then((reponse) => {
    if (!reponse.ok) {
      throw new Error(
          "Code d'erreur : " + reponse.status + " : "
          + reponse.statusText
      );
    }
    return reponse.json();
  })
  .then((donnee) => {
  })
}

const annulerOffre = (of) => {
  let session = recupUtilisateurDonneesSession()
  fetch(API_URL + "offres/annulerOffre", {
    method: "PUT",
    body: JSON.stringify(of),
    headers: {
      "Content-Type": "application/json",
      Authorization: session.token,
    },
  })
  .then((reponse) => {
    if (!reponse.ok) {
      throw new Error(
          "Code d'erreur : " + reponse.status + " : "
          + reponse.statusText
      );
    }
    return reponse.json();
  })
  .then(() => {
    Redirect("/mesOffres")
  })
}

const listeInteresse = (objet) => {
  let session = recupUtilisateurDonneesSession()
  fetch(API_URL + "interets/listeDesPersonnesInteressees/" + objet.idObjet, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: session.token,
    },
  })
  .then((reponse) => {
    if (!reponse.ok) {
      throw new Error(
          "Code d'erreur : " + reponse.status + " : "
          + reponse.statusText
      );
    }
    return reponse.json();
  })
  .then((donnees) => {
    let listeInteret = `<div class="interets">`;
    donnees.forEach((interet) => {
      let date = new Date(interet.dateRdv).toLocaleDateString("fr-BE")
      listeInteret += `
              <div class="interet">
                <input type="hidden" id="ut" value="${interet.utilisateur.idUtilisateur}">
                <input type="hidden" id="version" value="${interet.version}">
                <input type="hidden" id="date-interet" value="${interet.dateRdv}">
                <input type="hidden" id="vue" value="${interet.vue}">
                <p>${interet.utilisateur.nom}</p>
                <p>${interet.utilisateur.prenom}</p>
                <p>${interet.utilisateur.pseudo}</p>
                <p>Horaire :  ${date}</p>
              </div>`
    })
    listeInteret += `</div>`

    let intReceveur = {
      objet: objet
    }
    Swal.fire({
      title: `<strong>Liste d'intérêts</strong>`,
      html: `${listeInteret}`,
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
      if (r.isConfirmed) {
        fetch(API_URL + "interets/indiquerReceveur", {
          method: "PUT",
          body: JSON.stringify(intReceveur),
          headers: {
            "Content-Type": "application/json",
            "Authorization": session.token,
          },
        }).then((reponse) => {
          if (!reponse.ok) {
            throw new Error(
                "Code d'erreur : " + reponse.status + " : "
                + reponse.statusText);
          }
          return reponse.json();
        }).then(() => {

          Redirect("/mesOffres")
        })
      }
    })
    document.querySelectorAll(".interet").forEach((inte) => {
      inte.addEventListener("click", () => {
        let retirer = document.querySelector(".choisi")
        if (retirer) {
          retirer.classList.remove("choisi")
        }
        inte.classList.add("choisi")
        intReceveur = {
          ...intReceveur,
          utilisateur: {
            idUtilisateur: inte.querySelector("#ut").value
          },
          version: inte.querySelector("#version").value,
          dateRdv: inte.querySelector("#date-interet").value,
          vue: inte.querySelector("#vue").value,
        }
      })
    })
  })
}

const listeInteresseNonRemis = (objet) => {
  let session = recupUtilisateurDonneesSession()
  fetch(API_URL + "interets/listeDesPersonnesInteressees/" + objet.idObjet, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: session.token,
    },
  })
  .then((reponse) => {
    if (!reponse.ok) {
      throw new Error(
          "Code d'erreur : " + reponse.status + " : "
          + reponse.statusText
      );
    }
    return reponse.json();
  })
  .then((donnees) => {
    let listeInteret = `<div class="interets">`;
    donnees.forEach((interet) => {
      let date = new Date(interet.dateRdv).toLocaleDateString("fr-BE")
      listeInteret += `
              <div class="interet">
                <input type="hidden" id="ut" value="${interet.utilisateur.idUtilisateur}">
                <input type="hidden" id="version" value="${interet.version}">
                <input type="hidden" id="date-interet" value="${interet.dateRdv}">
                <input type="hidden" id="vue" value="${interet.vue}">
                <p>${interet.utilisateur.nom}</p>
                <p>${interet.utilisateur.prenom}</p>
                <p>${interet.utilisateur.pseudo}</p>
                <p>Horaire :  ${date}</p>
              </div>`
    })
    listeInteret += `</div>`

    let intReceveur = {
      objet: objet
    }
    Swal.fire({
      title: `<strong>Liste d'intérêts</strong>`,
      html: `${listeInteret}`,
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
      if (r.isConfirmed) {
        fetch(API_URL + "interets/indiquerReceveur", {
          method: "PUT",
          body: JSON.stringify(intReceveur),
          headers: {
            "Content-Type": "application/json",
            "Authorization": session.token,
          },
        }).then((reponse) => {
          if (!reponse.ok) {
            throw new Error(
                "Code d'erreur : " + reponse.status + " : "
                + reponse.statusText);
          }
          return reponse.json();
        }).then((donnee) => {
          nonRemis(objet)
          Redirect("/mesOffres")
        })
      }
    })
    document.querySelectorAll(".interet").forEach((inte) => {
      inte.addEventListener("click", () => {
        let retirer = document.querySelector(".choisi")
        if (retirer) {
          retirer.classList.remove("choisi")
        }
        inte.classList.add("choisi")
        intReceveur = {
          ...intReceveur,
          utilisateur: {
            idUtilisateur: inte.querySelector("#ut").value
          },
          version: inte.querySelector("#version").value,
          dateRdv: inte.querySelector("#date-interet").value,
          vue: inte.querySelector("#vue").value,
        }
      })
    })
  })
}

export default PageMesOffres;

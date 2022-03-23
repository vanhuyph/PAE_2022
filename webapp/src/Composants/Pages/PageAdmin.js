import {recupUtilisateurDonneesSession} from "../../utilitaires/session";
import {Redirect} from "../Router/Router";

const barVertical = `
<div id="bar-vertical" class="ui left sidebar visible vertical menu">
<h2 class="ui large header">Admin</h2>
  <div class="contenu-bar-vert">
    <a class="item active">Inscription</a>
    <a class="item">Membres</a>
  </div>
</div>
`
const barHori = `
<div id="bar-hori">
  <h2 class="ui large header">Admin</h2>
  <div id="choix-page" class="ui buttons">
    <button class="ui active button">Inscription</button>
    <button class="ui button">Membres</button>
  </div>
</div>
`

//Contenu pricipal de la page
const pricipal = `
<div id="principal">
  <div id="choix-demande" class="ui buttons">
    <button id="demandes" class="ui positive button">Demandes</button>
    <button id="refus" class="ui button">Refus</button>
  </div>
  <div id="contenu">
    
  </div>
</div>
`

const page = `
<div class="page-admin">
  <div id="bar">
    ${barVertical}
  </div>
  ${pricipal}
</div>
`

const PageAdmin = () => {
  const session = recupUtilisateurDonneesSession();
  if (!session || !session.utilisateur.estAdmin) {
    Redirect("/");
  }
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = page;

  // Gestion responsive avec la barre verticale pour pc et horizontale pour mobile
  if (window.innerWidth < 576) {
    document.getElementById("bar").innerHTML = barHori;
  } else {
    document.getElementById("bar").innerHTML = barVertical;
  }
  window.onresize = () => {
    if (window.innerWidth < 576) {
      document.getElementById("bar").innerHTML = barHori;
    } else {
      document.getElementById("bar").innerHTML = barVertical;
    }
  };

  // Récupération des utilisateurs en attente
  recupEnAttente()
  const demandesPage = document.querySelector("#demandes")
  const refusPage = document.querySelector("#refus")
  const contenu = document.querySelector("#contenu")
  const demandes = "<h2>Liste des demandes d'inscriptions</h2>"
  contenu.innerHTML = demandes

  // Changement contenu pricipal
  demandesPage.addEventListener("click", () => {
    refusPage.classList.remove("positive")
    demandesPage.classList.add("positive")
    recupEnAttente()
  })
  refusPage.addEventListener("click", () => {
    demandesPage.classList.remove("positive")
    refusPage.classList.add("positive")
    recupRefuse()
  })
}

// Récupération des utilisateurs en attente
const recupEnAttente = () => {
  const session = recupUtilisateurDonneesSession();
  fetch("/api/utilisateurs/attente", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: session.token,
    },
  })
  .then((response) => {
    if (!response.ok) {
      throw new Error(
          "Error code : " + response.status + " : " + response.statusText);
    }
    return response.json();
  })
  .then((data) => surListeAttente(data))
}

// Récupération des utilisateurs refusés
const recupRefuse = () => {
  const session = recupUtilisateurDonneesSession();
  fetch("/api/utilisateurs/refuse", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: session.token,
    },
  })
  .then((response) => {
    if (!response.ok) {
      throw new Error(
          "Error code : " + response.status + " : " + response.statusText);
    }
    return response.json();
  })
  .then((data) => surListeRefus(data))
}

// Affichage de la liste des inscriptions en attente
const surListeAttente = (data) => {
  let contenu = document.querySelector("#contenu");
  if (data.length === 0 || !data) {
    let listeVide = `<div><h2>Liste des demandes d'inscriptions</h2>Il n'y a aucune inscription en attente pour l'instant`;
    contenu.innerHTML = listeVide;
    return;
  }
  let liste = `<div class="liste-utilisateurs">
  <h2>Liste des demandes d'inscriptions</h2>
  <div class="tete-form">
        <div class="tete">
            <p>Pseudo</p>
            <p>Nom</p>
            <p>Prénom</p>
            <p>Est admin</p>
            <div class="boutons-tete">
                <div class="btn1"></div>
                <div class="btn2"></div>
            </div>
        </div>
    </div>
  `;

  // Affichage individuel des utilisateurs
  data.forEach((element) => {
    liste += `
    <div class="utilisateur">
        <form id="formulaire-confirme">
            <input id="id-utilisateur" type="hidden" value=${element.idUtilisateur}>
            <p>${element.pseudo}</p>
            <p>${element.nom}</p>
            <p>${element.prenom}</p>
            <div class="est-admin">
              <div class="ui checkbox">
              <input id="check" type="checkbox">
              </div>
            </div>
            <div class="boutons">
                <button id="confirmer" type="submit" class="ui green button">Confirmer</button>
                <button id="refuser" class="ui red button">Refuser</button>
            </div>
        </form>
        <div class="raison-refus">
          <form id="refus-form" class="ui form">
            <input id="id-utilisateur-refus" type="hidden" value=${element.idUtilisateur}>
            <label for="commentaire">Raison du refus : </label>
            <textarea id="raison" cols="3" rows="1"></textarea>
            <div>
              <button type="submit" class="ui button red">Soumettre</button>
            </div>
          </form>
        </div>
    </div>
    `;
  });
  liste += `</div>`;
  contenu.innerHTML = liste;

  document.querySelectorAll(".utilisateur").forEach((item) => {
    const session = recupUtilisateurDonneesSession();

    // Confirmation de l'utilisateur
    item.querySelector("#formulaire-confirme").addEventListener("submit",
        (e) => {
          e.preventDefault()
          const admin = item.querySelector("#check").checked
          const id = item.querySelector("#id-utilisateur").value

          let confirmation = {
            estAdmin: admin
          }
          fetch("/api/utilisateurs/confirme/" + id, {
            method: "PUT",
            body: JSON.stringify(confirmation),
            headers: {
              "Content-Type": "application/json",
              Authorization: session.token
            }
          })
          .then((reponse) => {
            if (!reponse.ok) {
              throw new Error(
                  "Error code : " + reponse.status + " : " + reponse.statusText)
            }
            return reponse.json();
          })
          .then(() => recupEnAttente())
        });

    // Refus de l'utilisateur
    item.querySelector("#refuser").addEventListener("click", (e) => {
      e.preventDefault()

      // Afficher la div de refus
      const refus = item.querySelector(".raison-refus")
      refus.classList.toggle("montrer-block")

      const refusForm = refus.querySelector("#refus-form")
      refusForm.addEventListener("submit", (e) => {
        e.preventDefault()
        const commentaire = refusForm.querySelector("#raison").value
        const id = item.querySelector("#id-utilisateur-refus").value
        const refus = {
          commentaire: commentaire
        }
        fetch("/api/utilisateurs/refuse/" + id, {
          method: "PUT",
          body: JSON.stringify(refus),
          headers: {
            "Content-Type": "application/json",
            Authorization: session.token
          }
        })
        .then((reponse) => {
          if (!reponse.ok) {
            throw new Error(
                "Error code : " + reponse.status + " : " + reponse.statusText)
          }
          return reponse.json();
        })
        .then(() => recupEnAttente())
      })
    });
  });
}

// Affichage de la liste des inscriptions refusées
const surListeRefus = (data) => {
  const session = recupUtilisateurDonneesSession();
  let contenu = document.querySelector("#contenu");
  if (data.length === 0 || !data) {
    let listeVide = `<div><h2>Liste des inscriptions refusées</h2>Il n'y a aucune inscription refusée pour l'instant`;
    contenu.innerHTML = listeVide;
    return;
  }
  let liste = `<div><h2>Liste des inscriptions refusées</h2>
<div class="tete-form">
        <div class="tete">
            <p>Pseudo</p>
            <p>Nom</p>
            <p>Prénom</p>
            <p>Est admin</p>
            <div class="btn-refus"></div>
        </div>
    </div>`;

  // Affichage individuel des utilisateurs
  data.forEach((element) => {
    liste += `
    <div class="utilisateur">
        <form id="formulaire-confirme">
            <input id="id-utilisateur" type="hidden" value=${element.idUtilisateur}>
            <p>${element.pseudo}</p>
            <p>${element.nom}</p>
            <p>${element.prenom}</p>
            <div class="est-admin">
              <div class="ui checkbox">
                <input id="check" type="checkbox">
              </div>
            </div>
            <div class="">
                <button id="confirmer" type="submit" class="ui green button">Confirmer</button>
            </div>
        </form>
        <div class="refus-raison">
            <label for="commentaire">Raison du refus : </label>
            <p class="commentaire-refus">${element.commentaire}</p>
        </div>
    </div>
    `;
  });
  liste += `</div>`;
  contenu.innerHTML = liste;
  document.querySelectorAll(".utilisateur").forEach((item) => {

    // Confirmation de l'utilisateur
    item.querySelector("#formulaire-confirme").addEventListener("submit",
        (e) => {
          e.preventDefault()
          const admin = item.querySelector("#check").checked
          const id = item.querySelector("#id-utilisateur").value
          let confirmation = {
            estAdmin: admin
          }
          fetch("/api/utilisateurs/confirme/" + id, {
            method: "PUT",
            body: JSON.stringify(confirmation),
            headers: {
              "Content-Type": "application/json",
              Authorization: session.token
            }
          })
          .then((reponse) => {
            if (!reponse.ok) {
              throw new Error(
                  "Error code : " + reponse.status + " : " + reponse.statusText)
            }
            return reponse.json();
          })
          .then(() => recupRefuse())
        });
  });
}

export default PageAdmin;
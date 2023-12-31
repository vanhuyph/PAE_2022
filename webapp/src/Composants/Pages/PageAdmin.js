import {recupUtilisateurDonneesSession} from "../../utilitaires/session";
import {Redirect} from "../Router/Router";
import {API_URL} from "../../utilitaires/serveur";
import autocomplete from 'autocompleter';
import rechecheIcon from "../../img/search.svg"

const barVertical = `
<div id="bar-vertical" class="ui left sidebar visible vertical menu">
<h2 class="ui large header">Admin</h2>
  <div class="contenu-bar-vert">
    <a class="item active" id="inscriptions">Inscription</a>
    <a class="item" id="membres">Membres</a>
  </div>
</div>
`

// Contenu principal de la page
const pagePrincipal = `
  <div id="choix-demande" class="ui buttons">
    <button id="demandes" class="ui positive button">Demandes</button>
    <button id="refus" class="ui button">Refus</button>
  </div>
  <div id="contenu">
    
  </div>
`

const pageMembres = `
<div class="rechercher-membre">
  <h2>Recherche sur les membres</h2>
  <div class="form-recherche">
    <form id="rechercherMembre" class="ui form">
    <div class="field">
      <input id="autoComplete" type="search">
      <button type="submit" class="button"><img src=${rechecheIcon} alt="recheche icon" height="20px"></button>
    </div>
    </form>
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
 <div id="principal"> 
  ${pagePrincipal}
 </div>
</div>
`

const PageAdmin = () => {
  const session = recupUtilisateurDonneesSession();
  if (!session || !session.utilisateur.estAdmin) {
    Redirect("/");
  } else {
    const pageDiv = document.querySelector("#page");
    pageDiv.innerHTML = page;
    afficherDemandes()
    const listeInscriptions = document.querySelector("#inscriptions")
    const listeMembres = document.querySelector("#membres")
    listeInscriptions.addEventListener("click", () => {
      listeMembres.classList.remove("active")
      listeInscriptions.classList.add("active")
      afficherDemandes()
    })
    listeMembres.addEventListener("click", () => {
      listeInscriptions.classList.remove("active")
      listeMembres.classList.add("active")
      afficherMembres()
    })
  }
}

const afficherDemandes = () => {
  const principal = document.querySelector("#principal")
  principal.innerHTML = pagePrincipal
  // Récupération des utilisateurs en attente
  recupEnAttente()
  const demandesPage = document.querySelector("#demandes")
  const refusPage = document.querySelector("#refus")
  const contenu = document.querySelector("#contenu")
  const demandes = "<h2>Liste des demandes d'inscriptions</h2>"
  contenu.innerHTML = demandes

  // Changement contenu principal
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

const afficherMembres = () => {
  const session = recupUtilisateurDonneesSession()
  const principal = document.querySelector("#principal")
  principal.innerHTML = pageMembres

  var recherche = [];
  fetch(API_URL + "utilisateurs/confirme", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: session.token,
    },
  })
  .then((reponse) => {
    if (!reponse.ok) {
      throw new Error(
          "Code d'erreur : " + reponse.status + " : " + reponse.statusText);
    }
    return reponse.json();
  })
  .then((donnees) => {
    donnees.forEach((u) => {
      if (recherche.findIndex(l => l.label === u.nom) === -1) {
        recherche.push({label: u.nom})
      }
      if (recherche.findIndex(l => l.label === u.adresse.commune) === -1) {
        recherche.push({label: u.adresse.commune})
      }
      if (recherche.findIndex(l => l.label === u.adresse.codePostal.toString())
          === -1) {
        recherche.push({label: u.adresse.codePostal.toString()})
      }
    })
    surListeConfirme(donnees)
  })

  // Autocomplétion barre de recherche admin
  var input = document.getElementById("autoComplete");
  autocomplete({
    input: input,
    minLength: 1,
    emptyMsg: "Recherche non trouvée",
    fetch: function (text, update) {
      text = text.toLowerCase();
      var suggestions = recherche.filter(
          n => n.label.toLowerCase().startsWith(text))
      update(suggestions);
    },
    onSelect: function (item) {
      input.value = item.label;
    },
  });
  document.getElementById("rechercherMembre").addEventListener("submit",
      envoyerRecherche)
}

const envoyerRecherche = (e) => {
  e.preventDefault()
  let session = recupUtilisateurDonneesSession()
  let rechercher = document.getElementById("autoComplete").value
  if (rechercher !== "") {
    fetch(API_URL + "utilisateurs/recherche/" + rechercher, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: session.token,
      },
    })
    .then((reponse) => {
      if (!reponse.ok) {
        throw new Error(
            "Code d'erreur : " + reponse.status + " : " + reponse.statusText);
      }
      return reponse.json();
    })
    .then((donnees) => {
      surListeConfirme(donnees)
    })
  } else {
    fetch(API_URL + "utilisateurs/confirme", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: session.token,
      },
    })
    .then((reponse) => {
      if (!reponse.ok) {
        throw new Error(
            "Code d'erreur : " + reponse.status + " : " + reponse.statusText);
      }
      return reponse.json();
    })
    .then((donnees) => surListeConfirme(donnees))
  }
}

const surListeConfirme = async (donnees) => {
  recupUtilisateurDonneesSession();
  let contenu = document.getElementById("contenu")
  let liste = `<div class="liste-utilisateurs">`
  contenu.innerHTML = `<div class="chargement-membres">
    <div class="ui text active centred inline loader">Chargement de la liste des membres</div>
</div>`
  // Affichage de tous les membres avec leur nombre d'objets
  for (const utilisateur of donnees) {
    liste += `
    <div class="utilisateur">
    <div class="est-empeche">
          <div class="ui buttons">
            <button class="ui positive button" id="buttonUtilisateurEmpeche">Est empêché</button>
          </div>
          <input id="etat-ut" type="hidden" value="${utilisateur.etatInscription}">
        </div>
      <div class="admin-membre">
        <div class="utilisateur-nom-prenom">
          <input id="id-ut" type="hidden" value="${utilisateur.idUtilisateur}">
          <p>${utilisateur.nom}</p> 
          <p>${utilisateur.prenom}</p>
        </div>
        <div class="utilisateur-pseudo"><p>${utilisateur.pseudo}</p></div>
        <div class="utilisateur-objets">
        <h4>Nombre d'objets</h4>
          <div class="utilisateur-objets-spe">
            <div><strong>Offerts : </strong><p>${utilisateur.nbObjetOfferts}</p></div>
            <div><strong>Donnés : </strong><p>${utilisateur.nbObjetDonnees}</p></div>
            <div><strong>Reçus : </strong><p>${utilisateur.nbObjetRecus}</p></div>
            <div><strong>Abandonnés : </strong><p>${utilisateur.nbObjetAbandonnes}</p></div>
          </div>
        </div>
      </div>
      <div class="liste-utilisateur-objets">
          <div class="ui buttons">
            <button class="ui positive button" id="obj-offert">Offert</button>
            <button class="ui button" id="obj-recu">Reçu</button>
          </div>
        <div class="liste-objets"></div>
      </div>
    </div>
    `
  }
  liste += `</div>`
  contenu.innerHTML = liste

  document.querySelectorAll(".utilisateur").forEach((u) => {
    const idUtilisateur = u.querySelector("#id-ut").value
    let boutonEmpeche = u.querySelector("#buttonUtilisateurEmpeche")
    let etatUtilisateur = u.querySelector("#etat-ut").value

    if (etatUtilisateur === "Confirmé") {
      boutonEmpeche.textContent = "Est empêché"
      boutonEmpeche.classList.add("positive")
      boutonEmpeche.classList.remove("yellow")
    }

    if (etatUtilisateur === "Empêché") {
      boutonEmpeche.textContent = "Est de retour"
      boutonEmpeche.classList.remove("positive")
      boutonEmpeche.classList.add("yellow")
    }

    boutonEmpeche.addEventListener("click", () => {
      changerEtatUtilisateur(idUtilisateur, etatUtilisateur)
    })

    u.querySelector(".admin-membre").addEventListener("click", () => {
      let objets = u.querySelector(".liste-utilisateur-objets")
      objets.classList.toggle("montrer-block")

      if (objets) {
        listeObjetsOffert(objets, idUtilisateur)
        objets.querySelector("#obj-offert").addEventListener("click", () => {
          objets.querySelector("#obj-offert").classList.add("positive")
          objets.querySelector("#obj-recu").classList.remove("positive")
          listeObjetsOffert(objets, idUtilisateur)
        })
        objets.querySelector("#obj-recu").addEventListener("click", () => {
          objets.querySelector("#obj-offert").classList.remove("positive")
          objets.querySelector("#obj-recu").classList.add("positive")
          listeObjetsRecu(objets, idUtilisateur)
        })
      }
    })
  })
}

const changerEtatUtilisateur = async (idUtilisateur, etatUtilisateur) => {
  let session = recupUtilisateurDonneesSession()

  if (etatUtilisateur === "Confirmé") {
    await fetch(
        API_URL + "utilisateurs/indiquerEmpecherUtilisateur/" + idUtilisateur, {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: session.token
          },
        })
    .then((reponse) => {
      if (!reponse.ok) {
        throw new Error(
            "Code d'erreur : " + reponse.status + " : " + reponse.statusText
        );
      }
      return reponse.json();
    })
  }

  if (etatUtilisateur === "Empêché") {
    await fetch(
        API_URL + "utilisateurs/indiquerConfirmerUtilisateur/" + idUtilisateur,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: session.token
          },
        })
    .then((reponse) => {
      if (!reponse.ok) {
        throw new Error(
            "Code d'erreur : " + reponse.status + " : " + reponse.statusText
        );
      }
      return reponse.json();
    })
  }
  afficherMembres()
}

// Récupération de la liste des objets offerts par l'utilisateur
const listeObjetsOffert = async (objets, idUtilisateur) => {
  let session = recupUtilisateurDonneesSession()
  let liste = `<p>Pas d'objet offert</p>`
  await fetch(API_URL + "offres/objetsOfferts/" + idUtilisateur, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: session.token,
    },
  })
  .then((reponse) => {
    if (!reponse.ok) {
      throw new Error(
          "Code d'erreur : " + reponse.status + " : " + reponse.statusText);
    }
    return reponse.json();
  })
  .then((donnees) => {
    if (donnees.length > 0) {
      liste = ""
      donnees.forEach((donnee) => {
        liste += `
      <div class="mon-offre">
      <div class="ui two column grid">
        <div class="column">
          <img src="/api/offres/photos/${donnee.objetDTO.photo}" height="110px">
        </div>
        <div class="column descri">
          <p>${donnee.objetDTO.description}</p>
        </div>
      </div> 
      </div>
      `
      })
    }
  })
  objets.querySelector(".liste-objets").innerHTML = liste
}

// Récupération de la liste des objets reçus de l'utilisateur
const listeObjetsRecu = async (objets, idUtilisateur) => {
  let session = recupUtilisateurDonneesSession()
  let liste = `<p>Pas d'objet reçu</p>`
  await fetch(API_URL + "offres/objetsRecus/" + idUtilisateur, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: session.token,
    },
  })
  .then((reponse) => {
    if (!reponse.ok) {
      throw new Error(
          "Code d'erreur : " + reponse.status + " : " + reponse.statusText);
    }
    return reponse.json();
  })
  .then((donnees) => {
    if (donnees.length > 0) {
      liste = ""
      donnees.forEach((donnee) => {
        liste += `
      <div class="mon-offre">
      <div class="ui two column grid">
        <div class="column">
          <img src="/api/offres/photos/${donnee.objetDTO.photo}" height="110px">
        </div>
        <div class="column descri">
          <p>${donnee.objetDTO.description}</p>
        </div>
      </div> 
      </div>
      `
      })
    }
  })
  objets.querySelector(".liste-objets").innerHTML = liste
}

// Récupération des utilisateurs en attente
const recupEnAttente = () => {
  const session = recupUtilisateurDonneesSession();
  fetch(API_URL + "utilisateurs/attente", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: session.token,
    },
  })
  .then((reponse) => {
    if (!reponse.ok) {
      throw new Error(
          "Code d'erreur : " + reponse.status + " : " + reponse.statusText);
    }
    return reponse.json();
  })
  .then((data) => surListeAttente(data))
}

// Récupération des utilisateurs refusés
const recupRefuse = () => {
  const session = recupUtilisateurDonneesSession();
  fetch(API_URL + "utilisateurs/refuse", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: session.token,
    },
  })
  .then((reponse) => {
    if (!reponse.ok) {
      throw new Error(
          "Code d'erreur : " + reponse.status + " : " + reponse.statusText);
    }
    return reponse.json();
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
            <p id="pseudo-utilisateur">${element.pseudo}</p>
            <p id="nom-utilisateur">${element.nom}</p>
            <p id="prenom-utilisateur">${element.prenom}</p>
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
            estAdmin: admin,
          }
          fetch(API_URL + "utilisateurs/confirme/" + id, {
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
                  "Code d'erreur : " + reponse.status + " : "
                  + reponse.statusText)
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
        fetch(API_URL + "utilisateurs/refuse/" + id, {
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
                "Code d'erreur : " + reponse.status + " : "
                + reponse.statusText)
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
            <p id="pseudo-utilisateur">${element.pseudo}</p>
            <p id="nom-utilisateur">${element.nom}</p>
            <p id="prenom-utilisateur">${element.prenom}</p>
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
            estAdmin: admin,
          }
          fetch(API_URL + "utilisateurs/confirme/" + id, {
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
                  "Code d'erreur : " + reponse.status + " : "
                  + reponse.statusText)
            }
            return reponse.json();
          })
          .then(() => recupRefuse())
        });
  });
}

export default PageAdmin;

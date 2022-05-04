import {
  creationDonneeSessionUtilisateur,
  enleverDonneeSession,
  recupUtilisateurDonneesSession
} from "../../utilitaires/session";
import Navbar from "../Navbar/Navbar";
import {Redirect} from "../Router/Router";
import {API_URL} from "../../utilitaires/serveur";
import rechecheIcon from "../../img/search.svg";
import Swal from 'sweetalert2'

// Page d'accueil
const PageAccueil = async () => {
  const pageDiv = document.querySelector("#page");
  const session = recupUtilisateurDonneesSession()
  let etatInscription
  let commentaire
  if (session) {
    if (session.utilisateur.etatInscription !== "Confirmé"
        && session.utilisateur.etatInscription !== "Empêché") {
      etatInscription = session.utilisateur.etatInscription
      if (session.utilisateur.etatInscription === "En attente") {
        commentaire = "Vous pourrez accéder aux fonctionnalités lorsqu'un administrateur aura confirmé votre inscription."
      } else {
        commentaire = session.utilisateur.commentaire
      }
    }
    if (session.utilisateur.etatInscription === "Empêché" && !session.empeche) {
      const {value: reponse} = await Swal.fire({
        title: 'Vous avez été marqué comme Empêché(e)',
        confirmButtonText: 'Confirmer',
        input: 'select',
        inputOptions: {
          oui: 'Oui',
          non: 'Non',
        },
        inputAttributes: {
          required: true,
        },
        inputPlaceholder: 'Sélectionner une réponse',
        validationMessage: 'Nous avons besoin d\'une réponse',
        allowOutsideClick: false,
        html: `<p>Êtes-vous toujours Empêché(e) ? :</p>`,
      })
      // Passe l'état de l'utilisateur de "Empêché" à "Confirmé"
      if (reponse === "non") {
        await changerEtatUtilisateur(session.utilisateur.idUtilisateur)
        let dansLocalStorage = localStorage.getItem("utilisateur")
        let souvenir = false;
        let utilisateur;
        if (!dansLocalStorage) {
          session.utilisateur.etatInscription = "Confirmé"
          utilisateur = {...session, isAutenticated: true}
        } else {
          souvenir = true;
          let ut = JSON.parse(localStorage.getItem("utilisateur"))
          ut.utilisateur.etatInscription = "Confirmé"
          utilisateur = {...ut, isAutenticated: true}
        }
        creationDonneeSessionUtilisateur(utilisateur, souvenir)
        fetch(API_URL + "utilisateurs/indiquerConfirmerUtilisateur/"
            + session.utilisateur.idUtilisateur, {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: session.token
          },
        }).then((reponse) => {
          if (!reponse.ok) {
            throw new Error(
                "Code d'erreur : " + reponse.status + " : "
                + reponse.statusText);
          }
          return reponse.json();
        })
      } else {
        Swal.fire({
          title: 'Vous avez été marqué comme Empêché(e)',
          confirmButtonText: 'Confirmer',
          allowOutsideClick: false,
          html: `<p>Certaines fonctionnalités vous sont désactivées tant que vous êtes Empêché(e)</p>`,
        })
        let dansLocalStorage = localStorage.getItem("utilisateur")
        let souvenir = false;
        let uti = {...session, isAutenticated: true, empeche: true}
        if (dansLocalStorage) {
          souvenir = true;
        }
        creationDonneeSessionUtilisateur(uti, souvenir)
      }
    }
  }

  // Lors de la connexion, liste les offres attribuées à l'utilisateur
  if (session) {
    fetch(
        API_URL + "offres/voirOffreAttribuer/"
        + session.utilisateur.idUtilisateur,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: session.token
          },
        }).then((reponse) => {
      if (!reponse.ok) {
        throw new Error(
            "Code d'erreur : " + reponse.status + " : " + reponse.statusText);
      }
      return reponse.json();
    }).then((donnees) => {
      let liste = `<p>Pas d'offres</p>`
      if (donnees.length > 0) {
        liste = ""
        donnees.forEach((donnee) => {
          liste += `
          <div class="objet-attr">
            <p>${donnee.objetDTO.description}</p>
          </div>
          `
        })
        Swal.fire({
          title: '<strong>Liste des offres qui vous ont été attribuées</strong>',
          html: `${liste}`,
          focusConfirm: false,
          confirmButtonText:
              '<i class="fa fa-thumbs-up"></i> OK',
          confirmButtonAriaLabel: 'Thumbs up, great!',
        })
      }
    })
    fetch(
        API_URL + "interets/listeDesPersonnesInteresseesVue/"
        + session.utilisateur.idUtilisateur,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: session.token
          },
        }).then((reponse) => {
      if (!reponse.ok) {
        throw new Error(
            "Code d'erreur : " + reponse.status + " : " + reponse.statusText);
      }
      return reponse.json();
    }).then((donnees) => {
      let liste = `<p>Pas d'intérêt</p>`
      if (donnees.length > 0) {
        liste = ""
        let obj;
        donnees.forEach((donnee) => {
          if (!obj) {
            obj = donnee.objet.description
            liste += `
          <div class="personne-interesse">
            <strong>${obj}</strong>
            `
          }
          if (obj === donnee.objet.description) {
            liste += `
              <div class="personne"> 
                  <p class="inf-int">${donnee.utilisateur.nom}</p>
                  <p class="inf-int">${donnee.utilisateur.prenom}</p>
                  <p class="inf-int">${donnee.utilisateur.pseudo}</p>
              </div> 
              `
          } else {
            liste += `</div>`
            obj = donnee.objet.description
            liste += `
          <div class="personne-interesse">
            <strong>${obj}</strong>
            <div class="personne"> 
                  <p class="inf-int">${donnee.utilisateur.nom}</p>
                  <p class="inf-int">${donnee.utilisateur.prenom}</p>
                  <p class="inf-int">${donnee.utilisateur.pseudo}</p>
              </div> 
            `
          }
        })
        // Popup à la connexion si des personnes ont marqué un intérêt pour les objets de l'utilisateur
        Swal.fire({
          title: '<strong>Liste des personnes intéressées pour vos objets</strong>',
          html: `${liste}`,
          focusConfirm: false,
          confirmButtonText:
              '<i class="fa fa-thumbs-up"></i> OK',
          confirmButtonAriaLabel: 'Thumbs up, great!',
        })
      }
    })
  }

  // Récupération des objets à évaluer lors de la connexion de l'utilisateur
  if (session) {
    fetch(
        API_URL + "offres/objetsAEvaluer/" + session.utilisateur.idUtilisateur,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: session.token
          },
        }).then((reponse) => {
      if (!reponse.ok) {
        throw new Error(
            "Code d'erreur : " + reponse.status + " : " + reponse.statusText);
      }
      return reponse.json();
    }).then((data) => objetsAEvaluer(data, session))
  }

  if (session) {
    fetch(API_URL + "interets/notifierReceveurEmpecher/"
        + session.utilisateur.idUtilisateur, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: session.token
      },
    }).then((reponse) => {
      if (!reponse.ok) {
        throw new Error(
            "Code d'erreur : " + reponse.status + " : " + reponse.statusText);
      }
      return reponse.json();
    }).then((data) => offreurEmpeche(data))
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
  fetch(API_URL + "offres/listerOffresRecentes", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
  }).then((reponse) => {
    if (!reponse.ok) {
      throw new Error(
          "Code d'erreur : " + reponse.status + " : " + reponse.statusText);
    }
    return reponse.json();
  }).then((data) => surListeOffresRecentes(data))
  if (session) {
    if (session.utilisateur.etatInscription !== "Confirmé"
        && session.utilisateur.etatInscription !== "Empêché") {
      conteneurModal.classList.add('active')
      enleverDonneeSession()
      Navbar()
    } else {
      pageAccueil += `
      <div class="offres" xmlns="http://www.w3.org/1999/html">
        <h2>Toutes les offres</h2>
        <form id="rechercherObjet" class="ui form">
          <div class="field">
            <input id="recherche-objet" type="search" placeholder="Recherche par : nom du membre, type, état">
            <button type="submit" class="button"><img src=${rechecheIcon} alt="recheche icon" height="20px"></button>
          </div>
          <div class="two fields">
            <div class="field">
              <input id="date-debut" type="date" min="1900-01-01" max="2300-12-12">
            </div>
            <div class="field">
              <input id="date-fin" type="date"  max="2300-12-12">
            </div>
          </div>
          <div id="reinitialiser-filtre" class="field">
            <button id="rei-filtre" type="button">Réinitialiser le filtre</button>
          </div>
        </form>
        <div id="liste-offres"> </div>
      </div>
      `;
      pageDiv.innerHTML = pageAccueil;
      fetch(API_URL + "offres/listerOffres", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: session.token
        },
      }).then((reponse) => {
        if (!reponse.ok) {
          throw new Error(
              "Code d'erreur : " + reponse.status + " : " + reponse.statusText
          );
        }
        return reponse.json();
      }).then((data) => {
        // Réinitialiser le filtre
        document.querySelector("#rei-filtre").addEventListener("click", (e) => {
          e.preventDefault()
          document.querySelector("#recherche-objet").value = ""
          document.querySelector("#date-debut").value = ""
          document.querySelector("#date-fin").value = ""
          rechercheObjet()
        })
        document.querySelector("#date-debut").onchange = function () {
          let input = document.querySelector("#date-fin");
          input.setAttribute("min", this.value);
        }
        document.querySelector("#rechercherObjet").addEventListener("submit",
            (e) => {
              e.preventDefault()
              rechercheObjet()
            })
        surListeOffres(data)
      })
    }
  }
  declencheurModal.forEach(decl => decl.addEventListener("click", () => {
    conteneurModal.classList.toggle("active")
  }))
};

// Affichage de la notification en cas d'offreur empêché
const offreurEmpeche = async (data) => {
  data.forEach((interet) => {
    Swal.fire({
      title: "Empêchement",
      confirmButtonText: 'OK',
      allowOutsideClick: false,
      html: `<p>L'offreur de l'objet : ${interet.objet.description} a eu un empêchement</p> 
             <p>Nous vous invitons à le contacter ultérieurement pour avoir plus d'informations</p>`,
    })
  })
}

// Change l'état d'un utilisateur
const changerEtatUtilisateur = async (idUtilisateur) => {
  let session = recupUtilisateurDonneesSession()
  let etatUtilisateurJson = {
    etatUtilisateur: "Confirmé"
  }
  fetch(API_URL + "utilisateurs/indiquerEmpecherUtilisateur/" + idUtilisateur, {
    method: "PUT",
    body: JSON.stringify(etatUtilisateurJson),
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

// Affichage des objets à évaluer
const objetsAEvaluer = (data, session) => {
  let note;
  let com;
  // S'il doit évaluer un ou des objets recus
  data.forEach(async (objet) => {
    // Donne les informations sur l'objet à évaluer
    const {value: suivant} = await Swal.fire({
      title: 'Évaluation en attente',
      confirmButtonText: 'Confirmer',
      input: 'select',
      inputOptions: {
        oui: 'Oui',
        non: 'Non',
      },
      inputAttributes: {
        required: true,
      },
      inputPlaceholder: 'Sélectionner une réponse',
      validationMessage: 'Nous avons besoin d\'une réponse',
      allowOutsideClick: false,
      html: `<p>Voulez-vous évaluer l'objet suivant :</p> 
<strong>${objet.description}</strong>`,
    })
    if (suivant === "oui") {
      const etapes = ['1', '2', '3']
      const Queue = Swal.mixin({
        progressSteps: etapes,
        confirmButtonText: 'Suivant',
        reverseButtons: true,
        allowOutsideClick: false
      })
      // Sélection de la note
      const {value: noteFinale} = await Queue.fire({
        title: 'Votre note',
        input: 'select',
        inputAttributes: {
          required: true,
        },
        inputOptions: {
          0: '0',
          1: '1',
          2: '2',
          3: '3',
          4: '4',
          5: '5',
        },
        inputPlaceholder: 'Sélectionner une note',
        validationMessage: 'Nous avons besoin d\'une note',
        currentProgressStep: 0
      })
      // Commentaire de l'évaluation
      const {value: commentaireEval} = await Queue.fire({
        title: 'Votre commentaire',
        currentProgressStep: 1,
        input: 'text',
        inputAttributes: {
          required: true
        },
        validationMessage: 'Nous avons besoin d\'un commentaire'
      })
      // Affichage de la confirmation pour la note et le commentaire
      await Queue.fire({
        title: 'Votre objet a bien été évalué',
        currentProgressStep: 2,
        confirmButtonText: 'Confirmer',
        text: `Vous avez donné une note de ${noteFinale} et le commentaire suivant : \n ${commentaireEval}`,
      })
      note = noteFinale;
      com = commentaireEval;
    } else {
      note = -1;
      com = "N'a pas été évalué.";
    }
    const evaluation = {
      objet: objet,
      note: note,
      commentaire: com,
    }
    await fetch(API_URL + "evaluations/creerEvaluation", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: session.token
      },
      body: JSON.stringify(evaluation),
    }).then((reponse) => {
      if (!reponse.ok) {
        throw  new Error(
            "Code d'erreur : " + reponse.status + " : " + reponse.statusText);
      }
      return reponse.json();
    })
  });
}

// Permet la recherche d'objets
const rechercheObjet = () => {
  const session = recupUtilisateurDonneesSession()
  const recherche = document.querySelector("#recherche-objet").value
  let dateDebut = document.querySelector("#date-debut").value
  let dateFin = document.querySelector("#date-fin").value
  if (dateDebut === "") {
    dateDebut = "1900-01-01"
  }
  if (dateFin === "") {
    dateFin = "2300-12-12"
  }
  const listeOffres = document.getElementById("liste-offres");
  listeOffres.innerHTML = `<div class="chargement-offres">
    <div class="ui text active centered inline loader">Chargement de la liste d'offres</div>
</div>`

  let critereRecherche = {
    recherche: recherche,
    dateDebut: dateDebut,
    dateFin: dateFin
  }

  if (recherche !== "" || dateDebut !== "1900-01-01" || dateFin
      !== "2300-12-12") {
    fetch(API_URL + "offres/recherche", {
      method: "POST",
      body: JSON.stringify(critereRecherche),
      headers: {
        "Content-Type": "application/json",
        Authorization: session.token
      },
    }).then((reponse) => {
      if (!reponse.ok) {
        throw new Error(
            "Code d'erreur : " + reponse.status + " : " + reponse.statusText
        );
      }
      return reponse.json();
    }).then((data) => surListeOffres(data))
  } else {
    fetch(API_URL + "offres/listerOffres", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: session.token
      },
    }).then((reponse) => {
      if (!reponse.ok) {
        throw new Error(
            "Code d'erreur : " + reponse.status + " : " + reponse.statusText
        );
      }
      return reponse.json();
    }).then((data) => surListeOffres(data))
  }
}

// Affichage d'une liste des offres les plus récentes
const surListeOffresRecentes = (data) => {
  const listeOffresRecentes = document.getElementById("liste-offres-recentes");
  const session = recupUtilisateurDonneesSession()
  let listeRecente = `<div class="ui three cards">`;
  data.forEach((offre) => {
    if (session && offre.objetDTO.etatObjet === "Annulé"
        && (session.utilisateur.estAdmin
            || (offre.objetDTO.offreur.idUtilisateur
                === session.utilisateur.idUtilisateur))) {
      listeRecente += `
      <a id="of" class="card" data-of="${offre.idOffre}">
        <div id=${offre.idOffre}></div>
        <div class="image">
          <img src="/api/offres/photos/${offre.objetDTO.photo}">
        </div>
        <div class="content">
          <h4 class="header">Description</h4>
           <p>${offre.objetDTO.description}</p>
        </div>
         <div class="extra content">
          <span class="right floated">
            Statut : ${offre.objetDTO.etatObjet}
          </span>
        </div>
      </a>
    `;
    } else if (offre.objetDTO.etatObjet !== "Annulé") {
      if (session) {
        listeRecente += `
      <a id="of" class="card" data-of="${offre.idOffre}">
        <div id=${offre.idOffre}></div>
        <div class="image">
          <img src="/api/offres/photos/${offre.objetDTO.photo}">
        </div>
        <div class="content">
          <h4 class="header">Description</h4>
           <p>${offre.objetDTO.description}</p>
        </div>
         <div class="extra content">
          <span class="right floated">
            Statut : ${offre.objetDTO.etatObjet}
          </span>
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
         <div class="extra content">
          <span class="right floated">
            Statut : ${offre.objetDTO.etatObjet}
          </span>
        </div>
      </div>
    `;
      }
    }
  })
  listeRecente += `</div>`;
  listeOffresRecentes.innerHTML = listeRecente;
};

// Affichage d'une liste de toutes les offres
const surListeOffres = (data) => {
  const listeOffres = document.getElementById("liste-offres");
  const session = recupUtilisateurDonneesSession()
  if (data.length > 0) {
    let liste = `<div class="ui three link cards">`;
    data.forEach((offre) => {
      if (session && offre.objetDTO.etatObjet === "Annulé"
          && (session.utilisateur.estAdmin
              || (offre.objetDTO.offreur.idUtilisateur
                  === session.utilisateur.idUtilisateur))) {
        liste += `
      <a id="of" class="card" data-of="${offre.idOffre}">
        <div id=${offre.idOffre}></div>
        <div class="image">
          <img src="/api/offres/photos/${offre.objetDTO.photo}">
        </div>
        <div class="content">
          <h4 class="header">Description</h4>
           <p>${offre.objetDTO.description}</p>
        </div>
         <div class="extra content">
          <span class="right floated">
            Statut : ${offre.objetDTO.etatObjet}
          </span>
        </div>
      </a>
    `;
      } else if (offre.objetDTO.etatObjet !== "Annulé") {
        liste += `
      <a id="of" class="card" data-of="${offre.idOffre}">
        <div class="image">
          <img src="/api/offres/photos/${offre.objetDTO.photo}">
        </div>
        <div class="content">
          <h4 class="header">Description</h4>
           <p>${offre.objetDTO.description}</p>
        </div>
        <div class="extra content">
          <span class="right floated">
            Statut : ${offre.objetDTO.etatObjet}
          </span>
        </div>
      </a>`;
      }
    })
    liste += `</div>`;
    listeOffres.innerHTML = liste;
    if (session) {
      document.querySelectorAll("#of").forEach(offre => {
        offre.addEventListener("click", (e) => {
          Redirect("/objet", offre.getAttribute("data-of"))
        })
      })
    }
  } else {
    listeOffres.innerHTML = `
<div class="ui basic center aligned segment">
<p >Pas de résultat correspondant à votre recherche</p>
</div>
`
  }
};

export default PageAccueil;

import {
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
  let evaluationEnAttente = 2 //fetch

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

  //si il doit évaluer un ou des objets recus
  while (evaluationEnAttente > 0) {

    const etapes = ['1', '2', '3','4']
    const Queue = Swal.mixin({
      progressSteps: etapes,
      confirmButtonText: 'suivant',
      cancelButtonText: 'retour',

      reverseButtons: true,
    })
    //donne les information sur l'objet à évaluer
    await Queue.fire({
      title: 'Evaluation 3/3',
      currentProgressStep: 0,
      showCancelButton: true,
      confirmButtonText: 'Confirmer',
      text:`Vous avez donné une note de ${note} et le commentaire suivant : \n ${commentaireEval}`,

    })
//note requise ??
    const {value: note} = await Queue.fire({
      title: 'Votre note',
      input:'range',

      inputAttributes: {
        min: 0,
        max: 5,
        step: 1,
        required:true
      },

      showCancelButton: false,
      validationMessage: 'Nous avons besoin d\'une note ',
      currentProgressStep: 1
    })
    const {value: commentaireEval} = await Queue.fire({
      title: 'Votre Commentaire',
      currentProgressStep: 2,
      input: 'text',
      inputAttributes: {
        required: true
      },
      showCancelButton:true,
      validationMessage: 'nous avons besoin de votre commentaire'

    })

    //afficher la note et le commentaire et demande de confirmation
    await Queue.fire({
      title: 'Confirmation',
      currentProgressStep: 3,
      showCancelButton: true,
      confirmButtonText: 'Confirmer',
      text:`Vous avez donné une note de ${note} et le commentaire suivant : \n ${commentaireEval}`,

    })
    evaluationEnAttente--;
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
    if (session.utilisateur.etatInscription !== "Confirmé") {
      conteneurModal.classList.add('active')
      enleverDonneeSession()
      Navbar()
    } else {
      pageAccueil += `
      <div class="offres">
        <h2>Toutes les offres</h2>
        <form id="rechercherObjet" class="ui form">
          <div class="field">
            <input id="recherche-objet" type="search" placeholder="Recherche par : nom du membre, type, état">
            <button type="submit" class="button"><img src=${rechecheIcon} alt="recheche icon" height="20px"></button>
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
  /*document.querySelector("#rechercherObjet").addEventListener("submit", (e) => {
    e.preventDefault()
    rechercheObjet()
  })*/
};

const rechercheObjet = () => {
  const session = recupUtilisateurDonneesSession()
  const recherche = document.querySelector("#recherche-objet").value
  const listeOffres = document.getElementById("liste-offres");
  listeOffres.innerHTML = `<div class="chargement-offres">
    <div class="ui text active centered inline loader">Chargement de la liste d'offres</div>
</div>`

  if (recherche !== "") {
    fetch(API_URL + "offres/recherche/" + recherche, {
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

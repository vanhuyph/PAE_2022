import {recupUtilisateurDonneesSession} from "../../utilitaires/session";
import Navbar from "../Navbar/Navbar";
import {Redirect} from "../Router/Router";
import {API_URL} from "../../utilitaires/serveur";

const formPhoto =
    `
        <div class="field">
        <form id="envoyerPhoto" class="ui form"  >
          <label>Sélectionner une photo</label>
          <input  name="photo" id="photo" type="file"/> <br/><br/>
          <img src="#" alt="" id="image" style="max-width: 500px; margin-top: 20px;" >
          <div class=" tertiary inverted "> 
          </div>
        </form>
       </div>
    `
const typesObjet =
    `
        <select class="ui search dropdown " type="text" id="choixTypeObjet" className="type" >
        </select>
        <p class="message-erreur erreur-type"></p>
    `
// Formulaire pour créer une offre
const pageOffrirObjet = `
    <div class="page-offrirObjet ">
    <h2>Offrir un objet</h2>
    <div class="ui horizontal segments">
    <div class="ui segment">
    <form id="formulaire-offrirObjet" class="ui form">
          <div class="description-conteneur field">
          <label for="description">Description</label>
              <input type="text" id="description" class="description ">
              <p class="message-erreur erreur-description"></p>
          </div>

        <div class="field">
          <label for="horaire">Plage horaire</label>
          <div class="horaire-conteneur">
              <input type="text" id="horaire" class="horaire">
              <p class="message-erreur erreur-horaire"></p>
          </div>
        </div>
         <div class="field">
          <label for="type">Type</label>
            ${typesObjet}
        </div>
        <div class=" tertiary inverted ">
        <button class="ui  button " type="submit">Offrir l'objet</button>
        </div>
    </form> 
     </div>
       <div class="ui  right floated segment">
    <div class="">
      ${formPhoto}
      </div>
    </div>
    </div>  
    </div>
    `

const PageOffrirObjet = () => {
  const pageDiv = document.querySelector("#page");
  const session = recupUtilisateurDonneesSession();

  pageDiv.innerHTML = pageOffrirObjet;
  const formulairePhoto = document.querySelector("#envoyerPhoto");
  const photo = document.querySelector("#photo");

  formulairePhoto.addEventListener("submit", envoyerPhoto);
  photo.addEventListener("change", previsualiserPhoto);
  const formOffrirObjet = document.querySelector("#formulaire-offrirObjet");

  if (session) {
    Navbar();
    fetch(API_URL + "typesObjet/liste", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: session.token,
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
    .then((data) => choixTypeObjet(data));
    formOffrirObjet.addEventListener("submit", surOffrirObjet);
  } else {
    Redirect("/connexion");
  }
}

// Permet l'affichage de la liste des types d'objets
const choixTypeObjet = (data) => {
  let choixTypeObjet = document.querySelector("#choixTypeObjet");
  if (data.length === 0 || !data) {
    let listeVide = `Il n'y a aucun type d'objets`;
    choixTypeObjet.innerHTML = listeVide;
    return;
  }
  let liste = `<option value="empty" selected hidden=true>Sélectionner le type</option>`;
  data.forEach((typeObjet) => {
    liste += `<option value=${typeObjet.idType}>${typeObjet.nom}</option>`;
  });
  choixTypeObjet.innerHTML = liste;
}

// Permet de prévisualiser la photo avant de l'upload
const previsualiserPhoto = (e) => {
  var image = document.getElementById("image")
  const photo = document.getElementById("photo").files[0];
  if (photo) {
    // On change l'URL de l'image
    image.src = URL.createObjectURL(photo)
  }
}

// Permet l'envoie de la photo vers le backend
const envoyerPhoto = async (e) => {
  const session = recupUtilisateurDonneesSession();
  let nomPhoto;
  const fichierDEntree = document.getElementById("photo");
  const formDonnee = new FormData();
  formDonnee.append('photo', fichierDEntree.files[0]);
  const options = {
    method: 'POST',
    body: formDonnee,
    headers: {
      Authorization: session.token
    },
  };
  await fetch(API_URL + 'offres/telechargementPhoto', options).then((res) => {
    if (!res.ok) {
      throw new Error(
          "Code d'erreur : " + res.status + " : " + res.statusText
      );
    }
    return res.text()
  }).then((data) => {
    nomPhoto = data.toString()
  })
  return nomPhoto;
}

const surOffrirObjet = async (e) => {
  e.preventDefault();
  let typeObjet = document.querySelector("#choixTypeObjet").value;
  let description = document.querySelector("#description").value;
  let plageHoraire = document.querySelector("#horaire").value;
  let photo = document.querySelector("#image");

  document.querySelector(".erreur-type").innerHTML = "";
  document.querySelector(".erreur-description").innerHTML = "";
  document.querySelector(".erreur-horaire").innerHTML = "";

  // Vérification si des champs sont manquants
  if (description === "") {
    document.querySelector(
        ".erreur-description").innerHTML = "Votre description est vide";
  }

  if (plageHoraire === "") {
    document.querySelector(
        ".erreur-horaire").innerHTML = "Votre plage horaire est vide";
  }

  if (typeObjet === "empty") {
    document.querySelector(
        ".erreur-type").innerHTML = "Vous devez sélectionner un type";
  }

  const session = recupUtilisateurDonneesSession();
  const offreur = session.utilisateur;
  const srcPhoto = photo.attributes.getNamedItem("src")
  let nomPhoto = "donnamis.png"
  if (srcPhoto.value !== "#") {
    nomPhoto = await envoyerPhoto()
  }

  if (description !== "" && plageHoraire !== "" && typeObjet !== "empty") {
    let nouvelObjet = {
      offreur: offreur,
      receveur: null,
      typeObjet: {idType: typeObjet},
      description: description,
      photo: nomPhoto.toString()
    }

    let nouvelleOffre = {
      objetDTO: nouvelObjet,
      plageHoraire: plageHoraire
    }

    // Fetch pour créer l'offre
    await fetch(API_URL + "offres/creerOffre", {
      method: "POST",
      body: JSON.stringify(nouvelleOffre),
      headers: {
        "Content-Type": "application/json",
        Authorization: session.token
      },
    })
    .then((reponse) => {
      if (!reponse.ok) {
        throw new Error(
            "Code d'erreur : " + reponse.status + " : " + reponse.statusText)
      }
      return reponse.json();
    }).then((donnee) => Redirect("/"))
  }
}

export default PageOffrirObjet;

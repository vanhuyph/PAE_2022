import {recupUtilisateurDonneesSession} from "../../utilitaires/session";
import Navbar from "../Navbar/Navbar";
import {Redirect} from "../Router/Router";
import {API_URL} from "../../utilitaires/serveur";
import Swal from "sweetalert2";

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
        <select id="choixTypeObjet" >
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
         <div class="field" id="typeObjet">
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

  if (session) {
    Navbar()
    afficherTypeObjet()

  } else {
    Redirect("/connexion");
  }
}

  const afficherTypeObjet = () => {
  const session = recupUtilisateurDonneesSession();
  const formOffrirObjet = document.querySelector("#formulaire-offrirObjet");

  fetch(API_URL + "typesObjet/liste", {
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
  .then((data) => choixTypeObjet(data));

  formOffrirObjet.addEventListener("submit", surOffrirObjet);
}

// Permet l'affichage de la liste des types d'objets
const choixTypeObjet = (data) => {
  let choixTypeObjet = document.querySelector("#choixTypeObjet")
  if (data.length === 0 || !data) {

    choixTypeObjet.innerHTML = `Il n'y a aucun type d'objets`
    return;
  }
  let liste = `<option value="empty" selected hidden>Sélectionner le type</option>
    <option value="nouveauType">Créer un nouveau type</option>`
  data.forEach((typeObjet) => {
    liste += `<option value=${typeObjet.idType}>${typeObjet.nom}</option>`
  });
  choixTypeObjet.innerHTML = liste;
  choixTypeObjet.addEventListener("change", surChoixTypeObjet)
}

//Permet la visualition de créer un nouveau type d'objet
const surChoixTypeObjet = () => {
  const choixTypeObjet = document.querySelector("#choixTypeObjet")
  let typeObjet = document.querySelector("#typeObjet")
  const formCreerType = document.querySelector("#formCreerType")
  if (choixTypeObjet.value === "nouveauType"){

    if(!formCreerType){
      document.querySelector(".erreur-type").innerHTML = ""
      typeObjet.innerHTML += `<div id="formCreerType">
    
      <div >
        <label for="nom">Nom du nouveau type de l'objet</label>
        <input type="text" id="nomType">
          <p class="message-erreur erreur-nomType"></p>
      </div>

      <div>
        <button id="buttonCreerType">Créer nouveau type</button>
      </div>
      </div> `;
      const buttonCreerType = document.querySelector("#buttonCreerType")
      const choixTypeObjet = document.querySelector("#choixTypeObjet")
      buttonCreerType.addEventListener("click", surCreerTypeObjet)
      choixTypeObjet.addEventListener("change", surChoixTypeObjet)
      choixTypeObjet.value = "nouveauType"
    }
  } else {
    if(formCreerType){
      formCreerType.remove();
    }
  }
}

//Permet la création d'un nouveau type d'objet
const surCreerTypeObjet = async (e) => {
  e.preventDefault();
  let nomTypeRecu = "vide";
  let nomNouveauType = document.querySelector("#nomType").value;
  let choixTypeObjet = document.querySelector("#choixTypeObjet")
  document.querySelector(".erreur-nomType").innerHTML = "";

  if (nomNouveauType === "") {
    document.querySelector(
        ".erreur-nomType").innerHTML = "Votre nouveau type d'objet est vide";
  } else {

    const session = recupUtilisateurDonneesSession();
    const nomType = document.getElementById("nomType");

    let typeObjetDTO = {
      idType: null,
      nom: nomType.value
    }
    const options = {
      method: 'POST',
      body: JSON.stringify(typeObjetDTO),
      headers: {
        "Content-Type": "application/json",
        Authorization: session.token
      },
    };
    await fetch('/api/typesObjet/creerTypeObjet', options).then((res) => {
      if (!res.ok) {
        document.querySelector(
            ".erreur-nomType").innerHTML = "Votre nouveau type d'objet existe déjà";
        throw new Error(
            "Code d'erreur : " + res.status + " : " + res.statusText
        );
      }

      return res.json();

    }).then((data) => {
      afficherTypeObjet()
      const formCreerType = document.querySelector("#formCreerType")
      formCreerType.remove();
      nomTypeRecu = data.toString()


      Swal.fire({
        title: `<strong>Créer un nouveau type d'objet</strong>`,
        html: `<p> Votre nouveau type a bien été créé </p>`,
        showCloseButton: true,
        showCancelButton: false,
        focusConfirm: false

      })

      choixTypeObjet.value = data.idType.toString()  //je n'arrive pas à select le nouveau type

    })

    return nomTypeRecu;

  }

}

// Permet de prévisualiser la photo avant de l'upload
const previsualiserPhoto = () => {
  var image = document.getElementById("image")
  const photo = document.getElementById("photo").files[0];
  if (photo) {
    // On change l'URL de l'image
    image.src = URL.createObjectURL(photo)
  }
}

// Permet l'envoie de la photo vers le backend
const envoyerPhoto = async () => {
  const session = recupUtilisateurDonneesSession();
  let nomPhoto;
  const fichierDEntree = document.getElementById("photo");
  const formDonnee = new FormData();
  formDonnee.append('photo', fichierDEntree.files[0]);
  const options = {
    method: 'POST',
    body: formDonnee,
    headers: {
      "Content-Type": "application/json",
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

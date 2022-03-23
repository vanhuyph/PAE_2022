import {
    recupUtilisateurDonneesSession
} from "../../utilitaires/session";
import Navbar from "../Navbar/Navbar";
import {Redirect} from "../Router/Router";

// style pas travailler

const formPhoto =
    `
        <div class="field">
        <form id="envoyerPhoto" class="ui form"  >
          <label>Selectionner une photo</label>
          <input  name="photo" id="photo" type="file"/><!--onchange=" previsualiserPhoto(this)"--> <br/><br/>
          <img src="#" alt="" id="image" style="max-width: 500px; margin-top: 20px;" >
          <div class=" tertiary inverted ">
          
          <button type="submit" class="ui button">Envoyer la photo</button>
          </div>
        </form>
       </div>
    `
const typesObjet =
    `
        <select class="ui search dropdown "  type="text" id="choixTypeObjet" className="type" >
        </select>
        <p class="message-erreur erreur-type"></p>
    `
//permettre à l'user de faire  un enter , passage a la ligne dans la description et la plage horaire
//pour l'instant envoie du formulaire direct..
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

    formulairePhoto.addEventListener("submit",envoyerPhoto);
    photo.addEventListener("change",previsualiserPhoto);
    const formOffrirObjet = document.querySelector("#formulaire-offrirObjet");

    if (session) {
        Navbar();

        fetch("/api/typesObjet/liste", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: session.token,
            },
        })
        .then((response) => {
            if (!response.ok) {
                throw new Error(
                    "Error code : " + response.status + " : " + response.statusText
                );
            }
            return response.json();
        })
        .then((data) => choixTypeObjet(data));

        formOffrirObjet.addEventListener("submit", surOffrirObjet);
    } else {
        Redirect("/connexion");
    }
}

const choixTypeObjet = (data) => {

    let choixTypeObjet = document.querySelector("#choixTypeObjet");
    if (data.length === 0 || !data) {
        let listeVide = `Il n'y aucun type d'objet`;
        choixTypeObjet.innerHTML = listeVide;
        return;
    }
    let liste = `<option value="empty" selected hidden=true>Selectionnez le type</option>`;
    data.forEach((typeObjet) => {
        liste += `<option value=${typeObjet.idType}>${typeObjet.nom}</option>`;
    });

    choixTypeObjet.innerHTML = liste;

}
const previsualiserPhoto  =  (e) => {
    var image = document.getElementById("image")

    const photo = document.getElementById("photo").files[0];
    if (photo) {
        // On change l'URL de l'image
        image.src = URL.createObjectURL(photo)
    }
}
const envoyerPhoto = async (e) => {
    e.preventDefault()
    const session = recupUtilisateurDonneesSession();
    const fichierDEntree =  document.getElementById("photo");
    const formDonnee = new FormData();
    formDonnee.append('photo', fichierDEntree.files[0]);
    const options = {
        method: 'POST',
        body: formDonnee,
        headers: {
            Authorization : session.token
        },
    };
    await fetch('/api/offres/telechargementPhoto', options);
    return false;
}
const surOffrirObjet = (e) => {
    e.preventDefault();
    let typeObjet = document.querySelector("#choixTypeObjet").value;
    let description = document.querySelector("#description").value;
    let plageHoraire = document.querySelector("#horaire").value;

    document.querySelector(".erreur-type").innerHTML = "";
    document.querySelector(".erreur-description").innerHTML = "";
    document.querySelector(".erreur-horaire").innerHTML = "";


    if (description === "") {
        console.log("echec description");
        document.querySelector(".erreur-description").innerHTML = "Votre description est vide";
    }

    if (plageHoraire === "") {
        console.log("echec plage horaire");
        document.querySelector(".erreur-horaire").innerHTML = "Votre plage horaire est vide";
    }

    if (typeObjet === "empty") {
        console.log("echec type");
        document.querySelector(".erreur-type").innerHTML = "Vous devez sélectionner un type";
    }

    //copie collé de code avec la recuperation déjà effectuée au dessus, mais j'arrivais pas à transférer les données
    // peut etre faire par un paramètre à la méthode?
    const session = recupUtilisateurDonneesSession();
    const offreur = session.utilisateur;

    if (description !== ""
        && plageHoraire !== ""
        && typeObjet !== "empty") {

        let nouvelObjet = {
            offreur: offreur,
            receveur: null,
            typeObjet: {idType: typeObjet},
            description: description,
            // photo: "photoTest"
        }

        let nouvelOffre = {
            objetDTO: nouvelObjet,
            plageHoraire: plageHoraire
        }
        console.log(nouvelOffre)

        fetch("/api/offres/creerOffre", {
            method: "POST",
            body: JSON.stringify(nouvelOffre),
            headers: {
                "Content-Type": "application/json",
                 Authorization : session.token
            },
        })
        .then((response) => {
            if (!response.ok) {
                throw new Error(
                    "Error code : " + response.status + " : " + response.statusText)
            }
            return response.json();
        }).then((donnee)=> Redirect("/"))
    }
}

export default PageOffrirObjet;

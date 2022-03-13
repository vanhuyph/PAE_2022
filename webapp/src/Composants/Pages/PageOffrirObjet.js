import {
    recupUtilisateurDonneesSession
} from "../../utilitaires/session";
import Navbar from "../Navbar/Navbar";
import {Redirect} from "../Router/Router";

// style pas travailler


// pas encore de possibilité d'ajout de photo à voir le cours d'architecture => cfr mail Leleux
const typesObjet =
    `
        <select type="text" id="choixTypeObjet" className="type">
        </select>
    `

const pageOffrirObjet = `
    <div class="page-offrirObjet">
    <h2>Offrir un objet</h2>
    <form id="formulaire-offrirObjet" class="ui form">
    
        
        
        <div class="field">
          <label for="description">Description</label>
          <div class="description-conteneur">
              <input type="text" id="description" class="description">
              <p class="message-erreur erreur-description"></p>
          </div>
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
        <button class="ui secondary inverted button" type="submit">Offrir l'objet</button>
        
    </form>    
    `
const PageOffrirObjet = () => {
    const pageDiv = document.querySelector("#page");
    const utilisateur = recupUtilisateurDonneesSession();
    pageDiv.innerHTML = pageOffrirObjet;


    const formOffrirObjet = document.querySelector("#formulaire-offrirObjet");

    if (utilisateur) {
        Navbar();

        fetch("/api/typesObjet/liste", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                //Authorization: utilisateur.token,
            },
        })
        .then((response) => {
            if (!response.ok) {
                throw new Error(
                    "Error code : " + response.status + " : " + response.statusText
                );
            }
            console.log(response);
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
        liste += `
    <option value=${typeObjet.idType}>${typeObjet.nom}</option>
    `;
    });
    liste += ` <p className="message-erreur erreur-type"></p>`;
    choixTypeObjet.innerHTML = liste;

}


const surOffrirObjet = (e) => {
    e.preventDefault();
    console.log("debutListenerOffriObjet");
    let typeObjet = document.querySelector("#choixTypeObjet");
    let description = document.querySelector("#description");
    let plageHoraire = document.querySelector("#horaire");

    document.querySelector(".erreur-type").innerHTML = "";
    document.querySelector(".erreur-description").innerHTML = "";
    document.querySelector(".erreur-horaire").innerHTML = "";


    if (description.value === "") {
        console.log("echec description");
        document.querySelector(".erreur-description").innerHTML = "Votre description est vide";
    }

    if (plageHoraire.value === "") {
        console.log("echec plage horaire");
        document.querySelector(".erreur-horaire").innerHTML = "Votre plage horaire est vide";
    }

    if (typeObjet.value === "empty") {
        console.log("echec type");
        document.querySelector(".erreur-type").innerHTML = "Vous devez sélectionner un type";
    }

    console.log("description :" + description.value);
    console.log("plage horaire :" + plageHoraire.value);
    console.log("type objet :" + typeObjet.value);

    //copie collé de code avec la recuperation déjà effectuée au dessus, mais j'arrivais pas à transférer les données
    // peut etre faire par un paramètre à la méthode?
    const utilisateur = recupUtilisateurDonneesSession();
    const offreur = utilisateur.utilisateur.idUtilisateur;
    

    console.log("id offreur : " + offreur);
    if (description.value !== ""
        && plageHoraire.value !== ""
        && typeObjet.value !== "empty") {

        console.log("avant création json objet");
        let nouvelObjet = {
            offreur: offreur,
            typeObjet: typeObjet,
            description: description,
            //photo: "photoTest"
        }
        console.log("juste avant premier fetch");
        const objet = fetch("/api/objets/creerObjet", {
            method: "POST",
            body: JSON.stringify(nouvelObjet),
            headers: {
                "Content-Type": "application/json",
                //Authorization: utilisateur.token,
            }
        })
        .then((reponse) => {
            if (!reponse.ok) {
                throw new Error(
                    "Error code : " + reponse.status + " : " + reponse.statusText + " : " + reponse.text())
            }
            console.log(reponse)
            return reponse.json()
        }).then((data) => {
            console.log(data.idObjet);
        })




        console.log("juste avant second fetch");

        // let offre = {
        //
        //     idObjet: reponseObjet.idObjet,
        //     plage_horaire: plageHoraire.value
        // }

        // fetch("/api/offres/creerOffre", {
        //     method: "POST",
        //     body: JSON.stringify(offre),
        //     headers: {
        //         "Content-Type": "application/json",
        //     }
        // })
        //     .then((reponseOffre) => {
        //         if (!reponseOffre.ok) {
        //             throw new Error(
        //                 "Error code : " + reponseOffre.status + " : " + reponseOffre.statusText)
        //         }
        //         return reponseOffre.json();
        //     })
    }

}

export default PageOffrirObjet;

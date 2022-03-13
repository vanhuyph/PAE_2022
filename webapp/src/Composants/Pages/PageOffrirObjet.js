import {
    recupUtilisateurDonneesSession
} from "../../utilitaires/session";
import Navbar from "../Navbar/Navbar";
import {Redirect} from "../Router/Router";

//pour l'instant la navbar ne rederige pas vers la bonne page (le bouton se clique pas)


// à regarder pour le framework je me souvenais plus c'était lequel


// pas encore de possibilité d'ajout de photo à voir le cours d'architecture

//différent type hardcodé pour l'instant
let pageOffrirObjet = `
    <div class="page-offrirObjet">
    <h2>Offrir un objet</h2>
    <form id="formulaire-offrirObjet" class="ui form">
    
        <div class="field">
          <label for="type">Type</label>
          <div class="type-conteneur">
              <select type="text" id="type" class="type">
                <option value="empty" selected hidden=true>Selectionnez le type</option>
                <option value="accessoire">Accessoire</option>
                <option value="decoration">Decoration</option>
                <option value="plante">Plante</option>
                <option value="jouet">Jouet</option>
                <option value="vetement">Vêtement</option>
              </select>
              <p class="message-erreur erreur-type"></p>
          </div>
        </div>
        
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
          
        <button class="ui secondary inverted button" type="submit">Offrir l'objet</button>
        
    </form>    
    `
const PageOffrirObjet = () => {
    const pageDiv = document.querySelector("#page");
    const utilisateur = recupUtilisateurDonneesSession();
    pageDiv.innerHTML = pageOffrirObjet;


    let formOffrirObjet = document.querySelector("#formulaire-offrirObjet");

    if (utilisateur) {
        Navbar();

        formOffrirObjet.addEventListener("submit", surOffrirObjet);
    } else {
        Redirect("/");
        //rediriger vers l'accueil avec un message d'erreur (on ne peut pas accéder à offrir un objet si pas connecté)
    }


}

//Méthode beaucoup trop longue a découper en deux Méthodes, créationObjet et une offrirObjet par exemple
const surOffrirObjet = (e) => {
    e.preventDefault();
    console.log("debutListenerOffriObjet");
    let typeObjet = document.querySelector("#type");
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
            }
        })
        .then((reponse) => {
            if (!reponse.ok) {
                throw new Error(
                    "Error code : " + reponse.status + " : " + reponse.statusText + " : " + reponse.text())
            }
            console.log(reponse)
            return reponse.json()
        })




        console.log("juste avant second fetch");
        console.log(objet.objetDTO.idObjet);
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

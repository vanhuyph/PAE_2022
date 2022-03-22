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
        <p class="message-erreur erreur-type"></p>
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
                Authorization: utilisateur.token,
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
        console.log(typeObjet)
        liste += `<option value=${typeObjet.idType}>${typeObjet.nom}</option>`;
    });

    choixTypeObjet.innerHTML = liste;

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

    console.log("description :" + description);
    console.log("plage horaire :" + plageHoraire);
    console.log("type objet :" + typeObjet);

    const utilisateur = recupUtilisateurDonneesSession();
    const offreur = utilisateur.utilisateur;    

    if (description !== ""
        && plageHoraire !== ""
        && typeObjet !== "empty") {

        let nouvelObjet = {
            offreur: offreur,
            receveur: null,
            typeObjet: typeObjet,
            etatObjet:"offre",
            description: description,
            photo: "photoTest"
        }

        let nouvelOffre = {
            objetDTO: nouvelObjet,
            plageHoraire: plageHoraire
        }

        fetch("/api/offres/creerOffre", {
            method: "POST",
            body: JSON.stringify(nouvelOffre),
            headers: {
                "Content-Type": "application/json",
                 Authorization : utilisateur.token    
            },
        })
        .then((response) => {
            console.log(response.json.stringify);
            if (!response.ok) {
                throw new Error(
                    "Error code : " + response.status + " : " + response.statusText)
            }
            console.log(response);
            return response.json();
        }).then((donnee)=> Redirect("/"))
    }
}

export default PageOffrirObjet;

import {
    recupUtilisateurDonneesSession
} from "../../utilitaires/session";
import Navbar from "../Navbar/Navbar";
import { Redirect } from "../Router/Router";


//il faut bien rediriger l'url avec les bons paramètres en partant de liste d'offres
//gérer le cas ou les gens écrivent n'importe quoi dans l'url?
//gérer messages de réussite même quand il ya redirection
//check pour la transaction
//indiquer sur l'objet était déjà annulé quand on annule un objet qui l'était déjà





//http://localhost:8081/objet/?idObjet=1&methode=offrir ==> offrir
//http://localhost:8081/objet/?idObjet=1&methode=details ==> voir details





const typesObjet =
    `
        <select id="choixTypeObjet" class="type">
        </select>
        <p class="message-erreur erreur-type"></p>
    `



const pageObjet = `
    <div class="page-Objet">
    <h2 id="titrePageObjet">Objet</h2>

    <div class="message-reussite">
        <p class="message-reussite reussite" ></p>

    </div>
    <form id="formulaire-Objet" class="ui form">



        <div class="field">
          <label for="description">Description</label>
          <div class="description-conteneur">
              <input type="text" id="description" class="description" placeholder="Entrez la description de l'objet">
              <p class="message-erreur erreur-description"></p>
          </div>
        </div>

        <div class="field">
          <label for="horaire">Plage horaire</label>
          <div class="horaire-conteneur">
              <input type="text" id="horaire" class="horaire" placeholder="Entrez vos disponibilités pour donner l'objet">
              <p class="message-erreur erreur-horaire"></p>
          </div>
        </div>
         <div class="field">
          <label for="type">Type</label>

            ${typesObjet}
        </div>
        <button class="ui secondary inverted button" id="boutonOffrirModifier" type="submit" >Offrir l'objet</button>

        </form>
        <form id="formulaire-Annuler" class="ui form" hidden='true'>
        <button class="ui secondary inverted button" id="boutonAnnuler" type="submit" >annuler l'offre</button>
        </form>
         <form id="formulaire-Interet" class="ui form" hidden='true'>
        <div class="field">
          <label for="date-rdv">Date de rendez vous</label>
          <div class="date-rdv-conteneur">
              <input type="datetime-local" id="dateRdv" class="date-rdv">
              <p class="message-erreur erreur-date-rdv"></p>
          </div>
        </div>

        <button class="ui secondary inverted button" id="boutonMarquerInteret" type="submit">Marquer interet</button>
     </form>

    `

// if créateur de l'offre, if pas créateur de l'offre, if offrir, servira à modifier offre aussi


const PageObjet = async () => {
    const pageDiv = document.querySelector("#page");
    const utilisateurSession = recupUtilisateurDonneesSession();
    pageDiv.innerHTML = pageObjet;
    let params = (new URL(document.location)).searchParams;
    const methodeUrl = params.get('methode');
    const idObjetUrl = parseInt(params.get('idObjet'));

    console.log("methodeUrl : " + methodeUrl);
    console.log("idObjetUrl : " + idObjetUrl);



    if (utilisateurSession) { //si connecté

        Navbar();
        const formObjet = document.querySelector("#formulaire-Objet");
        const idUtilisateur = utilisateurSession.utilisateur.idUtilisateur;
        const titrePageObjet = document.querySelector("#titrePageObjet");
        const formInteret = document.querySelector("#formulaire-Interet");
        const formAnnuler = document.querySelector("#formulaire-Annuler");
        const boutonOffrirModifier = document.querySelector("#boutonOffrirModifier");
        
        await fetch("/api/typesObjet/liste", {
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



        if (methodeUrl === "offrir" || methodeUrl === null) {
            titrePageObjet.innerHTML = "Offrir un objet";
            boutonOffrirModifier.innerHTML = "Offrir objet";
            formObjet.addEventListener("submit", surOffrirObjet);
            console.log("offrir");




        }
        else { //voir détails


            console.log("détails");
            let typeObjet = document.querySelector("#choixTypeObjet");
            let description = document.querySelector("#description");
            let plageHoraire = document.querySelector("#horaire");
            let idCreateurObjet = null;


            titrePageObjet.innerHTML = "Détails d'un objet";
            console.log("authorization before check details objet :"+utilisateurSession.token);
            console.log("authorization before check idUtilisateur :"+idUtilisateur
            );



            const apiVoirDetailsOffre = "/api/offres/voirDetailsOffre/" + idObjetUrl;
            await fetch(apiVoirDetailsOffre, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": utilisateurSession.token,
                    "user": idUtilisateur,
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
                .then((data) => {
                    console.log("type of data :" + typeof (data));
                    console.log("data 1 : " + data.objet.idObjet);
                    console.log("data 2 : " + data.objet.description);
                    description.value = data.objet.description;
                    plageHoraire.value = data.plageHoraire;
                    typeObjet.value = data.objet.typeObjet;
                    idCreateurObjet = data.objet.offreur;

                })
            console.log("idCreateurObjet : " + idCreateurObjet);
            console.log("utilisateur : " + idUtilisateur);


            if (idCreateurObjet === idUtilisateur) { //si l'id de l'utilisateur est le même que le créateur de l'offre alors activer le bouton modifier ?et le css des barre en texte et pas en champ?
                console.log("details et créateur");
                formObjet.addEventListener("submit", surModifierObjet);
                formAnnuler.hidden = false;
                formAnnuler.addEventListener("submit", surAnnulerObjet);
                boutonOffrirModifier.innerHTML = "Modifier offre";


            } else { //utilisateur qui n'a pas créé l'offre


                console.log("details et pas créateur");
                formInteret.hidden = false;
                formInteret.addEventListener("submit", surMarquerInteret);
                boutonOffrirModifier.style.visibility = 'hidden';

            }
        }





    } else { //pas connecté ou de token valide
        Redirect("/connexion");

    }



}

// fonction pour remplir la liste des type d'objets
const choixTypeObjet = (data) => {

    let choixTypeObjet = document.querySelector("#choixTypeObjet");
    if (data.length === 0 || !data) {

        choixTypeObjet.innerHTML = `Il n'y aucun type d'objet`;
        return;
    }
    let liste = `<option value="empty" selected hidden>Selectionnez le type</option>`;
    data.forEach((typeObjet) => {
        console.log(typeObjet)
        liste += `<option value=${typeObjet.idType}>${typeObjet.nom}</option>`;
    });

    choixTypeObjet.innerHTML = liste;

}

//Gestion des boutons

const surModifierObjet = (e) => {
    e.preventDefault();
    console.log("modifier objet");

}

const surAnnulerObjet = (e) => {
    e.preventDefault();
    console.log("annuler objet");

    let params = (new URL(document.location)).searchParams;   //pareil que pour marquer interet, copié collé de code , à voir si possible de passer en paramètre
    const idObjetUrl = parseInt(params.get('idObjet'));
    const messageReussite = document.querySelector(".reussite");
    const utilisateurSession = recupUtilisateurDonneesSession();
    const idUtilisateur = utilisateurSession.utilisateur.idUtilisateur;

    let objetAnnule = {
        idOffre: idObjetUrl,
    }

    fetch("/api/offres/annulerOffre", {
        method: "POST",
        body: JSON.stringify(objetAnnule),
        headers: {
            "Content-Type": "application/json",
            "Authorization": utilisateurSession.token,
            "user": idUtilisateur,
        },
    })
        .then((response) => {
            if (!response.ok) {
                //message echec?
                throw new Error(
                    "Error code : " + response.status + " : " + response.statusText
                );
            }
            console.log(response);

            messageReussite.innerHTML = "l'offre a bien été annulée";

            return response.json();
        })



}

const surMarquerInteret = (e) => { //devrait fonctionner après avoir fixe le init.sql
    e.preventDefault();
    let rdv = document.querySelector("#dateRdv");

    const messageReussite = document.querySelector(".reussite");
    document.querySelector(".erreur-date-rdv").innerHTML = "";



    if (rdv.value === "") {
        console.log("echec dateRdv");
        document.querySelector(".erreur-date-rdv").innerHTML = "Votre date de rendez vous est vide";
    }


    console.log("type objet :" + rdv.value);

    const utilisateurSession = recupUtilisateurDonneesSession(); //copié collié de plus haut , à voir si pas moyen de faire passer en paramètre
    const idUtilisateur = utilisateurSession.utilisateur.idUtilisateur;
    let params = (new URL(document.location)).searchParams;
    const idObjetUrl = parseInt(params.get('idObjet'));
    console.log("marquer interet");

    const dateRdv = document.querySelector("#dateRdv");

    console.log("idUtilisateurInteresse :" + idUtilisateur);
    console.log("idObjet :" + idObjetUrl);
    console.log("dateRdv :" + dateRdv.value);

    let interet = {
        idUtilisateurInteresse: idUtilisateur,
        idObjet: idObjetUrl,
        dateRdv: dateRdv.value,
    }


    fetch("/api/interets/creerInteret", {
        method: "POST",
        body: JSON.stringify(interet),
        headers: {
            "Content-Type": "application/json",
            "Authorization": utilisateurSession.token,
            "user": idUtilisateur,
        },
    })
        .then((response) => {
            if (!response.ok) {
                const messageReussite = document.querySelector(".reussite");
                messageReussite.innerHTML = "la date de rendez vous ne peut pas être dans le passé";
                throw new Error(
                    "Error code : " + response.status + " : " + response.statusText
                );

            }
            console.log(response);

            messageReussite.innerHTML = "L'interet pour l'objet a été enregistré";

            return response.json();

        })



}






const surOffrirObjet = (e) => {
    e.preventDefault();
    console.log("debutListenerOffriObjet");
    let typeObjet = document.querySelector("#choixTypeObjet");
    let description = document.querySelector("#description");
    let plageHoraire = document.querySelector("#horaire");
    const messageReussite = document.querySelector(".reussite");

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
    const utilisateurSession = recupUtilisateurDonneesSession();
    const idUtilisateur = utilisateurSession.utilisateur.idUtilisateur;



    if (description.value !== ""
        && plageHoraire.value !== ""
        && typeObjet.value !== "empty") {

        console.log("avant création json objet");
        let nouvelObjet = {
            offreur: idUtilisateur,
            typeObjet: typeObjet.value,
            description: description.value,
            //photo: "photoTest"
        }

        console.log("juste avant premier fetch");
        fetch("/api/objets/creerObjet", {
            method: "POST",
            body: JSON.stringify(nouvelObjet),
            headers: {
                "Content-Type": "application/json",
                "Authorization": utilisateurSession.token,
                "user": idUtilisateur,
            }
        })
            .then((reponse) => {
                if (!reponse.ok) {
                    throw new Error(
                        "Error code : " + reponse.status + " : " + reponse.statusText + " : " + reponse.text())
                }
                console.log(reponse)
                return reponse.json()
            }).then((data) => surCreerOffre(data))

    }
    const surCreerOffre = async (data) => {
        console.log("juste avant second fetch");
        console.log(data);
        console.log("plageHoraire " + plageHoraire.value);
        console.log(data.idObjet);

        let idOffreCree = null;
        let offre = {
            idObjet: data.idObjet,
            plageHoraire: plageHoraire.value,
        }

        await fetch("/api/offres/creerOffre", {
            method: "POST",
            body: JSON.stringify(offre),
            headers: {
                "Content-Type": "application/json",
                "Authorization": utilisateurSession.token,
                "user": idUtilisateur,
            }
        })
            .then((reponseOffre) => {

                if (!reponseOffre.ok) {
                    throw new Error(
                        "Error code : " + reponseOffre.status + " : " + reponseOffre.statusText)
                }
                return reponseOffre.json();
            }).then((data) => {
                console.log(data)
                idOffreCree = data.idOffre;

                console.log("after redirect offrir objet");
                Redirect("/objet/?idObjet=" + idOffreCree + "&methode=details");
                messageReussite.innerHTML = " L'objet et l'offre ont été créé"; //ne fonctionne pas
            })






    }



}

export default PageObjet;

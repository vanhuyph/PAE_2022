import {
    recupUtilisateurDonneesSession
} from "../../utilitaires/session";
import Navbar from "../Navbar/Navbar";
import {Redirect} from "../Router/Router";

// style pas travailler

const formPhoto =
    `
        <div class="field">
        <!--télechargement de la photo -->
        <script>
          const envoyerPhoto = (e) => {
                  const fichierDEntree = document.querySelector('input[name=photo]');
                const formDonnee = new FormData();
               formDonnee.append('file', fichierDEntree.files[0]);
                 const options = {
                 method: 'POST',
                 body: formDonnee
               };
             fetch('http://localhost:8080/telechargementPhoto', options);
            return false;
          }
        </script>
        <!--Prévisualisation de la photo-->
        <script>
  
           var image = document.getElementById("image");
           const previsualiserPhoto  =  (e) => {
           const [picture] = e.files

       
            if (picture) {
             // On change l'URL de l'image
               image.src = URL.createObjectURL(picture)
            }
    } 
</script>
        <form class="ui form">
          <label>Selectionner une photo</label>
          <input  name="photo" onchange="previsualiserPhoto(this)" type="file"/><br/><br/>
          <div class=" tertiary inverted ">
          <button  class="ui button" onclick="return envoyerPhoto()">Envoyer la photo</button>
          </div>
        </form>
        
        <img src="#" alt="" id="image" style="max-width: 500px; margin-top: 20px;" >
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
                Authorization: utilisateur.token,
            }
        })
        .then((reponse) => {
            if (!reponse.ok) {
                throw new Error(
                    "Error code : " + reponse.status + " : " + reponse.statusText + " : " + reponse.text())
            }
            console.log(reponse)
            return reponse.json()
        }).then((data) =>surCreerOffre(data))

    }
    const surCreerOffre = (data) =>{
        console.log("juste avant second fetch");
        console.log(data);
        console.log("plageHoraire "+plageHoraire.value);
        console.log(data.idObjet);
        let offre = {
            idObjet: data.idObjet,
            plageHoraire: plageHoraire.value,
        }

        fetch("/api/offres/creerOffre", {
            method: "POST",
            body: JSON.stringify(offre),
            headers: {
                "Content-Type": "application/json",
            }
        })
        .then((reponseOffre) => {

            if (!reponseOffre.ok) {
                throw new Error(
                    "Error code : " + reponseOffre.status + " : " + reponseOffre.statusText)
            }
            return reponseOffre.json();
        }).then((data) =>{ console.log(data)})
    }

}

export default PageOffrirObjet;

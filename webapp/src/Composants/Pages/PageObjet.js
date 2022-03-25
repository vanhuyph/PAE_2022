import {API_URL} from "../../utilitaires/serveur";
import {recupUtilisateurDonneesSession} from "../../utilitaires/session";
import {Redirect} from "../Router/Router";

const PageObjet = (id) => {
  const session = recupUtilisateurDonneesSession()
  // Récupère l'objet
  fetch(API_URL+"offres/voirDetailsOffre/"+id, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: session.token
    },
  })
  .then((reponse) => {
    if(!reponse.ok)
      throw new Error(
          "Code d'erreur : " + reponse.status + " : " + reponse.statusText
      )
    return reponse.json()
  })
  .then((donnee) => {
    if (donnee.objetDTO.offreur.idUtilisateur === session.utilisateur.idUtilisateur){
      surDetailObjetProprio(donnee)
    }else {
      surDetailObjet(donnee)
    }
  })
}
const surDetailObjet = (offre) => {
  let pageDiv = document.querySelector("#page");

  let offresPrecedentes = 'Changer';
  let offrePage = `
  <div class="ui container">
    <div class="ui two column grid">
      <div class="column">
        <img class="ui large rounded image" src="/api/offres/photos/${offre.objetDTO.photo}"/>
        <div class="ui two column grid">
          <div class="column">
          </div>
          <div class="column">
            <p>Proposé par: ${offre.objetDTO.offreur.pseudo}</p>
          </div>
        </div>
      </div>
      <div class="ui two column grid">
        <div class="row">
          <div class="column">
            <h4>Type</h4>
            <p>${offre.objetDTO.typeObjet.nom}</p>
          </div>
          <div class="column">
            <h4>Etat de l'objet</h4>
            <p>${offre.objetDTO.etatObjet}</p>
          </div>
        </div>
        <div class="row">
          <div class="column">
            <h4>Nombre de personnes intéressées</h4>
            <p>Changer</p>
          </div>
          <div class="column">
            <h4>Date</h4>
            <p>${offre.dateOffre[2]}/${offre.dateOffre[1]}/${offre.dateOffre[0]}</p>
          </div>
        </div>
      </div>
    </div>
    <div class="ui two column grid">
      <div class="row">
        <div class="column">
          <h4>Description</h4>
          <p>${offre.objetDTO.description}</p>
        </div>
        <div class="column">
          <h4>Date précedentes de l'offre</h4>
          <p>${offresPrecedentes}</p>
        </div>
      </div>
      <div class="row">
        <div class="column">
          <h4>Disponibilités de l'offreur : ${offre.plageHoraire}</h4>
        </div>
      </div>
    </div>
    <form class="ui form" id="form-interet">
      <div class="ui two column grid">
        <div class="row">
          <div class="column">
          <div class="field">
            <label for="dateRdv">Votre disponibilité :</label>
            <input type="date" id="dateRdv" class="date-rdv">
            </div>
          </div>
        </div>
        <div class="row">
          <div class="column">
            <div class="field">
              <label for="gsm-interet">Si vous souhaitez être appelé, veuillez introduire votre numero d'appel</label>
              <input type="text" id="gsm-interet">
            </div>
          </div>
        </div>
        <div class="row">
          <div class="column">
            <div class="field">
              <button type="submit" class="ui primary button">Marquer mon intérêt</button>
            </div>
          </div>
        </div>
      </div>
    </form>
  </div>
  `
  pageDiv.innerHTML = offrePage

  document.querySelector("#form-interet").addEventListener("submit", (e) => {
    let session = recupUtilisateurDonneesSession()
    let date = document.getElementById("dateRdv").value
    let utilisateur = {
      ...session.utilisateur,
      gsm: document.getElementById("gsm-interet").value
    }

    let interet = {
      utilisateur: utilisateur,
      objet: offre.objetDTO,
      dateRdv: date
    }
    console.log(interet)

    fetch(API_URL+'interets/creerInteret',{
      method: "POST",
      body: JSON.stringify(interet),
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

  })

}

const surDetailObjetProprio = (offre) => {
  let pageDiv = document.querySelector("#page");

  let offresPrecedentes = 'Changer';
  let offrePage = `
  <div class="ui container">
    <div class="ui two column grid">
      <div class="column">
        <img class="ui large rounded image" src="/api/offres/photos/${offre.objetDTO.photo}"/>
        <div class="ui two column grid">
          <div class="column">
          </div>
          <div class="column">
            <p>Proposé par: ${offre.objetDTO.offreur.pseudo}</p>
          </div>
        </div>
      </div>
      <div class="ui two column grid">
        <div class="row">
          <div class="column">
            <h4>Type</h4>
            <p>${offre.objetDTO.typeObjet.nom}</p>
          </div>
          <div class="column">
            <h4>Etat de l'objet</h4>
            <p>${offre.objetDTO.etatObjet}</p>
          </div>
        </div>
        <div class="row">
          <div class="column">
            <h4>Nombre de personnes intéressées</h4>
            <p>Changer</p>
          </div>
          <div class="column">
            <h4>Date</h4>
            <p>${offre.dateOffre[2]}/${offre.dateOffre[1]}/${offre.dateOffre[0]}</p>
          </div>
        </div>
      </div>
    </div>
    <div class="ui two column grid">
      <div class="row">
        <div class="column">
          <h4>Description</h4>
          <p>${offre.objetDTO.description}</p>
        </div>
        <div class="column">
          <h4>Date précedentes de l'offre</h4>
          <p>${offresPrecedentes}</p>
        </div>
      </div>
      <div class="row">
        <div class="column">
          <h4>Disponibilités de l'offreur : ${offre.plageHoraire}</h4>
        </div>
      </div>
    </div>
    <div class="ui two column grid">
      <div class="column">
      <div class="ui buttons">
        <button id="modifier-offre" class="ui green button">Modifier votre profil</button>
        <button id="annuler-offre" class="ui negative button">Annuler l'offre</button>
      </div>
      </div>
    </div>
  </div>
  `
  pageDiv.innerHTML = offrePage

  document.querySelector("#modifier-offre").addEventListener("click", () => {
    surDetailObjetProprioModifier(offre)
  })
  document.querySelector("#annuler-offre").addEventListener("click", () => {
    const session = recupUtilisateurDonneesSession()
    fetch("/api/offres/annulerOffre/"+offre.idOffre, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "Authorization": session.token,
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

      return response.json();
    })
    .then((donnee) => Redirect("/"))
  })
}

const surDetailObjetProprioModifier = (offre) => {
  let pageDiv = document.querySelector("#page");

  let offresPrecedentes = 'Changer';
  let offrePage = `
  <div class="ui container">
  <form class="ui form">
    <div class="ui two column grid">
      <div class="column">
        <img class="ui large rounded image" src="/api/offres/photos/${offre.objetDTO.photo}"/>
        <div class="ui two column grid">
          <div class="column">
          <button>Modifier la photo</button>
          </div>
          <div class="column">
            <p>Proposé par: ${offre.objetDTO.offreur.pseudo}</p>
          </div>
        </div>
      </div>
      <div class="ui two column grid">
        <div class="row">
          <div class="column">
            <h4>Type</h4>
            <p>${offre.objetDTO.typeObjet.nom}</p>
          </div>
          <div class="column">
            <h4>Etat de l'objet</h4>
            <p>${offre.objetDTO.etatObjet}</p>
          </div>
        </div>
        <div class="row">
          <div class="column">
            <h4>Nombre de personnes intéressées</h4>
            <p>Changer</p>
          </div>
          <div class="column">
            <h4>Date</h4>
            <p>${offre.dateOffre[2]}/${offre.dateOffre[1]}/${offre.dateOffre[0]}</p>
          </div>
        </div>
      </div>
    </div>
    <div class="ui two column grid">
      <div class="row">
        <div class="column">
        <div class="field">
          <h4>Description</h4>
          <input type="text" value="${offre.objetDTO.description}"/>
        </div>
        </div>
        <div class="column">
          <h4>Date précedentes de l'offre</h4>
          <p>${offresPrecedentes}</p>
        </div>
      </div>
      <div class="row">
        <div class="column">
        <div class="field">
          <h4>Disponibilités de l'offreur</h4>
          <input type="text" value="${offre.plageHoraire}"/>
         </div>
        </div>
      </div>
    </div>
    <div class="ui two column grid">
    <div class="column"></div>
      <div class="column">
      <div class="field">
        <div class="ui bouttons">
          <button id="confirmer" type="submit" class="ui positive button">Confirmer</button>
          <button id="annuler" class="ui negative button">Annuler</button>
        </div>
      </div>
      </div>
    </div>
  </form>
  </div>
  `
  pageDiv.innerHTML = offrePage

  document.querySelector("#annuler").addEventListener("click", () => {
    surDetailObjetProprio(offre)
  })
}
export default PageObjet;
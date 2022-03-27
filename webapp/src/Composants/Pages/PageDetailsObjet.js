import {API_URL} from "../../utilitaires/serveur";
import {recupUtilisateurDonneesSession} from "../../utilitaires/session";
import {Redirect} from "../Router/Router";

const PageDetailsObjet = (id) => {
  const session = recupUtilisateurDonneesSession()
  // Récupère l'objet
  fetch(API_URL + "offres/voirDetailsOffre/" + id, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: session.token
    },
  })
  .then((reponse) => {
    if (!reponse.ok) {
      throw new Error(
          "Code d'erreur : " + reponse.status + " : " + reponse.statusText
      )
    }
    return reponse.json()
  })
  .then((donnee) => {
    if (donnee.objetDTO.offreur.idUtilisateur
        === session.utilisateur.idUtilisateur) {
      surDetailObjetProprio(donnee)
    } else {
      surDetailObjet(donnee)
    }
  })
}
const surDetailObjet = async (offre) => {
  // Page détails de l'objet
  let dateOffre = new Date(offre.dateOffre[0], offre.dateOffre[1] - 1,
      offre.dateOffre[2]).toLocaleDateString("fr-BE")
  let pageDiv = document.querySelector("#page");
  let session = recupUtilisateurDonneesSession()
  let nbInteressees = 0;

  // Récupération du nombre d'intérêts
  await fetch(
      API_URL + 'interets/nbPersonnesInteresees/' + offre.objetDTO.idObjet, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: session.token,
        },
      })
  .then((reponse) => {
    if (!reponse.ok) {
      throw new Error(
          "Code erreur : " + reponse.status + " : " + reponse.statusText
      );
    }
    return reponse.json();
  })
  .then((nbInt) => nbInteressees = nbInt)

  // Récupération des offres précedentes
  let offresPrecedentes = "Pas d'offres précédentes";
  await fetch(
      API_URL + 'offres/offresPrecedentes/' + offre.objetDTO.idObjet, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: session.token,
        },
      })
  .then((reponse) => {
    if (!reponse.ok) {
      //message echec?
      throw new Error(
          "Code erreur : " + reponse.status + " : " + reponse.statusText
      );
    }
    return reponse.json();
  })
  .then((offres) => {
    if (offres.length > 1) {
      offresPrecedentes = ""
      offres.forEach((off) => {
        let daOf = new Date(off.dateOffre[0], off.dateOffre[1] - 1,
            off.dateOffre[2]).toLocaleDateString("fr-BE")
        if (daOf !== dateOffre) {
          offresPrecedentes += `<p>${daOf}</p>`
        }
      })
    }
  })

  // Date de demain pour éviter le choix des dates dans le passé
  var ajd = new Date();
  var dd = String(ajd.getDate() + 1).padStart(2, '0');
  var mm = String(ajd.getMonth() + 1).padStart(2, '0');
  var yyyy = ajd.getFullYear();
  ajd = yyyy + '-' + mm + '-' + dd;

  // Page des détails de l'objet
  let offrePage = `
  <div class="interet-popup">
    <div class="container-popup-interet">
    </div>
  </div>
  <div class="ui container">
    <div class="ui two column grid">
      <div class="column">
        <img class="ui large rounded image" src="/api/offres/photos/${offre.objetDTO.photo}"/>
        <div class="ui two column grid">
          <div class="column">
          </div>
          <div class="column propose">
            <p>Proposé par : ${offre.objetDTO.offreur.pseudo}</p>
          </div>
        </div>
      </div>
      <div class="ui two column grid caracteristique-objet">
        <div class="row">
          <div class="column">
            <h4>Type de l'objet</h4>
            <p>${offre.objetDTO.typeObjet.nom}</p>
          </div>
          <div class="column">
            <h4>État de l'objet</h4>
            <p class="etat-objet">${offre.objetDTO.etatObjet}</p>
          </div>
        </div>
        <div class="row">
          <div class="column nb-inter">
            <h4>Nombre de personnes intéressées</h4>
            <p class="nb-interessees">${nbInteressees}</p>
          </div>
          <div class="column">
            <h4>Date de l'offre</h4>
            <p>${dateOffre}</p>
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
          <h4>Date(s) précedente(s) de l'offre</h4>
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
            <label for="dateRdv">Indiquer votre disponibilité :</label>
            <input type="date" id="dateRdv" min=${ajd} class="date-rdv">
            <div id="messageErreur" class="message-erreur"></div>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="column">
            <div class="field">
              <label for="gsm-interet">Si vous souhaitez être appelé, veuillez introduire votre numéro d'appel</label>
              <input type="text" id="gsm-interet">
            </div>
          </div>
        </div>
        <div class="row">
          <div class="column">
            <div class="field">
              <button type="submit" class="ui primary button" id="marquer-interet">Marquer mon intérêt</button>
              <p id="serveurErreur" class="message-erreur"></p>
            </div>
          </div>
        </div>
      </div>
    </form>
  </div>
  `
  pageDiv.innerHTML = offrePage

  document.querySelector("#form-interet").addEventListener("submit", (e) => {
    e.preventDefault();
    let dateRdv = document.querySelector("#dateRdv").value;
    let messageErreur = document.querySelector("#messageErreur");
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

    // Vérifie si une disponibilité est introduite
    if (dateRdv === "") {
      messageErreur.innerText = "Veuillez introduire une disponibilité";
    } else {
      messageErreur.innerText = "";
      document.querySelector("#marquer-interet").classList.add("loading")
      fetch(API_URL + 'interets/creerInteret', {
        method: "POST",
        body: JSON.stringify(interet),
        headers: {
          "Content-Type": "application/json",
          Authorization: session.token,
        },
      })
      .then((response) => {
        if (!response.ok) {
          document.querySelector("#marquer-interet").classList.remove("loading")
          throw new Error(
              "Code d'erreur : " + response.status + " : " + response.statusText
          );
        }
        return response.json();
      }).then(() => {
        let premierInter = setInterval(() => {
          document.querySelector("#marquer-interet").classList.remove("loading")
          document.querySelector("#marquer-interet").classList.add("disabled")
          let popup = document.querySelector(".interet-popup");
          document.querySelector(
              ".container-popup-interet").innerHTML = "<p>Votre intérêt a bien été marqué</p>"
          popup.style = "transform: translateX(-310px); opacity: 1;transition: all 1s ease;";
          let deuxiemeInter = setInterval(() => {
            document.querySelector(".nb-interessees").innerHTML = nbInteressees
                + 1;
            document.querySelector(".etat-objet").innerHTML = 'Intéressé'
            popup.style = "transform: translateX(300px); opacity: 0; transition: all 3.5s ease;";
            clearInterval(deuxiemeInter);
          }, 3000)
          clearInterval(premierInter);
        }, 1000)
      })
      .catch(err => surErreur(err))
    }
  })
}

const surDetailObjetProprio = async (offre) => {
  let pageDiv = document.querySelector("#page");
  let dateOffre = new Date(offre.dateOffre[0], offre.dateOffre[1] - 1,
      offre.dateOffre[2]).toLocaleDateString("fr-BE")
  let nbInteressees = 0;
  let session = recupUtilisateurDonneesSession()
  await fetch(
      API_URL + 'interets/nbPersonnesInteresees/' + offre.objetDTO.idObjet, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: session.token,
        },
      })
  .then((reponse) => {
    if (!reponse.ok) {
      throw new Error(
          "Code erreur : " + reponse.status + " : " + reponse.statusText
      );
    }

    return reponse.json();
  })
  .then((nbInt) => nbInteressees = nbInt)
  let offresPrecedentes = "Pas d'offres précédentes";
  await fetch(
      API_URL + 'offres/offresPrecedentes/' + offre.objetDTO.idObjet, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: session.token,
        },
      })
  .then((reponse) => {
    if (!reponse.ok) {
      throw new Error(
          "Code erreur : " + reponse.status + " : " + reponse.statusText
      );
    }
    return reponse.json();
  })
  .then((offres) => {
    if (offres.length > 1) {
      offresPrecedentes = ""
      offres.forEach((off) => {
        let daOf = new Date(off.dateOffre[0], off.dateOffre[1] - 1,
            off.dateOffre[2]).toLocaleDateString("fr-BE")
        if (daOf !== dateOffre) {
          offresPrecedentes += `<p>${daOf}</p>`
        }
      })
    }
  })
  let offrePage = `
  <div class="ui container">
    <div class="ui two column grid">
      <div class="column">
        <img class="ui large rounded image" src="/api/offres/photos/${offre.objetDTO.photo}"/>
        <div class="ui two column grid">
          <div class="column">
          </div>
          <div class="column propose">
            <p>Proposé par: ${offre.objetDTO.offreur.pseudo}</p>
          </div>
        </div>
      </div>
      <div class="ui two column grid caracteristique-objet">
        <div class="row">
          <div class="column">
            <h4>Type</h4>
            <p>${offre.objetDTO.typeObjet.nom}</p>
          </div>
          <div class="column">
            <h4>État de l'objet</h4>
            <p>${offre.objetDTO.etatObjet}</p>
          </div>
        </div>
        <div class="row">
          <div class="column">
            <h4>Nombre de personnes intéressées</h4>
            <p>${nbInteressees}</p>
          </div>
          <div class="column">
            <h4>Date</h4>
            <p>${dateOffre}</p>
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
          <h4>Date(s) précedente(s) de l'offre</h4>
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
        <button id="modifier-offre" class="ui green button">Modifier votre offre</button>
        <button id="annuler-offre" class="ui negative button">Annuler votre offre</button>
      </div>
      </div>
    </div>
  </div>
  `
  pageDiv.innerHTML = offrePage

  document.querySelector("#modifier-offre").addEventListener("click", () => {
    //surDetailObjetProprioModifier(offre)
  })
  document.querySelector("#annuler-offre").addEventListener("click", () => {
    const session = recupUtilisateurDonneesSession()
    fetch("/api/offres/annulerOffre/" + offre.idOffre, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "Authorization": session.token,
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
    .then(() => Redirect("/"))
  })
}

const surDetailObjetProprioModifier = async (offre) => {
  let session = recupUtilisateurDonneesSession()
  let pageDiv = document.querySelector("#page");
  let nbInteressees = 0;
  let dateOffre = new Date(offre.dateOffre[0], offre.dateOffre[1] - 1,
      offre.dateOffre[2]).toLocaleDateString("fr-BE")

  await fetch(
      API_URL + 'interets/nbPersonnesInteresees/' + offre.objetDTO.idObjet, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: session.token,
        },
      })
  .then((reponse) => {
    if (!reponse.ok) {
      throw new Error(
          "Code erreur : " + reponse.status + " : " + reponse.statusText
      );
    }
    return reponse.json();
  })
  .then((nbInt) => nbInteressees = nbInt)

  let offresPrecedentes = "Pas d'offres précédentes";
  // Récupère les dates des offres précédentes
  await fetch(
      API_URL + 'offres/offresPrecedentes/' + offre.objetDTO.idObjet, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: session.token,
        },
      })
  .then((reponse) => {
    if (!reponse.ok) {
      throw new Error(
          "Code erreur : " + reponse.status + " : " + reponse.statusText
      );
    }
    return reponse.json();
  })
  .then((offres) => {
    if (offres.length > 1) {
      offresPrecedentes = ""
      offres.forEach((off) => {
        let daOf = new Date(off.dateOffre[0], off.dateOffre[1] - 1,
            off.dateOffre[2]).toLocaleDateString("fr-BE")
        if (daOf !== dateOffre) {
          offresPrecedentes += `<p>${daOf}</p>`
        }
      })
    }
  })

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
          <div class="column propose">
            <p>Proposé par: ${offre.objetDTO.offreur.pseudo}</p>
          </div>
        </div>
      </div>
      <div class="ui two column grid caracteristique-objet">
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
            <p>${nbInteressees}</p>
          </div>
          <div class="column">
            <h4>Date</h4>
            <p>${dateOffre}</p>
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
          <h4>Date(s) précedente(s) de l'offre</h4>
          ${offresPrecedentes}
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

// Si erreur lors de la soumission du formulaire
const surErreur = (err) => {
  let messageErreur = document.querySelector("#serveurErreur");
  let erreurMessage = "";
  if (err.message.includes("412")) {
    erreurMessage = "Erreur lors du marquage de l'intérêt";
  } else {
    erreurMessage = err.message;
  }
  messageErreur.innerText = erreurMessage;
}

export default PageDetailsObjet;

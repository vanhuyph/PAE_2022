import {API_URL} from "../../utilitaires/serveur";
import {recupUtilisateurDonneesSession} from "../../utilitaires/session";
import {Redirect} from "../Router/Router";
import Swal from "sweetalert2";

const PageDetailsObjet = (id) => {
  const session = recupUtilisateurDonneesSession()
  // Redirige vers connexion si pas de session
  if (!session) {
    Redirect("/connexion");
  }
  // Récupère l'objet
  else {
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

  // Récupération des offres précédentes
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
        offresPrecedentes += `<p>${daOf}</p>`
      })
    }
  })

  // Date de demain pour éviter le choix des dates dans le passé
  var ajd = new Date();
  var dd = String(ajd.getDate()).padStart(2, '0');
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
          <h4>Date(s) précédente(s) de l'offre</h4>
          <p>${offresPrecedentes}</p>
        </div>
      </div>
      <div class="row">
        <div class="column">
          <h4>Disponibilités de l'offreur</h4>
          <p>${offre.plageHoraire}</p>
          <p></p>
        </div>
      </div>
    </div>
    <form class="ui form" id="form-interet">
      <div class="ui two column grid">
        <div class="row">
          <div class="column">
          <div class="field">
            <label for="dateRdv">Indiquer votre disponibilité</label>
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

  // Vérifie si l'utilisateur à déjà marqué un intérêt pour l'objet pour disable le boutton
  fetch(
      API_URL + "interets/interetUtilisateurPourObjet/" + offre.objetDTO.idObjet
      + "?utilisateur=" + session.utilisateur.idUtilisateur, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: session.token
        }
      })
  .then((reponse) => {
    if (!reponse.ok) {
      throw new Error(
          "Code d'erreur : " + reponse.status + " : " + reponse.statusText
      );
    }
    if (reponse.statusText === "OK") {
      return reponse.json();
    }
  }).then((donne) => {
    if (donne) {
      document.querySelector("#marquer-interet").classList.add("disabled")
    }
  })

  // Marquage de l'intérêt
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
      .then((reponse) => {
        if (!reponse.ok) {
          document.querySelector("#marquer-interet").classList.remove("loading")
          throw new Error(
              "Code d'erreur : " + reponse.status + " : " + reponse.statusText
          );
        }
        return reponse.json();
      }).then(() => {
        let premierInter = setInterval(() => {
          document.querySelector("#marquer-interet").classList.remove("loading")
          document.querySelector("#marquer-interet").classList.add("disabled")
          Swal.fire({
            position: 'top-end',
            icon: 'success',
            title: 'Vous avez bien marqué votre intérêt',
            showConfirmButton: false,
            toast: true,
            timer: 3000,
            showClass: {
              popup: 'animate__animated animate__fadeInRight'
            },
            hideClass: {
              popup: 'animate__animated animate__fadeOutRight'
            }
          })
          clearInterval(premierInter);
        }, 1000)
        let deuxiemeInter = setInterval(() => {
          clearInterval(deuxiemeInter)
          Redirect("/")
        }, 5000)
      })
      .catch(err => surErreur(err))
    }
  })

  if (session.utilisateur.etatInscription === "Empêché") {
    document.querySelector("#marquer-interet").classList.add("disabled")
  }
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
    if (offres.length > 0) {
      offresPrecedentes = ""
      offres.forEach((off) => {
        let daOf = new Date(off.dateOffre[0], off.dateOffre[1] - 1,
            off.dateOffre[2]).toLocaleDateString("fr-BE")
        offresPrecedentes += `<p>${daOf}</p>`
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
          <h4>Date(s) précédente(s) de l'offre</h4>
          <p>${offresPrecedentes}</p>
        </div>
      </div>
      <div class="row">
        <div class="column">
          <h4>Disponibilités de l'offreur</h4>
          <p>${offre.plageHoraire}</p>
        </div>
      </div>
    </div>
    <div class="ui two column grid">
      <div class="column">
      <div class="two fields">
        <div class="field">
          <button id="modifier-offre" class="ui green button">Modifier votre offre</button>
        </div>
        <div class="field">
          <button id="annuler-offre" class="ui negative button">Annuler votre offre</button>
        </div>
      </div> 
      </div>
    </div>
    </div>
  </div>
  `
  pageDiv.innerHTML = offrePage
  if (offre.objetDTO.etatObjet === "Annulé") {
    document.querySelector("#annuler-offre").classList.add("disabled");
  }
  if (session.utilisateur.etatInscription === "Empêché") {
    document.querySelector("#modifier-offre").classList.add("disabled");
  }
  document.querySelector("#modifier-offre").addEventListener("click", () => {
    surDetailObjetProprioModifier(offre)
  })
  document.querySelector("#annuler-offre").addEventListener("click", () => {
    const session = recupUtilisateurDonneesSession()
    fetch(API_URL + "offres/annulerOffre", {
      method: "PUT",
      body: JSON.stringify(offre),
      headers: {
        "Content-Type": "application/json",
        "Authorization": session.token,
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
    if (offres.length > 0) {
      offresPrecedentes = ""
      offres.forEach((off) => {
        let daOf = new Date(off.dateOffre[0], off.dateOffre[1] - 1,
            off.dateOffre[2]).toLocaleDateString("fr-BE")
        offresPrecedentes += `<p>${daOf}</p>`
      })
    }
  })

  let offrePage = `
  <div class="ui container">
  <form class="ui form">
    <div class="ui two column grid">
      <div class="column">
        <img class="ui large rounded image" id="image" src="/api/offres/photos/${offre.objetDTO.photo}"/>
        <div class="ui two column grid" style="margin-top: 5px">
          <div class="column field">
            <input  name="ModifierPhoto" id="photo" type="file"/>
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
            <h4>Date de l'offre</h4>
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
          <input type="text" value="${offre.objetDTO.description}" id="description"/>
        </div>
        </div>
        <div class="column">
          <h4>Date(s) précédente(s) de l'offre</h4>
          ${offresPrecedentes}
        </div>
      </div>
      <div class="row">
        <div class="column">
        <div class="field">
          <h4>Disponibilités de l'offreur</h4>
          <input type="text" value="${offre.plageHoraire}" id="horaire"/>
         </div>
        </div>
      </div>
    </div>
    <div class="ui two column grid">
    <div class="column"></div>
      <div class="column">
      <div class="field">
      <div id="modifier-erreur" class="message-erreur"></div>
        <div class="two fields">
          <div class="field">
            <button id="confirmer" type="submit" class="ui positive button">Confirmer</button>
          </div>
          <div class="field">
            <button id="annuler" class="ui negative button">Annuler</button>
          </div>
        </div> 
          </div>
      </div>
      </div>
    </div>
  </form>
  </div>
  `
  pageDiv.innerHTML = offrePage
  let changerPhoto = document.getElementById("photo")
  changerPhoto.addEventListener("change", previsualiserPhoto)
  document.getElementById("confirmer").addEventListener("click", (e) => {
    e.preventDefault()
    envoiModification(offre)
  })
  document.querySelector("#annuler").addEventListener("click", () => {
    surDetailObjetProprio(offre)
  })
}

const envoiModification = async (offre) => {
  let session = recupUtilisateurDonneesSession()
  let description = document.querySelector("#description").value;
  let plageHoraire = document.querySelector("#horaire").value;
  let photo = document.querySelector("#image");
  let srcPhoto = photo.attributes.getNamedItem("src")
  let nomPhoto = offre.objetDTO.photo;
  let compNomPhoto = API_URL + "offres/photos/" + nomPhoto

  if (srcPhoto.value !== compNomPhoto) {
    nomPhoto = "donnamis.png"
    if (srcPhoto.value !== "#") {
      nomPhoto = await envoyerPhoto()
    }
  }
  if (description !== "" && plageHoraire !== "") {
    let objetModifie = {
      idObjet: offre.objetDTO.idObjet,
      offreur: offre.objetDTO.offreur,
      receveur: null,
      typeObjet: {
        idType: offre.objetDTO.typeObjet.idType,
        nom: offre.objetDTO.typeObjet.nom
      },
      etatObjet: offre.objetDTO.etatObjet,
      description: description,
      photo: nomPhoto,
      version: offre.objetDTO.version
    }

    let offreModifiee = {
      idOffre: offre.idOffre,
      objetDTO: objetModifie,
      plageHoraire: plageHoraire,
      dateOffre: offre.dateOffre,
      version: offre.version
    }

    const options = {
      method: "PUT",
      body: JSON.stringify(offreModifiee),
      headers: {
        "Content-Type": "application/json",
        Authorization: session.token,
      },
    }
    fetch(
        API_URL + 'offres/modifierOffre', options)
    .then((reponse) => {
      if (!reponse.ok) {
        throw new Error(
            "Code erreur : " + reponse.status + " : " + reponse.statusText
        );
      }
      return reponse.json()
    })
    .then((donnee) => {
      Swal.fire({
        position: 'top-end',
        icon: 'success',
        title: 'Vous avez bien modifié les informations de votre objet',
        showConfirmButton: false,
        toast: true,
        timer: 3000,
        showClass: {
          popup: 'animate__animated animate__fadeInRight'
        },
        hideClass: {
          popup: 'animate__animated animate__fadeOutRight'
        }
      })
      surDetailObjetProprio(donnee)
    })
  } else {
    let messageErreur = document.querySelector("#modifier-erreur");
    messageErreur.innerHTML = "Des champs ne peuvent pas être vides";
  }
}
// Permet de prévisualiser la photo avant de l'upload
const previsualiserPhoto = (e) => {
  let image = document.getElementById("image")
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

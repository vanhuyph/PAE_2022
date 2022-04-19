import {recupUtilisateurDonneesSession} from '../../utilitaires/session'
import {Redirect} from "../Router/Router";
import {API_URL} from "../../utilitaires/serveur";
import Swal from 'sweetalert2'
import 'animate.css'

const PageProfil = () => {
  let session = recupUtilisateurDonneesSession()
  if (!session) {
    Redirect('/connexion')
  }

  let idUtilisateur = session.utilisateur.idUtilisateur;
  fetch(API_URL + "utilisateurs/voirProfil/" + idUtilisateur, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: session.token
    },
  })
  .then((reponse) => {
    if (!reponse.ok) {
      throw new Error(
          "Error code : " + reponse.status + " : " + reponse.statusText
      );
    }
    return reponse.json();
  })
  .then((data) => surProfilUtilisateur(data))
}

// Affichage profil de l'utilisateur avec ses informations
const surProfilUtilisateur = (data) => {
  let session = recupUtilisateurDonneesSession()
  let boite = "Pas de boite"
  let gsm = "Pas de numéro"
  if (data.adresse.boite) {
    boite = data.adresse.boite
  }
  if (data.gsm) {
    gsm = data.gsm
  }

  let profil = `
  <div class="page-profil">
    <h2>Mon profil</h2>
    <form class="ui form">
      <div class="formulaire-profil">
        <div class="two fields">
          <div class="field">
            <div class="two fields">
              <div class="field">
                <label>Nom</label>
                <p>${data.nom}</p>
              </div>
              <div class="field">
                <label>Prénom</label>
                <p>${data.prenom}</p>
              </div>
            </div>
            <div class="two fields">
              <div class="field">
                <label>Pseudo</label>
                <p>${data.pseudo}</p>
              </div>
              <div class="field">
                <label>GSM</label>
                <p>${gsm}</p>
              </div>
            </div>
            <div class="field">
              <label>Mot de passe</label>
              <input type="password" disabled value="password" id="mdp-profil">
            </div>
            <div class="field">
                <button id="changer-mdp-profil" class="ui button inverted secondary">Changer mot de passe</button>
            </div>
            <div class="changer-mdp">
             <div class="field">
              <label for="mdp-actuel">Mot de passe actuel</label>
              <input type="password" name="mdp" id="mdp-actuel">
             </div> 
             <div class="field">
              <label for="nv-mdp">Nouveau mot de passe</label>
              <input type="password" name="nv-mdp" id="nv-mdp"> 
             </div> 
             <div class="field">
              <label for="conf-mdp">Confirmer nouveau mot de passe</label>  
              <input type="password" name="conf-mdp" id="conf-mdp">
             </div>
             <div class="field">
              <p class="message-erreur" id="mdp-erreur"></p>
             </div>
              <div class="two fields">
              <div class="field">
              </div>
              <div class="field">
                <button id="modifier-mdp" class="ui yellow button">Modifier mot de passe</button>
              </div>
            </div>
            </div>
          </div>
          <div class="field">
            <div class="rue">
            <div class="field">
              <label>Rue</label>
              <p>${data.adresse.rue}</p>
              </div>
            </div>
            <div class="two fields">
              <div class="field">
                <label>Numéro</label>
                <p>${data.adresse.numero}</p>
              </div>
              <div class="field">
                <label>Boite</label>
                <p>${boite}</p>
              </div>
            </div>
            <div class="two fields">
              <div class="field">
                <label>Code postal</label>
                <p>${data.adresse.codePostal}</p>
              </div>
              <div class="field">
                <label>Commune</label>
                <p>${data.adresse.commune}</p>
              </div>
            </div>
            <div class="field">
                <button id="modifier-profil" class="ui button inverted secondary">Modifier profil</button>
            </div>
          </div>
        </div>
      </div>
    </form>
  </div>
  `
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = profil;

  // Changement du mdp de l'utilisateur
  document.getElementById("changer-mdp-profil").addEventListener("click",
      (e) => {
        e.preventDefault()
        let mdpActuel = document.querySelector("#mdp-actuel")
        let nouvMdp = document.querySelector("#nv-mdp")
        let confMdp = document.querySelector("#conf-mdp")
        let msgErr = document.querySelector("#mdp-erreur")
        mdpActuel.value = ""
        nouvMdp.value = ""
        confMdp.value = ""
        msgErr.innerHTML = ""
        if (e.pointerId !== -1) {
          document.querySelector(".changer-mdp").classList.toggle(
              "montrer-block")
        }
        document.querySelector("#modifier-mdp").addEventListener("click",
            (e) => {
              e.preventDefault()
              if (mdpActuel.value === "" || nouvMdp.value === ""
                  || confMdp.value === "") {
                msgErr.innerHTML = "Des champs sont manquants"
              } else if (nouvMdp.value !== confMdp.value) {
                msgErr.innerHTML = "Les mots de passe ne correspondent pas"
              } else {
                let aEnvoyer = {
                  mdpActuel: mdpActuel.value,
                  nouvMdp: nouvMdp.value,
                  confNouvMdp: confMdp.value
                }
                fetch(
                    API_URL + "utilisateurs/modifierMdp/" + data.idUtilisateur,
                    {
                      method: "PUT",
                      body: JSON.stringify(aEnvoyer),
                      headers: {
                        "Content-Type": "application/json",
                        Authorization: session.token,
                      }
                    })
                .then((reponse) => {
                  if (!reponse.ok) {
                    throw new Error(
                        "Error code : " + reponse.status + " : "
                        + reponse.statusText
                        + " : " + reponse.text())
                  }
                  console.log(reponse)
                  return reponse.json()
                })
                .then((donnee) => {
                  Swal.fire({
                    position: 'top-end',
                    icon: 'success',
                    title: 'Votre mot de passe a bien été changé',
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
                  surProfilUtilisateur(donnee)
                })
                .catch((err) => {
                  console.log(err)
                  Swal.fire({
                    position: 'top-end',
                    icon: 'error',
                    title: 'Votre mot de passe n\'a pas pu etre changé',
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
                  if (err.message.includes('412')) {
                    document.querySelector(
                        "#mdp-erreur").innerHTML = "Mot de passe incorrect"
                  } else {
                    document.querySelector(
                        "#mdp-erreur").innerHTML = err.message
                  }
                })
              }
            })
      })
  document.getElementById("modifier-profil").addEventListener("click", (e) => {
    e.preventDefault()
    surModifierProfilUtilisateur(data)
  })
}

// Affiche les inputs pour pouvoir changer les informations du profil
const surModifierProfilUtilisateur = (data) => {
  let boite = ""
  let gsm = ""
  if (data.adresse.boite) {
    boite = data.adresse.boite
  }
  if (data.gsm) {
    gsm = data.gsm
  }

  let profilModifie = `
  <div class="page-profil">
    <h2>Modification du profil</h2>
    <form id="formulaire-profil" class="ui form">
      <div>
        <div class="two fields">
          <div class="field">
            <div class="two fields">
              <div class="field">
                <label>Nom</label>
                <input id="nom" type="text" name="nom" value="${data.nom}">
              </div>
              <div class="field">
                <label>Prénom</label>
                <input id="prenom" type="text" name="prenom" value="${data.prenom}">
              </div>
            </div>
            <div class="two fields">
              <div class="field">
                <label>Pseudo</label>
                <input id="pseudo" type="text" name="pseudo" value="${data.pseudo}">
              </div>
              <div class="field">
                <label>GSM</label>
                <input id="gsm" type="text" name="gsm" value="${gsm}">
              </div>
            </div>
            <div class="field">
              <label>Mot de passe</label>
              <input type="password" disabled name="mdp" value="password">
            </div>
          </div>
          <div class="field">
            <div class="rue">
            <div class="field">
              <label>Rue</label>
              <input id="rue" type="text" name="rue" value="${data.adresse.rue}">
              </div>
            </div>
            <div class="two fields">
              <div class="field">
                <label>Numéro</label>
                <input id="numero" type="number" name="numero" value=${data.adresse.numero}>
              </div>
              <div class="field">
                <label>Boite</label>
                <input id="boite" type="number" name="boite" value="${boite}">
              </div>
            </div>
            <div class="two fields">
              <div class="field">
                <label>Code postal</label>
                <input id="code-postal" type="number" name="codepostal" value=${data.adresse.codePostal}>
              </div>
              <div class="field">
                <label>Commune</label>
                <input id="commune" type="text" name="commune" value="${data.adresse.commune}">
              </div>
            </div>
           <div class="two fields">
              <div class="field">
                <button id="confirmer-modifier" type="submit" class="ui green button">Modifier</button>
              </div>
              <div class="field">
                <button id="annuler-modifier" class="ui red button">Annuler</button>
              </div>
            </div> 
            <div class="field">
              <p class="message-erreur" id="profil-erreur"></p>
             </div>
          </div>
        </div>
      </div>
    </form>
  </div>
  `
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = profilModifie;
  // Lors du clique sur le bouton annuler, renvoie vers le profil de l'utilisateur
  document.getElementById('annuler-modifier').addEventListener('click', () => {
    surProfilUtilisateur(data)
  })

  // Soumet les nouveaux changements du profil
  document.getElementById('formulaire-profil').addEventListener('submit',
      (e) => {
        e.preventDefault();
        let nouveauGsm = document.getElementById("gsm").value
        if (nouveauGsm === "") {
          nouveauGsm = null
        }

        let nouvelleAdresse = {
          idAdresse: data.adresse.idAdresse,
          rue: document.getElementById("rue").value,
          numero: document.getElementById("numero").value,
          boite: document.getElementById("boite").value,
          codePostal: document.getElementById("code-postal").value,
          commune: document.getElementById("commune").value,
          version: data.adresse.version
        }

        let utilisateur = {
          idUtilisateur: data.idUtilisateur,
          pseudo: document.getElementById("pseudo").value,
          nom: document.getElementById("nom").value,
          prenom: document.getElementById("prenom").value,
          gsm: nouveauGsm,
          estAdmin: data.estAdmin,
          etatInscription: data.etatInscription,
          commentaire: data.commentaire,
          adresse: nouvelleAdresse,
          version: data.version
        }

        let session = recupUtilisateurDonneesSession()
        fetch(API_URL + "utilisateurs", {
          method: "PUT",
          body: JSON.stringify(utilisateur),
          headers: {
            "Content-Type": "application/json",
            Authorization: session.token,
          }
        })
        .then((reponse) => {
          if (!reponse.ok) {
            throw new Error(
                "Error code : " + reponse.status + " : " + reponse.statusText
                + " : " + reponse.text())
          }
          console.log(reponse)
          return reponse.json()
        })
        .then((donnee) => {
          Swal.fire({
            position: 'top-end',
            icon: 'success',
            title: 'Votre profil a bien été modifié',
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
          Redirect("/profil")
        })
        .catch(err => surErreur(err))
      })
}

// Si erreur dans le fetch
const surErreur = (err) => {
  let messageErreur = document.querySelector("#profil-erreur");
  let erreurMessage = "";
  if (err.message.includes("400")) {
    erreurMessage = "Des champs ne peuvent pas être vides";
  } else if (err.message.includes("409")) {
    erreurMessage = "Ce pseudo est déjà utilisé";
  } else {
    erreurMessage = err.message;
  }
  messageErreur.innerText = erreurMessage;
}

export default PageProfil

import {recupUtilisateurDonneesSession} from '../../utilitaires/session'
import {Redirect} from "../Router/Router";


const PageProfil = () => {
    let session = recupUtilisateurDonneesSession()
    
    if(!session){
        Redirect('/connexion')
    }


    let pseudo = session.utilisateur.pseudo;
    fetch("/api/utilisateurs/" + pseudo, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            Authorization: session.token
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
    .then((data) => surProfilUtilisateur(data))
}

const surProfilUtilisateur = (data) => {
    console.log(data)
    let utilisateurPseudo = document.querySelector("#utilisateur-pseudo");
    let utilisateurNom = document.querySelector("#utilisateur-nom");
    let utilisateurPrenom = document.querySelector("#utilisateur-prenom");
    let utilisateurGsm = document.querySelector("#utilisateur-gsm");
    let utilisateurRue = document.querySelector("#utilisateur-rue");
    let utilisateurNumero = document.querySelector("#utilisateur-numero");
    let utilisateurBoite = document.querySelector("#utilisateur-boite");
    let utilisateurCommune = document.querySelector("#utilisateur-commune");
    let utilisateurCodePostal = document.querySelector(
        "#utilisateur-code-postal");
    let pseudoProfil = `Pseudo : ${data.pseudo}`;
    let nomProfil = `Nom : ${data.nom}`;
    let prenomProfil = `Prenom : ${data.prenom}`;
    let gsmProfil = `Téléphone : ${data.gsm}`;
    let rueProfil = `Rue : ${data.adresse.rue}`;
    let numeroProfil = `Numero : ${data.adresse.numero}`;
    let boiteProfil = `Boite : ${data.adresse.boite}`;
    let communeProfil = `Commmune : ${data.adresse.commune}`;
    let codePostalProfil = `Code postal : ${data.adresse.codePostal}`;
    utilisateurPseudo.innerHTML = pseudoProfil;
    utilisateurNom.innerHTML = nomProfil;
    utilisateurPrenom.innerHTML = prenomProfil;
    utilisateurGsm.innerHTML = gsmProfil;
    utilisateurRue.innerHTML = rueProfil;
    utilisateurNumero.innerHTML = numeroProfil;
    utilisateurBoite.innerHTML = boiteProfil;
    utilisateurCommune.innerHTML = communeProfil;
    utilisateurCodePostal.innerHTML = codePostalProfil;
}

export default PageProfil
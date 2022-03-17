import logo from "../../img/logo.png";
import {recupUtilisateurDonneesSession} from "../../utilitaires/session";

const Navbar = () => {
  const navbarWrapper = document.querySelector("#navbarWrapper");
  let navbar
  let utilisateur = recupUtilisateurDonneesSession()
  if (utilisateur && utilisateur.utilisateur.estAdmin === false) {
    navbar = `
    <nav class="navbar">
        <div class="conteneur-navbar">
            <a href="/" data-uri="/">
              <img src="${logo}" alt="logo donnamis" height="41px">
            </a>
            <ul class="nav-list">
                <li><a href="#" data-uri="/">Accueil</a></li>
                <li><a href="#" data-uri="/objet/">Créer une offre</a></li>
                <li><a href="#" data-uri="/deconnexion">Se déconnecter</a></li>
            </ul>
            <div class="menu"></div>
        </div>
        </nav>
    `;
  } else if (utilisateur && utilisateur.utilisateur.estAdmin === true) {
    navbar = `
    <nav class="navbar">
        <div class="conteneur-navbar">
            <a href="/" data-uri="/">
              <img src="${logo}" alt="logo donnamis" height="41px">
            </a>
            <ul class="nav-list">
                <li><a href="#" data-uri="/">Accueil</a></li>
                <li><a href="#" data-uri="/objet/">Créer une offre</a></li>
                <li><a href="#" data-uri="/">Admin</a></li>
                <li><a href="#" data-uri="/deconnexion">Se déconnecter</a></li>
            </ul>
            <div class="menu"></div>
        </div>
        </nav>
    `;
  } else {
    navbar = `
    <nav class="navbar">
        <div class="conteneur-navbar">
            <a href="/" data-uri="/">
              <img src="${logo}" alt="logo donnamis" height="41px">
            </a>
            <ul class="nav-list">
                <li><a href="#" data-uri="/">Accueil</a></li>
                <li><a href="#" data-uri="/connexion">Se connecter</a></li>
            </ul>
            <div class="menu"></div>
        </div>
        </nav>
    `;
  }
  navbarWrapper.innerHTML = navbar;
};

export default Navbar;

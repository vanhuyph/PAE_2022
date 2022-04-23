import logo from "../../img/donnamis.png";
import {recupUtilisateurDonneesSession} from "../../utilitaires/session";
import logoD from "../../img/icone.png"

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
                <li><a href="#" data-uri="/offrirObjet">Offrir un objet</a></li>
                <li class="li-menu">
                <div class="ui pointing top right dropdown">
                  <div class="avatar">
                    <img src="${logoD}" class="ui image">
                  </div>
                  <div class="menu">
                    <div class="item" data-uri="/profil">Profil</div>
                    <div class="item" data-uri="/mesOffres">Mes offres</div>
                    <div class="item" data-uri="/deconnexion">Se déconnecter</div>
                  </div>
                  </div>
                </li>
            </ul>
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
                <li><a href="#" data-uri="/offrirObjet">Offrir un objet</a></li>
                <li><a href="#" data-uri="/admin">Admin</a></li>
                <li class="li-menu">
                <div class="ui pointing top right dropdown">
                  <div class="avatar">
                    <img src="${logoD}" class="ui image">
                  </div>
                  <div class="menu">
                    <div class="item" data-uri="/profil">Profil</div>
                    <div class="item" data-uri="/mesOffres">Mes offres</div>
                    <div class="item" data-uri="/deconnexion">Se déconnecter</div>
                  </div>
                  </div>
                </li>
            </ul>
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
        </div>
        </nav>
    `;
  }
  navbarWrapper.innerHTML = navbar;
  if (utilisateur) {
    window.addEventListener("click", (e) => {
      if (document.querySelector(".menu").classList.contains("montrer-block")) {
        if (!document.querySelector(".menu").contains(e.target)
            && !document.querySelector(".li-menu").contains(e.target)) {
          document.querySelector(".menu").classList.remove("montrer-block")
        }
      }
    })
    document.querySelector(".li-menu").addEventListener("click", () => {
      document.querySelector(".menu").classList.toggle("montrer-block")
    })
  }
};

export default Navbar;

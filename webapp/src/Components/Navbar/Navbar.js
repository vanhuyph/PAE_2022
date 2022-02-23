import logo from "../../img/logo.png";
const Navbar = () => {
  const navbarWrapper = document.querySelector("#navbarWrapper");
  let navbar = `
  <nav class="navbar">
      <div class="conteneur-navbar">
          <a href="#" data-uri="/">
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
  navbarWrapper.innerHTML = navbar;
};

export default Navbar;

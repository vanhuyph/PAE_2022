import PageAccueil from "../Pages/PageAccueil";
import PageConnexion from "../Pages/PageConnexion";
import ComposantDeconnexion from "../Pages/ComposantDeconnexion";
import PageInscription from "../Pages/PageInscription";
import PageAdmin from "../Pages/PageAdmin";
import PageOffrirObjet from "../Pages/PageOffrirObjet";
import PageDetailsObjet from "../Pages/PageDetailsObjet";
import PageMesOffres from "../Pages/PageMesOffres";

// Configuration des routeurs ici
const routes = {
  "/": PageAccueil,
  "/connexion": PageConnexion,
  "/deconnexion": ComposantDeconnexion,
  "/inscription": PageInscription,
  "/admin": PageAdmin,
  "/offrirObjet": PageOffrirObjet,
  "/objet": PageDetailsObjet,
  "/mesOffres": PageMesOffres,
};

/**
 * Gère l'appel et le rendu automatique des composants fonctionnels suite à des événements de clics
 * sur la navbar, aux opérations de chargement / rafraîchissement, l'historique du navigateur (retour or avant) ou redirections.
 * Un composant fonctionnel est responsable de son auto-rendu : Pages, En-tête...
 */

const Router = () => {
  /* Gère le clic sur la navbar */
  let navbarWrapper = document.querySelector("#navbarWrapper");
  navbarWrapper.addEventListener("click", (e) => {
    // Pour obtenir un attribut de données par l'intermédiaire de l'objet dataset, il faut obtenir la propriété par la partie du nom de l'attribut après data (notez que les tirets sont convertis en camelCase).
    let uri = e.target.dataset.uri;
    if (uri) {
      e.preventDefault();
      /* Utilise l'API Historique Web pour ajouter l'URL de la page actuelle à l'historique de navigation de l'utilisateur
       & définir la bonne URL dans le navigateur (au lieu de "#") */
      window.history.pushState({}, uri, window.location.origin + uri);
      /* Fait un rendu du composant demandé
      NB : pour les composants qui incluent du JS, nous voulons nous assurer que le JS inclus
      ne soit pas exécuté lorsque le fichier JS est chargé par le navigateur
      donc, ces composants doivent être soit une fonction, soit une classe. */
      const componentToRender = routes[uri];
      if (routes[uri]) {
        componentToRender();
      } else {
        throw Error("La " + uri + " ressource n'existe pas");
      }
    }
  });

  /* Achemine le bon composant lorsque la page est chargée / rafraîchie */
  window.addEventListener("load", (e) => {
    let chemins = window.location.pathname.split("/")
    const componentToRender = routes["/" + chemins[1]];
    if (!componentToRender) {
      throw Error(
          "La " + window.location.pathname + " ressource n'existe pas"
      );
    }
    if (chemins[2]) {
      componentToRender(chemins[2])
    } else {
      componentToRender();
    }
  });

  // Achemine le bon composant lorsque l'utilisateur utilise l'historique de navigation.
  window.addEventListener("popstate", () => {
    let chemins = window.location.pathname.split("/")
    const componentToRender = routes["/" + chemins[1]];
    if (chemins[2]) {
      componentToRender(chemins[2])
    } else {
      componentToRender();
    }
  });
};

/**
 * Appel et fait un rendu automatique des composants fonctionnels associés à l'URL donnée.
 * @param {*} uri - Fournit une URL associée à un composant fonctionnel dans le tableau
 * tableau des routes du routeur
 */

const Redirect = (uri, data) => {
  // Utilise l'API Historique Web pour ajouter l'URL de la page actuelle à l'historique de navigation de l'utilisateur et définir la bonne URL dans le navigateur (au lieu de "#")
  if (!data) {
    window.history.pushState({}, uri, window.location.origin + uri);
  } else {
    window.history.pushState({}, uri + "/" + data,
        window.location.origin + uri + "/" + data);
  }
  // Rend le composant demandé
  const componentToRender = routes[uri];
  if (routes[uri]) {
    if (!data) {
      componentToRender()
    } else {
      componentToRender(data)
    }
  } else {
    throw Error("La " + uri + " ressource n'existe pas");
  }
};

export {Router, Redirect};

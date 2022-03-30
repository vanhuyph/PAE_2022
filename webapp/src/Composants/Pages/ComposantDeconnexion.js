import {enleverDonneeSession} from "../../utilitaires/session"
import Navbar from "../Navbar/Navbar";
import {Redirect} from "../Router/Router";

const ComposantDeconnexion = () => {
  enleverDonneeSession()
  Navbar()
  Redirect("/")
}

<<<<<<< HEAD
export default ComposantDeconnexion;
=======
export default ComposantDeconnexion;
>>>>>>> 7c87eaba1a636c9f1ce51d2578b509b32dcd7e5f

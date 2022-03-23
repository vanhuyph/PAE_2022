import {enleverDonneeSession} from "../../utilitaires/session"
import Navbar from "../Navbar/Navbar";
import {Redirect} from "../Router/Router";

const ComposantDeconnexion = () => {
  enleverDonneeSession()
  Navbar()
  Redirect("/")
}

export default ComposantDeconnexion;
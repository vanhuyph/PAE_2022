import {enleverDonneeSession} from "../../utils/session"
import Navbar from "../Navbar/Navbar";
import {Redirect} from "../Router/Router";

const PageDeconnexion = () => {
  enleverDonneeSession()

  Navbar()
  Redirect("/")
}
export default PageDeconnexion;
import "./stylesheets/style.css";
import Navbar from "./Composants/Navbar/Navbar";
import {Router} from "./Composants/Router/Router";
import {verifierTokenAuChargement} from "./utilitaires/session";
import Footer from "./Composants/Footer/Footer";

verifierTokenAuChargement();
Navbar();
Router(); // Le routeur va automatiquement charger la page root
Footer();
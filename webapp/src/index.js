import "./stylesheets/style.css"; // If you prefer to style your app with vanilla CSS rather than with Bootstrap
import Navbar from "./Composants/Navbar/Navbar";
import {Router} from "./Composants/Router/Router";

Navbar();

Router(); // The router will automatically load the root page

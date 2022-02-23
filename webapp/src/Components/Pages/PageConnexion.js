

const PageConnexion = () => {
  const pageDiv = document.querySelector("#page");

  let pageCon = `
    <div class="page-connexion">
        <h2>Connexion</h2>
        <form class="formulaire-connexion">
            <label for="pseudo">Pseudo</label>
            <div class="pseudo-conteneur">
                <input type="text" id="pseudo" class="pseudo">
            </div>
            <label for="mdp">Mot de passe</label>
            <div class="mdp-conteneur">
                <input type="password" id="mdp" class="mdp">
                
            </div>
            <button class="connexion" type="submit">CONNEXION</button>
            <p class="separateur-ou">ou</p>
            <button class="insciption">S'INSCRIRE</button>
        </form>
    </div>
  `;
  pageDiv.innerHTML = pageCon;
}

export default PageConnexion
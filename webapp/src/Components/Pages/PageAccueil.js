/**
 * Render the PageAccueil
 */

let modal = `
<div class="conteneur-modal">
  <div class="overlay declancheur-modal"></div>
  <div class="modal">
    <button class="fermer-modal declancheur-modal">X</button>
    <h2>Votre inscription est en "attente"</h2>
    <p>Commentaire de l'inscription</p>  
  </div>
</div>
`

const PageAccueil = () => {
  const pageDiv = document.querySelector("#page");

  pageDiv.innerHTML = modal;

  const conteneurModal = document.querySelector(".conteneur-modal")
  const declancheurModal = document.querySelectorAll(".declancheur-modal")

  declancheurModal.forEach(decl => decl.addEventListener("click", () => {
    conteneurModal.classList.toggle("active")
  }))

};

export default PageAccueil;

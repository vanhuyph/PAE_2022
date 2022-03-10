
const barVertical = `
<div id="bar-vertical" class="ui left sidebar visible vertical menu">
<a class="fermer-bar">X</a>
<h2 class="ui large header">Admin</h2>
  <div class="contenu-bar-vert">
    <a class="item active">Inscription</a>
    <a class="item">Membres</a>
  </div>
</div>
`

const pricipal = `
<div id="principal">
  <button class="burger">☰</button>
  <div id="choix-page" class="ui buttons">
    <button id="demandes" class="ui positive button">Demandes</button>
    <button id="refus" class="ui button">Refus</button>
  </div>
  <div class="contenu">
    
  </div>
</div>
`

const page = `
<div>
${barVertical}
${pricipal}
</div>

`

const PageAdmin = () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = page;

  // Gestion responsive
  document.querySelector(".burger").addEventListener("click", () => {
    document.getElementById("bar-vertical").style.width = "35% !important";
    document.querySelector(".fermer-bar").addEventListener("click", () => {
      //document.getElementById("bar-vertical").style.display = "0 !important";
    })

  })

}

export default PageAdmin;
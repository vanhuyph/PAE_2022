
const barVertical = `
<div id="bar-vertical" class="ui left sidebar visible vertical menu">
<h2 class="ui large header">Admin</h2>
  <div class="contenu-bar-vert">
    <a class="item active">Inscription</a>
    <a class="item">Membres</a>
  </div>
</div>
`
const barHori = `
<div id="bar-hori">
  <h2 class="ui large header">Admin</h2>
  <div id="choix-page" class="ui buttons">
    <button class="ui active button">Inscription</button>
    <button class="ui button">Membres</button>
  </div>
</div>
`

const pricipal = `
<div id="principal">
  <div id="choix-demande" class="ui buttons">
    <button id="demandes" class="ui positive button">Demandes</button>
    <button id="refus" class="ui button">Refus</button>
  </div>
  <div id="contenu">
    
  </div>
</div>
`

const page = `
<div class="page-admin">
<div id="bar">
  ${barVertical}
</div>
${pricipal}
</div>



`

const PageAdmin = () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = page;

  // Gestion responsive

  if(window.innerWidth < 576){
    document.getElementById("bar").innerHTML = barHori;
  }else{
    document.getElementById("bar").innerHTML = barVertical;
  }
  window.onresize = () => {
    if(window.innerWidth < 576){
      document.getElementById("bar").innerHTML = barHori;
    }else{
      document.getElementById("bar").innerHTML = barVertical;
    }
  };

  const demandesPage = document.querySelector("#demandes")
  const refusPage = document.querySelector("#refus")
  const contenu = document.querySelector("#contenu")

  const demandes = "<h2>Liste des demandes d'inscriptions</h2>"
  contenu.innerHTML= demandes
      demandesPage.addEventListener("click", () => {
    refusPage.classList.remove("positive")
    demandesPage.classList.add("positive")
    contenu.innerHTML= demandes
  })
  refusPage.addEventListener("click", () => {
    demandesPage.classList.remove("positive")
    refusPage.classList.add("positive")
    contenu.innerHTML= "<h2>Liste des inscriptions refusées</h2>"
  })


}

export default PageAdmin;
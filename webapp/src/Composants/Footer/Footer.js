import logo from '../../img/DonnamisBlanc.png'

const Footer = () => {
  const footer = document.getElementById("footer")
  footer.innerHTML = `
 <div class="ui inverted vertical footer segment">
    <div class="ui center aligned container">
      <div class="ui stackable inverted divided grid">
      <div class="eight wide column">
          <img src="${logo}" class="ui centered small image" alt="donnamis">
          <br>
          <p>©2022 Site développé par le groupe 15 PAE.</p>
        </div>
        <div class="eight wide column">
          <h4 class="ui inverted header">Groupe 15</h4>
          <div class="ui inverted link list">
            <a href="mailto: abdenour.didi@student.vinci.be" class="item">Didi Abdenour</a>
            <a href="mailto: nicolas.nederlandt@student.vinci.be" class="item">Nederlandt Nicolas</a>
            <a href="mailto: vanhuy.pham@student.vinci.be" class="item">Pham Van-Huy</a>
            <a href="mailto: leandro.silvadecarval@student.vinci.be" class="item">Silva De Carvalho Leandro</a>
            <a href="mailto: bastien.verstappen@student.vinci.be" class="item">Verstappen Bastien</a>
          </div>
        </div>
      </div>
    </div>
  </div>
  `
}

export default Footer

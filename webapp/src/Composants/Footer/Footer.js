import logo from '../../img/donnamis.png'

const Footer = () => {
  const footer = document.getElementById("footer")
  footer.innerHTML = `
  <div class="ui center aligned basic segment">
    <div class="ui internally celled grid">
      <div id="footer-logo" class="eight wide column">
        <img src="${logo}" width="200px">
        <p>Développé par le groupe 15</p>
      </div>
      <div class="eight wide column">
        <p><a href = "mailto: abdenour.didi@student.vinci.be">Abdenou Didi</a></p>
        <p><a href = "mailto: bastien.verstappen@student.vinci.be">Bastien Verstappen</a></p>
        <p><a href = "mailto: leandro.silvadecarval@student.vinci.be">Leandro Silva de Carvalho</a></p>
        <p><a href = "mailto: nicolas.nederlandt@student.vinci.be">Nicolas Nederlandt</a></p>
        <p><a href = "mailto: vanhuy.pham@student.vinci.be">Van Huy Pham</a></p> 
      </div>
    </div>
  </div>
</div>
  `
}
export default Footer
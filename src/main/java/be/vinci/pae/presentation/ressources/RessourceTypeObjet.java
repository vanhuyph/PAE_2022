package be.vinci.pae.presentation.ressources;

import be.vinci.pae.business.typeobjet.TypeObjetDTO;
import be.vinci.pae.business.typeobjet.TypeObjetUCC;
import be.vinci.pae.presentation.ressources.filtres.Autorisation;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Singleton
@Path("/typesObjet")
public class RessourceTypeObjet {

  @Inject
  private TypeObjetUCC typeObjetUCC;

  /**
   * Liste les types d'objet.
   *
   * @return la liste des types d'objet
   */
  @GET
  @Path("liste")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public List<TypeObjetDTO> listerTypeObjet() {
    List<TypeObjetDTO> typesObjet;
    typesObjet = typeObjetUCC.listerTypeObjet();
    return typesObjet;
  }

}
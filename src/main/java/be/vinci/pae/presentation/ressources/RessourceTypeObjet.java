package be.vinci.pae.presentation.ressources;

import be.vinci.pae.business.typeobjet.TypeObjetDTO;
import be.vinci.pae.business.typeobjet.TypeObjetUCC;
import be.vinci.pae.presentation.ressources.filtres.Autorisation;
import be.vinci.pae.utilitaires.exceptions.PresentationException;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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




  @POST
  @Path("/creerTypeObjet")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public String creerTypeObjet(JsonNode json) {
    String nom = json.get("nomType").asText();
    System.out.println("nomType in ressource Type Objet :" + nom);
    if (nom.isBlank() || nom.isEmpty()) {
      throw new PresentationException("Le nom du nouveau type d'objet ne peut pas etre vide", Response.Status.BAD_REQUEST);
    }
    String  nomNouveauTypeObjet = typeObjetUCC.creerTypeObjet(nom);
    return nomNouveauTypeObjet;
  }
}

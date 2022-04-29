package be.vinci.pae.presentation.ressources;

import be.vinci.pae.business.typeobjet.TypeObjetDTO;
import be.vinci.pae.business.typeobjet.TypeObjetUCC;
import be.vinci.pae.presentation.ressources.filtres.Autorisation;
import be.vinci.pae.utilitaires.exceptions.PresentationException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
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
   * @return liste : la liste des types d'objet
   */
  @GET
  @Path("liste")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public List<TypeObjetDTO> listerTypeObjet() {
    List<TypeObjetDTO> liste;
    liste = typeObjetUCC.listerTypeObjet();
    return liste;
  }

  /**
   * Créer un nouveau type d'objet.
   *
   * @param typeObjetDTO : type d'objet reç du formulaire créer un type d'objet
   * @return typeObjetDTO : le type d'objet créé
   * @throws PresentationException : est lancée si le type d'objet est vide
   */
  @POST
  @Path("/creerTypeObjet")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public TypeObjetDTO creerTypeObjet(TypeObjetDTO typeObjetDTO) {
    if (typeObjetDTO.getNom().isBlank() || typeObjetDTO.getNom().isEmpty()) {
      throw new PresentationException("Le nom du nouveau type d'objet ne peut pas être vide",
          Response.Status.BAD_REQUEST);
    }
    typeObjetDTO = typeObjetUCC.creerTypeObjet(typeObjetDTO);
    return typeObjetDTO;
  }

}

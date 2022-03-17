package be.vinci.pae.presentation.ressources;

import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.objet.ObjetUCC;
import be.vinci.pae.presentation.ressources.filtres.Autorisation;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


@Singleton
@Path("/objets")
public class RessourceObjet {


  @Inject
  private ObjetUCC objetUCC;

  //ajouter l'authorize une fois que l'uc fonctionne complètement sans

  /**
   * Creer un objet.
   *
   * @param json : json reçu du formulaire de connexion *
   * @return noeud : l'objet json contenant le token et l'utilisateur *
   * @throws WebApplicationException si type de l'objet ou description manquant
   */
  @POST
  @Path("creerObjet")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public ObjetDTO creerObjet(JsonNode json) {

    if (!json.hasNonNull("offreur")
        || !json.hasNonNull("typeObjet")
        || !json.hasNonNull("description")
    ) {

      throw new WebApplicationException(
          Response.status(Response.Status.BAD_REQUEST)
              .entity("type de l'objet ou description manquant").type("text/plain").build());
    }

    int idOffreur = json.get("offreur").asInt();
    int typeObjet = json.get("typeObjet").asInt();
    String description = json.get("description").asText();
    // String photo = json.get("photo").asText();
    // pour l'instant je hardcore une string "photo" dans l'appele à l'ucc

    System.out.println("elements du JSON récupérés dans attribut ");
    //peut etre que c'est mieux de test si la photo est null
    // dans le json avant d'essayer de la convertir inutiliement

    ObjetDTO objet = objetUCC.creerUnObjet(idOffreur, typeObjet, description, 1, "photo");
    if (objet == null) {
      throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity("L'ajout de l'objet a échoué").type(MediaType.TEXT_PLAIN)
          .build());
    }
    //ObjetDTO objetDTO = Json.filtrePublicJsonVue((ObjetDTO) objet,
    //  ObjetDTO.class);

    //ObjectNode noeud = jsonMapper.createObjectNode().putPOJO("objetDTO", objetDTO);
    return objet;
  }

}

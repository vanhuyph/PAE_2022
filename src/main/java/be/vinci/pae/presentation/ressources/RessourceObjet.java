package be.vinci.pae.presentation.ressources;

import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.objet.ObjetUCC;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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


  private static final ObjectMapper jsonMapper = new ObjectMapper();
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
  public ObjectNode creerObjet(JsonNode json) {
    if (!json.hasNonNull("idOffreur")
        || !json.hasNonNull("typeObjet")
        || !json.hasNonNull("description")
    ) {
      throw new WebApplicationException(
          Response.status(Response.Status.BAD_REQUEST)
              .entity("type de l'objet ou description manquant").type("text/plain").build());
    }

    System.out.println("la methode a passee la web App exception");
    Integer idOffreur = json.get("idOffreur").asInt();
    String typeObjet = json.get("typeObjet").asText();
    String description = json.get("description").asText();
    // String photo = json.get("photo").asText();
    // pour l'instant je hardcore une string "photo" dans l'appele à l'ucc

    System.out.println("elements du JSON récupérés dans attribut ");
    //peut etre que c'est mieux de test si la photo est null
    // dans le json avant d'essayer de la convertir inutiliement

    ObjetDTO objetDTO = objetUCC.creerUnObjet(idOffreur, typeObjet, description, 1, "photo");
    if (objetDTO == null) {
      throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity("L'ajout de l'objet a échoué").type(MediaType.TEXT_PLAIN)
          .build());
    }

    ObjectNode noeud = jsonMapper.createObjectNode().putPOJO("objetDTO", objetDTO);
    return noeud;
  }

}

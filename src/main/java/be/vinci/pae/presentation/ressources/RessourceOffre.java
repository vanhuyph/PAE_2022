package be.vinci.pae.presentation.ressources;

import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.business.offre.OffreUCC;
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
@Path("/offres")
public class RessourceOffre {


  private static final ObjectMapper jsonMapper = new ObjectMapper();
  @Inject
  private OffreUCC offreUCC;

  /**
   * Methode qui créé une offre
   *
   * @param json : json reçu du formulaire de connexion *
   * @return noeud : l'objet json contenant le token et l'utilisateur *
   * @throws WebApplicationException si id_objet ou plage_horaire sont null ou si l'offre n'a pas
   *                                 été créée
   */
  @POST
  @Path("creerOffre")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode creerObjet(JsonNode json) {
    if (!json.hasNonNull("id_objet") || !json.hasNonNull("plage_horaire")
    ) {
      throw new WebApplicationException(
          Response.status(Response.Status.BAD_REQUEST)
              .entity("type de l'objet ou description manquant").type("text/plain").build());
    }

    Integer id_objet = json.get("id_objet").asInt();
    String plage_horaire = json.get("plage_horaire").asText();

    OffreDTO offreDTO = offreUCC.creerUneOffre(id_objet, plage_horaire);
    if (offreDTO == null) {
      throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity("L'ajout de l'offre a échoué").type(MediaType.TEXT_PLAIN)
          .build());
    }

    ObjectNode noeud = jsonMapper.createObjectNode().putPOJO("offreDTO", offreDTO);
    return noeud;
  }

}

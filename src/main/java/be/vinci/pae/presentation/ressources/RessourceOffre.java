package be.vinci.pae.presentation.ressources;

import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.business.offre.OffreUCC;
import be.vinci.pae.presentation.ressources.filtres.Autorisation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;


@Singleton
@Path("/offres")
public class RessourceOffre {


  private static final ObjectMapper jsonMapper = new ObjectMapper();
  @Inject
  private OffreUCC offreUCC;

  /**
   * Creer une offre.
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
  public ObjectNode creerOffre(JsonNode json) {
    if (!json.hasNonNull("idObjet") || !json.hasNonNull("plageHoraire")
    ) {
      throw new WebApplicationException(
          Response.status(Response.Status.BAD_REQUEST)
              .entity("type de l'objet ou description manquant").type("text/plain").build());
    }

    Integer idObjet = json.get("idObjet").asInt();
    String plageHoraire = json.get("plageHoraire").asText();

    OffreDTO offreDTO = offreUCC.creerUneOffre(idObjet, plageHoraire);
    if (offreDTO == null) {
      throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity("L'ajout de l'offre a échoué").type(MediaType.TEXT_PLAIN)
          .build());
    }

    ObjectNode noeud = jsonMapper.createObjectNode().putPOJO("offreDTO", offreDTO);
    return noeud;
  }

  /**
   * Liste les offres.
   *
   * @return noeud : la liste des offres
   */
  @GET
  @Path("listOffres")
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public List<OffreDTO> listOffres() {
    List<OffreDTO> offreDTO = offreUCC.listOffres();
    if (offreDTO == null) {
      throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity("Liste des offres a echoué").type(MediaType.TEXT_PLAIN)
          .build());
    }
    return offreDTO;
  }


  /**
   * Liste les offres recentes.
   *
   * @return noeud : la liste des offres recentes
   */
  @GET
  @Path("listOffresRecent")
  @Produces(MediaType.APPLICATION_JSON)
  public List<OffreDTO> listOffresRecent() {
    List<OffreDTO> offreDTO = offreUCC.listOffresRecent();
    if (offreDTO == null) {
      throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity("Liste des offres a echoué").type(MediaType.TEXT_PLAIN)
          .build());
    }
    return offreDTO;
  }


}

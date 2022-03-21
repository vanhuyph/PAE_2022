package be.vinci.pae.presentation.ressources;

import be.vinci.pae.business.objet.ObjetDTO;
import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.business.offre.OffreUCC;
import be.vinci.pae.presentation.ressources.filtres.Autorisation;
import be.vinci.pae.utilitaires.exceptions.PresentationException;
import com.fasterxml.jackson.databind.JsonNode;
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

  @Inject
  private OffreUCC offreUCC;

  /**
   * Créer une offre.
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
  //@Authorisation
  public OffreDTO creerOffre(JsonNode json) {
    ObjetDTO nObjet = creerObjet(json);
    if (!json.hasNonNull("plageHoraire")) {
      throw new WebApplicationException(
          Response.status(Response.Status.BAD_REQUEST)
              .entity("Type de l'objet ou description manquant").type("text/plain").build());
    }
    int idObjet = nObjet.getIdObjet();
    String plageHoraire = json.get("plageHoraire").asText();
    OffreDTO offreDTO = offreUCC.creerUneOffre(idObjet, plageHoraire);
    if (offreDTO == null) {
      throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity("L'ajout de l'offre a échoué").type(MediaType.TEXT_PLAIN)
          .build());
    }
    return offreDTO;
  }

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

    ObjetDTO objet = offreUCC.creerUnObjet(idOffreur, typeObjet, description, 1, "photo");
    if (objet == null) {
      throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity("L'ajout de l'objet a échoué").type(MediaType.TEXT_PLAIN)
          .build());
    }
    //ObjetDTO objetDTO = Json.filtrePublicJsonVue((ObjetDTO) objet,
    //  ObjetDTO.class);

    //ObjectNode noeud = jsonMapper.createObjectNode().putPOJO("objetDTO", objetDTO);
    System.out.println("fin creerObjet");

    return objet;

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
      throw new PresentationException("Liste des offres a echoué",Status.BAD_REQUEST);
    }
    return offreDTO;
  }


}

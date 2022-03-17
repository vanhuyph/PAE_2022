package be.vinci.pae.presentation.ressources;

import be.vinci.pae.business.objet.ObjetUCC;
import be.vinci.pae.business.offre.OffreDTO;
import be.vinci.pae.business.offre.OffreUCC;
import be.vinci.pae.presentation.ressources.filtres.Autorisation;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/offres")
public class RessourceOffre {

  @Inject
  private OffreUCC offreUCC;

  @Inject
  private ObjetUCC objetUCC;

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
  public OffreDTO creerOffre(JsonNode json) {
    if (!json.hasNonNull("idObjet") || !json.hasNonNull("plageHoraire")) {
      throw new WebApplicationException(
          Response.status(Response.Status.BAD_REQUEST)
              .entity("Type de l'objet ou description manquant").type("text/plain").build());
    }
    int idObjet = json.get("idObjet").asInt();
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
   * Recherche l'offre avec l'id id.
   *
   * @param json    json contenant le token.
   * @param idOffre id de l'offre
   * @return l'offre correspondant a l'id
   */
  @GET
  @Path("voirDetailsOffre/{idOffre}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Autorisation
  public OffreDTO rechercheOffreParId(JsonNode json, @PathParam("idOffre") int idOffre) {
    if (idOffre <= 0) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("L'offre n'existe pas").type(MediaType.TEXT_PLAIN)
          .build());
    }
    OffreDTO offreDTO = offreUCC.rechercheParId(idOffre);
    if (offreDTO == null) {
      throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity("L'offre n'a pas été trouvée").type(MediaType.TEXT_PLAIN)
          .build());
    }
    return offreDTO;

  }

}

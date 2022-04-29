package be.vinci.pae.presentation.ressources.filtres;

import be.vinci.pae.business.utilisateur.UtilisateurDTO;
import be.vinci.pae.business.utilisateur.UtilisateurUCC;
import be.vinci.pae.utilitaires.Config;
import be.vinci.pae.utilitaires.exceptions.NonAutoriseException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;

public abstract class AutorisationAbstraite {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getPropriete("JWTSecret"));
  private final JWTVerifier jwtVerifier = JWT.require(this.jwtAlgorithm).withIssuer("auth0")
      .build();
  @Inject
  private UtilisateurUCC utilisateurUCC;

  /**
   * Récupère un utilisateur depuis un token ou annule la requête en cas d'erreur.
   *
   * @param contexteRequete : contient dans le header le token en tant que "Authorization"
   * @return un utilisateur ou null si le token n'est pas présent
   */
  public UtilisateurDTO tokenDecode(ContainerRequestContext contexteRequete) {
    String token = contexteRequete.getHeaderString("Authorization");
    if (token == null) {
      contexteRequete.abortWith(Response.status(Response.Status.UNAUTHORIZED)
          .entity("Un token est nécessaire pour accéder à cette ressource").build());
      return null;
    } else {
      return siTokenDecode(token);
    }
  }

  /**
   * Récupère un utilisateur depuis un token.
   *
   * @param token : contient l'ID de l'utilisateur
   * @return un utilisateur ou null si l'utilisateur n'existe pas
   * @throws NonAutoriseException : est lancée si le token est expiré ou malformé
   */
  public UtilisateurDTO siTokenDecode(String token) {
    DecodedJWT tokenDecode;
    try {
      tokenDecode = this.jwtVerifier.verify(token);
    } catch (TokenExpiredException e) {
      throw new NonAutoriseException("Token expiré");
    } catch (Exception e) {
      System.out.println("token: " + token);
      throw new NonAutoriseException("Token malformé");
    }
    return utilisateurUCC.rechercheParId(tokenDecode.getClaim("utilisateur").asInt());
  }

}

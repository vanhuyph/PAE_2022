package be.vinci.pae.presentation.ressources.utilitaires;

import be.vinci.pae.vue.Vues;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {

  private static final ObjectMapper jsonMapper = new ObjectMapper();

  /**
   * Sérialise un objet java en json.
   *
   * @param item        : l'objet à sérialiser
   * @param classeCible : la classe ciblé faisant référence à l'objet
   * @return : l'objet sérialisé
   */
  public static <T> T filtrePublicJsonVue(T item, Class<T> classeCible) {
    try {
      String publicItemCommeString =
          jsonMapper.writerWithView(Vues.Public.class).writeValueAsString(item);
      return jsonMapper.readerWithView(Vues.Public.class).forType(classeCible)
          .readValue(publicItemCommeString);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }

}

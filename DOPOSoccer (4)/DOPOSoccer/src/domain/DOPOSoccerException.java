
package domain;
/**
 * Representa las excepciones del juego
 */
public class DOPOSoccerException extends RuntimeException {
    public static final String INVALID_DAMAGE = "El daño no puede ser negativo.";
    public static final String INVALID_STATISTICS = "Las estadísticas deben estar entre 1 y 10.";
    public static final String INVALID_PLAYER = "El jugador no es válido.";
    public static final String INVALID_CHARACTER = "El personaje no es válido.";
    public static final String INVALID_GAME_STATE = "La operación no se puede realizar en el estado actual del juego.";
    public static final String INVALID_BALL = "El radio del balón debe ser mayor que cero.";
    public static final String INVALID_FIELD = "Las dimensiones de la cancha deben ser mayores que cero.";
    public static final String INVALID_GOAL = "Las dimensiones de la portería deben ser mayores que cero.";
    public static final String INVALID_MATCH_CONFIGURATION = "La configuración de la partida no es válida.";
    public static final String INVALID_POSITION = "La posición del jugador no es válida.";

    public DOPOSoccerException(String message) {
        super(message);
    }
}

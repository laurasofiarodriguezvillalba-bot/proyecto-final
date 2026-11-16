package domain;

/**
 * Poder especial Tornado de Nigeria
 */
public class Tornado extends Power {

    /**
     * Crea el poder Tornado
     */
    public Tornado() {
        super("Tornado", 3.0, 8, 40);
    }

    /**
     * Ejecuta el disparo del tornado
     * @param user jugador que activa el poder
     * @param rival jugador contrario
     * @param ball balón de la partida
     */
    @Override
    public void activate(Player user, Player rival, Ball ball) {
        double direction;
        if (user.getX() < rival.getX()) {
            direction = 1;
        } else {
            direction = -1;
        }
        ball.setSpeed(direction * 15, 3);
    }

    /**
     * Deja inconsciente al rival cuando recibe el impacto
     * @param user jugador que utiliza el poder
     * @param rival jugador que recibe el efecto
     * @param field campo de juego
     */
    @Override
    public void applyEffect(Player user, Player rival, Field field) {
        rival.becomeUnconscious((int) getDuration());
    }
}
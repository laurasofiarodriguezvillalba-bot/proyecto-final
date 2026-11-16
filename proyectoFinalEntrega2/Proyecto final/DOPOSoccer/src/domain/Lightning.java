package domain;

/**
 * Poder especial Rayo de Camerún
 */
public class Lightning extends Power {
    /**
     * Crea el poder Rayo
     */
    public Lightning() {
        super("Rayo", 3.0, 12, 60);
    }

    /**
     * Ejecuta el disparo eléctrico del rayo
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
        ball.setSpeed(direction * 25, 0);
    }

    /**
     * Electrocuta al rival cuando recibe el impacto
     * @param user jugador que utiliza el poder
     * @param rival jugador que recibe el efecto
     * @param field campo de juego
     */
    @Override
    public void applyEffect(Player user, Player rival, Field field) {
        rival.electrocute((int) getDuration());
    }
}
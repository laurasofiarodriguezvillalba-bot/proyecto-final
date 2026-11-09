package domain;

/**
 * Poder especial Tornado de Nigeria
 */
public class Tornado extends Power {
    public Tornado() {
        super("Tornado", 3.0, 8, 40);
    }

    /**
     * Ejecuta el efecto del tornado
     * @param user jugador que activa el poder
     * @param rival jugador contrario que recibe el efecto
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
        rival.takeDamage(getDamage());
        rival.becomeUnconscious((int) getDuration());
    }
}
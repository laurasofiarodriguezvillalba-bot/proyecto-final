package domain;

/**
 * Poder especial Rayo de Camerún
 */
public class Lightning extends Power {
    public Lightning() {
        super("Rayo", 3.0, 12, 60);
    }

    /**
     * Ejecuta el disparo electrico del rayo
     * @param user jugador que activa el poder
     * @param rival jugador contrario que recibe el daño
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
        rival.takeDamage(getDamage());
        rival.electrocute((int) getDuration());
    }
}
package domain;

/**
 * Poder especial Ilusión de Estados Unidos
 */
public class Illusion extends Power {

    public Illusion() {
        super("Ilusión", 2.5, 6, 30);
    }

    /**
     * Ejecuta el efecto de ilusión
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
        ball.setSpeed(direction * 20, 0);
        rival.takeDamage(getDamage());
        rival.updateEffects(0);
    }
}
package domain;

/**
 * Poder especial Aura Azul de Corea del Sur
 */
public class BlueAura extends Power {

    /**
     * Crea el poder Aura Azul
     */
    public BlueAura() {
        super("Aura Azul", 1.5, 8, 40);
    }

    /**
     * Ejecuta el disparo horizontal de Aura Azul
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
        ball.setSpeed(direction * 20, 0);
    }

    /**
     * Empuja al rival hacia su propio arco
     * @param user jugador que utiliza el poder
     * @param rival jugador que recibe el efecto
     * @param field campo de juego
     */
    public void applyEffect(Player user, Player rival, Field field) {
        double direction;
        if (user.getX() < rival.getX()) {
            direction = 1;
        } else {
            direction = -1;
        }
        rival.push(direction * 30, field);
    }
}
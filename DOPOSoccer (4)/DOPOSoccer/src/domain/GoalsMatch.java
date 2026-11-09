
package domain;

/**
 * Representa un partido que termina al alcanzar una cantidad determinada de goles
 */
public class GoalsMatch extends DOPOSoccer {
    private int elapsedSeconds;

    /**
     * Crea un partido por goles.
     * @param player1 primer jugador
     * @param player2 segundo jugador
     * @param ball balón
     * @param field cancha
     * @param goal1 portería izquierda
     * @param goal2 portería derecha
     * @param goalLimit cantidad de goles para ganar
     */
    public GoalsMatch(Player player1, Player player2, Ball ball, Field field, Goal goal1, Goal goal2, int goalLimit) {
        super(player1, player2, ball, field, goal1, goal2, goalLimit);
        elapsedSeconds = 0;
    }

    /**
     * Registra un gol y comprueba si termina el partido
     *  @param player jugador que marcó el gol
     */
    @Override
    public void registerGoal(Player player) {
        if (getState() != GameState.PLAYING) {
            return;
        }
        if (player != getPlayer1() && player != getPlayer2()) {
            throw new DOPOSoccerException(DOPOSoccerException.INVALID_PLAYER);
        }
        player.scoreGoal();
        if (player.getGoals() >= getMatchLimit()) {
            finish();
        }
    }

    /**
     * Actualiza el tiempo transcurrido
     */
    @Override
    public void updateTime() {
        if (getState() == GameState.PLAYING) {
            elapsedSeconds++;
        }
    }

    /**
     * Devuelve el tiempo que se muestra en la partida
     * @return tiempo transcurrido
     */
    @Override
    public String getTimeText() {
        int minutes = elapsedSeconds / 60;
        int seconds = elapsedSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
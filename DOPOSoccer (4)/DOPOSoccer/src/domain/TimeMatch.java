
package domain;

/**
 * Representa un partido que tiene un límite de tiempo
 */
public class TimeMatch extends DOPOSoccer {
    private int remainingSeconds;
    private boolean suddenDeath;

    /**
     * Crea un partido por tiempo
     * @param player1 primer jugador
     * @param player2 segundo jugador
     * @param ball balon
     * @param field cancha
     * @param goal1 porteria izquierda
     * @param goal2 porteria derecha
     * @param minutes duración del partido en minutos
     */
    public TimeMatch(Player player1, Player player2, Ball ball, Field field, Goal goal1, Goal goal2, int minutes) {
        super(player1, player2, ball, field, goal1, goal2, minutes);
        if (minutes <= 0) {
            throw new DOPOSoccerException(DOPOSoccerException.INVALID_MATCH_CONFIGURATION);
        }
        remainingSeconds = minutes * 60;
        suddenDeath = false;
    }

    /**
     * Registra un gol y comprueba si termina el partido
     * @param player jugador que marca el gol
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

        // El primer gol en muerte súbita termina el partido
        if (suddenDeath) {
            finish();
        }
    }

    /**
     * Actualiza el tiempo restante del partido
     */
    @Override
    public void updateTime() {
        if (getState() != GameState.PLAYING || suddenDeath) {
            return;
        }
        if (remainingSeconds > 0) {
            remainingSeconds--;
        }
        if (remainingSeconds == 0) {
            if (getPlayer1().getGoals() == getPlayer2().getGoals()) {
                suddenDeath = true;
            } else {
                finish();
            }
        }
    }

    /**
     * Obtiene el tiempo que se muestra en la partida
     * @return tiempo restante
     */
    @Override
    public String getTimeText() {
        if (suddenDeath) {
            return "Muerte súbita";
        }
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Comprueba si el partido se encuentra en muerte súbita
     * @return true si está en muerte súbita o false si no.
     */
    public boolean isSuddenDeath() {
        return suddenDeath;
    }
}
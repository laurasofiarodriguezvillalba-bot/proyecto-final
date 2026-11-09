
package domain;

/**
 * Representa las estadísticas y habilidades de un jugador
*/
 
public class Statistics {
    private int speed;
    private int kick;
    private int jump;
    private int survival;

    /**
     * Crea las estadísticas iniciales del jugador
     */
    public Statistics() {
        speed = 1;
        kick = 1;
        jump = 1;
        survival = 1;
    }

    /**
     * Calcula el multiplicador de daño de las patadas
     * @return multiplicador de daño
     */
    public double getKickMultiplier() {
        return 1 + 0.10 * (kick - 1);
    }

    /**
     * Calcula la vida máxima del jugador según el nivel de Survival
     * @return vida máxima del jugador
     */
    public int getMaxLife() {
        return (int) (100 * (1 + 0.10 * (survival - 1)));
    }

    /**
     * Calcula el daño de una patada normal
     * @return daño de la patada
     */
    public int getKickDamage() {
        return (int) Math.round(10 * getKickMultiplier());
    }

    /**
     * Obtiene el nivel de velocidad del jugador
     * @return nivel de Speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Obtiene el nivel de daño de las patadas del jugador
     * @return nivel de Kick
     */
    public int getKick() {
        return kick;
    }

    /**
     * Obtiene el nivel de salto del jugador
     * @return nivel de Jump
     */
    public int getJump() {
        return jump;
    }

    /**
     * Obtiene el nivel de supervivencia del jugador
     * @return nivel de Survival
     */
    public int getSurvival() {
        return survival;
    }

    /**
     * Modifica las estadísticas del jugador
     * @param speed nuevo nivel de velocidad
     * @param kick nuevo nivel de patada
     * @param jump nuevo nivel de salto
     * @param survival nuevo nivel de supervivencia
     */
    public void setStatistics(int speed, int kick, int jump, int survival) {
        if (speed < 1 || speed > 10 || kick < 1 || kick > 10 || jump < 1 || jump > 10 || 
            survival < 1 || survival > 10) {
            throw new DOPOSoccerException (DOPOSoccerException.INVALID_STATISTICS);
        }
        this.speed = speed;
        this.kick = kick;
        this.jump = jump;
        this.survival = survival;
    }
}
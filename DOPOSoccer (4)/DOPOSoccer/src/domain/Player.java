
package domain;

/**
 * Representa un jugador de DOPOSoccer.
 * Controla su movimiento, estadísticas, vida, goles, saltos y efectos especiales durante la partida
 */
public class Player {
    private String name;
    private SoccerCharacter character;
    private Statistics statistics;
    private int goals;
    private int currentLife;
    private double x;
    private double y;
    private double speedY;
    private double groundY;
    private boolean jumping;
    private boolean electrocuted;
    private boolean unconscious;
    private boolean stunned;
    private int effectTimeRemaining;

    /**
     * Crea un jugador con su nombre y personaje seleccionado
     * @param name nombre del jugador
     * @param character personaje seleccionado
     */
    public Player(String name, SoccerCharacter character) {
        if (name == null || name.isEmpty() || character == null) {
            throw new DOPOSoccerException(DOPOSoccerException.INVALID_PLAYER);
        }
        this.name = name;
        this.character = character;
        statistics = new Statistics();
        goals = 0;
        currentLife = statistics.getMaxLife();
        x = 0;
        y = 0;
        speedY = 0;
        groundY = 0;
        jumping = false;
        electrocuted = false;
        unconscious = false;
        stunned = false;
        effectTimeRemaining = 0;
    }

    /**
     * Mueve al jugador hacia la izquierda
     * @param field cancha de la partida
     */
    public void moveLeft(Field field) {
        if (field == null) {
            throw new DOPOSoccerException(DOPOSoccerException.INVALID_FIELD);
        }
        if (unconscious || stunned) {
            return;
        }
        double movement = statistics.getSpeed();
        if (electrocuted) {
            movement *= 0.5;
        }
        x -= movement;
        if (x < field.getLeftLimit()) {
            x = field.getLeftLimit();
        }
    }

    /**
     * Mueve al jugador hacia la derecha
     * @param field cancha de la partida
     */
    public void moveRight(Field field) {
        if (field == null) {
            throw new DOPOSoccerException(DOPOSoccerException.INVALID_FIELD);
        }
        if (unconscious || stunned) {
            return;
        }
        double movement = statistics.getSpeed();
        if (electrocuted) {
            movement *= 0.5;
        }
        x += movement;
        if (x > field.getRightLimit()) {
            x = field.getRightLimit();
        }
    }

    /**
     * Establece la posición del jugador en la cancha
     * @param x coordenada horizontal del jugador
     * @param y coordenada vertical del jugador
     * @param field cancha 
     */
    public void setPosition(double x, double y, Field field) {
        if (field == null || x < field.getLeftLimit() || x > field.getRightLimit() ||
            y < field.getTopLimit() || y > field.getGroundY()) {
            throw new DOPOSoccerException(DOPOSoccerException.INVALID_POSITION);
        }
        this.x = x;
        this.y = y;
        groundY = y;
        speedY = 0;
        jumping = false;
    }

    /**
     * Reduce la vida del jugador al recibir daño
     * @param damage cantidad de daño recibido
     */
    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new DOPOSoccerException(DOPOSoccerException.INVALID_DAMAGE);
        }
        currentLife -= damage;
        if (currentLife <= 0) {
            currentLife = 0;
            stunned = true;
        }
    }

    /**
     * Registra un gol del jugador
     */
    public void scoreGoal() {
        goals++;
    }

    /**
     * Restablece la vida del jugador
     */
    public void resetLife() {
        currentLife = statistics.getMaxLife();
    }

    /**
     * Obtiene el nombre del jugador
     * @return nombre del jugador
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene el personaje seleccionado
     * @return personaje del jugador
     */
    public SoccerCharacter getCharacter() {
        return character;
    }

    /**
     * Obtiene las estadísticas del jugador
     * @return estadísticas del jugador
     */
    public Statistics getStatistics() {
        return statistics;
    }

    /**
     * Obtiene los goles anotados
     * @return cantidad de goles
     */
    public int getGoals() {
        return goals;
    }

    /**
     * Obtiene la vida actual del jugador
     * @return vida actual
     */
    public int getCurrentLife() {
        return currentLife;
    }

    /**
     * Obtiene la posición horizontal del jugador
     * @return coordenada X del jugador
     */
    public double getX() {
        return x;
    }

    /**
     * Obtiene la posición vertical del jugador
     * @return coordenada Y del jugador
     */
    public double getY() {
        return y;
    }

    /**
     * Hace que el jugador salte
     */
    public void jump() {
        if (!jumping && !electrocuted && !unconscious && !stunned) {
            speedY = -5 * statistics.getJump();
            jumping = true;
        }
    }

    /**
     * Actualiza la posición vertical del jugador durante el salto
     */
    public void updateJump() {
        if (jumping) {
            y += speedY;
            speedY += 0.5;

            if (y >= groundY) {
                y = groundY;
                speedY = 0;
                jumping = false;
            }
        }
    }

    /**
     * Calcula el daño de una patada normal
     * @return daño causado por la patada
     */
    public int kick() {
        if (unconscious || stunned) {
            return 0;
        }
        return statistics.getKickDamage();
    }

    /**
     * Activa el efecto de electrocución
     * @param duration duración del efecto en segundos
     */
    public void electrocute(int duration) {
        electrocuted = true;
        effectTimeRemaining = duration * 1000;
    }

    /**
     * Activa el efecto de inconsciencia
     * @param duration duración del efecto en segundos
     */
    public void becomeUnconscious(int duration) {
        unconscious = true;
        effectTimeRemaining = duration * 1000;
    }

    /**
     * Activa el efecto de empuje
     * @param force fuerza y dirección del empuje
     */
    public void push(double force) {
        if (!unconscious && !stunned) {
            x += force;
        }
    }

    /**
     * Actualiza la duración de los efectos temporales
     * @param elapsedTime tiempo transcurrido en milisegundos
     */
    public void updateEffects(int elapsedTime) {
        if (effectTimeRemaining > 0) {
            effectTimeRemaining -= elapsedTime;

            if (effectTimeRemaining <= 0) {
                effectTimeRemaining = 0;
                electrocuted = false;
                unconscious = false;
            }
        }
    }
}
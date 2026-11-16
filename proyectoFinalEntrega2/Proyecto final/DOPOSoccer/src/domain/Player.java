
package domain;

/**
 * Representa un jugador de DOPOSoccer
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
    private boolean powerActive;
    private int effectTimeRemaining;
    private int stunnedTimeRemaining;
    private boolean confused;

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
        powerActive = false;
        effectTimeRemaining = 0;
        stunnedTimeRemaining = 0;
        confused = false;
    }

    /**
     * Mueve al jugador hacia la izquierda
     * @param field cancha de la partida
     */
    public void moveLeft(Field field) {
        if (field == null) {
            throw new DOPOSoccerException(DOPOSoccerException.INVALID_FIELD);
        }
        if (unconscious || stunned || confused) {
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
        if (unconscious || stunned || confused) {
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
     * @param x coordenada horizontal
     * @param y coordenada vertical
     * @param field cancha
     */
    public void setPosition(double x, double y, Field field) {
        if (field == null || x < field.getLeftLimit() || x > field.getRightLimit() || y < field.getTopLimit() || y > field.getGroundY()) {
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
        if (stunned) {
            return;
        }
        currentLife -= damage;
        if (currentLife <= 0) {
            currentLife = 0;
            stunned = true;
            stunnedTimeRemaining = 3000;
            electrocuted = false;
            confused = false;
            unconscious = false;
            powerActive = false;
            effectTimeRemaining = 0;
            jumping = false;
            speedY = 0;
        }
    }

    /**
     * Registra un gol del jugador
     */
    public void scoreGoal() {
        goals++;
    }

    /**
     * Restablece la vida del jugador a su vida máxima
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
     * Obtiene el personaje seleccionado por el jugador
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
     * Obtiene la cantidad de goles marcados por el jugador
     * @return número de goles
     */
    public int getGoals() {
        return goals;
    }

    /**
     * Obtiene la vida actual del jugador
     * @return puntos de vida actuales
     */
    public int getCurrentLife() {
        return currentLife;
    }

    /**
     * Obtiene la vida máxima del jugador según su estadística Survival
     * @return cantidad maxima de puntos de vida
     */
    public int getMaxLife() {
        return statistics.getMaxLife();
    }

    /**
     * Obtiene la coordenada horizontal del jugador
     * @return posición horizontal
     */
    public double getX() {
        return x;
    }

    /**
     * Obtiene la coordenada vertical del jugador
     * @return posición vertical
     */
    public double getY() {
        return y;
    }

    /**
     * Indica si el jugador está saltando
     * @return true si está saltando
     */
    public boolean isJumping() {
        return jumping;
    }

    /**
     * Indica si el jugador está electrocutado
     * @return true si está electrocutado
     */
    public boolean isElectrocuted() {
        return electrocuted;
    }

    /**
     * Indica si el jugador está inconsciente
     * @return true si está inconsciente
     */
    public boolean isUnconscious() {
        return unconscious;
    }

    /**
     * Indica si el jugador está aturdido
     * @return true si está aturdido
     */
    public boolean isStunned() {
        return stunned;
    }

    /**
     * Indica si el jugador tiene el aura de poder activa
     * @return true si tiene el aura activa
     */
    public boolean hasPowerActive() {
        return powerActive;
    }

    /**
     * Activa el aura del poder
     */
    public void activatePowerAura() {
        if (!powerActive && !electrocuted && !unconscious && !stunned && !confused) {
            powerActive = true;
        }
    }

    /**
     * Desactiva el aura cuando el jugador ejecuta su tiro de poder
     */
    public void deactivatePowerAura() {
        powerActive = false;
    }

    /**
     * Hace que el jugador salte
     */
    public void jump() {
        if (!jumping && !electrocuted && !unconscious && !stunned && !confused) {
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
        if (unconscious || stunned || confused) {
            return 0;
        }
        return statistics.getKickDamage();
    }

    /**
     * Activa el efecto de electrocución
     * @param duration duración del efecto en segundos
     */
    public void electrocute(int duration) {
        if (duration <= 0 || stunned) {
            return;
        }
        unconscious = false;
        confused = false;
        electrocuted = true;
        effectTimeRemaining = duration * 1000;
    }

    /**
     * Activa el efecto de inconsciencia y lanza al jugador al aire
     * @param duration duración del efecto en segundos
     */
    public void becomeUnconscious(int duration) {
        if (duration <= 0 || stunned) {
            return;
        }
        electrocuted = false;
        confused = false;
        unconscious = true;
        effectTimeRemaining = duration * 1000;
        //Lanza al jugador hacia arriba.
        jumping = true;
        speedY = -15;
    }

    /**
     * Empuja al jugador hacia una dirección
     * @param force fuerza y dirección del empuje
     * @param field cancha de la partida
     */
    public void push(double force, Field field) {
        if (!unconscious && !stunned && !confused && field != null) {
            x += force;
            if (x < field.getLeftLimit()) {
                x = field.getLeftLimit();
            }
            if (x > field.getRightLimit()) {
                x = field.getRightLimit();
            }
        }
    }

    /**
     * Actualiza la duración de los efectos temporales y del aturdimiento
     * @param elapsedTime tiempo transcurrido en milisegundos
     */
    public void updateEffects(int elapsedTime) {
        if (elapsedTime < 0) {
            throw new IllegalArgumentException("El tiempo no puede ser negativo");
        }
        if (effectTimeRemaining > 0) {
            effectTimeRemaining -= elapsedTime;
            if (effectTimeRemaining <= 0) {
                effectTimeRemaining = 0;
                electrocuted = false;
                confused = false;
                unconscious = false;
            }
        }
        if (stunned) {
            stunnedTimeRemaining -= elapsedTime;
            if (stunnedTimeRemaining <= 0) {
                stunnedTimeRemaining = 0;
                stunned = false;
                resetLife();
            }
        }
    }

    /**
     * Indica si el jugador está confundido
     * @return true si está confundido
     */
    public boolean isConfused() {
        return confused;
    }

    /**
     * Confunde al jugador durante un tiempo determinado
     * @param duration duración del efecto en segundos
     */
    public void becomeConfused(double duration) {
        if (duration <= 0 || stunned) {
            return;
        }
        electrocuted = false;
        unconscious = false;
        confused = true;
        effectTimeRemaining = (int) (duration * 1000);
        // Detener cualquier salto anterior.
        jumping = false;
        speedY = 0;
    }
}
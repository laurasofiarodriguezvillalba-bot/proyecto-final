package domain;

/**
 * Representa un poder especial de un personaje
 */
public abstract class Power {
    private String name;
    private double duration;
    private int damage;
    private int shotDamage;

    /**
     * Construye un poder especial
     */
    public Power(String name, double duration, int damage, int shotDamage) {
        if (name == null || name.isEmpty() || duration <= 0 || damage <= 0 || shotDamage <= 0) {
            throw new IllegalArgumentException("La configuración del poder no es válida.");
        }
        this.name = name;
        this.duration = duration;
        this.damage = damage;
        this.shotDamage = shotDamage;
    }

    /**
     * Obtiene el nombre del poder especial
     * @return nombre del poder
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene la duración del efecto del poder
     * @return duración en segundos
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Obtiene el daño que causa el efecto del poder al rival
     * @return cantidad de daño
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Obtiene el daño base que causa el tiro especial
     * @return daño base del tiro
     */
    public int getShotDamage() {
        return shotDamage;
    }

    /**
     * Calcula el daño final del tiro especial
     * @param kickMultiplier multiplicador de patada del jugador
     * @return daño final
     */
    public int calculateDamage(double kickMultiplier) {
        return (int) Math.round(shotDamage * kickMultiplier);
    }

    /**
     * Lanza el balón como un tiro especial
     * @param user jugador que activa el poder
     * @param rival jugador contrario
     * @param ball balón de la partida
     */
    public abstract void activate(Player user, Player rival, Ball ball);

    /**
     * Aplica el efecto cuando el tiro especial golpea al rival
     * @param user jugador que utiliza el poder
     * @param rival jugador que recibe el efecto
     */
    public abstract void applyEffect(Player user, Player rival, Field field);

    /**
     * Actualiza el efecto del poder
     * @param elapsedTime tiempo transcurrido en milisegundos
     */
    public void updateEffect(int elapsedTime) {
    }

    /**
     * Indica si el efecto del poder está activo
     * @return true si el efecto está activo
     */
    public boolean isEffectActive() {
        return false;
    }

    /**
     * Finaliza el efecto del poder
     */
    public void endEffect() {
    }
}
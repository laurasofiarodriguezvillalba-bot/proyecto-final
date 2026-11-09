package domain;

public abstract class Power {
    private String name;
    private double duration;
    private int damage;
    private int shotDamage;

    /**
     * Crea un poder especial
     * @param name nombre del poder
     * @param duration duración del efecto en segundos
     * @param damage daño real original
     * @param shotDamage daño ajustado del tiro de poder
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
     * Obtiene la duración del efecto especial
     * @return duración del efecto en segundos
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Obtiene el daño real original del podeer
     * @return damage daño real original
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Devuelve el daño ajustado del tiro de poder
     * @return daño ajustado del tiro
     */
    public int getShotDamage() {
        return shotDamage;
    }

    /**
     * Calcula el daño final según el multiplicador de patada
     * @param kickMultiplier multiplicador de patada del jugador
     * @return daño final calculado
     */
    public int calculateDamage(double kickMultiplier) {
        return (int) Math.round(shotDamage * kickMultiplier);
    }

    /**
     * Ejecuta el efecto especial correspondiente a cada poder
     * @param user jugador que activa el poder
     * @param rival jugador contrario que recibe el efecto
     * @param ball balón de la partida
     */
    public abstract void activate(Player user, Player rival, Ball ball);
}
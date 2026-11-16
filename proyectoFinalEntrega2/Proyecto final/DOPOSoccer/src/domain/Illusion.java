package domain;

/**
 * Poder especial Ilusión de Estados Unidos
 */
public class Illusion extends Power {
    private boolean illusionActive;
    private boolean confusionStarted;
    private int effectTimeRemaining;
    private Ball[] illusionBalls;
    private Field illusionField;

    /**
     * Crea el poder Ilusión
     */
    public Illusion() {
        super("Ilusión", 2.5, 6, 30);
        illusionActive = false;
        confusionStarted = false;
        effectTimeRemaining = 0;
        illusionBalls = new Ball[7];
        illusionField = null;
    }

    /**
     * Ejecuta el disparo de ilusión y crea los siete balones adicionales
     * @param user jugador que activa el poder
     * @param rival jugador contrario
     * @param ball balón verdadero de la partida
     */
    @Override
    public void activate(Player user, Player rival, Ball ball) {
        double direction;
        if (user.getX() < rival.getX()) {
            direction = 1;
        } else {
            direction = -1;
        }
        //Lanzar el balón verdadero
        ball.setSpeed(direction * 20, 0);

        //Crear los siete balones de ilusión
        double[] horizontalSpeeds = {-12, -8, -4, 5, 9, 13, 16};
        double[] verticalSpeeds = {-9, -5, -12, -7, -3, -10, -6};

        for (int i = 0; i < illusionBalls.length; i++) {
            illusionBalls[i] = new Ball(ball.getX(), ball.getY(), ball.getRadius());
            illusionBalls[i].setSpeed(horizontalSpeeds[i], verticalSpeeds[i]);
        }

        illusionActive = true;
        confusionStarted = false;
        effectTimeRemaining = 0;
    }

    /**
     * Confunde al rival cuando recibe el impacto
     * @param user jugador que utiliza el poder
     * @param rival jugador que recibe el efecto
     * @param field campo de juego
     */
    @Override
    public void applyEffect(Player user, Player rival, Field field) {
        rival.becomeConfused(getDuration());
        confusionStarted = true;
        effectTimeRemaining = (int) (getDuration() * 1000);
    }

    /**
     * Actualiza el movimiento y la duración de los balones de ilusión
     * @param elapsedTime tiempo transcurrido en milisegundos
     */
    @Override
    public void updateEffect(int elapsedTime) {
        if (!illusionActive) {
            return;
        }
        //Actualizar el movimiento de los siete balones
        if (illusionField != null) {
            for (int i = 0; i < illusionBalls.length; i++) {
                if (illusionBalls[i] != null) {
                    illusionBalls[i].move(illusionField);
                }
            }
        }
        //Controlar la duración cuando el rival recibe el impacto
        if (confusionStarted) {
            effectTimeRemaining -= elapsedTime;
            if (effectTimeRemaining <= 0) {
                endEffect();
            }
        }
    }

    /**
     * Indica si deben mostrarse los ocho balones
     * @return true si la ilusión está activa
     */
    @Override
    public boolean isEffectActive() {
        return illusionActive;
    }

    /**
     * Finaliza la ilusión
     */
    @Override
    public void endEffect() {
        illusionActive = false;
        confusionStarted = false;
        effectTimeRemaining = 0;
    }

    /**
     * Asigna la cancha donde se moverán los balones 
     * @param field cancha del partido
     */
    public void setField(Field field) {
        illusionField = field;
    }

    /**
     * Obtiene los siete balones de ilusión
     * @return arreglo de balones 
     */
    public Ball[] getIllusionBalls() {
        return illusionBalls;
    }

    /**
     * Indica si la ilusión está activa
     * @return true si la ilusión está activa
     */
    public boolean isIllusionActive() {
        return illusionActive;
    }
}
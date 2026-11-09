package domain;

/**
 * Representa el balón del partido y controla su posición, velocidad y movimiento
 */
public class Ball {
    private double x;
    private double y;
    private double speedX;
    private double speedY;
    private double radius;
    private double gravity;
    private boolean initialFall;
    private boolean moving;

    /**
     * Crea un balón en una posición determinada.
     * @param x posición horizontal
     * @param y posición vertical
     * @param radius radio del balón.
     * @throws DOPOSoccerException si el radio es menor o igual a cero
     */
    public Ball(double x, double y, double radius) {
        if (radius <= 0) { 
            throw new DOPOSoccerException(DOPOSoccerException.INVALID_BALL);
        }
        this.x = x;
        this.y = y;
        this.radius = radius;
        speedX = 0;
        speedY = 0;
        gravity = 0.3;
        initialFall = true;
        moving = false;
    }

    /**
     * Actualiza la posición del balón
     */
    public void move() {
        x += speedX;
        y += speedY;
        speedY += gravity;
        if (speedX > 0) {
            speedX -= 0.05;
            if (speedX < 0) {
                speedX = 0;
            }
        }
        else if (speedX < 0) {
            speedX += 0.05;
            if (speedX > 0) {
                speedX = 0;
            }
        }
    }

    /**
     * Hace que el balón rebote cuando toca el suelo
     *  @param groundY posición vertical del suelo
     */
    public void bounceOnGround(double groundY) {
        if (y + radius >= groundY) {
            y = groundY - radius;
            speedY = -Math.abs(speedY) * 0.7;
            if (Math.abs(speedY) < 0.2) {
                speedY = 0;
            }
        }
    }

    /**
     * Hace que el balón rebote contra los bordes laterales
     */
    public void bounceOnWall() {
        speedX = -speedX;
    }

    /**
     * Establece la velocidad del balón
     * @param speedX velocidad horizontal
     * @param speedY velocidad vertical
     */
    public void setSpeed(double speedX, double speedY) {
        this.speedX = speedX;
        this.speedY = speedY;
        moving = true;
        initialFall = false;
    }

    /**
     * Devuelve la posición horizontal del balón
     * @return posición horizontal
     */
    public double getX() {
        return x;
    }

    /**
     * Devuelve la posición vertical del balón
     * @return posición vertical
     */
    public double getY() {
        return y;
    }

    /**
     * Devuelve la velocidad horizontal del balón
     * @return velocidad horizontal
     */
    public double getSpeedX() {
        return speedX;
    }

    /**
     * Devuelve la velocidad vertical del balón
     * @return velocidad vertical
     */
    public double getSpeedY() {
        return speedY;
    }

    /**
     * Devuelve el radio del balón
     * @return radio del balón
     */
    public double getRadius() {
        return radius;
    }

    /**
    * Mueve el balón y controla sus rebotes contra los límites de la cancha
    * * @param field cancha
    */
    public void move(Field field) {
        // Caida inicial del balón
        if (initialFall) {
            y += speedY;
            speedY += gravity;
            if (y + radius >= 350) {
                y = 350 - radius;
                speedY = -Math.abs(speedY) * 0.7;
                if (Math.abs(speedY) < 1) {
                    speedY = 0;
                    initialFall = false;
                }
            }
            return;
        }

        // Si el balón no ha sido pateado, permanece quieto
        if (!moving) {
            return;
        }
        move();

        // Rebote contra el borde izquierdo
        if (x - radius < field.getLeftLimit()) {
            x = field.getLeftLimit() + radius;
            speedX = Math.abs(speedX);
        }

        // Rebote contra el borde derecho
        if (x + radius > field.getRightLimit()) {
            x = field.getRightLimit() - radius;
            speedX = -Math.abs(speedX);
        }

        // Rebote contra el borde superior
        if (y - radius < field.getTopLimit()) {
            y = field.getTopLimit() + radius;
            speedY = Math.abs(speedY);
        }

        // Rebote contra el suelo
        bounceOnGround(field.getGroundY());
    }

    /**
     * Establece la posición del balón
     * @param x nueva posicion horizontal
     * @param y nueva posición vertical
     */
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Reinicia la posición y el movimiento del balón
     */
    public void reset() {
        x = 490;
        y = 100;
        speedX = 0;
        speedY = 0;
        initialFall = true;
        moving = false;
    }
}

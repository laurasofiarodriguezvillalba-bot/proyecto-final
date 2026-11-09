package domain;

/**
 * Representa una portería dentro de la cancha
 */
public class Goal {
    private double x;
    private double y;
    private double width;
    private double height;

    /**
     * Crea una porteria en una posición determinada
     */
    public Goal(double x, double y, double width, double height) {
        if (width <= 0 || height <= 0) {
            throw new DOPOSoccerException(DOPOSoccerException.INVALID_GOAL);
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Comprueba si el balón entró a la porteria
     */
    public boolean isGoal(Ball ball) {
        return ball.getX() + ball.getRadius() >= x && ball.getX() - ball.getRadius() <= x + width &&
               ball.getY() + ball.getRadius() >= y && ball.getY() - ball.getRadius() <= y + height;
    }

    /**
     * Obtiene la posición horizontal de la porteria
     * @return coordenada X de la porteria
     */
    public double getX() {
        return x;
    }

    /**
     * Obtiene la posición vertical de la porteria
     * @return coordenada Y de la porteria
     */
    public double getY() {
        return y;
    }

    /**
     * Obtiene el ancho de la porteria
     * @return ancho de la porteria
     */
    public double getWidth() {
        return width;
    }

    /**
     * Obtiene el alto de la porteria
     * @return alto de la porteria
     */
    public double getHeight() {
        return height;
    }
}
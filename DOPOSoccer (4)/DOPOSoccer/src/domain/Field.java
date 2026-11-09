package domain;

/**
 * Representa la cancha del partido
 */
public class Field {
    private double width;
    private double height;

    /**
     * Construye la cancha con un ancho y alto determinado
     * @param width ancho de la cancha
     * @param height alto de la cancha
     */
    public Field(double width, double height) {
        if (width <= 0 || height <= 0) {
            throw new DOPOSoccerException(DOPOSoccerException.INVALID_FIELD);
        }
        this.width = width;
        this.height = height;
    }

    /**
     * Obtiene el ancho de la cancha
     * @return ancho de la cancha
     */
    public double getWidth() {
        return width;
    }

    /**
     * Obtiene el alto de la cancha
     * @return alto de la cancha
     */
    public double getHeight() {
        return height;
    }

    /**
     * Obtiene la coordenada vertical del suelo de la cancha
     * @return coordenada Y del suelo
     */
    public double getGroundY() {
        return 350;
    }

    /**
     * Obtiene el límite izquierdo de la cancha
     * @return coordenada X del límite izquierdo
     */
    public double getLeftLimit() {
        return 0;
    }

    /**
     * Obtiene el límite derecho de la cancha
     * @return coordenada X del límite derecho
     */
    public double getRightLimit() {
        return width;
    }

    /**
     * Obtiene el límite superior de la cancha
     * @return coordenada Y del límite superior
     */
    public double getTopLimit() {
        return 0;
    }
}
package domain;

/**
 * Clase padre abstracta de los personajes
 */
public abstract class SoccerCharacter {
    private String country;
    private Power power;

    /**
     * Crea un personaje con su país y poder especial
     * @param country país del personaje
     * @param power poder especial del personaje
     */
    public SoccerCharacter(String country, Power power) {
        if (country == null || country.isEmpty() || power == null) {
            throw new DOPOSoccerException(DOPOSoccerException.INVALID_CHARACTER);
        }
        this.country = country;
        this.power = power;
    }

    /**
     * Obtiene el pais del personaje
     * @return país del personaje
     */
    public String getCountry() {
        return country;
    }

    /**
     * Obtiene el poder especial
     * @return poder del personaje
     */
    public Power getPower() {
        return power;
    }

    /**
     * Obtiene el nombre del poder especial
     * @return nombre del poder
     */
    public String getPowerName() {
        return power.getName();
    }
}
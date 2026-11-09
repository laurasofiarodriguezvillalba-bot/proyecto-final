package domain;

public abstract class DOPOSoccer {
    private Player player1;
    private Player player2;
    private Ball ball;
    private Field field;
    private Goal goal1;
    private Goal goal2;
    private int matchLimit;
    private GameState state;
    private int powerCharge1;
    private int powerCharge2;
    private int powerChargeElapsed;
    private int matchTimeElapsed;

    /**
     * Construye una partida de DOPOSoccer
     */
    public DOPOSoccer(Player player1, Player player2, Ball ball, Field field, Goal goal1, Goal goal2, int matchLimit) {
        if (player1 == null || player2 == null || player1 == player2 || ball == null || field == null ||  goal1 == null || goal2 == null || matchLimit <= 0) {
            throw new DOPOSoccerException(DOPOSoccerException.INVALID_MATCH_CONFIGURATION);
        }
        this.player1 = player1;
        this.player2 = player2;
        this.ball = ball;
        this.field = field;
        this.goal1 = goal1;
        this.goal2 = goal2;
        this.matchLimit = matchLimit;
        state = GameState.READY;
        powerCharge1 = 0;
        powerCharge2 = 0;
        powerChargeElapsed = 0;
        matchTimeElapsed = 0;
    }

    /**
     * Inicia la partida
     */
    public void start() {
        if (state == GameState.READY) {
            state = GameState.PLAYING;
        }
    }

    /**
     * Pausa la partida.
     */
    public void pause() {
        if (state == GameState.PLAYING) {
            state = GameState.PAUSED;
        }
    }

    /**
     * Reanuda una partida pausada
     */
    public void resume() {
        if (state == GameState.PAUSED) {
            state = GameState.PLAYING;
        }
    }

    /**
     * Finaliza la partida
     */
    public void finish() {
        state = GameState.FINISHED;
    }

    /**
     * Obtiene el primer jugador
     * @return primer jugador de la partida
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * Obtiene el segundo jugador
     * @return segundo jugador de la partida
     */
    public Player getPlayer2() {
        return player2;
    }

    /**
     * Obtiene el balón de la partida
     * @return balón de la partida
     */
    public Ball getBall() {
        return ball;
    }

    /**
     * Obtiene la cancha de la partida
     * @return cancha de la partida
     */
    public Field getField() {
        return field;
    }

    /**
     * Obtiene la primera porteria
     * @return primera porteria
     */
    public Goal getGoal1() {
        return goal1;
    }

    /**
     * Obtiene la segunda porteria
     * @return segunda porteria
     */
    public Goal getGoal2() {
        return goal2;
    }

    /**
     * Obtiene el límite establecido para la partida
     * @return límite de la partida
     */
    public int getMatchLimit() {
        return matchLimit;
    }

    /**
     * Obtiene el estado actual de la partida
     * @return estado de la partida
     */
    public GameState getState() {
        return state;
    }

    /**
     * Registra un gol 
     */
    public abstract void registerGoal(Player player);

    /**
     * Actualiza el tiempo de la partida
     */
    public abstract void updateTime();

    /**
     * Obtiene el texto que representa el tiempo de la partida
     */
    public abstract String getTimeText();

    /**
     * Obtiene la carga de poder del jugador 1
     * @return porcentaje de carga
     */
    public int getPowerCharge1() {
        return powerCharge1;
    }

    /**
     * Obtiene la carga de poder del jugador 2
     * @return porcentaje de carga
     */
    public int getPowerCharge2() {
        return powerCharge2;
    }

    /**
     * Aumenta la carga de poder de un jugador.
     * @param player jugador que recibe la carga
     * @param amount cantidad que aumenta
     */
    public void increasePowerCharge(Player player, int amount) {
        if (player == player1) {
            powerCharge1 = Math.min(100, powerCharge1 + amount);
        } else if (player == player2) {
            powerCharge2 = Math.min(100, powerCharge2 + amount);
        }
    }

    /**
     * Consume la carga de poder de un jugador si esta completa
     * @param player jugador que activa su poder
     * @return true si tenía la carga completa y pudo consumirla
     */
    public boolean consumePowerCharge(Player player) {
        if (player == player1 && powerCharge1 == 100) {
            powerCharge1 = 0;
            return true;
        }
        if (player == player2 && powerCharge2 == 100) {
            powerCharge2 = 0;
            return true;
        }
        return false;
    }

    /**
     * Activa el poder especial de un jugador
     * @param player jugador que activa el poder
     * @return true si pudo activar el poder
     */
    public boolean activatePower(Player player) {

        if (player != player1 && player != player2) {
            return false;
        }
        if (state != GameState.PLAYING) {
            return false;
        }
        if (!consumePowerCharge(player)) {
            return false;
        }
        Player rival;
        if (player == player1) {
            rival = player2;
        } else {
            rival = player1;
        }
        Power power = player.getCharacter().getPower();
        power.activate(player, rival, ball);
        return true;
    }

    /**
     * Permite que un jugador patee el balón o golpee al rival cuando está cerca
     * @param player jugador que realiza la patada
     * @param kickRight indica si la patada va hacia la derecha
     */
    public void kickBall(Player player, boolean kickRight) {
        if (state != GameState.PLAYING) {
            return;
        }
        if (player != player1 && player != player2) {
            return;
        }
        double playerCenterX = player.getX() + 35;
        double playerCenterY = player.getY() + 50;
        double distanceX = Math.abs(ball.getX() - playerCenterX);
        double distanceY = Math.abs(ball.getY() - playerCenterY);

        if (distanceX < 90 && distanceY < 90) {
            if (kickRight) {
                ball.setSpeed(7, -6);
            } else {
                ball.setSpeed(-7, -6);
            }
            return;
        }

        // Identificar al jugador rival
        Player rival;
        if (player == player1) {
            rival = player2;
        } else {
            rival = player1;
        }
        double rivalCenterX = rival.getX() + 35;
        double rivalCenterY = rival.getY() + 50;
        double rivalDistanceX = Math.abs(rivalCenterX - playerCenterX);
        double rivalDistanceY = Math.abs(rivalCenterY - playerCenterY);
        // Si el rival está dentro del alcance, recibe daño
        if (rivalDistanceX < 90 && rivalDistanceY < 90) {
            rival.takeDamage(player.kick());
        }
    }

    /**
     * Detecta la colisión entre el balón y un jugador
     * @param player jugador que puede colisionar con el balón
     * @return true si existe una colisión y false si no
     */
    public boolean checkBallCollision(Player player) {
        double closestX = Math.max(player.getX(),
                Math.min(ball.getX(), player.getX() + 70));
        double closestY = Math.max(player.getY(),
                Math.min(ball.getY(), player.getY() + 100));
        double distanceX = ball.getX() - closestX;
        double distanceY = ball.getY() - closestY;
        return distanceX * distanceX + distanceY * distanceY <= ball.getRadius() * ball.getRadius();
    }

    /**
     * Cambia la dirección del balón cuando golpea a un jugador
     * @param player jugador que recibe el impacto
     */
    public void handleBallCollision(Player player) {
        double playerCenterX = player.getX() + 35;
        if (ball.getX() < playerCenterX) {
            ball.setPosition(
                    player.getX() - ball.getRadius(), ball.getY());
            ball.setSpeed(-4, -3);
        } else {
            ball.setPosition(player.getX() + 70 + ball.getRadius(), ball.getY());
            ball.setSpeed(4, -3);
        }
    }

    /**
     * Comprueba si el balón ha entrado en alguna portería
     * @return el jugador que marcó, o null si no hubo gol
     */
    public Player checkGoals() {
        if (goal1.isGoal(ball)) {
            registerGoal(player2);
            increasePowerCharge(player2, 25);
            resetPositions();
            return player2;
        }
        if (goal2.isGoal(ball)) {
            registerGoal(player1);
            increasePowerCharge(player1, 25);
            resetPositions();
            return player1;
        }
        return null;
    }

    /**
     * Reinicia las posiciones de los jugadores y del balón
     */
    public void resetPositions() {
        player1.setPosition(150, 250, field);
        player2.setPosition(750, 250, field);
        ball.reset();
    }

    /**
     * Mueve un jugador hacia la derecha o izquierda
     * @param player jugador que se va a mover
     * @param direction dirección del movimiento
     */
    public void movePlayer(Player player, String direction) {
        if (state != GameState.PLAYING) {
            return;
        }
        if (player != player1 && player != player2) {
            return;
        }
        if ("izquierda".equals(direction)) {
            player.moveLeft(field);
        } else if ("derecha".equals(direction)) {
            player.moveRight(field);
        }
    }

    /**
     * Actualiza el estado del partido
     * @param elapsedTime tiempo transcurrido en milisegundos
     */
    public void updateGame(int elapsedTime) {
        if (state != GameState.PLAYING) {
            return;
        }
        updatePowerCharges(elapsedTime);
        matchTimeElapsed += elapsedTime;
        if (matchTimeElapsed >= 1000) {
            matchTimeElapsed -= 1000;
            updateTime();
        }

        //Actualizar los saltos, los efectos especiales y el movimiento del balon
        player1.updateJump();
        player2.updateJump();
        player1.updateEffects(elapsedTime);
        player2.updateEffects(elapsedTime);
        ball.move(field);

        Player scorer = checkGoals();
        if (scorer != null) {
            return;
        }
        //Detectar colisiones con los jugadores
        if (checkBallCollision(player1)) {
            handleBallCollision(player1);
        }
        if (checkBallCollision(player2)) {
            handleBallCollision(player2);
        }
    }

    /**
     * Hace saltar a un jugador.
     * @param player jugador que va a saltar
     */
    public void jumpPlayer(Player player) {
        if (state != GameState.PLAYING) {
            return;
        }
        if (player != player1 && player != player2) {
            return;
        }
        player.jump();
    }

    /**
     * Aumenta automáticamente la carga de poder cada segundo
     * @param elapsedTime tiempo transcurrido en milisegundos
     */
    private void updatePowerCharges(int elapsedTime) {
        powerChargeElapsed += elapsedTime;
        while (powerChargeElapsed >= 1000) {
            powerChargeElapsed -= 1000;
            increasePowerCharge(player1, 10);
            increasePowerCharge(player2, 10);
        }
    }  
}
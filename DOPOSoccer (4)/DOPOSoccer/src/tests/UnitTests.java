package tests;

import org.junit.Test;
import static org.junit.Assert.*;
import domain.*;

public class UnitTests {
    private Player createPlayer1() {
        return new Player("Jugador 1", new SouthKorea());
    }
    private Player createPlayer2() {
        return new Player("Jugador 2", new Nigeria());
    }
    private Field createField() {
        return new Field(1000, 400);
    }
    private Ball createBall() {
        return new Ball(490, 100, 10);
    }
    private Goal createGoal1() {
        return new Goal(0, 300, 50, 50);
    }
    private Goal createGoal2() {
        return new Goal(950, 300, 50, 50);
    }
    private GoalsMatch createGoalsMatch() {
        return new GoalsMatch(createPlayer1(), createPlayer2(), createBall(),
            createField(), createGoal1(), createGoal2(), 3
        );
    }

    private TimeMatch createTimeMatch() {
        return new TimeMatch(createPlayer1(), createPlayer2(), createBall(),
            createField(), createGoal1(), createGoal2(), 1
        );
    }

    //PRUEBAS DE BALL
    //prueba que el balón cambia de posición al moverse
    @Test
    public void shouldMoveBall() {
        Ball ball = createBall();
        ball.setSpeed(2, 0);
        ball.move();
        assertEquals(492, ball.getX(), 0.01);
    }

    //prueba que el balón rebota cuando toca el suelo
    @Test
    public void shouldBounceBallOnGround() {
        Ball ball = new Ball(100, 345, 10);
        ball.setSpeed(0, 2);
        ball.bounceOnGround(350);
        assertEquals(340, ball.getY(), 0.01);
        assertTrue(ball.getSpeedY() < 0);
    }

    //prueba que el balón recupera su posición y velocidad iniciales
    @Test
    public void shouldResetBall() {
        Ball ball = createBall();
        ball.setSpeed(10, 5);
        ball.reset();
        assertEquals(490, ball.getX(), 0.01);
        assertEquals(100, ball.getY(), 0.01);
        assertEquals(0, ball.getSpeedX(), 0.01);
        assertEquals(0, ball.getSpeedY(), 0.01);
    }

    //PRUEBAS DE FIELD
     //prueba que la cancha se crea con sus dimensiones
    @Test
    public void shouldCreateField() {
        Field field = new Field(1000, 400);
        assertEquals(1000, field.getWidth(), 0.01);
        assertEquals(400, field.getHeight(), 0.01);
    }

    //prueba que la cancha devuelve correctamente sus límites
    @Test
    public void shouldReturnFieldLimits() {
        Field field = createField();
        assertEquals(0, field.getLeftLimit(), 0.01);
        assertEquals(1000, field.getRightLimit(), 0.01);
        assertEquals(0, field.getTopLimit(), 0.01);
        assertEquals(350, field.getGroundY(), 0.01);
    }

    //PRUEBAS DE GOAL
    //prueba que se detecta cuando el balón llega la zona de la portería
    @Test
    public void shouldDetectBallInsideGoal() {
        Goal goal = new Goal(100, 100, 100, 50);
        Ball ball = new Ball(150, 125, 10);
        assertTrue(goal.isGoal(ball));
    }

    //prueba que no se detecta un gol cuando el balón está fuera de la portería
    @Test
    public void shouldNotDetectBallOutsideGoal() {
        Goal goal = new Goal(100, 100, 100, 50);
        Ball ball = new Ball(300, 300, 10);
        assertFalse(goal.isGoal(ball));
    }

    //PRUEBAS DE STATISTICS
    //prueba que todas las estadísticas comienzan en nivel uno
    @Test
    public void shouldCreateStatisticsWithInitialValues() {
        Statistics statistics = new Statistics();
        assertEquals(1, statistics.getSpeed());
        assertEquals(1, statistics.getKick());
        assertEquals(1, statistics.getJump());
        assertEquals(1, statistics.getSurvival());
    }

    //prueba que el multiplicador aumenta según el nivel de Kick
    @Test
    public void shouldCalculateKickMultiplier() {
        Statistics statistics = new Statistics();
        statistics.setStatistics(1, 5, 1, 1);
        assertEquals(1.4, statistics.getKickMultiplier(), 0.01);
    }

    //prueba que la vida máxima aumenta según el nivel de Survival
    @Test
    public void shouldCalculateMaximumLife() {
        Statistics statistics = new Statistics();
        statistics.setStatistics(1, 1, 1, 5);
        assertEquals(140, statistics.getMaxLife());
    }

    //prueba que se pueden modificar correctamente las estadísticas
    @Test
    public void shouldSetStatistics() {
        Statistics statistics = new Statistics();
        statistics.setStatistics(5, 6, 7, 8);
        assertEquals(5, statistics.getSpeed());
        assertEquals(6, statistics.getKick());
        assertEquals(7, statistics.getJump());
        assertEquals(8, statistics.getSurvival());
    }

    //PRUEBAS DE PLAYER
    //prueba que el jugador se crea con sus valores iniciales
    @Test
    public void shouldCreatePlayer() {
        Player player = createPlayer1();
        assertEquals("Jugador 1", player.getName());
        assertEquals(0, player.getGoals());
        assertEquals(100, player.getCurrentLife());
        assertNotNull(player.getCharacter());
        assertNotNull(player.getStatistics());
    }

    //prueba que el jugador puede desplazarse hacia la derecha
    @Test
    public void shouldMovePlayerRight() {
        Player player = createPlayer1();
        player.setPosition(100, 200, createField());
        player.moveRight(createField());
        assertEquals(101, player.getX(), 0.01);
    }

    //prueba que el jugador puede desplazarse hacia la izquierda
    @Test
    public void shouldMovePlayerLeft() {
        Player player = createPlayer1();
        player.setPosition(100, 200, createField());
        player.moveLeft(createField());
        assertEquals(99, player.getX(), 0.01);
    }

    //prueba que recibir daño disminuye la vida del jugador
    @Test
    public void shouldTakeDamage() {
        Player player = createPlayer1();
        player.takeDamage(20);
        assertEquals(80, player.getCurrentLife());
    }

    //prueba que se incrementa la cantidad de goles del jugador
    @Test
    public void shouldScoreGoal() {
        Player player = createPlayer1();
        player.scoreGoal();
        assertEquals(1, player.getGoals());
    }

    //prueba que el jugador se desplaza hacia arriba al saltar
    @Test
    public void shouldJumpPlayer() {
        Player player = createPlayer1();
        player.setPosition(100, 200, createField());
        player.jump();
        player.updateJump();
        assertEquals(195, player.getY(), 0.01);
    }

    //prueba que la electrocución reduce la velocidad de movimiento
    @Test
    public void shouldReduceMovementWhenElectrocuted() {
        Player player = createPlayer1();
        player.setPosition(100, 200, createField());
        player.electrocute(2);
        player.moveRight(createField());
        assertEquals(100.5, player.getX(), 0.01);
    }

    //prueba que un jugador inconsciente no puede patear
    @Test
    public void shouldNotKickWhenUnconscious() {
        Player player = createPlayer1();
        player.becomeUnconscious(2);
        assertEquals(0, player.kick());
    }

    //PRUEBAS DE LOS PERSONAJES
    //prueba que Corea del Sur tiene asignado el poder Aura Azul
    @Test
    public void shouldCreateSouthKoreaCharacter() {
        SoccerCharacter character = new SouthKorea();
        assertEquals("Corea del Sur", character.getCountry());
        assertEquals("Aura Azul", character.getPowerName());
    }

    //prueba que Camerún tiene asignado el poder Rayo
    @Test
    public void shouldCreateCameroonCharacter() {
        SoccerCharacter character = new Cameroon();
        assertEquals("Camerún", character.getCountry());
        assertEquals("Rayo", character.getPowerName());
    }

    //prueba que Nigeria tiene asignado el poder Tornado
    @Test
    public void shouldCreateNigeriaCharacter() {
        SoccerCharacter character = new Nigeria();
        assertEquals("Nigeria", character.getCountry());
        assertEquals("Tornado", character.getPowerName());
    }

    //prueba que Estados Unidos tiene asignado el poder Ilusión
    @Test
    public void shouldCreateUnitedStatesCharacter() {
        SoccerCharacter character = new UnitedStates();
        assertEquals("Estados Unidos", character.getCountry());
        assertEquals("Ilusión", character.getPowerName());
    }

    //PRUEBAS DE POWER
    //prueba que el daño del poder se calcula con el multiplicador
    @Test
    public void shouldCalculatePowerDamage() {
        Power power = new BlueAura();
        assertEquals(56, power.calculateDamage(1.4));
    }

    //prueba que Aura Azul impulsa el balOn y causa daño al rival
    @Test
    public void shouldActivateBlueAura() {
        Player user = createPlayer1();
        Player rival = createPlayer2();
        Ball ball = createBall();
        user.setPosition(100, 200, createField());
        rival.setPosition(200, 200, createField());
        new BlueAura().activate(user, rival, ball);
        assertEquals(20, ball.getSpeedX(), 0.01);
        assertEquals(92, rival.getCurrentLife());
        assertEquals(230, rival.getX(), 0.01);
    }

    //prueba que Rayo impulsa el balón y causa daño al rival
    @Test
    public void shouldActivateLightning() {
        Player user = createPlayer1();
        Player rival = createPlayer2();
        Ball ball = createBall();
        user.setPosition(100, 200, createField());
        rival.setPosition(200, 200, createField());
        new Lightning().activate(user, rival, ball);
        assertEquals(25, ball.getSpeedX(), 0.01);
        assertEquals(88, rival.getCurrentLife());
    }

    //prueba que Tornado impulsa el balón y causa daño al rival
    @Test
    public void shouldActivateTornado() {
        Player user = createPlayer1();
        Player rival = createPlayer2();
        Ball ball = createBall();
        user.setPosition(100, 200, createField());
        rival.setPosition(200, 200, createField());
        new Tornado().activate(user, rival, ball);
        assertEquals(15, ball.getSpeedX(), 0.01);
        assertEquals(92, rival.getCurrentLife());
        assertEquals(0, rival.kick());
    }

    //prueba que Ilusión impulsa el balón y causa daño al rival
    @Test
    public void shouldActivateIllusion() {
        Player user = createPlayer1();
        Player rival = createPlayer2();
        Ball ball = createBall();
        user.setPosition(100, 200, createField());
        rival.setPosition(200, 200, createField());
        new Illusion().activate(user, rival, ball);
        assertEquals(20, ball.getSpeedX(), 0.01);
        assertEquals(94, rival.getCurrentLife());
    }

    //PRUEBAS DE DOPOSOCCER
    //prueba que una partida nueva comienza en estado READY
    @Test
    public void shouldCreateMatchInReadyState() {
        GoalsMatch match = createGoalsMatch();
        assertEquals(GameState.READY, match.getState());
    }

    //prueba que la partida cambia a PLAYING al iniciarse
    @Test
    public void shouldStartMatch() {
        GoalsMatch match = createGoalsMatch();
        match.start();
        assertEquals(GameState.PLAYING, match.getState());
    }

    //prueba que una partida en curso puede pausarse
    @Test
    public void shouldPauseMatch() {
        GoalsMatch match = createGoalsMatch();
        match.start();
        match.pause();
        assertEquals(GameState.PAUSED, match.getState());
    }

    //prueba que una partida pausada puede reanudarse
    @Test
    public void shouldResumeMatch() {
        GoalsMatch match = createGoalsMatch();
        match.start();
        match.pause();
        match.resume();
        assertEquals(GameState.PLAYING, match.getState());
    }

    //prueba que la partida cambia al estado FINISHED al finalizar
    @Test
    public void shouldFinishMatch() {
        GoalsMatch match = createGoalsMatch();
        match.start();
        match.finish();
        assertEquals(GameState.FINISHED, match.getState());
    }

    //prueba que se puede aumentar la carga de poder de un jugador
    @Test
    public void shouldIncreasePowerCharge() {
        GoalsMatch match = createGoalsMatch();
        match.increasePowerCharge(match.getPlayer1(), 30);
        assertEquals(30, match.getPowerCharge1());
    }

    //prueba que un jugador puede consumir su carga cuando llega a 100
    @Test
    public void shouldConsumePowerCharge() {
        GoalsMatch match = createGoalsMatch();
        Player player = match.getPlayer1();
        match.increasePowerCharge(player, 100);
        assertTrue(match.consumePowerCharge(player));
        assertEquals(0, match.getPowerCharge1());
    }

    //prueba que un jugador puede activar su poder con la carga completa
    @Test
    public void shouldActivatePlayerPower() {
        GoalsMatch match = createGoalsMatch();
        match.start();
        Player player = match.getPlayer1();
        match.increasePowerCharge(player, 100);
        assertTrue(match.activatePower(player));
        assertEquals(0, match.getPowerCharge1());
    }

    //prueba que no se puede activar un poder sin carga completa
    @Test
    public void shouldNotActivatePowerWithoutCompleteCharge() {
        GoalsMatch match = createGoalsMatch();
        match.start();
        match.increasePowerCharge(match.getPlayer1(), 50);
        assertFalse(match.activatePower(match.getPlayer1()));
    }

    @Test
    public void shouldMovePlayerThroughMatch() {
        //prueba que se puede mover al jugador hacia la derecha
        GoalsMatch match = createGoalsMatch();
        match.start();
        match.getPlayer1().setPosition(100, 200, match.getField());
        match.movePlayer(match.getPlayer1(), "derecha");
        assertEquals(101, match.getPlayer1().getX(), 0.01);
    }

    //prueba que la partida permite que un jugador salte
    @Test
    public void shouldJumpPlayerThroughMatch() {
        GoalsMatch match = createGoalsMatch();
        match.start();
        match.getPlayer1().setPosition(100, 200, match.getField());
        match.jumpPlayer(match.getPlayer1());
        match.getPlayer1().updateJump();
        assertEquals(195, match.getPlayer1().getY(), 0.01);
    }

    //prueba que un jugador cercano puede patear el balón
    @Test
    public void shouldKickBall() {
        GoalsMatch match = createGoalsMatch();
        match.start();
        Player player = match.getPlayer1();
        player.setPosition(100, 100, match.getField());
        match.getBall().setPosition(135, 150);
        match.kickBall(player, true);
        assertEquals(7, match.getBall().getSpeedX(), 0.01);
        assertEquals(-6, match.getBall().getSpeedY(), 0.01);
    }

    //prueba que las posiciones se reinician después de marcar un gol
    @Test
    public void shouldResetPositionsAfterGoal() {
        GoalsMatch match = createGoalsMatch();
        match.resetPositions();
        assertEquals(150, match.getPlayer1().getX(), 0.01);
        assertEquals(750, match.getPlayer2().getX(), 0.01);
        assertEquals(490, match.getBall().getX(), 0.01);
    }

    //PRUEBAS DE GOALS MATCH
    //prueba que se registra un gol durante una partida por goles
    @Test
    public void shouldRegisterGoalInGoalsMatch() {
        GoalsMatch match = createGoalsMatch();
        match.start();
        match.registerGoal(match.getPlayer1());
        assertEquals(1, match.getPlayer1().getGoals());
    }

    //prueba que la partida termina cuando un jugador alcanza el límite de goles
    @Test
    public void shouldFinishGoalsMatchWhenGoalLimitIsReached() {
        GoalsMatch match = new GoalsMatch(createPlayer1(), createPlayer2(), createBall(),
            createField(), createGoal1(), createGoal2(), 1
        );
        match.start();
        match.registerGoal(match.getPlayer1());
        assertEquals(GameState.FINISHED, match.getState());
    }

    //prueba que el tiempo no aumenta cuando la partida esta pausada
    @Test
    public void shouldNotUpdateElapsedTimeWhenPaused() {
        GoalsMatch match = createGoalsMatch();
        match.start();
        match.pause();
        match.updateTime();
        assertEquals("00:00", match.getTimeText());
    }

    //PRUEBAS DE TIME MATCH
    //prueba que el partido por tiempo inicia con la duración indicada
    @Test
    public void shouldCreateTimeMatch() {
        TimeMatch match = createTimeMatch();
        assertEquals("01:00", match.getTimeText());
        assertFalse(match.isSuddenDeath());
    }

    //prueba que se activa la muerte subita si el partido termina empatado
    @Test
    public void shouldActivateSuddenDeathWhenMatchIsTied() {
        TimeMatch match = createTimeMatch();
        match.start();
        for (int i = 0; i < 60; i++) {
            match.updateTime();
        }
        assertTrue(match.isSuddenDeath());
        assertEquals("Muerte súbita", match.getTimeText());
        assertEquals(GameState.PLAYING, match.getState());
    }

    //prueba que el partido termina si hay un ganador al agotarse el tiempo
    @Test
    public void shouldFinishTimeMatchWhenThereIsAWinner() {
        TimeMatch match = createTimeMatch();
        match.start();
        match.getPlayer1().scoreGoal();
        for (int i = 0; i < 60; i++) {
            match.updateTime();
        }
        assertEquals(GameState.FINISHED, match.getState());
    }

    //prueba que el primer gol durante la muerte súbita termina el partido
    @Test
    public void shouldFinishSuddenDeathAfterGoal() {
        TimeMatch match = createTimeMatch();
        match.start();
        for (int i = 0; i < 60; i++) {
            match.updateTime();
        }
        match.registerGoal(match.getPlayer1());
        assertEquals(GameState.FINISHED, match.getState());
    }
}

package presentation;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.HashSet;
import java.util.Set;
import domain.*;

public class DOPOSoccerGUI extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JButton buttonStart;
    private Player player1;
    private Player player2;
    private Ball ball;
    private DOPOSoccer game;
    private Image fieldImage;
    private Image player1Image;
    private Image player2Image;
    private Image ballImage;
    private Field field;
    private JPanel fieldPanel;
    private Timer gameTimer;
    private Set<Integer> keysPressed = new HashSet<Integer>();
    private Goal goal1;
    private Goal goal2;
    private JLabel scoreLabel;
    private JLabel timeLabel;
    private JButton buttonPause;
    private JProgressBar powerBar1;
    private JProgressBar powerBar2;

    private Image koreaNormal;
    private Image koreaRun1;
    private Image koreaRun2;
    private Image koreaRun3;
    private Image koreaKick;
    private Image koreaPower;
    
    private Image camerunNormal;
    private Image camerunRun1;
    private Image camerunRun2;
    private Image camerunRun3;
    private Image camerunKick;
    private Image camerunPower;

    private Image nigeriaNormal;
    private Image nigeriaRun1;
    private Image nigeriaRun2;
    private Image nigeriaRun3;
    private Image nigeriaKick;
    private Image nigeriaPower;

    private Image usaNormal;
    private Image usaRun1;
    private Image usaRun2;
    private Image usaRun3;
    private Image usaKick;
    private Image usaPower;

    private int runFrame1 = 0;
    private int runFrame2 = 0;
    private int animationElapsed = 0;

    private int kickTime1 = 0;
    private int kickTime2 = 0;

    /**
     * Construye la ventana principal del juego y configura sus componentes
     */
    public DOPOSoccerGUI() {
        prepareElements();
        prepareMenu();
        prepareHome();
        prepareController();
    }

    /**
     * Configura las características principales de la ventana
     */
    private void prepareElements() {
        setTitle("DOPOSoccer");
        setSize(1000, 650);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    /**
     * Construye el menú superior
     */
    private void prepareMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuGame = new JMenu("Partida");
        JMenuItem itemNew = new JMenuItem("Nueva partida");
        JMenuItem itemExit = new JMenuItem("Salir");
        menuGame.add(itemNew);
        menuGame.addSeparator();
        menuGame.add(itemExit);

        JMenu menuHelp = new JMenu("Ayuda");
        JMenuItem itemAbout = new JMenuItem("Acerca de");
        menuHelp.add(itemAbout);

        menuBar.add(menuGame);
        menuBar.add(menuHelp);

        buttonPause = new JButton("Pausar");
        buttonPause.setEnabled(false);

        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(buttonPause);
        setJMenuBar(menuBar);
    
        //oyente para pausar o reanudar la partida
        buttonPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePause();
            }
        });

        //oyente para iniciar una nueva partida
        itemNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showConfiguration();
            }
        });

        //oyente para cerrar el juego
        itemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        //oyente para la informacion del juego
        itemAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(DOPOSoccerGUI.this, "DOPOSoccer\nJuego de fútbol para dos jugadores.", "Acerca de", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    /**
     * Construye la pantalla inicial
     */
    private void prepareHome() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(35, 100, 65));

        titleLabel = new JLabel("DOPOSoccer", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 55));
        titleLabel.setForeground(Color.WHITE);

        buttonStart = new JButton("Comenzar partida");
        buttonStart.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(35, 100, 65));
        buttonPanel.add(buttonStart);
        mainPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Configura los eventos de los botones
     */
    private void prepareController() {
        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showConfiguration();
            }
        });
    }

    /**
     * Muestra la configuración de una nueva partida
     */
    private void showConfiguration() {
        JTextField name1 = new JTextField("Jugador 1");
        JTextField name2 = new JTextField("Jugador 2");
        
        String[] characters = {"Corea del Sur", "Camerún", "Nigeria", "Estados Unidos"};
        JComboBox<String> character1 = new JComboBox<String>(characters);
        JComboBox<String> character2 = new JComboBox<String>(characters);

        String[] matchTypes = {"Por tiempo", "Por goles"};
        JComboBox<String> type = new JComboBox<String>(matchTypes);
        JTextField limit = new JTextField();

        JPanel configurationPanel = new JPanel();
        configurationPanel.setLayout(new GridLayout(7, 2, 8, 8));
        configurationPanel.add(new JLabel("Nombre del jugador 1:"));
        configurationPanel.add(name1);
        configurationPanel.add(new JLabel("Personaje del jugador 1:"));
        configurationPanel.add(character1);
        configurationPanel.add(new JLabel("Nombre del jugador 2:"));
        configurationPanel.add(name2);
        configurationPanel.add(new JLabel("Personaje del jugador 2:"));
        configurationPanel.add(character2);
        configurationPanel.add(new JLabel("Tipo de partido:"));
        configurationPanel.add(type);
        configurationPanel.add(new JLabel("Tiempo (minutos) o goles:"));
        configurationPanel.add(limit);
        
        int option = JOptionPane.showConfirmDialog(this, configurationPanel, "Configuración de partida", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String playerName1 = name1.getText().trim();
                String playerName2 = name2.getText().trim();
                int selectedLimit = Integer.parseInt(limit.getText().trim());
                if (playerName1.isEmpty() || playerName2.isEmpty() || selectedLimit <= 0) {
                    throw new IllegalArgumentException("Completa todos los campos correctamente.");
                }

                SoccerCharacter selectedCharacter1 = createCharacter(character1.getSelectedItem().toString());
                SoccerCharacter selectedCharacter2 = createCharacter(character2.getSelectedItem().toString());

                player1 = new Player(playerName1, selectedCharacter1);
                player2 = new Player(playerName2, selectedCharacter2);
                field = new Field(1000, 500);
                ball = new Ball(490, 100, 15);
                goal1 = new Goal(0, 230, 40, 120);
                goal2 = new Goal(960, 230, 40, 120);
                player1.setPosition(150, 250, field);
                player2.setPosition(750, 250, field);
                
                if (type.getSelectedItem().equals("Por tiempo")) {
                    game = new TimeMatch(player1, player2, ball, field, goal1, goal2, selectedLimit);
                } else {
                    game = new GoalsMatch(player1, player2, ball, field, goal1, goal2, selectedLimit);
                }

                game.start();
                showGameScreen();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El límite debe ser un número entero.", "Error de configuración", JOptionPane.ERROR_MESSAGE);

            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error de configuración", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Crea un personaje según el país seleccionado
     * @param country país seleccionado por el jugador
     * @return personaje correspondiente al país
     * @throws IllegalArgumentException si el país no es válido
     */
    private SoccerCharacter createCharacter(String country) {
        if (country.equals("Corea del Sur")) {
            return new SouthKorea();
        } else if (country.equals("Camerún")) {
            return new Cameroon();
        } else if (country.equals("Nigeria")) {
            return new Nigeria();
        } else if (country.equals("Estados Unidos")) {
            return new UnitedStates();
        }
        throw new IllegalArgumentException("El personaje seleccionado no es válido.");
    }

    /**
     * Muestra la pantalla principal de juego
     */
    private void showGameScreen() {
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());

        JPanel scorePanel = new JPanel(new BorderLayout());

        //Barra de poder del jugador 1
        powerBar1 = new JProgressBar(0, 100);
        powerBar1.setValue(0);
        powerBar1.setStringPainted(true);
        powerBar1.setPreferredSize(new Dimension(150, 20));

        JPanel leftPanel = new JPanel();
        leftPanel.add(powerBar1);

        //Configurar el marcador y el tiempo
        JPanel centerPanel = new JPanel();
        scoreLabel = new JLabel(player1.getName() + "       0 - 0       " + player2.getName());
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 25));

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Arial", Font.BOLD, 20));

        centerPanel.add(scoreLabel);
        centerPanel.add(timeLabel);

        //Barra de poder del jugador 2
        powerBar2 = new JProgressBar(0, 100);
        powerBar2.setValue(0);
        powerBar2.setStringPainted(true);
        powerBar2.setPreferredSize(new Dimension(150, 20));

        JPanel rightPanel = new JPanel();
        rightPanel.add(powerBar2);

        scorePanel.add(leftPanel, BorderLayout.WEST);
        scorePanel.add(centerPanel, BorderLayout.CENTER);
        scorePanel.add(rightPanel, BorderLayout.EAST);

        //Cargar imágenes de la cancha y el balón
        fieldImage = loadImage("cancha");
        ballImage = loadImage("balon");

        //Cargar imagenes de las animaciones
        loadKoreaImages();
        loadCamerunImages();
        loadNigeriaImages();
        loadUsaImages();

        //Cargar imagen inicial del jugador 1
        if (player1.getCharacter().getCountry().equals("Corea del Sur")) {
            player1Image = koreaNormal;
        }
        else if (player1.getCharacter().getCountry().equals("Camerún")) {
            player1Image = camerunNormal;
        }
        else if (player1.getCharacter().getCountry().equals("Nigeria")) {
            player1Image = nigeriaNormal;
        }
        else if (player1.getCharacter().getCountry().equals("Estados Unidos")) {
            player1Image = usaNormal;
        }
        else {
            player1Image = loadImage(
                player1.getCharacter().getCountry()
            );
        }

        //Cargar imagen inicial del jugador 2
        if (player2.getCharacter().getCountry().equals("Corea del Sur")) {
            player2Image = koreaNormal;
        }
        else if (player2.getCharacter().getCountry().equals("Camerún")) {
            player2Image = camerunNormal;
        }
        else if (player2.getCharacter().getCountry().equals("Nigeria")) {
            player2Image = nigeriaNormal;
        }
        else if (player2.getCharacter().getCountry().equals("Estados Unidos")) {
            player2Image = usaNormal;
        }
        else {
            player2Image = loadImage(
                player2.getCharacter().getCountry()
            );
        }

        //Panel donde se dibuja la cancha
        fieldPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Dibujar cancha
                if (fieldImage != null) {
                    g.drawImage(
                        fieldImage, 0, 0,
                        getWidth(), getHeight(), this
                    );
                }

                //Dibujar barra de vida del jugador 1
                drawLifeBar(g, player1, 20, 30);
                //Dibujar barra de vida del jugador 2
                drawLifeBar(g, player2, getWidth() - 270, 30);
                // Dibujar jugador 1
                if (player1Image != null) {
                    g.drawImage(player1Image, (int) player1.getX(), (int) player1.getY(), 70, 100, this);
                }
                //Dibujar jugador 2 invertido horizontalmente
                if (player2Image != null) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.drawImage(player2Image, (int) player2.getX() + 70, (int) player2.getY(), -70, 100, this);
                    g2.dispose();
                }
                //Dibujar balón
                if (ballImage != null) {
                    int size = (int) (ball.getRadius() * 2);
                    g.drawImage(ballImage, (int) ball.getX() - size / 2, (int) ball.getY() - size / 2, size, size, this);
                }
                //Dibujar efecto Aura Azul
                if (getActivePowerName().equals("Aura Azul")) {
                    drawBlueAura(g);
                }
                //Dibujar efecto Rayo
                if (getActivePowerName().equals("Rayo")) {
                    drawLightning(g);
                }
                //Dibujar efecto Tornado
                if (getActivePowerName().equals("Tornado")) {
                    drawTornado(g);
                }
                //Dibujar efecto Ilusión
                if (getActivePowerName().equals("Ilusión")) {
                    drawIllusion(g);
                }

                //Dibujar porterías
                g.setColor(Color.RED);
                //Portería izquierda
                g.drawRect((int) goal1.getX(), (int) goal1.getY(), (int) goal1.getWidth(), (int) goal1.getHeight());
                //Portería derecha
                g.drawRect((int) goal2.getX(), (int) goal2.getY(), (int) goal2.getWidth(), (int) goal2.getHeight());
            }
        };

        mainPanel.add(scorePanel, BorderLayout.NORTH);
        mainPanel.add(fieldPanel, BorderLayout.CENTER);
        prepareControllerGame();
        buttonPause.setEnabled(true);
        buttonPause.setText("Pausar");
        mainPanel.revalidate();
        mainPanel.repaint();
        fieldPanel.requestFocusInWindow();
    }

    /**
     * Carga la imagen correspondiente a la cancha, al personaje, al balón o a sus animaciones
     * @param name nombre de la imagen que se desea cargar
     * @return imagen cargada o null si el nombre no corresponde a una image
     */
    private Image loadImage(String name) {
        String path = "";
        if (name.equals("cancha")) {
            path = "resources/images/cancha.png";
        }
        else if (name.equals("Camerún")) {
            path = "resources/images/players/camerun.png";
        }
        else if (name.equals("Nigeria")) {
            path = "resources/images/players/nigeria.png";
        }
        else if (name.equals("Estados Unidos")) {
            path = "resources/images/players/estados_unidos.png";
        }
        else if (name.equals("balon")) {
            path = "resources/images/balon.png";
        }

        //Imágenes de Corea del Sur
        else if (name.equals("koreaNormal")) {
            path = "resources/images/players/corea_del_sur/normal.png";
        }
        else if (name.equals("koreaRun1")) {
            path = "resources/images/players/corea_del_sur/correr1.png";
        }
        else if (name.equals("koreaRun2")) {
            path = "resources/images/players/corea_del_sur/correr2.png";
        }
        else if (name.equals("koreaRun3")) {
            path = "resources/images/players/corea_del_sur/correr3.png";
        }
        else if (name.equals("koreaKick")) {
            path = "resources/images/players/corea_del_sur/patear.png";
        }
        else if (name.equals("koreaPower")) {
            path = "resources/images/players/corea_del_sur/poder.png";
        }

        //Imágenes de Camerún
        else if (name.equals("camerunNormal")) {
            path = "resources/images/players/camerun/normal.png";
        }
        else if (name.equals("camerunRun1")) {
            path = "resources/images/players/camerun/correr1.png";
        }
        else if (name.equals("camerunRun2")) {
            path = "resources/images/players/camerun/correr2.png";
        }
        else if (name.equals("camerunRun3")) {
            path = "resources/images/players/camerun/correr3.png";
        }
        else if (name.equals("camerunKick")) {
            path = "resources/images/players/camerun/patear.png";
        }
        else if (name.equals("camerunPower")) {
            path = "resources/images/players/camerun/power.png";
        }

        //Imágenes de Nigeria
        else if (name.equals("nigeriaNormal")) {
            path = "resources/images/players/nigeria/normal.png";
        }
        else if (name.equals("nigeriaRun1")) {
            path = "resources/images/players/nigeria/correr1.png";
        }
        else if (name.equals("nigeriaRun2")) {
            path = "resources/images/players/nigeria/correr2.png";
        }
        else if (name.equals("nigeriaRun3")) {
            path = "resources/images/players/nigeria/correr3.png";
        }
        else if (name.equals("nigeriaKick")) {
            path = "resources/images/players/nigeria/patear.png";
        }
        else if (name.equals("nigeriaPower")) {
            path = "resources/images/players/nigeria/power.png";
        }

        //Imágenes de Estados Unidos
        else if (name.equals("usaNormal")) {
            path = "resources/images/players/estados_unidos/normal.png";
        }
        else if (name.equals("usaRun1")) {
            path = "resources/images/players/estados_unidos/correr1.png";
        }
        else if (name.equals("usaRun2")) {
            path = "resources/images/players/estados_unidos/correr2.png";
        }
        else if (name.equals("usaRun3")) {
            path = "resources/images/players/estados_unidos/correr3.png";
        }
        else if (name.equals("usaKick")) {
            path = "resources/images/players/estados_unidos/patear.png";
        }
        else if (name.equals("usaPower")) {
            path = "resources/images/players/estados_unidos/power.png";
        }
        else {
            return null;
        }
        ImageIcon icon = new ImageIcon(path);
        return icon.getImage();
    }
       
    /**
     * Configura los controles del teclado para los jugadores
     */
    private void prepareControllerGame() {
        fieldPanel.setFocusable(true);
        fieldPanel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                keysPressed.clear();
            }
        });
        fieldPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keysPressed.add(e.getKeyCode());
                //Salto y patada del jugador 1
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    game.jumpPlayer(player1);
                }
                else if (e.getKeyCode() == KeyEvent.VK_S) {
                    game.kickBall(player1, true);
                    kickTime1 = 250;
                }
                // Salto y patada del jugador 2
                else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    game.jumpPlayer(player2);
                }
                else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    game.kickBall(player2, false);
                    kickTime2 = 250;
                }
                //Activar poder especial del jugador 1
                else if (e.getKeyCode() == KeyEvent.VK_F) {
                    activatePower(player1);
                }
                //Activar poder especial del jugador 2
                else if (e.getKeyCode() == KeyEvent.VK_M) {
                    activatePower(player2);
                }
                fieldPanel.repaint();
            }
            @Override
            public void keyReleased(KeyEvent e) {
                keysPressed.remove(e.getKeyCode());
            }
        });
        if (gameTimer != null) {
            gameTimer.stop();
        }

        gameTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Movimiento del jugador 1
                if (keysPressed.contains(KeyEvent.VK_A)) {
                    game.movePlayer(player1, "izquierda");
                }
                if (keysPressed.contains(KeyEvent.VK_D)) {
                    game.movePlayer(player1, "derecha");
                }
                //Movimiento del jugador 2
                if (keysPressed.contains(KeyEvent.VK_LEFT)) {
                    game.movePlayer(player2, "izquierda");
                }
                if (keysPressed.contains(KeyEvent.VK_RIGHT)) {
                    game.movePlayer(player2, "derecha");
                }

                game.updateGame(20);

                //Comprobar si la partida terminó
                if (game.getState() == GameState.FINISHED) {
                    gameTimer.stop();
                    updateScore();
                    String message;
                    if (player1.getGoals() > player2.getGoals()) {
                        message = "¡" + player1.getName() + " ha ganado!";
                    } else if (player2.getGoals() > player1.getGoals()) {
                        message = "¡" + player2.getName() + " ha ganado!";
                    } else {
                        message = "¡La partida terminó en empate!";
                    }
                    JOptionPane.showMessageDialog(DOPOSoccerGUI.this, message, "Partida terminada", JOptionPane.INFORMATION_MESSAGE);

                    buttonPause.setEnabled(false);
                    showConfiguration();
                    return;
                }

                updateAnimations();
                timeLabel.setText(game.getTimeText());
                updateScore();
                updatePowerBars();
                fieldPanel.repaint();
            }
        });
        gameTimer.start();
    }

    /**
     * Actualiza el marcador de los jugadores
     */
    private void updateScore() {
        scoreLabel.setText(
            player1.getName() + "       " +
            player1.getGoals() + " - " +
            player2.getGoals() + "       " +
            player2.getName()
        );
    }

    /**
     * Activa el poder especial del jugador si cumple las condiciones necesarias
     * @param player jugador que intenta activar su poder
     */
    private void activatePower(Player player) {
        if (!game.activatePower(player)) {
            keysPressed.clear();
            fieldPanel.requestFocusInWindow();
            return;
        }
        keysPressed.clear();
        updatePowerBars();
        fieldPanel.requestFocusInWindow();
    }
    
    /**
     * Pausa o reanuda la partida
     */
    private void togglePause() {
        if (game.getState() == GameState.PLAYING) {
            game.pause();
            gameTimer.stop();
            buttonPause.setText("Reanudar");
            keysPressed.clear();
        } else if (game.getState() == GameState.PAUSED) {
            game.resume();
            gameTimer.start();
            buttonPause.setText("Pausar");
            fieldPanel.requestFocusInWindow();
        }
    }

    /**
     * Actualiza visualmente las barras de poder
     */
    private void updatePowerBars() {
        powerBar1.setValue(game.getPowerCharge1());
        powerBar2.setValue(game.getPowerCharge2());
    }

    /**
     * Inicia la aplicación mostrando la ventana principal del juego
     */
    public static void main(String[] args) {
        DOPOSoccerGUI window = new DOPOSoccerGUI();
        window.setVisible(true);
    }

    /**
     * Carga las imágenes de las animaciones de Corea del Sur
     */
    private void loadKoreaImages() {
        koreaNormal = loadImage("koreaNormal");
        koreaRun1 = loadImage("koreaRun1");
        koreaRun2 = loadImage("koreaRun2");
        koreaRun3 = loadImage("koreaRun3");
        koreaKick = loadImage("koreaKick");
        koreaPower = loadImage("koreaPower");
    }

    /**
     * Carga las imágenes de las animaciones de Camerún
     */
    private void loadCamerunImages() {
        camerunNormal = loadImage("camerunNormal");
        camerunRun1 = loadImage("camerunRun1");
        camerunRun2 = loadImage("camerunRun2");
        camerunRun3 = loadImage("camerunRun3");
        camerunKick = loadImage("camerunKick");
        camerunPower = loadImage("camerunPower");
    }

    /**
     * Carga las imágenes de las animaciones de Nigeria
     */
    private void loadNigeriaImages() {
        nigeriaNormal = loadImage("nigeriaNormal");
        nigeriaRun1 = loadImage("nigeriaRun1");
        nigeriaRun2 = loadImage("nigeriaRun2");
        nigeriaRun3 = loadImage("nigeriaRun3");
        nigeriaKick = loadImage("nigeriaKick");
        nigeriaPower = loadImage("nigeriaPower");
    }

    /**
     * Carga las imágenes de las animaciones de Estados Unidos
     */
    private void loadUsaImages() {
        usaNormal = loadImage("usaNormal");
        usaRun1 = loadImage("usaRun1");
        usaRun2 = loadImage("usaRun2");
        usaRun3 = loadImage("usaRun3");
        usaKick = loadImage("usaKick");
        usaPower = loadImage("usaPower");
    }
    
    /**
     * Actualiza las imágenes de los jugadores según su movimiento
     */
    private void updateAnimations() {
        animationElapsed += 20;
        if (animationElapsed >= 120) {
            animationElapsed = 0;
            runFrame1 = (runFrame1 + 1) % 3;
            runFrame2 = (runFrame2 + 1) % 3;
        }

        if (kickTime1 > 0) {
            kickTime1 -= 20;
        }
        if (kickTime2 > 0) {
            kickTime2 -= 20;
        }

        boolean moving1 = keysPressed.contains(KeyEvent.VK_A) || keysPressed.contains(KeyEvent.VK_D);
        boolean moving2 = keysPressed.contains(KeyEvent.VK_LEFT) || keysPressed.contains(KeyEvent.VK_RIGHT);

        //Imagen del jugador 1
        String country1 = player1.getCharacter().getCountry();
        if (country1.equals("Corea del Sur")) {
            if (kickTime1 > 0) {
                player1Image = koreaKick;
            }
            else if (player1.hasPowerActive()) {
                player1Image = koreaPower;
            }
            else if (moving1) {
                player1Image = getKoreaRunImage(runFrame1);
            }
            else {
                player1Image = koreaNormal;
            }
        }
        else if (country1.equals("Camerún")) {
            if (kickTime1 > 0) {
                player1Image = camerunKick;
            }
            else if (player1.hasPowerActive()) {
                player1Image = camerunPower;
            }
            else if (moving1) {
                player1Image = getCamerunRunImage(runFrame1);
            }
            else {
                player1Image = camerunNormal;
            }
        }
        else if (country1.equals("Nigeria")) {
            if (kickTime1 > 0) {
                player1Image = nigeriaKick;
            }
            else if (player1.hasPowerActive()) {
                player1Image = nigeriaPower;
            }
            else if (moving1) {
                player1Image = getNigeriaRunImage(runFrame1);
            }
            else {
                player1Image = nigeriaNormal;
            }
        }
        else if (country1.equals("Estados Unidos")) {
            if (kickTime1 > 0) {
                player1Image = usaKick;
            }
            else if (player1.hasPowerActive()) {
                player1Image = usaPower;
            }
            else if (moving1) {
                player1Image = getUsaRunImage(runFrame1);
            }
            else {
                player1Image = usaNormal;
            }
        }

        //Imagen del jugador 2
        String country2 = player2.getCharacter().getCountry();
        if (country2.equals("Corea del Sur")) {
            if (kickTime2 > 0) {
                player2Image = koreaKick;
            }
            else if (player2.hasPowerActive()) {
                player2Image = koreaPower;
            }
            else if (moving2) {
                player2Image = getKoreaRunImage(runFrame2);
            }
            else {
                player2Image = koreaNormal;
            }
        }
        else if (country2.equals("Camerún")) {
            if (kickTime2 > 0) {
                player2Image = camerunKick;
            }
            else if (player2.hasPowerActive()) {
                player2Image = camerunPower;
            }
            else if (moving2) {
                player2Image = getCamerunRunImage(runFrame2);
            }
            else {
                player2Image = camerunNormal;
            }
        }
        else if (country2.equals("Nigeria")) {
            if (kickTime2 > 0) {
                player2Image = nigeriaKick;
            }
            else if (player2.hasPowerActive()) {
                player2Image = nigeriaPower;
            }
            else if (moving2) {
                player2Image = getNigeriaRunImage(runFrame2);
            }
            else {
                player2Image = nigeriaNormal;
            }
        }
        else if (country2.equals("Estados Unidos")) {
            if (kickTime2 > 0) {
                player2Image = usaKick;
            }
            else if (player2.hasPowerActive()) {
                player2Image = usaPower;
            }
            else if (moving2) {
                player2Image = getUsaRunImage(runFrame2);
            }
            else {
                player2Image = usaNormal;
            }
        }
    }

    /**
     * Obtiene la imagen de correr de Camerún según el cuadro de animación
     * @param frame cuadro actual de la animación
     * @return imagen correspondiente al movimiento
     */
    private Image getCamerunRunImage(int frame) {
        if (frame == 0) {
            return camerunRun1;
        }
        else if (frame == 1) {
            return camerunRun2;
        }
        else {
            return camerunRun3;
        }
    }

    /**
     * Obtiene la imagen correspondiente al fotograma de carrera
     * @param frame número del fotograma
     * @return imagen de carrera
     */
    private Image getKoreaRunImage(int frame) {
        if (frame == 0) {
            return koreaRun1;
        } else if (frame == 1) {
            return koreaRun2;
        } else {
            return koreaRun3;
        }
    }

    /**
     * Obtiene la imagen de correr de Nigeria según el cuadro de animación
     * @param frame cuadro actual de la animación
     * @return imagen correspondiente al movimiento
     */
    private Image getNigeriaRunImage(int frame) {
        if (frame == 0) {
            return nigeriaRun1;
        }
        else if (frame == 1) {
            return nigeriaRun2;
        }
        else {
            return nigeriaRun3;
        }
    }

    /**
     * Obtiene la imagen de correr de Estados Unidos 
     * @param frame cuadro actual de la animación
     * @return imagen correspondiente al movimiento
     */
    private Image getUsaRunImage(int frame) {
        if (frame == 0) {
            return usaRun1;
        }
        else if (frame == 1) {
            return usaRun2;
        }
        else {
            return usaRun3;
        }
    }

    /**
     * Obtiene el nombre del poder que está activo
     * @return nombre del poder o cadena vacía si no hay ninguno
     */
    private String getActivePowerName() {
        Power power = game.getActivePowerEffect();
        if (power == null) {
            return "";
        }
        return power.getName();
    }

    /**
     * Dibuja el efecto visual del poder Aura Azul
     * @param g contexto gráfico
     */
    private void drawBlueAura(Graphics g) {
        int x = (int) ball.getX();
        int y = (int) ball.getY();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0, 150, 255, 100));
        g2.fillOval(x - 25, y - 25, 50, 50);
        g2.setColor(new Color(0, 100, 255, 180));
        g2.fillOval(x - 18, y - 18, 36, 36);
        g2.setColor(Color.CYAN);
        g2.setStroke(new BasicStroke(3));
        g2.drawOval(x - 18, y - 18, 36, 36);
        g2.dispose();
    }

    /**
     * Dibuja el efecto visual del poder Rayo
     * @param g contexto gráfico
     */
    private void drawLightning(Graphics g) {
        int x = (int) ball.getX();
        int y = (int) ball.getY();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.YELLOW);
        g2.setStroke(new BasicStroke(4));
        int[] xPoints = {x - 25, x - 10, x - 18, x + 5, x - 2, x + 22};
        int[] yPoints = {y - 30, y - 10, y - 5, y + 5, y + 15, y + 30};
        g2.drawPolyline(xPoints, yPoints, xPoints.length);
        g2.setColor(Color.ORANGE);
        g2.setStroke(new BasicStroke(2));
        int[] xPoints2 = {x + 20, x + 10, x + 17, x + 5};
        int[] yPoints2 = {y - 25, y - 10, y, y + 15};
        g2.drawPolyline(xPoints2, yPoints2, xPoints2.length);
        g2.dispose();
    }

    /**
     * Dibuja el efecto visual del poder Tornado
     * @param g contexto gráfico
     */
    private void drawTornado(Graphics g) {
        int x = (int) ball.getX();
        int y = (int) ball.getY();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0, 220, 100, 180));
        g2.setStroke(new BasicStroke(3));
        g2.drawArc(x - 30, y - 15, 60, 30, 0, 300);
        g2.drawArc(x - 25, y - 25, 50, 50, 30, 280);
        g2.drawArc(x - 20, y - 35, 40, 70, 50, 260);
        g2.setColor(new Color(100, 255, 150, 200));
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(x - 12, y - 12, 24, 24);
        g2.drawArc(x - 18, y - 18, 36, 36, 90, 250);
        g2.dispose();
    }

    /**
     * Dibuja los siete balones de ilusion
     * @param g contexto gráfico
     */
    private void drawIllusion(Graphics g) {
        Power power = game.getActivePowerEffect();
        if (!(power instanceof Illusion)) {
            return;
        }
        Illusion illusion = (Illusion) power;
        if (!illusion.isIllusionActive()) {
            return;
        }
        Ball[] illusionBalls = illusion.getIllusionBalls();
        int size = (int) (ball.getRadius() * 2);
        for (int i = 0; i < illusionBalls.length; i++) {
            if (illusionBalls[i] != null) {
                g.drawImage(ballImage, (int) illusionBalls[i].getX() - size / 2, (int) illusionBalls[i].getY() - size / 2, size, size, this);
            }
        }
    }

    /**
     * Dibuja la barra de vida de un jugador
     * @param g contexto gráfico
     * @param player jugador cuya vida se va a representar
     * @param x posición horizontal de la barra
     * @param y posición vertical de la barra
     */
    private void drawLifeBar(Graphics g, Player player, int x, int y) {
        int barWidth = 250;
        int barHeight = 18;
        int currentLife = player.getCurrentLife();
        int maxLife = player.getMaxLife();
        int currentWidth = (int) (barWidth * currentLife / (double) maxLife);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString(player.getName() + " - Vida: " + currentLife + " / " + maxLife, x, y - 5);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, barWidth, barHeight);
        if (currentLife > maxLife * 0.5) {
            g.setColor(Color.GREEN);
        } else if (currentLife > maxLife * 0.25) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.RED);
        }
        g.fillRect(x, y, currentWidth, barHeight);
        g.setColor(Color.WHITE);
        g.drawRect(x, y, barWidth, barHeight);
    }
}
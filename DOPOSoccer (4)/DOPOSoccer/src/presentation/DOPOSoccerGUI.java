
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
     * Configura los eventos de los botones.
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
     * Crea un personaje según el país seleccionado.
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
     * Muestra la pantalla principal de juego.
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

        // Barra de poder del jugador 2 
        powerBar2 = new JProgressBar(0, 100);
        powerBar2.setValue(0);
        powerBar2.setStringPainted(true);
        powerBar2.setPreferredSize(new Dimension(150, 25));
        JPanel rightPanel = new JPanel();
        rightPanel.add(powerBar2);

        scorePanel.add(leftPanel, BorderLayout.WEST);
        scorePanel.add(centerPanel, BorderLayout.CENTER);
        scorePanel.add(rightPanel, BorderLayout.EAST);

        fieldImage = loadImage("cancha");
        player1Image = loadImage(player1.getCharacter().getCountry());
        player2Image = loadImage(player2.getCharacter().getCountry());
        ballImage = loadImage("balon");
        
        //Panel donde se dibuja la cancha
        fieldPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (fieldImage != null) {
                    g.drawImage(fieldImage, 0, 0, getWidth(), getHeight(), this);
                }
                //Dibujar jugador 1
                if (player1Image != null) {
                    g.drawImage(player1Image, (int) player1.getX(), (int) player1.getY(), 70, 100, this);
                }
                //Dibujar jugador 2
                if (player2Image != null) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.drawImage(player2Image, (int) player2.getX() + 70, (int) player2.getY(), -70, 100, this);
                    g2.dispose();
                }
                // Dibuja el balón
                if (ballImage != null) {
                    int size = (int) (ball.getRadius() * 2);
                    g.drawImage(ballImage, (int) ball.getX() - size / 2, (int) ball.getY() - size / 2, size, size, this);
                }

                g.setColor(Color.RED);
                // Portería izquierda.
                g.drawRect((int) goal1.getX(), (int) goal1.getY(), (int) goal1.getWidth(), (int) goal1.getHeight());
                // Portería derecha.
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
     * Carga la imagen correspondiente a la cancha, al personaje o el balon
     */
    private Image loadImage(String name) {
        String path = "";
        if (name.equals("cancha")) {
            path = "resources/images/cancha.png";
        }
        else if (name.equals("Corea del Sur")) {
            path = "resources/images/players/corea_del_sur.png";
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
        fieldPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keysPressed.add(e.getKeyCode());
                //salto y patada del jugador 1
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    game.jumpPlayer(player1);
                }
                else if (e.getKeyCode() == KeyEvent.VK_S) {
                    game.kickBall(player1, true);
                }
                //salto y patada del jugador 2
                else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    game.jumpPlayer(player2);
                }
                else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    game.kickBall(player2, false);
                }
                //activar poder especial del jugador 1
                else if (e.getKeyCode() == KeyEvent.VK_F) {
                    activatePower(player1);
                }
                //activar poder especial del jugador 2
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

        gameTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //movimiento del jugador 1
                if (keysPressed.contains(KeyEvent.VK_A)) {
                    game.movePlayer(player1, "izquierda");
                }
                if (keysPressed.contains(KeyEvent.VK_D)) {
                    game.movePlayer(player1, "derecha");
                }
                //movimiento del jugador 2
                if (keysPressed.contains(KeyEvent.VK_LEFT)) {
                    game.movePlayer(player2, "izquierda");
                }
                if (keysPressed.contains(KeyEvent.VK_RIGHT)) {
                    game.movePlayer(player2, "derecha");
                }

                game.updateGame(20);
                timeLabel.setText(game.getTimeText());
                updateScore();
                updatePowerBars();
                fieldPanel.repaint();
            }
        });
        gameTimer.start();
    }

    /**
     * Actualiza el marcador de los jugadores.
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
     * Activa el poder especial del jugador
     */
    private void activatePower(Player player) {
        if (!game.activatePower(player)) {
            JOptionPane.showMessageDialog(this, "No puedes activar el poder. ¡Comprueba tu barra!");
            return;
        }
        updatePowerBars();
        Power power = player.getCharacter().getPower();
        JOptionPane.showMessageDialog(this, "¡" + power.getName() + " activado!");
    }
    
    /**
     * Pausa o reanuda la partida.
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
     * Actualiza visualmente las barras de poder.
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
}

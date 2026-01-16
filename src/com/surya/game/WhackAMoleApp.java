package com.surya.game;

import com.surya.objects.HoleOccupant;
import com.surya.persistence.PlayerScore;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Main Swing application window for the Whack-A-Mole game.
 */
public class WhackAMoleApp extends JFrame {

    // Theme colors
    private static final Color COLOR_WOOD  = new Color(139, 90, 59);
    private static final Color COLOR_DARK  = new Color(44, 62, 80);
    private static final Color COLOR_GRASS = new Color(125, 178, 62);
    private static final Color COLOR_START = new Color(88, 164, 96);
    private static final Color COLOR_EXIT  = new Color(231, 76, 60);

    //  Labels
    private final JLabel lblScoreVal = new JLabel("0");
    private final JLabel lblHighVal  = new JLabel("0");
    private final JLabel lblTimeVal  = new JLabel("30s");

    // Layered display
    private final JLayeredPane layers = new JLayeredPane();
    private final JPanel pnlGrid;
    private final JPanel pnlGameOver;

    private final HoleButton[] holes = new HoleButton[12];
    private final JTextArea txtLeaderboard = new JTextArea();
    private final JButton btnStart = new StyledButton("Start Game", COLOR_START);
    private final JLabel lblFinalScore = new JLabel();

    // Game engine
    private GameEngine engine;

    public WhackAMoleApp() {
        super("Whack-A-Mole");

        this.engine = new GameEngine(this);
        this.pnlGrid = initGridPanel();
        this.pnlGameOver = initGameOverPanel();

        initFrame();
        initUI();
        initShutdownHook();
    }

    private void initFrame() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(850, 650);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
    }

    private void initUI() {
        JPanel root = new TexturedPanel("/images/wood.png", COLOR_WOOD);
        root.setLayout(new BorderLayout());
        root.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(root);

        JPanel inner = new JPanel(new BorderLayout());
        inner.setOpaque(false);

        inner.add(initHeader(), BorderLayout.NORTH);

        // Prepare layered screen
        layers.setPreferredSize(new Dimension(800, 450));

        layers.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                Rectangle r = layers.getBounds();
                pnlGrid.setBounds(0, 0, r.width, r.height);
                pnlGameOver.setBounds(0, 0, r.width, r.height);
                layers.revalidate();
            }
        });

        pnlGrid.setVisible(true);
        pnlGameOver.setVisible(false);

        layers.add(pnlGrid, JLayeredPane.DEFAULT_LAYER);
        layers.add(pnlGameOver, JLayeredPane.POPUP_LAYER);

        inner.add(layers, BorderLayout.CENTER);
        inner.add(initFooter(), BorderLayout.SOUTH);
        add(inner, BorderLayout.CENTER);
    }

    private JPanel initHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(COLOR_DARK);
        p.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("Whack-A-Mole");
        title.setFont(new Font("SansSerif", Font.PLAIN, 24));
        title.setForeground(Color.WHITE);
        p.add(title, BorderLayout.WEST);

        JPanel stats = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 0));
        stats.setOpaque(false);

        stats.add(createStat("0",    "/images/coin.png", lblScoreVal));
        stats.add(createStat("0",    "/images/hs.png",   lblHighVal));
        stats.add(createStat("30s",  "/images/time.png", lblTimeVal));

        p.add(stats, BorderLayout.EAST);
        return p;
    }

    private JLabel createStat(String init, String icon, JLabel ref) {
        ref.setText(init);
        ref.setFont(new Font("SansSerif", Font.PLAIN, 16));
        ref.setForeground(Color.WHITE);

        URL url = getClass().getResource(icon);
        if (url != null) {
            Image img = new ImageIcon(url).getImage()
                    .getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            ref.setIcon(new ImageIcon(img));
        }
        return ref;
    }

    private JPanel initGridPanel() {
        TexturedPanel p = new TexturedPanel("/images/grass.png", COLOR_GRASS);
        p.setLayout(new GridLayout(3, 4, 20, 20));
        p.setBorder(new EmptyBorder(20, 40, 20, 40));

        Image holeImg = null;
        URL url = getClass().getResource("/images/hole.png");
        if (url != null) holeImg = new ImageIcon(url).getImage();

        for (int i = 0; i < 12; i++) {
            int id = i;
            holes[i] = new HoleButton(i, holeImg, e -> handleClick(id));
            p.add(holes[i]);
        }
        return p;
    }

    private JPanel initFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        p.setBackground(COLOR_DARK);

        btnStart.addActionListener(e -> startSession());
        p.add(btnStart);

        JButton btnExit = new StyledButton("Exit", COLOR_EXIT);
        btnExit.addActionListener(e -> attemptExit());
        p.add(btnExit);

        return p;
    }

    private JPanel initGameOverPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(0, 0, 0, 200));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 5, 10, 5);

        JLabel title = new JLabel("GAME OVER");
        title.setFont(new Font("Arial", Font.BOLD, 60));
        title.setForeground(COLOR_EXIT);

        c.gridy = 0; p.add(title, c);
        c.gridy = 1; p.add(lblFinalScore, c);

        txtLeaderboard.setEditable(false);
        txtLeaderboard.setOpaque(false);
        txtLeaderboard.setForeground(Color.WHITE);
        txtLeaderboard.setFont(new Font("Monospaced", Font.BOLD, 16));
        c.gridy = 2; p.add(txtLeaderboard, c);

        JButton btnAgain = new StyledButton("Play Again", COLOR_START);
        btnAgain.addActionListener(e -> {
            pnlGameOver.setVisible(false);
            startSession();
        });
        c.gridy = 3; p.add(btnAgain, c);

        return p;
    }

    // --- GAME LOGIC ---

    private void startSession() {
        engine.resetGame();
        new Thread(engine).start();
        btnStart.setEnabled(false);
        pnlGameOver.setVisible(false);
    }

    private void handleClick(int id) {
        HoleOccupant occ = engine.getGrid().getOccupant(id);
        if (occ != null && occ.isVisible()) {
            engine.processScore(occ.whack());
            holes[id].setOccupantImage(null);
            engine.getGrid().clearOccupant(id);
        }
    }

    public void updateHUD(int score, int high, int time) {
        SwingUtilities.invokeLater(() -> {
            lblScoreVal.setText(String.valueOf(score));
            lblHighVal.setText(String.valueOf(high));
            lblTimeVal.setText(time + "s");
        });
    }

    public void showGameOver(int score, List<PlayerScore> leaders) {
        SwingUtilities.invokeLater(() -> {
            lblFinalScore.setText("Your Score: " + score);

            StringBuilder sb = new StringBuilder("Top Scores:\n");
            int r = 1;
            for (PlayerScore ps : leaders) {
                sb.append(String.format("%d. %-8s %d\n",
                        r++, ps.getPlayerName(), ps.getScore()));
                if (r > 5) break;
            }
            txtLeaderboard.setText(sb.toString());

            pnlGameOver.setVisible(true);
            btnStart.setEnabled(true);
        });
    }

    public HoleButton getButton(int id) {
        return holes[id];
    }

    // --- SYSTEM HANDLING ---

    private void initShutdownHook() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { attemptExit(); }
        });
    }

    private void attemptExit() {
        engine.stop();
        dispose();
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WhackAMoleApp().setVisible(true));
    }

    // --- INNER UI CLASSES ---

    class StyledButton extends JButton {
        private Color base;
        private final Color hover;

        public StyledButton(String text, Color color) {
            super(text);
            this.base = color;
            this.hover = color.brighter();

            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("SansSerif", Font.PLAIN, 18));
            setPreferredSize(new Dimension(140, 45));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { base = hover; repaint(); }
                public void mouseExited (MouseEvent e) { base = color; repaint(); }
            });
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(base);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 45, 45);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    class TexturedPanel extends JPanel {
        private Image img;
        private final Color fallback;

        public TexturedPanel(String path, Color color) {
            this.fallback = color;
            URL u = getClass().getResource(path);
            if (u != null) img = new ImageIcon(u).getImage();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (img != null) {
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(fallback);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}

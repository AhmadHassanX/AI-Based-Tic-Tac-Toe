import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToe::new);
    }
}

class TicTacToe implements ActionListener {
    enum Difficulty {
        EASY, MEDIUM, HARD
    }

    JFrame frame = new JFrame();
    JPanel titlePanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JLabel statusLabel = new JLabel();
    JButton[] buttons = new JButton[9];
    boolean playerTurn = true;
    boolean playerStarts = true;
    Difficulty difficulty = Difficulty.HARD; // Default difficulty
    Random random = new Random();

    TicTacToe() {
        setupFrame();
        setupMenu();
        setupStatusLabel();
        setupButtonPanel();
        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        startGame();
    }

    private void setupFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(60, 60, 60));
    }

    private void setupMenu() {
    JMenuBar menuBar = new JMenuBar();

    JMenu gameMenu = new JMenu("Game");
    JMenuItem playerFirst = new JMenuItem("Player Starts");
    JMenuItem computerFirst = new JMenuItem("Computer Starts");
    JMenuItem resetGameItem = new JMenuItem("Reset");

    playerFirst.addActionListener(e -> {
        playerStarts = true;
        resetGame();
    });

    computerFirst.addActionListener(e -> {
        playerStarts = false;
        resetGame();
    });

    resetGameItem.addActionListener(e -> resetGame());

    gameMenu.add(playerFirst);
    gameMenu.add(computerFirst);
    gameMenu.addSeparator(); // adds a separator line
    gameMenu.add(resetGameItem);

    JMenu difficultyMenu = new JMenu("Difficulty");

    JMenuItem easy = new JMenuItem("Easy");
    JMenuItem medium = new JMenuItem("Medium");
    JMenuItem hard = new JMenuItem("Hard");

    easy.addActionListener(e -> {
        difficulty = Difficulty.EASY;
        resetGame();
    });

    medium.addActionListener(e -> {
        difficulty = Difficulty.MEDIUM;
        resetGame();
    });

    hard.addActionListener(e -> {
        difficulty = Difficulty.HARD;
        resetGame();
    });

    difficultyMenu.add(easy);
    difficultyMenu.add(medium);
    difficultyMenu.add(hard);

    menuBar.add(gameMenu);
    menuBar.add(difficultyMenu);
    frame.setJMenuBar(menuBar);
}


    private void setupStatusLabel() {
        statusLabel.setBackground(new Color(25, 25, 112));
        statusLabel.setForeground(new Color(255, 255, 0));
        statusLabel.setFont(new Font("Ink Free", Font.BOLD, 75));
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        statusLabel.setOpaque(true);
        statusLabel.setText("Tic-Tac-Toe");
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBounds(0, 0, 800, 100);
        titlePanel.add(statusLabel);
    }

    private void setupButtonPanel() {
        buttonPanel.setLayout(new GridLayout(3, 3));
        buttonPanel.setBackground(new Color(60, 60, 60));
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            buttons[i].setFont(new Font("MV Boli", Font.BOLD, 120));
            buttons[i].setFocusable(false);
            buttons[i].setBackground(Color.WHITE);
            buttons[i].setForeground(Color.BLACK);
            buttons[i].setBorder(BorderFactory.createLineBorder(new Color(25, 25, 112)));
            buttons[i].addActionListener(this);
            final int index = i;
            buttons[i].addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent evt) {
                    if (buttons[index].getText().isEmpty()) {
                        buttons[index].setBackground(new Color(173, 216, 230));
                    }
                }

                public void mouseExited(MouseEvent evt) {
                    if (buttons[index].getText().isEmpty()) {
                        buttons[index].setBackground(Color.WHITE);
                    }
                }
            });
            buttonPanel.add(buttons[i]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 9; i++) {
            if (e.getSource() == buttons[i] && buttons[i].getText().isEmpty()) {
                if (playerTurn) {
                    buttons[i].setText("X");
                    buttons[i].setForeground(new Color(255, 69, 0));
                    playerTurn = false;
                    updateGame();
                    if (!playerTurn) {
                        SwingUtilities.invokeLater(this::computerMove);
                    }
                }
                break;
            }
        }
    }

    private void startGame() {
        if (playerStarts) {
            playerTurn = true;
            statusLabel.setText("X turn");
        } else {
            playerTurn = false;
            statusLabel.setText("O turn");
            SwingUtilities.invokeLater(this::computerMove);
        }
    }

    private void updateGame() {
        if (checkWin("X")) {
            win("X");
        } else if (checkWin("O")) {
            win("O");
        } else if (isDraw()) {
            draw();
        } else {
            statusLabel.setText(playerTurn ? "X turn" : "O turn");
        }
    }

    private boolean checkWin(String player) {
        int[][] wins = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                {0, 4, 8}, {2, 4, 6}
        };
        for (int[] win : wins) {
            if (buttons[win[0]].getText().equals(player) &&
                    buttons[win[1]].getText().equals(player) &&
                    buttons[win[2]].getText().equals(player)) {
                return true;
            }
        }
        return false;
    }

    private boolean isDraw() {
        for (JButton button : buttons) {
            if (button.getText().isEmpty()) return false;
        }
        return true;
    }

    private void win(String player) {
        int[][] wins = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                {0, 4, 8}, {2, 4, 6}
        };
        for (int[] win : wins) {
            if (buttons[win[0]].getText().equals(player) &&
                    buttons[win[1]].getText().equals(player) &&
                    buttons[win[2]].getText().equals(player)) {
                Color color = player.equals("X") ? Color.GREEN : Color.RED;
                for (int i : win) buttons[i].setBackground(color);
                break;
            }
        }
        for (JButton button : buttons) button.setEnabled(false);
        statusLabel.setText(player + " wins");
    }

    private void draw() {
        statusLabel.setText("Draw");
    }

    private void computerMove() {
        int move = -1;
        switch (difficulty) {
            case EASY:
                move = getRandomMove();
                break;
            case MEDIUM:
                move = getBestMoveLimitedDepth(3);  // limit depth to 3 for medium
                break;
            case HARD:
                move = getBestMoveLimitedDepth(9);  // effectively full minimax
                break;
        }
        if (move != -1) {
            buttons[move].setText("O");
            buttons[move].setForeground(new Color(30, 144, 255));
        }
        playerTurn = true;
        updateGame();
    }

    private int getRandomMove() {
        List<Integer> availableMoves = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().isEmpty()) {
                availableMoves.add(i);
            }
        }
        if (availableMoves.isEmpty()) return -1;
        return availableMoves.get(random.nextInt(availableMoves.size()));
    }

    private int getBestMoveLimitedDepth(int maxDepth) {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;
        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().isEmpty()) {
                buttons[i].setText("O");
                int score = minimax(0, false, maxDepth);
                buttons[i].setText("");
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }
        return bestMove;
    }

    private int minimax(int depth, boolean isMaximizing, int maxDepth) {
        if (checkWin("O")) return 10 - depth;  // Prefer faster wins
        if (checkWin("X")) return depth - 10;  // Prefer slower losses
        if (isDraw() || depth >= maxDepth) return 0;

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (buttons[i].getText().isEmpty()) {
                    buttons[i].setText("O");
                    int score = minimax(depth + 1, false, maxDepth);
                    buttons[i].setText("");
                    bestScore = Math.max(score, bestScore);
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (buttons[i].getText().isEmpty()) {
                    buttons[i].setText("X");
                    int score = minimax(depth + 1, true, maxDepth);
                    buttons[i].setText("");
                    bestScore = Math.min(score, bestScore);
                }
            }
            return bestScore;
        }
    }

    private void resetGame() {
        for (JButton button : buttons) {
            button.setText("");
            button.setBackground(Color.WHITE);
            button.setEnabled(true);
        }
        startGame();
    }
}

package othello;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class GameGUI {

    private JLabel whiteScoreLabel;
    private JLabel blackScoreLabel;
    private JButton passButton;
    private int whiteScore;
    private int blackScore;
    private Piece currentPlayer;
    private PiecePanel[][] gameBoard;
    private final Color GREEN = new Color(0, 119, 33);
    private final Color LIGHT_BLACK = new Color(0, 0, 0, 90);
    private final Color LIGHT_WHITE = new Color(255, 255, 255, 90);
    private final Color GRAY = new Color(175,180,182);
    private boolean isLegalAvailable;
    JPanel gamePanel;

    public GameGUI() {
        //Our grid is 8x8 and remember to call y (row) first while looping.
        this.gameBoard = new PiecePanel[8][8];

        //Start drawing out game GUI frame
        JFrame baseFrame = new JFrame("Othello");
        baseFrame.setBounds(300, 300, 500, 580);
        baseFrame.setResizable(false);

        //Adding main panels
        JPanel menuPanel = new JPanel(new GridLayout(1, 3));
        //For padding
        menuPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
        menuPanel.setBackground(GRAY);
        baseFrame.add(menuPanel, BorderLayout.NORTH);

        gamePanel = new JPanel(new GridLayout(this.gameBoard.length, this.gameBoard[0].length));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        gamePanel.setBackground(Color.WHITE);
        baseFrame.add(gamePanel);

        /* MENU PANEL */
        blackScoreLabel = new JLabel("", JLabel.LEFT);
        blackScoreLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        menuPanel.add(blackScoreLabel);

        passButton = new JButton("Skip this turn");
        passButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                nextTurn();
            }
        });
        menuPanel.add(passButton);

        whiteScoreLabel = new JLabel("", JLabel.RIGHT);
        whiteScoreLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        menuPanel.add(whiteScoreLabel);
        /* GAME PANEL */
        for (int y = 0; y < this.gameBoard.length; y++) {
            for (int x = 0; x < this.gameBoard[0].length; x++) {
                final PiecePanel piecePanel = new PiecePanel(GREEN, Color.BLACK, Color.WHITE);
                piecePanel.setBorder(BorderFactory.createLineBorder(Color.black));
                piecePanel.setBackground(GREEN);
                final int pieceY = y;
                final int pieceX = x;
                this.gameBoard[y][x] = piecePanel;
                piecePanel.addMouseListener(new MouseListener() {

                    public void mouseClicked(MouseEvent me) {
                        if (checkValidity(pieceY, pieceX, false) && gameBoard[pieceY][pieceX].getShowingColor() == enumToColor(Piece.EMPTY)) { //checking for all the legal moves
                            checkValidity(pieceY, pieceX, true); //Check again to ensure flipping
                            setPieceColor(piecePanel, enumToColor(currentPlayer));
                            nextTurn();

                        } else {
                            GUIConsole.display("Oh snap! This move is not valid. Please try again");
                        }

                    }

                    public void mousePressed(MouseEvent me) {
                        //throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public void mouseReleased(MouseEvent me) {
                        //throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public void mouseEntered(MouseEvent me) {
                        //throw new UnsupportedOperationException("Not supported yet.");
                    }

                    public void mouseExited(MouseEvent me) {
                        //throw new UnsupportedOperationException("Not supported yet.");
                    }
                });
                gamePanel.add(piecePanel);
            }
        }

        /* MENU */
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.LIGHT_GRAY);
        JMenu gameMenu = new JMenu("Menu");
        gameMenu.setBackground(Color.LIGHT_GRAY);
        menuBar.add(gameMenu);

        JMenuItem newGameItem = new JMenuItem("Start new game");
        gameMenu.add(newGameItem);

        newGameItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                initGame();
            }
        });

        baseFrame.setJMenuBar(menuBar);
        baseFrame.setVisible(true);

        this.initGame();
    }

    public void initGame() {
        this.isLegalAvailable = true;
        this.currentPlayer = Piece.BLACK;
        for (int y = 0; y < gameBoard.length; y++) {
            for (int x = 0; x < gameBoard.length; x++) {
                this.gameBoard[y][x].setShowingColor(GREEN);
            }
        }
        this.gameBoard[3][3].setShowingColor(enumToColor(Piece.WHITE));
        this.gameBoard[3][4].setShowingColor(enumToColor(Piece.BLACK));
        this.gameBoard[4][4].setShowingColor(enumToColor(Piece.WHITE));
        this.gameBoard[4][3].setShowingColor(enumToColor(Piece.BLACK));
        updateGUI();


    }

    private void checkWinner() {
        int legalCount = 0;
        for (int y = 0; y < this.gameBoard.length; y++) {
            for (int x = 0; x < this.gameBoard[0].length; x++) {
                if (checkValidity(y, x, false)) {
                    legalCount++;
                }
            }
        }
        if (legalCount <= 0 || (blackScore + whiteScore) == this.gameBoard.length * this.gameBoard[0].length) {
            isLegalAvailable = false;
            if (blackScore > whiteScore) {
                GUIConsole.display("Hooray! " + Piece.BLACK + " has won the game");
            } else if (whiteScore > blackScore) {
                GUIConsole.display("Hooray! " + Piece.WHITE + " has won the game");
            } else {
                GUIConsole.display("Nice! A draw!");
            }
            initGame();
        }

    }

    private void updateGUI() {
        whiteScore = 0;
        blackScore = 0;
        for (int y = 0; y < this.gameBoard.length; y++) {
            for (int x = 0; x < this.gameBoard[0].length; x++) {

                if (this.gameBoard[y][x].getShowingColor() == enumToColor(Piece.BLACK)) {
                    blackScore++;

                }
                if (this.gameBoard[y][x].getShowingColor() == enumToColor(Piece.WHITE)) {
                    whiteScore++;
                }
                this.gameBoard[y][x].setBackground(GREEN);
                if (checkValidity(y, x, false) && gameBoard[y][x].getShowingColor() == enumToColor(Piece.EMPTY)) {
                    this.gameBoard[y][x].setBackground((this.currentPlayer == Piece.BLACK) ? LIGHT_BLACK : LIGHT_WHITE);
                }

            }
        }
        whiteScoreLabel.setText(whiteScore + " : " + Piece.WHITE.toString());
        blackScoreLabel.setText(Piece.BLACK.toString() + " : " + blackScore);
        checkWinner();
    }

    private Color enumToColor(Piece color) {
        if (color == Piece.BLACK) {
            return Color.BLACK;
        } else if (color == Piece.WHITE) {
            return Color.WHITE;
        } else {
            return GREEN;
        }
    }

    private Piece colorToEnum(Color color) {
        if (color == Color.BLACK) {
            return Piece.BLACK;
        } else if (color == Color.WHITE) {
            return Piece.WHITE;
        } else {
            return Piece.EMPTY;
        }
    }

    public void setPieceColor(PiecePanel panel, Color color) {
        panel.setShowingColor(color);
        panel.updateUI();
    }

    public void nextTurn() {
        this.currentPlayer = (this.currentPlayer == Piece.BLACK ? Piece.WHITE : Piece.BLACK);
        updateGUI();
    }

    public boolean checkValidity(int originY, int originX, boolean doFlip) {
        boolean valid = false;
        for (int angleY = -1; angleY < 2; angleY++) {
            for (int angleX = -1; angleX < 2; angleX++) {
                if (angleY == 0 && angleX == 0) {
                    continue; //this will be the origin
                }
                int distanceFromOrigin = 0;
                int checkYAtThisAngle = 0;
                int checkXAtThisAngle = 0;
                do {
                    distanceFromOrigin++;
                    checkYAtThisAngle = originY + distanceFromOrigin * angleY;
                    checkXAtThisAngle = originX + distanceFromOrigin * angleX;

                    if (!withinBounds(checkYAtThisAngle, checkXAtThisAngle)) {
                        checkYAtThisAngle = originY;
                        checkXAtThisAngle = originX;
                        break;
                    }

                } while (colorToEnum(this.gameBoard[checkYAtThisAngle][checkXAtThisAngle].getShowingColor())
                        == (this.currentPlayer == Piece.BLACK ? Piece.WHITE : Piece.BLACK));

                if (colorToEnum(this.gameBoard[checkYAtThisAngle][checkXAtThisAngle].getShowingColor())
                        == this.currentPlayer && distanceFromOrigin > 1) {
                    valid = true;
                    if (doFlip) {
                        for (int flippingDistance = 1; flippingDistance < distanceFromOrigin; flippingDistance++) {
                            int flipY = originY + flippingDistance * angleY;
                            int flipX = originX + flippingDistance * angleX;
                            this.gameBoard[flipY][flipX].setShowingColor(enumToColor(this.currentPlayer));
                            new Thread(this.gameBoard[flipY][flipX]).start();
                        }
                    }
                }

            }
        }
        return valid;
    }

    public boolean withinBounds(int Y, int X) {
        if (Y < 0 || Y > 7 || X < 0 || X > 7) {
            return false;
        }
        return true;
    }
}

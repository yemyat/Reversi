
package othello;

import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

public class PiecePanel extends JPanel implements Runnable{
    private final int PADDING = 10;
    private Color mEmpty;
    private Color mFace;
    private Color mBack;
    private Color threadColor; //only for thread
    private Color showingColor; //Because of threads, we can't update the UI instantly. Added this to overcome
    private int circleWidth = 0;
    private int circleHeight = 0;
    private int circleX = 0;

    public PiecePanel(Color empty, Color face, Color back) {
        super();
        this.mEmpty = empty; // Should be the same with board color
        this.mFace = face; 
        this.mBack = back;
        this.showingColor = this.mEmpty;
        this.threadColor = this.mEmpty;
    }

    public void run() {
        int frames = 0;
        this.setThreadColor(flipColor());
        do {
            this.circleX++;
            this.circleWidth = this.circleWidth - 2;
            frames++;
            repaint();
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                Logger.getLogger(PiecePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (this.circleWidth > 0);
        this.setThreadColor(flipColor());
        do {
            this.circleX--;
            this.circleWidth = this.circleWidth + 2;
            repaint();
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                Logger.getLogger(PiecePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            frames--;
        } while (frames > 0);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //g.setColor(flipColor());
        //First run
        g.setColor(this.getThreadColor());
        if(this.circleWidth == 0 && this.circleX == 0 && this.circleHeight == 0) {
            this.circleWidth = super.getWidth() - PADDING;
            this.circleHeight = this.circleWidth;
            this.circleX = PADDING - PADDING/2;
            g.setColor(this.getShowingColor());
        }

        g.drawOval(this.circleX, PADDING-PADDING/2,
                this.circleWidth, this.circleHeight);
        g.fillOval(this.circleX, PADDING-PADDING/2,
                this.circleWidth, this.circleHeight);
    }

    public Color getShowingColor() {
        return showingColor;
    }

    public void setShowingColor(Color showingColor) {
        this.showingColor = showingColor;
        this.threadColor = showingColor;
        this.updateUI();
    }

    public Color getThreadColor() {
        return threadColor;
    }

    public void setThreadColor(Color threadColor) {
        this.threadColor = threadColor;
    }

    public Color flipColor() {
        if(this.threadColor == this.mFace)
            return this.mBack;
        return this.mFace;
    }   
}

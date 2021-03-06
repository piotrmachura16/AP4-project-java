package visual;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import algorithm.parser.exception.CalculatorException;
import algorithm.parser.function.Complex;
import algorithm.parser.main.Parser;
import algorithm.parser.util.Variable;

/**
 * @Author Kacper Ledwosiński
 */
public class InputSpace extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {
    private static final long serialVersionUID = 1L;

    int originX = -1, originY = -1;
    int offX, offY;
    int scrollSpeed = 2;
    int centerX = 10, centerY = 10;
    float zoomX = 200, zoomY = 200;
    float scaleX = 4, scaleY = 4;
    String f;
    ArrayList<Complex> sq_points = new ArrayList<Complex>();
    boolean panning = false;
    Graphics2D previous;

    public InputSpace(String f) {
        this.f = f;
        addMouseMotionListener(this);
        addMouseListener(this);
        addMouseWheelListener(this);
    }

    public void addPoint(Complex p) {
        sq_points.add(p);
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.translate(getWidth() / 2, getHeight() / 2);

        if (panning) {
            g2.setColor(Color.black);
            g2.drawLine(-getWidth() / 2 + originX, -getHeight() / 2 + originY, -getWidth() / 2 + originX - offX,
                    -getHeight() / 2 + originY - offY);
            g2.drawLine(-getWidth() / 2, -centerY - offY, getWidth() / 2, -centerY - offY);
            g2.drawLine(centerX - offX, -getHeight() / 2, centerX - offX, getHeight() / 2);
            return;
        }

        double tickX = scaleX / zoomX;
        double tickY = scaleY / zoomY;

        /**
         * Paints output space with small 1x1 pixel rectangles, color chosen as follows:
         * hue = phase f(z) , saturation = 1, brightness = raidus f(z)
         */
        for (int j = -getHeight() / 2 + centerY; j < getHeight() / 2 + centerY; j++) {
            for (int i = -getWidth() / 2 - centerX; i < getWidth() / 2 - centerX; i++) {

                double x = i * tickX;
                double y = j * tickY;
                Variable z0 = new Variable("z", new Complex(x, y));
                Complex z = new Complex();
                try {
                    z = Parser.eval(f, z0).getComplexValue();
                } catch (CalculatorException e) {
                    z = new Complex(1000000, 1000000);
                }

                double fi;
                try {
                    fi = Complex.phase(z) / 2 / Math.PI;
                } catch (Exception e) {
                    fi = 0;
                }

                double r = Complex.abs(z);
                if (r > 1)
                    r = 1;

                g2.setColor(Color.getHSBColor((float) fi, 1, (float) r));
                g2.fillRect(i + centerX, j - centerY, 1, 1);

            }
        }

        // draw axes
        g2.setColor(Color.gray);
        g2.drawLine(-getWidth() / 2, -centerY, getWidth() / 2, -centerY);
        g2.drawLine(centerX, -getHeight() / 2, centerX, getHeight() / 2);

        // draw ticks
        g2.setColor(Color.gray);
        for (int i = -(int) ((getWidth() + 2 * centerX) / (2 * zoomX / scaleX))
                - 1; i < (int) ((getWidth() - 2 * centerX) / (2 * zoomX / scaleX)) + 1; i++) {
            g2.fillRect((int) (i * zoomX / scaleX) - 1 + centerX, -5 - centerY, 2, 10);
        }
        for (int i = -(int) ((getHeight() - 2 * centerY) / (2 * zoomY / scaleY))
                - 1; i < (int) ((getHeight() + 2 * centerY) / (2 * zoomY / scaleY)) + 1; i++) {
            g2.fillRect(-5 + centerX, (int) (i * zoomY / scaleY) - 1 - centerY, 10, 2);
        }

        // draw rectangle points
        // g2.setColor(Color.black);
        // for (Complex p : sq_points) {
        // g2.fillRect((int) (p.getRe() * zoomY / scaleY) - 2, (int) (p.getIm() * zoomY
        // / scaleY) - 2, 4, 4);
        // }

        previous = g2;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        originX = e.getX();
        originY = e.getY();
        panning = true;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (originX != -1 && originY != -1) {
            offX = originX - e.getX();
            offY = originY - e.getY();
            this.repaint();
        }
        this.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        centerX -= offX;
        centerY += offY;
        panning = false;
        this.repaint();
        originX = -1;
        originY = -1;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        zoomX -= e.getPreciseWheelRotation() * scrollSpeed;
        zoomY -= e.getPreciseWheelRotation() * scrollSpeed;
        this.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
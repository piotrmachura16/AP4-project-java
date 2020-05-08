/**
 * Made by: Kacper Ledwosiński
 */
package animation;

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

import parser.exception.CalculatorException;
import parser.function.Complex;
import parser.main.Parser;
import parser.util.Variable;

public class GraphicSolver extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {
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

  double tickX = scaleX / zoomX;
  double tickY = scaleY / zoomY;

  /** Animation components */
  int way = 0; // 0-right, 1-down, 2-left, 3-up
  Complex A, B, C, D;
  int range;

  public GraphicSolver(String f, int range) {
    this.f = f;
    this.range = range;
    addMouseMotionListener(this);
    addMouseListener(this);
    addMouseWheelListener(this);

    A = new Complex(-range, range);
    B = new Complex(range, range);
    C = new Complex(range, -range);
    D = new Complex(-range, -range);
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

    // add square points
    int sq_pts_len = sq_points.size();
    if (sq_pts_len < 1)
      sq_points.add(A);
    else {
      Complex last = sq_points.get(sq_pts_len - 1);
      double l_re = last.getRe();
      double l_im = last.getIm();

      if (way == 0) {
        double new_re = l_re + 0.1;
        sq_points.add(new Complex(new_re, l_im));
        if (new_re < B.getRe())
          way++;
      }

      if (way == 1) {
        double new_im = l_re - 0.1;
        sq_points.add(new Complex(l_re, new_im));
        if (new_im > C.getIm())
          way++;
      }

      if (way == 2) {
        double new_re = l_re - 0.1;
        sq_points.add(new Complex(new_re, l_im));
        if (new_re > C.getRe())
          way++;
      }

      if (way == 3) {
        double new_im = l_re + 0.1;
        sq_points.add(new Complex(l_re, new_im));
        if (new_im < A.getIm())
          way = -1;
      }

    }

    // draw rectangle points
    for (Complex p : sq_points) {
      Variable z0 = new Variable("z", p);
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
      g2.fillRect((int) (p.getRe() * zoomX / scaleX) - 2 + centerX, (int) (p.getIm() * zoomY / scaleY) - 2 - centerY, 4,
          4);
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
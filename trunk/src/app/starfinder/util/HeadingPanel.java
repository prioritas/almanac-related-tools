package app.starfinder.util;

import astro.calc.GeoPoint;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import app.starfinder.ctx.SFContext;
import app.starfinder.ctx.StarFinderEventListener;

import java.awt.GradientPaint;
import java.awt.Graphics2D;

/**
 * A Compass
 */
public class HeadingPanel extends JPanel implements MouseListener, MouseMotionListener
{
  private int hdg = 0;
  
  public HeadingPanel()
  {
    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private void jbInit()
    throws Exception
  {
        SFContext.getInstance().addApplicationListener(new StarFinderEventListener()
        {
          public void headingHasChanged(int h)
          {
            hdg = h;
            repaint();
          }
        });
    this.setLayout( null );
    this.setSize(new Dimension(190, 30));
    addMouseMotionListener(this);
    addMouseListener(this);
  }
  
  public void paintComponent(Graphics gr)
  {    
    int w = this.getWidth();
    int h = this.getHeight();
    final int FONT_SIZE = 12;

    Color startColor = Color.black; // new Color(255, 255, 255);
    Color endColor   = Color.gray; // new Color(102, 102, 102);
//  gr.setColor(Color.black);
    GradientPaint gradient = new GradientPaint(0, this.getHeight(), startColor, 0, 0, endColor); // vertical, upside down
    ((Graphics2D)gr).setPaint(gradient);
    gr.fillRect(0, 0, w, h);
    // Width: 30 on each side = 60
    gr.setColor(Color.white);
    float oneDegree = (float)w / 60f; // 30 degrees each side
    // One graduation every 1 & 5, one label every 15
    for (int rose=hdg-30; rose<=hdg+30; rose++)
    {
      int roseToDisplay = rose;
      while (roseToDisplay >= 360) roseToDisplay -= 360;
      while (roseToDisplay < 0) roseToDisplay += 360;
      int abscisse = (int)Math.round((float)(rose + 30 - hdg) * oneDegree);
//    System.out.println("(w=" + w + ") Abscisse for " + rose + "=" + abscisse);
      gr.drawLine(abscisse, 0, abscisse, 2);
      gr.drawLine(abscisse, h - 2, abscisse, h);
      if (rose % 5 == 0)
      {
        gr.drawLine(abscisse, 0, abscisse, 5);
        gr.drawLine(abscisse, h - 5, abscisse, h);
      }
      if (rose % 15 == 0)
      {
        Font f = gr.getFont();
        gr.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
        String roseStr = Integer.toString((int)Math.round(roseToDisplay));
        if (roseToDisplay == 0)
          roseStr = "N";
        else if (roseToDisplay == 180)
          roseStr = "S";    
        else if (roseToDisplay == 90)
          roseStr = "E";    
        else if (roseToDisplay == 270)
          roseStr = "W";    
        else if (roseToDisplay == 45)
          roseStr = "NE";    
        else if (roseToDisplay == 135)
          roseStr = "SE";    
        else if (roseToDisplay == 225)
          roseStr = "SW";    
        else if (roseToDisplay == 315)
          roseStr = "NW";    
//      System.out.println("String:" + roseStr);
        int strWidth  = gr.getFontMetrics(gr.getFont()).stringWidth(roseStr);
        gr.drawString(roseStr, abscisse - strWidth / 2, (h / 2) + (FONT_SIZE / 2) );
        gr.setFont(f);        
      }
    }    
    gr.setColor(Color.red);
    gr.drawLine(w/2, 0, w/2, h);
    
  }

  public void setHdg(int hdg)
  {
    this.hdg = hdg;
  }

  public int getHdg()
  {
    return hdg;
  }

  public void mouseDragged(MouseEvent e)
  {
    int hdgOffset = (int)((mousePressedFrom.x - e.getPoint().x) * (60D / (double)this.getWidth()));
    hdg = headingFrom + hdgOffset;
    this.repaint();
        SFContext.getInstance().fireHeadingHasChanged(hdg);
  }

  public void mouseMoved(MouseEvent e)
  {
    int hdgOffset = (int)(((this.getWidth() / 2) - e.getPoint().x) * (60D / (double)this.getWidth()));
    this.setToolTipText(Integer.toString(hdg - hdgOffset));
  }

  public void mouseClicked(MouseEvent e)
  {
  }

  Point mousePressedFrom = null;
  int headingFrom = 0;
  
  public void mousePressed(MouseEvent e)
  {
    mousePressedFrom = e.getPoint();  
    headingFrom = hdg;
  }

  public void mouseReleased(MouseEvent e)
  {
  }

  public void mouseEntered(MouseEvent e)
  {
  }

  public void mouseExited(MouseEvent e)
  {
  }
}

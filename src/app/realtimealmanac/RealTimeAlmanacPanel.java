package app.realtimealmanac;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.Font;
import java.awt.Graphics;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.GregorianCalendar;

import java.util.TimeZone;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import nauticalalmanac.Anomalies;
import nauticalalmanac.Context;
import nauticalalmanac.Core;
import nauticalalmanac.Jupiter;
import nauticalalmanac.Mars;
import nauticalalmanac.Moon;
import nauticalalmanac.Saturn;
import nauticalalmanac.Venus;

import user.util.GeomUtil;

import util.images.Images;

public class RealTimeAlmanacPanel
  extends JPanel
{
  private float deltaT = 0f;
  private final static SimpleDateFormat SDF = new SimpleDateFormat("EEE, dd MMM yyyy', at' HH:mm:ss 'UTC'");
  static { SDF.setTimeZone(TimeZone.getTimeZone("etc/UTC")); }
  private final static int FONT_SIZE = 14;
  
  private ImageIcon aries   = new ImageIcon(Images.class.getResource("aries.png"));
  private ImageIcon sun     = new ImageIcon(Images.class.getResource("sun.png"));
  private ImageIcon moon    = new ImageIcon(Images.class.getResource("moon.png"));
  private ImageIcon venus   = new ImageIcon(Images.class.getResource("venus.png"));
  private ImageIcon mars    = new ImageIcon(Images.class.getResource("mars.png"));
  private ImageIcon jupiter = new ImageIcon(Images.class.getResource("jupiter.png"));
  private ImageIcon saturn  = new ImageIcon(Images.class.getResource("saturn.png"));
  
  public RealTimeAlmanacPanel()
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
    try { deltaT = Float.parseFloat(System.getProperty("deltaT", "65.5")); }
    catch (NumberFormatException nfe) { System.out.println("-DdeltaT contains bad value..."); }
    System.out.println("Delta T = " + deltaT);

    this.setLayout( null );
    this.setSize(new Dimension(400, 327));
    
    Thread realTimeThread = new Thread()
      {
        public void run()
        {
          try 
          {
            while (true)
            {
              repaint();
              try { Thread.sleep(1000L); } catch (Exception ignore) {}
            }
          }
          catch (Exception ex)
          {   
            ex.printStackTrace();
          }
        }
      };
    realTimeThread.start();
  }
  
  public void paintComponent(Graphics gr)
  {
    Graphics2D g2d = (Graphics2D)gr;
    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);      
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);      

    gr.setColor(this.getBackground());
    gr.fillRect(0, 0, this.getWidth(), this.getHeight());
    gr.setColor(Color.black);

//  gr.setFont(gr.getFont().deriveFont(FONT_SIZE));    
    gr.setFont(new Font("Courier new", Font.PLAIN, FONT_SIZE));    

    int y = FONT_SIZE;
    int x = 10;

    Calendar now = GregorianCalendar.getInstance();
    now.setTimeZone(TimeZone.getTimeZone("etc/UTC"));
    Core.julianDate(now.get(Calendar.YEAR), 
                    now.get(Calendar.MONTH) + 1, 
                    now.get(Calendar.DAY_OF_MONTH), 
                    now.get(Calendar.HOUR_OF_DAY), 
                    now.get(Calendar.MINUTE), 
                    now.get(Calendar.SECOND), 
                    deltaT);
    
    Anomalies.nutation();
    Anomalies.aberration();
    
    Core.aries();
    Core.sun();
    Venus.compute();
    Mars.compute();
    Jupiter.compute();
    Saturn.compute();
    Moon.compute();
    Core.polaris();
    Core.moonPhase();
    Core.weekDay();

    String str = SDF.format(now.getTime());                
    gr.drawString(str, x, y); 
    
    y += (FONT_SIZE + 2);  
    gr.drawImage(aries.getImage(), x, y, null);
    y += (aries.getIconHeight() + FONT_SIZE + 2);
    str = "GHA Aries   :" + GeomUtil.decToSex(Context.GHAAtrue, GeomUtil.SWING, GeomUtil.NONE);                
    gr.drawString(str, x, y); 
    
    y += (FONT_SIZE + 2);
    gr.drawImage(sun.getImage(), x, y, null);
    y += (sun.getIconHeight() + FONT_SIZE + 2);
    str = "GHA Sun     :" + GeomUtil.decToSex(Context.GHAsun, GeomUtil.SWING, GeomUtil.NONE);                 
    gr.drawString(str, x, y); 
    y += (FONT_SIZE + 1);
    str = "D Sun       :" + GeomUtil.decToSex(Context.DECsun, GeomUtil.SWING, GeomUtil.NS, GeomUtil.LEADING_SIGN);                 
    gr.drawString(str, x, y); 

    y += (FONT_SIZE + 1);
    gr.drawImage(moon.getImage(), x, y, null);
    y += (moon.getIconHeight() + FONT_SIZE + 2);
    str = "GHA Moon    :" + GeomUtil.decToSex(Context.GHAmoon, GeomUtil.SWING, GeomUtil.NONE);                  
    gr.drawString(str, x, y); 
    y += (FONT_SIZE + 1);
    str = "D Moon      :" + GeomUtil.decToSex(Context.DECmoon, GeomUtil.SWING, GeomUtil.NS, GeomUtil.LEADING_SIGN);                 
    gr.drawString(str, x, y); 

    y += (FONT_SIZE + 1);
    gr.drawImage(venus.getImage(), x, y, null);
    y += (venus.getIconHeight() + FONT_SIZE + 2);
    str = "GHA Venus   :" + GeomUtil.decToSex(Context.GHAvenus, GeomUtil.SWING, GeomUtil.NONE);                  
    gr.drawString(str, x, y); 
    y += (FONT_SIZE + 1);
    str = "D Venus     :" + GeomUtil.decToSex(Context.DECvenus, GeomUtil.SWING, GeomUtil.NS, GeomUtil.LEADING_SIGN);                 
    gr.drawString(str, x, y); 

    y += (FONT_SIZE + 1);
    gr.drawImage(mars.getImage(), x, y, null);
    y += (mars.getIconHeight() + FONT_SIZE + 2);
    str = "GHA Mars    :" + GeomUtil.decToSex(Context.GHAmars, GeomUtil.SWING, GeomUtil.NONE);                  
    gr.drawString(str, x, y); 
    y += (FONT_SIZE + 1);
    str = "D Mars      :" + GeomUtil.decToSex(Context.DECmars, GeomUtil.SWING, GeomUtil.NS, GeomUtil.LEADING_SIGN);                 
    gr.drawString(str, x, y); 

    y += (FONT_SIZE + 1);
    gr.drawImage(jupiter.getImage(), x, y, null);
    y += (jupiter.getIconHeight() + FONT_SIZE + 2);
    str = "GHA Jupiter :" + GeomUtil.decToSex(Context.GHAjupiter, GeomUtil.SWING, GeomUtil.NONE);                  
    gr.drawString(str, x, y); 
    y += (FONT_SIZE + 1);
    str = "D Jupiter   :" + GeomUtil.decToSex(Context.DECjupiter, GeomUtil.SWING, GeomUtil.NS, GeomUtil.LEADING_SIGN);                 
    gr.drawString(str, x, y); 

    y += (FONT_SIZE + 1);
    gr.drawImage(saturn.getImage(), x, y, null);
    y += (saturn.getIconHeight() + FONT_SIZE + 2);
    str = "GHA Saturn  :" + GeomUtil.decToSex(Context.GHAsaturn, GeomUtil.SWING, GeomUtil.NONE);                  
    gr.drawString(str, x, y); 
    y += (FONT_SIZE + 1);
    str = "D Saturn    :" + GeomUtil.decToSex(Context.DECsaturn, GeomUtil.SWING, GeomUtil.NS, GeomUtil.LEADING_SIGN);                 
    gr.drawString(str, x, y); 
  }
}

package app.planetarium;

import astro.calc.GeoPoint;
import chart.components.ui.ChartPanel;
import chart.components.ui.ChartPanelInterface;
import chart.components.ui.ChartPanelParentInterface;
import chart.components.util.World;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import java.text.DecimalFormat;

import java.util.EventObject;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import javax.swing.text.NumberFormatter;

import nauticalalmanac.Anomalies;
import nauticalalmanac.Context;
import nauticalalmanac.Core;
import nauticalalmanac.Jupiter;
import nauticalalmanac.Mars;
import nauticalalmanac.Moon;
import nauticalalmanac.Saturn;
import nauticalalmanac.Star;
import nauticalalmanac.Venus;

import user.util.GeomUtil;

public class PlanetariumPanel 
     extends JPanel
  implements ChartPanelParentInterface
{
  private BorderLayout borderLayout;
  private JScrollPane jScrollPane;
  private ChartPanel chartPanel;
  private JPanel bottomPanel;
  private JButton zoomInButton;
  private JButton zoomOutButton;
  private JButton spinButton;
  private JCheckBox transparentCheckBox;
  private JButton setValuesButton;
  private JFormattedTextField leftRight;
  private JFormattedTextField foreAft;

  private boolean videoHasBeenPlayed = false;
  
  public PlanetariumPanel()
  {
    borderLayout = new BorderLayout();
    jScrollPane = new JScrollPane();
    chartPanel = new ChartPanel(this, 600, 400);
    chartPanel.setProjection(ChartPanelInterface.GLOBE_VIEW);
    bottomPanel = new JPanel();
    zoomInButton = new JButton();
    zoomOutButton = new JButton();
    spinButton = new JButton();
    transparentCheckBox = new JCheckBox();
    leftRight = new JFormattedTextField(new DecimalFormat("00.00"));
    leftRight.setPreferredSize(new Dimension(50, 20));
    foreAft = new JFormattedTextField(new DecimalFormat("#0.00"));
    foreAft.setPreferredSize(new Dimension(50, 20));
    setValuesButton = new JButton();
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    setLayout(borderLayout);
    zoomInButton.setText("Zoom In");
    zoomInButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent e)
      {
        zoomInActionPerformed(e);
      }
    });
    zoomOutButton.setText("Zoom Out");
    zoomOutButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent e)
      {
        zoomOutActionPerformed(e);
      }
    });
    spinButton.setText("Spin");
    spinButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent e)
      {
        spinActionPerformed(e);
      }
    });
    transparentCheckBox.setText("Transparent");
    transparentCheckBox.setSelected(true);
    transparentCheckBox.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent e)
      {
        transparentActionPerformed(e);
      }
    });
    
    leftRight.setText(((NumberFormatter)leftRight.getFormatter()).getFormat().format(23.0));
    leftRight.setToolTipText("Left-Right tilt (degrees)");
    foreAft.setText(((NumberFormatter)foreAft.getFormatter()).getFormat().format(40.0));
    foreAft.setToolTipText("Latitude of the eye (degrees)");
    setValuesButton.setText("Set");
    setValuesButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent e)
      {
        setValuesActionPerformed(e);
      }
    });
    jScrollPane.getViewport().add(chartPanel, null);
    add(jScrollPane, BorderLayout.CENTER);
    bottomPanel.add(zoomInButton, null);
    bottomPanel.add(zoomOutButton, null);
    bottomPanel.add(spinButton, null);
    bottomPanel.add(transparentCheckBox, null);
    bottomPanel.add(leftRight, null);
    bottomPanel.add(foreAft, null);
    bottomPanel.add(setValuesButton, null);
    add(bottomPanel, BorderLayout.SOUTH);
    double nLat  =   90D;
    double sLat  =  -90D;
    double wLong = -180.0D;
    double eLong =  180.0D; // chartPanel.calculateEastG(nLat, sLat, wLong);
    
    chartPanel.setPositionToolTipEnabled(false);
    
    chartPanel.setGlobeViewLngOffset(-140.0);
    chartPanel.setGlobeViewRightLeftRotation(23.0);
    chartPanel.setGlobeViewForeAftRotation(40.0);  
    
    chartPanel.setTransparentGlobe(true);
    
    chartPanel.setEastG(eLong);
    chartPanel.setWestG(wLong);
    chartPanel.setNorthL(nLat);
    chartPanel.setSouthL(sLat);
    chartPanel.setHorizontalGridInterval(10D);
    chartPanel.setVerticalGridInterval(10D);
    chartPanel.setWithScale(false);
    chartPanel.setChartColor(Color.black);
    chartPanel.setGridColor(Color.gray);
    chartPanel.setPostitBGColor(new Color(0f, 0f, 0f, 0.5f));
  }

  private void zoomInActionPerformed(ActionEvent e)
  {
    chartPanel.zoomIn();
  }

  boolean spinning = false;
  SpinningThread spinningThread = null;
  
  private void spinActionPerformed(ActionEvent e)
  {
    spinning = !spinning;
    if (spinning)
    {
      // Start the thread
      spinningThread = new SpinningThread();
      spinningThread.start();
    }
    else
    {
      // Stop the thread
      spinningThread.stopSpinning();
    }
  }
  
  private void transparentActionPerformed(ActionEvent e)
  {
    chartPanel.setTransparentGlobe(transparentCheckBox.isSelected());
    chartPanel.repaint();
  }
  
  private void zoomOutActionPerformed(ActionEvent e)
  {
    chartPanel.zoomOut();
  }

  private void setValuesActionPerformed(ActionEvent e)
  {
    double lr = 0.0;
    double fa = 0.0;
    try
    {
      lr = Double.parseDouble(leftRight.getText());
      fa = Double.parseDouble(foreAft.getText());
    }
    catch (NumberFormatException nfe)
    {
      System.err.println(nfe.getMessage());
    }
    chartPanel.setGlobeViewRightLeftRotation(lr);
    chartPanel.setGlobeViewForeAftRotation(fa);      
    chartPanel.repaint();
  }

  GeoPoint from = null;
  GeoPoint to   = null;

  // For video
  GeoPoint[] gpa = 
  {
    new GeoPoint(GeomUtil.sexToDec("37", "56"), -GeomUtil.sexToDec("123", "4")), // SF
    new GeoPoint(GeomUtil.sexToDec("34", "0"), -GeomUtil.sexToDec("120", "0")), // Channel Islands
    new GeoPoint(-GeomUtil.sexToDec("9", "0"), -GeomUtil.sexToDec("140", "0")), // Marquesas
    new GeoPoint(-GeomUtil.sexToDec("15", "29"), -GeomUtil.sexToDec("145", "44")), // Tuamotus
    new GeoPoint(-GeomUtil.sexToDec("18", "0"), -GeomUtil.sexToDec("149", "0")), // Tahiti
    new GeoPoint(-GeomUtil.sexToDec("19", "0"), -GeomUtil.sexToDec("160", "0")), // Cook Islands
    new GeoPoint(-GeomUtil.sexToDec("18", "30"), -GeomUtil.sexToDec("173", "0")), // Tonga
    new GeoPoint(-GeomUtil.sexToDec("18", "0"), -GeomUtil.sexToDec("180", "0")), // Fiji
    new GeoPoint(GeomUtil.sexToDec("21", "15"), -GeomUtil.sexToDec("157", "40")), // Hawaii
//    new GeoPoint(-GeomUtil.sexToDec("15", "0"), GeomUtil.sexToDec("168", "0")), // Vanuatu
//    new GeoPoint(-GeomUtil.sexToDec("8", "0"), GeomUtil.sexToDec("159", "0")), // Solomon
//    new GeoPoint(GeomUtil.sexToDec("9", "0"), GeomUtil.sexToDec("155", "0")), // Caroline
//    new GeoPoint(GeomUtil.sexToDec("13", "15"), GeomUtil.sexToDec("144", "45")), // Guam
//    new GeoPoint(GeomUtil.sexToDec("35", "0"),  GeomUtil.sexToDec("140", "0")), // Tokyo
    new GeoPoint(GeomUtil.sexToDec("50", "0"), -GeomUtil.sexToDec("127", "0")), // Vancouver
    new GeoPoint(GeomUtil.sexToDec("37", "56"), -GeomUtil.sexToDec("123", "4")) // SF
  };
  
  private boolean isVisible(double l, double g)
  {
    boolean plot = true;
    if (chartPanel.getProjection() == ChartPanelInterface.GLOBE_VIEW)
    {
      if (!chartPanel.isTransparentGlobe() && chartPanel.isBehind(l, g - chartPanel.getGlobeViewLngOffset()))
        plot = false;
    }
    return plot;
  }
  
  public void chartPanelPaintComponent(Graphics gr)
  {
    Graphics2D g2d = null;
    if (gr instanceof Graphics2D)
      g2d = (Graphics2D)gr;
    
    chartPanel.setBackground(Color.black);
    chartPanel.setChartBackGround(Color.black);
    chartPanel.setChartColor(Color.gray);
    chartPanel.setGridColor(Color.lightGray);
    World.drawChart(chartPanel, gr);
    
    calculateCelestData();

    plotBody(gr, Context.DECsun, Context.GHAsun, "Sun");

    plotBody(gr, Context.DECmoon, Context.GHAmoon, "Moon");

    plotBody(gr, Context.DECvenus, Context.GHAvenus, "Venus");
    plotBody(gr, Context.DECmars, Context.GHAmars, "Mars");
    plotBody(gr, Context.DECjupiter, Context.GHAjupiter, "Jupiter");
    plotBody(gr, Context.DECsaturn, Context.GHAsaturn, "Saturn");

    for (int i=0; i<Star.getCatalog().length; i++)
    {
      String starName = Star.getCatalog()[i].getStarName();
      Core.starPos(starName);
      plotBody(gr, Context.DECstar, Context.GHAstar, starName);
    }
  }
  
  private void plotBody(Graphics gr, double l, double g, String bodyName)
  {
    Point p = chartPanel.getPanelPoint(l, g);
//  System.out.println("Plotting Sun:" + Context.DECsun + ", " + Context.GHAsun);
    gr.setColor(Color.orange);
    gr.fillOval(p.x - 2, p.y - 2, 4, 4);    
  }

  public boolean onEvent(EventObject e, int type)
  {
    if (type == ChartPanel.MOUSE_CLICKED)
    {
      if (from == null)
        from = chartPanel.getGeoPos(((MouseEvent)e).getX(), ((MouseEvent)e).getY()); 
      else
        to = chartPanel.getGeoPos(((MouseEvent)e).getX(), ((MouseEvent)e).getY()); 
      chartPanel.repaint();
    }
    return true;
  }

  public String getMessForTooltip()
  {
    return null;
  }

  public boolean replaceMessForTooltip()
  {
    return false;
  }

  public void videoCompleted() 
  {
    videoHasBeenPlayed = true;   
  }
  
  public void videoFrameCompleted(Graphics g, Point p) {}

  public void zoomFactorHasChanged(double d)
  {
  }

  public void chartDDZ(double top, double bottom, double left, double right)
  {
  }

  private void calculateCelestData()
  {
//  Core.julianDate(year, month, day, hour, minute, second, deltaT);
    Core.julianDate(2010, 1, 19, 0, 0, 0, 65.984); // TODO Parameter the date & time
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
  }
  
  class SpinningThread extends Thread
  {
    private boolean go = true;
    public SpinningThread()
    { super(); }
    
    public void stopSpinning()
    {
      go = false;
    }
    public void run()
    {
      while (go)
      {
        try
        {
          double g = chartPanel.getGlobeViewLngOffset();
          g -= 1.0;
          while (g < -180) g += 360;
          chartPanel.setGlobeViewLngOffset(g);
//        System.out.println("G:" + g);
          synchronized (this)
          {
            chartPanel.repaint();
          }
          sleep(1000L);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    }
  }
}

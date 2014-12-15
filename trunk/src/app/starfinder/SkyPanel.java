package app.starfinder;


import app.starfinder.ctx.SFContext;
import app.starfinder.ctx.StarFinderEventListener;

import app.util.PositionPanel;

import astro.calc.GeoPoint;

import calculation.SightReductionUtil;

import chart.components.ui.ChartPanel;
import chart.components.ui.ChartPanelParentInterface_II;

import coreutilities.ctx.CoreContext;

import coreutilities.ctx.CoreEventListener;

import coreutilities.gui.HeadingPanel;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import java.io.FileNotFoundException;
import java.io.FileReader;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nauticalalmanac.Anomalies;
import nauticalalmanac.Context;
import nauticalalmanac.Core;
import nauticalalmanac.Jupiter;
import nauticalalmanac.Mars;
import nauticalalmanac.Moon;
import nauticalalmanac.Saturn;
import nauticalalmanac.Star;
import nauticalalmanac.Venus;

import nmea.event.NMEAReaderListener;

import nmea.server.ctx.NMEAContext;
import nmea.server.ctx.NMEADataCache;

import ocss.nmea.parser.Angle360;
import ocss.nmea.parser.GeoPos;
import ocss.nmea.parser.SVData;

import oracle.xml.parser.v2.DOMParser;
import oracle.xml.parser.v2.XMLDocument;
import oracle.xml.parser.v2.XMLElement;

import org.w3c.dom.NodeList;

import user.util.GeomUtil;
import user.util.TimeUtil;


public class SkyPanel
  extends JPanel
  implements ChartPanelParentInterface_II
{
  private boolean displayStars      = true;
  private boolean displayPlanets    = true;
  private boolean displaySun        = true;
  private boolean displayMoon       = true;
  
  private boolean calculationRequired = true;
  private float horizonTransparency = 0.5f;
  
  private boolean displayNegativeAltitudes = false;
  private boolean displayStarsBehind       = false;
  private boolean displayHZ                = true;
  
  private boolean displayZenithalGrid = false;
  private boolean displayCardinalGrid = false;
  
  private static int year  = 0, 
                     month = 0, 
                     day = 0,  
                     hour = 0, 
                     minute = 0;
  private static float second = 0f, deltaT = 65.5f;
  
  private final static double HALF_SPAN = 180D; 
  private final static double EYE_OFFSET = -10d;

  public final static String POSITION_PROPRTIES_FILE_NAME = "last-position.properties";
  private double latitude  =   37.66426777566215, 
                 longitude = -122.38021695622592; // The ketch at its dock
  
  private double observerHeading = 180D;

  private ChartPanel chartPanel;
  private HeadingPanel headingPanel;
  private JScrollPane jScrollPane;
  private JPanel bottomPanel;
  private JPanel topRightPanel;
  private JPanel rightPanel;
  private PositionPanel positionPanel = new PositionPanel();
  
  private JPanel chartPlusCompassPanel;
  
  private JButton right = new JButton(">");
  private JButton left  = new JButton("<");
  
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private JCheckBox displayNegativeAltCheckBox = new JCheckBox();
  private JCheckBox displayStarsBehindCheckBox = new JCheckBox();
  private JSeparator jSeparator3 = new JSeparator();
  private JLabel jLabel1 = new JLabel();
  private JLabel jLabel2 = new JLabel();
  private JLabel jLabel3 = new JLabel();
  private JLabel jLabel4 = new JLabel();
  private JLabel jLabel5 = new JLabel();
  private JLabel jLabel6 = new JLabel();
  private JLabel jLabel7 = new JLabel();
  private JSeparator jSeparator1 = new JSeparator();
  private JCheckBox displaySunCheckBox = new JCheckBox();
  private JCheckBox displayMoonCheckBox = new JCheckBox();
  private JCheckBox displayPlanetsCheckBox = new JCheckBox();
  private JCheckBox displayStarsCheckBox = new JCheckBox();
  private JSeparator jSeparator2 = new JSeparator();
  private JTextField yearTextField = new JTextField();
  private JTextField dayTextField = new JTextField();
  private JTextField hourTextField = new JTextField();
  private JTextField minuteTextField = new JTextField();
  private JTextField secondTextField = new JTextField();
  private JTextField deltaTTextField = new JTextField();
  private JComboBox monthComboBox = new JComboBox();
  private JLabel jLabel10 = new JLabel();
  private JButton timeButton = new JButton();
  private JPanel timeButtonPanel = new JPanel();
  private JButton nowButton = new JButton();
  private JCheckBox realTimeCheckBox = new JCheckBox();

  private transient HashMap<String, DisplayedBody> displayedBodies = null;
  private List<SVData> displayedSatellites = null;
  
  private boolean replaceTooltipMess = false;
  private String tooltipMess = null;
  private JCheckBox posGPSCheckBox = new JCheckBox();
  private JCheckBox hdgGPSCheckBox = new JCheckBox();
  private JCheckBox gpsSatellitesCheckBox = new JCheckBox();
  private JSeparator jSeparator4 = new JSeparator();
  private JLabel ariesGHALabel = new JLabel();
  private JLabel ariesLHALabel = new JLabel();
  private JSeparator jSeparator5 = new JSeparator();
  private JCheckBox hzCheckBox = new JCheckBox();
  private JCheckBox zenithalGridCheckBox = new JCheckBox();
  private JCheckBox cardinalGridCheckBox = new JCheckBox();
  private JSeparator jSeparator6 = new JSeparator();
  private JPanel nmeaStreamPanel = new JPanel();
  private JCheckBox nmeaStreamCheckBox = new JCheckBox();
  private JSlider horizonTransparencySlider = new JSlider();

  private transient NMEAReaderListener nmeaListener = null;
  
  private transient CoreEventListener coreListener = new CoreEventListener()
       {
         @Override
         public void headingHasChanged(int hdg) 
         {
           SFContext.getInstance().fireHeadingHasChanged(hdg);
         }
       };
  
  public SkyPanel()
  {
    try { deltaT = Float.parseFloat(System.getProperty("deltaT", "65.5")); }
    catch (NumberFormatException nfe) { System.out.println("-DdeltaT contains bad value..."); }
    System.out.println("Delta T = " + deltaT);
    try { jbInit(); }
    catch (Exception e) { e.printStackTrace(); }
  }

  private transient StarFinderEventListener sfel = new StarFinderEventListener()
    {
      public void requestChartPanelRepaint()
      {
        chartPanel.repaint();
      }
      
      public void headingHasChanged(int h)
      {
        observerHeading = h;
        updateHeading();
      }
      
      public void positionHasChanged(GeoPoint newPos)
      {
        latitude = newPos.getL();
        longitude = newPos.getG();
        chartPanel.repaint();
      }
      
      public void setGPSSatellites(ArrayList<SVData> svData) 
      {
        displayedSatellites = svData;
        chartPanel.repaint();
      }
    };

  private void jbInit()
    throws Exception
  {
    try
    {
    Properties props = new Properties();
    props.load(new FileReader(POSITION_PROPRTIES_FILE_NAME));
    try { latitude = Double.parseDouble(props.getProperty("last.latitude")); } catch (Exception ignore) {}
    try { longitude = Double.parseDouble(props.getProperty("last.longitude")); } catch (Exception ignore) {}
    }
    catch (FileNotFoundException fnfe)
    {
      System.err.println("Position properties file not found...");
    }
    SFContext.getInstance().addApplicationListener(sfel);

    this.setLayout( new BorderLayout() );

    this.setSize(new Dimension(781, 615));
    chartPlusCompassPanel = new JPanel();
    chartPlusCompassPanel.setLayout(new BorderLayout());
    
    jScrollPane = new JScrollPane();
    chartPanel = new ChartPanel(this);
    chartPanel.setEnforceTooltip(true);
    chartPanel.setMouseDraggedEnabled(true);
    headingPanel = new HeadingPanel(true);
    headingPanel.setDraggable(true);
    CoreContext.getInstance().addApplicationListener(coreListener);
    jScrollPane.getViewport().add(chartPanel, null);
    
    chartPlusCompassPanel.add(jScrollPane, BorderLayout.CENTER);
    
    this.add(chartPlusCompassPanel, BorderLayout.CENTER);

    bottomPanel = new JPanel();
    bottomPanel.setLayout(new BorderLayout());
    right.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          right_actionPerformed(e);
        }
      });
    right.setToolTipText("Turn the boat to starboard");
    left.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          left_actionPerformed(e);
        }
      });
    left.setToolTipText("Turn the boat to port");
    displayNegativeAltCheckBox.setText("Display Neg. Alt.");
    displayNegativeAltCheckBox.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          displayNegativeAltCheckBox_actionPerformed(e);
        }
      });
    displayStarsBehindCheckBox.setText("Display bodies behind");
    displayStarsBehindCheckBox.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          displayStarsBehindCheckBox_actionPerformed(e);
        }
      });
    chartPlusCompassPanel.add(bottomPanel, BorderLayout.SOUTH);
    bottomPanel.add(headingPanel, BorderLayout.CENTER);
    bottomPanel.add(left, BorderLayout.WEST);
    bottomPanel.add(right, BorderLayout.EAST);

    headingPanel.setHdg((int) observerHeading);
    headingPanel.setPreferredSize(new Dimension(190, 30));
    
    rightPanel = new JPanel();
    rightPanel.setLayout(new BorderLayout());
    topRightPanel = new JPanel();
    topRightPanel.setLayout(gridBagLayout1);
    topRightPanel.add(displayNegativeAltCheckBox, new GridBagConstraints(0, 7, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
    topRightPanel.add(displayStarsBehindCheckBox, new GridBagConstraints(0, 8, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));

    topRightPanel.add(jSeparator3, new GridBagConstraints(0, 9, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    topRightPanel.add(jLabel1, new GridBagConstraints(0, 21, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
    topRightPanel.add(jLabel2, new GridBagConstraints(0, 15, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
    topRightPanel.add(jLabel3, new GridBagConstraints(0, 16, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
    topRightPanel.add(jLabel4, new GridBagConstraints(0, 17, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
    topRightPanel.add(jLabel5, new GridBagConstraints(0, 18, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
    topRightPanel.add(jLabel6, new GridBagConstraints(0, 19, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
    topRightPanel.add(jLabel7, new GridBagConstraints(0, 20, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
    topRightPanel.add(jSeparator1, new GridBagConstraints(0, 23, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    topRightPanel.add(displaySunCheckBox, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
    topRightPanel.add(displayMoonCheckBox, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
    topRightPanel.add(displayPlanetsCheckBox, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
    topRightPanel.add(displayStarsCheckBox, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
    topRightPanel.add(jSeparator2, new GridBagConstraints(0, 6, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    topRightPanel.add(yearTextField, new GridBagConstraints(1, 15, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 3), 0, 0));
    topRightPanel.add(dayTextField, new GridBagConstraints(1, 17, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 3), 0, 0));
    topRightPanel.add(hourTextField, new GridBagConstraints(1, 18, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 3), 0, 0));
    topRightPanel.add(minuteTextField, new GridBagConstraints(1, 19, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 3), 0, 0));
    topRightPanel.add(secondTextField, new GridBagConstraints(1, 20, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 3), 0, 0));
    topRightPanel.add(deltaTTextField, new GridBagConstraints(1, 21, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 3), 0, 0));
    topRightPanel.add(monthComboBox, new GridBagConstraints(1, 16, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 3), 0, 0));
    topRightPanel.add(jLabel10, new GridBagConstraints(0, 14, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 3, 0, 0), 0, 0));
    topRightPanel.add(positionPanel, new GridBagConstraints(0, 24, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    topRightPanel.add(timeButtonPanel, new GridBagConstraints(0, 22, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
    topRightPanel.add(posGPSCheckBox, new GridBagConstraints(0, 26, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 3, 0, 0), 0, 0));
    topRightPanel.add(hdgGPSCheckBox, new GridBagConstraints(0, 27, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 3, 0, 0), 0, 0));
    topRightPanel.add(gpsSatellitesCheckBox, new GridBagConstraints(0, 28, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 3, 0, 0), 0, 0));

    topRightPanel.add(jSeparator4, new GridBagConstraints(0, 29, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 10, 0), 0, 0));
    topRightPanel.add(ariesGHALabel, new GridBagConstraints(0, 30, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    topRightPanel.add(ariesLHALabel, new GridBagConstraints(0, 31, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    topRightPanel.add(jSeparator5, new GridBagConstraints(0, 13, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    topRightPanel.add(hzCheckBox, new GridBagConstraints(0, 10, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
    topRightPanel.add(zenithalGridCheckBox, new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
    topRightPanel.add(cardinalGridCheckBox, new GridBagConstraints(0, 12, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
    topRightPanel.add(jSeparator6, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    nmeaStreamPanel.add(nmeaStreamCheckBox, null);

    nmeaStreamCheckBox.setSelected(false);
    nmeaStreamCheckBox.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          posGPSCheckBox.setEnabled(nmeaStreamCheckBox.isSelected());
          hdgGPSCheckBox.setEnabled(nmeaStreamCheckBox.isSelected());
          gpsSatellitesCheckBox.setEnabled(false); // nmeaStreamCheckBox.isSelected());
          // Add/Remove listener
          if (nmeaStreamCheckBox.isSelected())
          {
            nmeaListener = new NMEAReaderListener("SkyPanel", "SkyPanel")
              {
                /**
                 * On this event, activate the possibility to read from the NMEA Port.
                 * If activated, send position to the sky panels.
                 */
                @Override
                public void manageNMEAString(String string)
                {
                  // If NMEA Stream chosen, send position to the sky panels
                  // Read the cache to find the already calculated (parsed) position
                  if (posGPSCheckBox.isSelected())
                  {
                    GeoPos gps = (GeoPos)NMEAContext.getInstance().getCache().get(NMEADataCache.POSITION, true);
                    if (gps != null)
                    {
              //      System.out.println("Your fix:" + gps.toString());
                      positionPanel.setPosition(gps.lat, gps.lng);
                      SFContext.getInstance().firePositionHasChanged(new GeoPoint(gps.lat, gps.lng));
                    }
                    else
                      System.out.println("No position yet...");
                  }
                  // Heading
                  if (hdgGPSCheckBox.isSelected())
                  {
                    int hdg = 0; 
                    try 
                    { 
                      hdg = (int)Math.round(((Angle360) NMEAContext.getInstance().getCache().get(NMEADataCache.HDG_TRUE)).getValue()); 
                      headingPanel.setHdg(hdg);
                      SFContext.getInstance().fireHeadingHasChanged(hdg);
                    } 
                    catch (Exception ignore) {}
                  }
                  // Satellites
                  if (gpsSatellitesCheckBox.isSelected())
                  {
                    // TODO Implement
//                  List<SVData> al = new ArrayList<SVData>(satList.getLength());
//                  SFContext.getInstance().fireSetGPSSatellites(al);
                  }
                }
              };
            NMEAContext.getInstance().addNMEAReaderListener(nmeaListener);
          }
          else
            NMEAContext.getInstance().removeNMEAReaderListener(nmeaListener);
          
        }
      });
    topRightPanel.add(nmeaStreamPanel, new GridBagConstraints(0, 25, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    topRightPanel.add(horizonTransparencySlider, new GridBagConstraints(0, 5, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    horizonTransparencySlider.setToolTipText("Horizon Opacity");
    
    timeButtonPanel.add(timeButton, null);
    timeButtonPanel.add(nowButton, null);
    timeButtonPanel.add(realTimeCheckBox, null);
    rightPanel.add(topRightPanel, BorderLayout.NORTH);

    monthComboBox.removeAllItems();
    monthComboBox.addItem("Jan");
    monthComboBox.addItem("Feb");
    monthComboBox.addItem("Mar");
    monthComboBox.addItem("Apr");
    monthComboBox.addItem("May");
    monthComboBox.addItem("Jun");
    monthComboBox.addItem("Jul");
    monthComboBox.addItem("Aug");
    monthComboBox.addItem("Sep");
    monthComboBox.addItem("Oct");
    monthComboBox.addItem("Nov");
    monthComboBox.addItem("Dec");

    displaySunCheckBox.setSelected(displaySun);
    displayMoonCheckBox.setSelected(displayMoon);
    displayPlanetsCheckBox.setSelected(displayPlanets);
    displayStarsCheckBox.setSelected(displayStars);

    displayNegativeAltCheckBox.setSelected(displayNegativeAltitudes);
    displayNegativeAltCheckBox.setFont(new Font("Tahoma", 0, 9));
    displayStarsBehindCheckBox.setSelected(displayStarsBehind);

    displayStarsBehindCheckBox.setFont(new Font("Tahoma", 0, 9));
    jLabel1.setText("Delta T");
    jLabel1.setFont(new Font("Tahoma", 0, 9));
    jLabel2.setText("Year");
    jLabel2.setFont(new Font("Tahoma", 0, 9));
    jLabel3.setText("Month");
    jLabel3.setFont(new Font("Tahoma", 0, 9));
    jLabel4.setText("Day");
    jLabel4.setFont(new Font("Tahoma", 0, 9));
    jLabel5.setText("Hours");
    jLabel5.setFont(new Font("Tahoma", 0, 9));
    jLabel6.setText("Minutes");
    jLabel6.setFont(new Font("Tahoma", 0, 9));
    jLabel7.setText("Seconds");
    jLabel7.setFont(new Font("Tahoma", 0, 9));
    displaySunCheckBox.setText("Display Sun");
    displaySunCheckBox.setFont(new Font("Tahoma", 0, 9));
    displaySunCheckBox.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          displaySunCheckBox_actionPerformed(e);
        }
      });
    displayMoonCheckBox.setText("Display Moon");
    displayMoonCheckBox.setFont(new Font("Tahoma", 0, 9));
    displayMoonCheckBox.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          displayMoonCheckBox_actionPerformed(e);
        }
      });
    displayPlanetsCheckBox.setText("Display Planets");
    displayPlanetsCheckBox.setFont(new Font("Tahoma", 0, 9));
    displayPlanetsCheckBox.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          displayPlanetsCheckBox_actionPerformed(e);
        }
      });
    displayStarsCheckBox.setText("Display Stars");
    displayStarsCheckBox.setFont(new Font("Tahoma", 0, 9));
    displayStarsCheckBox.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          displayStarsCheckBox_actionPerformed(e);
        }
      });
    yearTextField.setHorizontalAlignment(JTextField.RIGHT);
    yearTextField.setFont(new Font("Tahoma", 0, 9));
    dayTextField.setHorizontalAlignment(JTextField.RIGHT);
    dayTextField.setFont(new Font("Tahoma", 0, 9));
    hourTextField.setHorizontalAlignment(JTextField.RIGHT);
    hourTextField.setFont(new Font("Tahoma", 0, 9));
    minuteTextField.setHorizontalAlignment(JTextField.RIGHT);
    minuteTextField.setFont(new Font("Tahoma", 0, 9));
    secondTextField.setHorizontalAlignment(JTextField.RIGHT);
    secondTextField.setFont(new Font("Tahoma", 0, 9));
    deltaTTextField.setHorizontalAlignment(JTextField.RIGHT);
    deltaTTextField.setFont(new Font("Tahoma", 0, 9));
    monthComboBox.setFont(new Font("Tahoma", 0, 9));
    jLabel10.setText("UTC");
    jLabel10.setFont(new Font("Tahoma", 1, 9));
    timeButton.setText("Set");
    timeButton.setFont(new Font("Tahoma", 0, 9));
    timeButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          timeButton_actionPerformed(e);
        }
      });
    nowButton.setText("Now");
    nowButton.setFont(new Font("Tahoma", 0, 9));
    nowButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          nowButton_actionPerformed(e);
        }
      });
    realTimeCheckBox.setText("R.T.");
    realTimeCheckBox.setToolTipText("Real Time");
    realTimeCheckBox.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          realTimeCheckBox_actionPerformed(e);
        }
      });

    posGPSCheckBox.setEnabled(false);
    hdgGPSCheckBox.setEnabled(false);
    gpsSatellitesCheckBox.setEnabled(false);

    posGPSCheckBox.setText("Position from GPS");
    posGPSCheckBox.setFont(new Font("Tahoma", 0, 9));
    hdgGPSCheckBox.setText("Heading from GPS");
    hdgGPSCheckBox.setFont(new Font("Tahoma", 0, 9));
    gpsSatellitesCheckBox.setText("Show GPS Satellites");
    gpsSatellitesCheckBox.setFont(new Font("Tahoma", 0, 9));

    ariesGHALabel.setText("Aries GHA");
    ariesGHALabel.setFont(new Font("Tahoma", 0, 9));
    ariesLHALabel.setText("Aries LHA");
    ariesLHALabel.setFont(new Font("Tahoma", 0, 9));
    hzCheckBox.setText("Altitude and Azimuth");
    hzCheckBox.setSelected(true);
    hzCheckBox.setFont(new Font("Tahoma", 0, 9));
    hzCheckBox.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          hzCheckBox_actionPerformed(e);
        }
      });
    zenithalGridCheckBox.setText("Azimuthal Grid");
    zenithalGridCheckBox.setSelected(displayZenithalGrid);
    zenithalGridCheckBox.setFont(new Font("Tahoma", 0, 9));
    zenithalGridCheckBox.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          zenithalGridCheckBox_actionPerformed(e);
        }
      });
    cardinalGridCheckBox.setText("Equatorial Grid");
    cardinalGridCheckBox.setSelected(displayCardinalGrid);
    cardinalGridCheckBox.setFont(new Font("Tahoma", 0, 9));
    cardinalGridCheckBox.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cardinalGridCheckBox_actionPerformed(e);
        }
      });
    nmeaStreamCheckBox.setText("Read NMEA Source");
    nmeaStreamCheckBox.setFont(new Font("Tahoma", 0, 9));
    horizonTransparencySlider.addChangeListener(new ChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          JSlider slider = (JSlider)e.getSource();
          int value = slider.getValue();
          horizonTransparency = value / 100f;
          chartPanel.repaint();
        }
      });
    this.add(rightPanel, BorderLayout.EAST);

    double nLat  =  90D;
    double sLat  = -90D;
    
    double wLong = -180d; // observerHeading - HALF_SPAN;
    double eLong =  180d; // observerHeading + HALF_SPAN; // chartPanel.calculateEastG(nLat, sLat, wLong);
    chartPanel.setEastG(eLong);
    chartPanel.setWestG(wLong);
    chartPanel.setNorthL(nLat);
    chartPanel.setSouthL(sLat);
    chartPanel.setHorizontalGridInterval(10D);
    chartPanel.setVerticalGridInterval(10D);
    
    Color startColor = Color.black; 
    Color endColor   = new Color(0, 0, 160); 
    GradientPaint gradient = new GradientPaint(0, 0, startColor, this.getWidth(), this.getHeight(), endColor);
//  GradientPaint gradient = new GradientPaint(0, this.getHeight(), startColor, 0, 0, endColor); // vertical, upside down
    
//  chartPanel.setChartBackGround(Color.black);
    chartPanel.setChartBackGround(gradient);
    
    chartPanel.setGridColor(Color.darkGray);
    chartPanel.setAltGridColor(Color.gray);
    
    chartPanel.setProjection(ChartPanel.GLOBE_VIEW);
    double lngOffset = (observerHeading + 180) % 360d;
    chartPanel.setGlobeViewLngOffset(lngOffset);
    chartPanel.setGlobeViewRightLeftRotation(0.0);
    chartPanel.setGlobeViewForeAftRotation(EYE_OFFSET);    
    
    chartPanel.setTransparentGlobe(displayStarsBehind);
    chartPanel.setAntiTransparentGlobe(!displayStarsBehind); // was true
    
    jScrollPane.setSize(chartPanel.getSize());
    jScrollPane.setPreferredSize(chartPanel.getSize());
    
    positionPanel.setPosition(latitude, longitude);
    setCurrentTime();   
    chartPanel.repaint();
  }
  
  public void onExit()
  {
//  System.out.println("Removing Listeners from Planetarium");
    CoreContext.getInstance().removeApplicationListener(coreListener);
    NMEAContext.getInstance().removeNMEAReaderListener(nmeaListener);    
  }
  
  public void chartPanelPaintComponentBefore(Graphics graphics)
  {
    chartPanel.setSouthL(displayNegativeAltCheckBox.isSelected()?-90:0);
    chartPanel.setWithGrid(displayZenithalGrid);
    
    if (displayCardinalGrid) // Equatorial
    {
      Color gc = chartPanel.getGridColor();
      Color agc = chartPanel.getAltGridColor();
      
      double lr = chartPanel.getGlobeViewRightLeftRotation();
      double fa = chartPanel.getGlobeViewForeAftRotation();    
      double lo = chartPanel.getGlobeViewLngOffset();
      
      double lngOffset = observerHeading; // (observerHeading + 180) % 360d;
      chartPanel.setGlobeViewLngOffset(lngOffset);
      
      // Equateur Celeste
      chartPanel.setGridColor(new Color(20, 30, 170)); // Dark Blue
      chartPanel.setAltGridColor(Color.blue); 
      double colatitude = (latitude > 0) ? (90d - latitude) : (-90d - latitude); 
      double oh = observerHeading;// + 180d;
      while (oh > 360) oh -= 360d;
      while (oh < 0) oh += 360d;
      double newLR =  colatitude * Math.sin(Math.toRadians((oh)));
      double newFA = (colatitude * Math.cos(Math.toRadians((oh))) + EYE_OFFSET);
      
//    System.out.println("LeftRight:" + newLR + ", ForeAft:" + newFA);
      
      chartPanel.setGlobeViewRightLeftRotation(newLR);
      chartPanel.setGlobeViewForeAftRotation(newFA);
      chartPanel.redrawGrid(graphics);
  
      // Reset as it was
      chartPanel.setGridColor(gc);
      chartPanel.setAltGridColor(agc);
      chartPanel.setGlobeViewRightLeftRotation(lr);
      chartPanel.setGlobeViewForeAftRotation(fa);
      chartPanel.setGlobeViewLngOffset(lo);
    }
  }

  public void chartPanelPaintComponent(Graphics graphics)
  {
    Graphics2D g2d = (Graphics2D)graphics;
    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);      
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);      
    if (true)
      chartPanelPaintComponentBefore(graphics);
  }

  public void chartPanelPaintComponentAfter(Graphics graphics)
  {
    // Display Celestial Bodies
    /*
    year   = 2008;
    month  = 12;
    day    = 18;
    hour   = 16;
    minute = 42;
    second = 0;
    */
  //  System.out.println("Date set to " + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + ", " + deltaT);
    if (calculationRequired)
    {
      Core.julianDate(year, month, day, hour, minute, second, deltaT);
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
      calculationRequired = false;
    }
    displayedBodies = new HashMap<String, DisplayedBody>();
    SightReductionUtil dr = new SightReductionUtil();
    
    dr.setL(latitude);
    dr.setG(longitude);
    
    dr.setAHG(Context.GHAsun);
    dr.setD(Context.DECsun);    
    dr.calculate();

    Color skylineColor = Color.blue;
    if (dr.getHe() > 0)
      skylineColor = Color.green;
    if (Math.abs(dr.getHe()) < 10)
      skylineColor = Color.yellow;
    if (Math.abs(dr.getHe()) < 5)
      skylineColor = Color.red;

    // Skyline
    Color startColor = Color.black; 
    Color endColor   = skylineColor; // new Color(0, 0, 160); 
    GradientPaint gradient = new GradientPaint(0, 0, startColor, this.getWidth(), this.getHeight(), endColor);
    ((Graphics2D)graphics).setPaint(gradient);
  //  graphics.setColor(skylineColor);
    
    Polygon polygon = new Polygon();
    for (double d=-180; d<=180; d+=10)
    {
      Point plot = chartPanel.getPanelPoint(0D, d);
      polygon.addPoint(plot.x, plot.y);
    }
    ((Graphics2D)graphics).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, horizonTransparency));    
    graphics.fillPolygon(polygon);
    ((Graphics2D)graphics).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));    

    // Bodies to plot
    if (displaySun)
    {
      plotBody(graphics, dr.getHe(), dr.getZ(), "Sun");
    }

    if (displayPlanets)
    {
      dr.setAHG(Context.GHAvenus);
      dr.setD(Context.DECvenus);    
      dr.calculate();
      plotBody(graphics, dr.getHe(), dr.getZ(), "Venus");
  
      dr.setAHG(Context.GHAmars);
      dr.setD(Context.DECmars);    
      dr.calculate();
      plotBody(graphics, dr.getHe(), dr.getZ(), "Mars");
  
      dr.setAHG(Context.GHAjupiter);
      dr.setD(Context.DECjupiter);    
      dr.calculate();
      plotBody(graphics, dr.getHe(), dr.getZ(), "Jupiter");
  
      dr.setAHG(Context.GHAsaturn);
      dr.setD(Context.DECsaturn);    
      dr.calculate();
      plotBody(graphics, dr.getHe(), dr.getZ(), "Saturn");
    }
    if (displayMoon)
    {
      dr.setAHG(Context.GHAmoon);
      dr.setD(Context.DECmoon);    
      dr.calculate();
      plotBody(graphics, dr.getHe(), dr.getZ(), "Moon", Color.WHITE);
    }
    if (displayStars)
    {
      for (int i=0; i<Star.getCatalog().length; i++)
      {
        String starName = Star.getCatalog()[i].getStarName();
        Core.starPos(starName);
        dr.setAHG(Context.GHAstar);
        dr.setD(Context.DECstar);    
        dr.calculate();
        plotBody(graphics, dr.getHe(), dr.getZ(), starName);
      }
    }
    // Constellations
    if (true) // With Constellations
    {
      graphics.setColor(Color.white);
      DOMParser parser = SFContext.getInstance().getParser();
      XMLDocument constell = null;
      synchronized (parser)
      {
        try
        {
          parser.parse(this.getClass().getResource("constellations.xml"));
          constell = parser.getDocument();
        }
        catch (Exception ex)
        {
          ex.printStackTrace();     
        }
      }
      try
      {
        NodeList constellations = constell.selectNodes("/constellations/constellation");
        for (int i=0; i<constellations.getLength(); i++)
        {
          XMLElement constellation = (XMLElement)constellations.item(i);
  //      System.out.println("Drawing " + constellation.getAttribute("name"));
          NodeList lines = constellation.selectNodes("./lines/line");
          for (int j=0; j<lines.getLength(); j++)
          {
            XMLElement line = (XMLElement)lines.item(j);
            String from = line.getAttribute("from");
            String to   = line.getAttribute("to");
            try
            {
              XMLElement fromStar = (XMLElement)constellation.selectNodes("./stars/star[@name='" + from + "']").item(0);
              XMLElement toStar   = (XMLElement)constellation.selectNodes("./stars/star[@name='" + to + "']").item(0);
              double fromRA = Double.parseDouble(fromStar.getElementsByTagName("ra").item(0).getFirstChild().getNodeValue());
              double fromD  = Double.parseDouble(fromStar.getElementsByTagName("d").item(0).getFirstChild().getNodeValue());
              double toRA   = Double.parseDouble(toStar.getElementsByTagName("ra").item(0).getFirstChild().getNodeValue());
              double toD    = Double.parseDouble(toStar.getElementsByTagName("d").item(0).getFirstChild().getNodeValue());

              double fromSHA = 360 - (fromRA * 360d / 24d);
              if (fromSHA > 180) fromSHA -= 360;
              dr.setAHG((Context.GHAAtrue + fromSHA) % 360d);
              dr.setD(fromD);    
              dr.calculate();
              double fromHe = dr.getHe();
              double fromZ  = dr.getZ();
              double toSHA = 360 - (toRA * 360d / 24d);
              if (toSHA > 180) toSHA -= 360;
              dr.setAHG((Context.GHAAtrue + toSHA) % 360d);
              dr.setD(toD);    
              dr.calculate();
              double toHe = dr.getHe();
              double toZ  = dr.getZ();
              drawConstellationLine(graphics, fromHe, fromZ, toHe, toZ, from, to, false);
            }
            catch (Exception ex)
            {
              System.out.println("From " + from + " to " + to);
              System.out.println(ex.toString());            
            }
          }
        }
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }
    if (displayedSatellites != null)
    {
      for (SVData svd : displayedSatellites)
      {
        plotBody(graphics, svd.getElevation(), svd.getAzimuth(), Integer.toString(svd.getSvID()), Color.blue);
      }
    }    
    // GHA, LHA Aries
    setGHAAries(Context.GHAAtrue);
    double lhaAries = Context.GHAAtrue + longitude;
    while (lhaAries > 360) lhaAries -= 360d;
    while (lhaAries < 0) lhaAries += 360d;
    setLHAAries(lhaAries);  
  }
  
  private Point mousePressedFrom = null;
  private int headingFrom = 0;

  public boolean onEvent(EventObject eventObject, int type)
  {
    boolean ret = true;
    if (type == ChartPanel.MOUSE_DRAGGED)
    {
      MouseEvent me = (MouseEvent)eventObject;
      Point mouse = me.getPoint();
      ret = false; // Override default (DDZoom)
      int hdgOffset = (int)((mousePressedFrom.x - me.getPoint().x) * (60D / (double)this.getWidth()));
      observerHeading = headingFrom + hdgOffset;
//    this.repaint();
      SFContext.getInstance().fireHeadingHasChanged((int)Math.round(observerHeading));
    }
    else if (type == ChartPanel.MOUSE_PRESSED)
    {
      MouseEvent me = (MouseEvent)eventObject;
      mousePressedFrom = me.getPoint();  
      headingFrom = (int)Math.round(observerHeading);      
    }
    else if (type == ChartPanel.MOUSE_MOVED)    
    {
      MouseEvent me = (MouseEvent)eventObject;
      Point mouse = me.getPoint();
//    System.out.println("Mouse:" + mouse.getX() + "/" + mouse.getY());
      // Display bubble for displayed stars
      if (displayedBodies != null)
      {
        Set<String> keys = displayedBodies.keySet();
        chartPanel.setPositionToolTipEnabled(false);
        tooltipMess = null;
        replaceTooltipMess = false;
        for (String k : keys)
        {
          if ((Math.abs(displayedBodies.get(k).getP().x - mouse.x) <= 3) &&
              (Math.abs(displayedBodies.get(k).getP().y - mouse.y) <= 3))
          {
  //        System.out.println("Found [" + displayedBodies.get(k).getName() + "]");
            chartPanel.setPositionToolTipEnabled(true);
            tooltipMess = "<html><table border='0'><tr><td colspan='2'><b>" + displayedBodies.get(k).getName() + "</b></td></tr>" +
                          "<tr><td>Z</td><td>" + Integer.toString((int)Math.round(displayedBodies.get(k).getZ())) + "</td></tr>" +
                          "<tr><td>Alt</td><td>" + GeomUtil.decToSex(displayedBodies.get(k).getAlt(), GeomUtil.SWING, GeomUtil.NONE) + "</td></tr></table>" +
                          "</html>";
            replaceTooltipMess = true;
            break;
          }
        }
      }
      // TODO Same for the GPS Satellites
    }
    return ret;
  }

  public void afterEvent(EventObject e, int type)
  { }
  
  public String getMessForTooltip()
  {
    return tooltipMess;
  }

  public boolean replaceMessForTooltip()
  {
    return replaceTooltipMess;
  }

  public void videoCompleted()
  {
  }

  public void videoFrameCompleted(Graphics graphics, Point point)
  {
  }

  public void zoomFactorHasChanged(double d)
  {
  }

  public void chartDDZ(double d, double d2, double d3, double d4)
  {
  }

  private void setGHAAries(double d)
  {
    ariesGHALabel.setText("GHA Aries : " + GeomUtil.decToSex(d, GeomUtil.SWING, GeomUtil.NONE));
  }
  
  private void setLHAAries(double d)
  {
    ariesLHALabel.setText("LHA Aries : " + GeomUtil.decToSex(d, GeomUtil.SWING, GeomUtil.NONE));
  }
    
  private void plotBody(Graphics graphics, double altitude, double azimuth, String label)
  {
    plotBody(graphics, altitude, azimuth, label, Color.yellow);
  }
  
  private void plotBody(Graphics graphics, double altitude, double azimuth, String label, Color c)
  {
    if (altitude < 0 && !displayNegativeAltitudes)
      return;
    double lngOffset = (observerHeading - azimuth);
    
    if (!displayStarsBehind && Math.cos(Math.toRadians(lngOffset)) < 0)
      return;
    
//  System.out.println(label + ": Alt=" + altitude + ", Z=" + azimuth);
    // Plot stuff here
    // Z & Ea  
    if (displayHZ)
    {
      graphics.setColor(Color.red);
      float alpha = 0.5F;
      ((Graphics2D)graphics).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));    
      int centerX = chartPanel.getW() / 2;
      int centerY = chartPanel.getH() / 2;
      Point Z = chartPanel.getPanelPoint(0D, opposite(azimuth));
      graphics.drawLine(centerX, centerY, Z.x, Z.y);
      double from, to;
      from = Math.min(0, altitude);
      to   = Math.max(0, altitude);
      Point previous = chartPanel.getPanelPoint(from, opposite(azimuth));
      for (double d=from; d<to; d+=5D)
      {
        Point p = chartPanel.getPanelPoint(d, opposite(azimuth));
        graphics.drawLine(previous.x, previous.y, p.x, p.y);
        previous = p;
      }
      Point p = chartPanel.getPanelPoint(to, opposite(azimuth));
      graphics.drawLine(previous.x, previous.y, p.x, p.y);
      alpha = 1F;
      ((Graphics2D)graphics).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));    
    }
    // Plot the body
    graphics.setColor(c); // The color as parameter
    Point plot = chartPanel.getPanelPoint(altitude, opposite(azimuth));
    if ("Sun".equals(label))
      graphics.fillOval(plot.x - 8, plot.y - 8, 16, 16);
    else if ("Moon".equals(label))
      graphics.fillOval(plot.x - 6, plot.y - 6, 12, 12);
    else if ("Venus".equals(label) ||
             "Mars".equals(label) ||
             "Jupiter".equals(label) ||
             "Saturn".equals(label))
      graphics.fillOval(plot.x - 4, plot.y - 4, 8, 8);
    else
      graphics.fillOval(plot.x - 2, plot.y - 2, 4, 4);
    
    if (label != null && label.trim().length() > 0)
      chartPanel.postit(graphics, label, plot.x, plot.y, Color.yellow, Color.blue, 0.5f);
    
    displayedBodies.put(label, new DisplayedBody(plot, label, azimuth, altitude));
  }

  private void drawConstellationLine(Graphics graphics, 
                                     double altitudeFrom, double azimuthFrom, 
                                     double altitudeTo,   double azimuthTo,
                                     String nameFrom,     String nameTo, 
                                     boolean plotStar)
  {
    if (plotStar)
    {
      Point pointF = chartPanel.getPanelPoint(altitudeFrom, opposite(azimuthFrom));
      graphics.fillOval(pointF.x - 3, pointF.y - 3, 6, 6);
      if (nameFrom != null && nameFrom.trim().length() > 0)
        chartPanel.postit(graphics, nameFrom, pointF.x, pointF.y, Color.yellow, Color.blue, 0.5f);
    }
    // divide in 10
    double startFromAlt = Math.min(altitudeFrom, altitudeTo);
    double stepAlt = Math.abs(altitudeFrom - altitudeTo) / 10d;
    double stepZ = 0d;
    double startFromZ = 0d;
    if (Math.abs(azimuthFrom - azimuthTo) > 180d)
    {
      if (azimuthFrom > 180)
        azimuthFrom = azimuthFrom - 360d; // 360d - azimuthFrom;
      else
        azimuthTo = azimuthTo - 360d; // 360d - azimuthTo;
    }
    if (startFromAlt == altitudeFrom)
    {
      startFromAlt = altitudeFrom;
      startFromZ = azimuthFrom;
      stepZ = (azimuthTo - azimuthFrom) / 10d;
    }
    else
    {
      startFromAlt = altitudeTo;
      startFromZ = azimuthTo;
      stepZ = (azimuthFrom - azimuthTo) / 10d;
    }
    Point previous = null;
//  System.out.println("-----------------------------");
//  System.out.println("From " + nameFrom + " to " + nameTo);
    for (int i=0; i<=10; i++)
    {
      double altitude = startFromAlt + ((double)i * stepAlt);
      double azimuth = startFromZ + ((double)i * stepZ);
      
//    System.out.println("i=" + Integer.toString(i) + ", Alt:" + altitude + ", Z:" + azimuth);
      if (altitude < 0 && !displayNegativeAltitudes)
      {
        previous = null;
        continue;
      }
      double lngOffset = (observerHeading - azimuth);
      
      if (!displayStarsBehind && Math.cos(Math.toRadians(lngOffset)) < 0)
      {
        previous = null;
        continue;
      }
      
      float alpha = 0.5F;
      ((Graphics2D)graphics).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));    
      Point point = chartPanel.getPanelPoint(altitude, opposite(azimuth));
      if (previous != null)
        graphics.drawLine(previous.x, previous.y, point.x, point.y);
      previous = point;
      alpha = 1F;
      ((Graphics2D)graphics).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));    
    }    
  }

  private double opposite(double d)
  {
    return (d + 180) % 360d;      
  }
  
  private void left_actionPerformed(ActionEvent e)
  {
    observerHeading -= 1d;
    updateHeading();
  }

  private void right_actionPerformed(ActionEvent e)
  {
    observerHeading += 1d;
    updateHeading();
  }
  
  private void updateHeading()
  {
    /*
    double wLong = observerHeading - HALF_SPAN;
    double eLong = observerHeading + HALF_SPAN; // chartPanel.calculateEastG(nLat, sLat, wLong);
    chartPanel.setEastG(eLong);
    chartPanel.setWestG(wLong);
    */
    double lngOffset = opposite(observerHeading);
//  System.out.println("Lng Offset:" + lngOffset);
    chartPanel.setGlobeViewLngOffset(lngOffset);
    headingPanel.setHdg((int)observerHeading);
    chartPanel.repaint();    
    headingPanel.repaint();
  }

  private void displayNegativeAltCheckBox_actionPerformed(ActionEvent e)
  {
    displayNegativeAltitudes = displayNegativeAltCheckBox.isSelected();
    chartPanel.repaint();
  }

  private void displayStarsBehindCheckBox_actionPerformed(ActionEvent e)
  {
    displayStarsBehind = displayStarsBehindCheckBox.isSelected();
    chartPanel.setTransparentGlobe(displayStarsBehind);
//  chartPanel.setAntiTransparentGlobe(displayStarsBehind); // was not !
    chartPanel.repaint();
  }

  private void displaySunCheckBox_actionPerformed(ActionEvent e)
  {
    displaySun = displaySunCheckBox.isSelected();
    chartPanel.repaint();
  }

  private void displayMoonCheckBox_actionPerformed(ActionEvent e)
  {
    displayMoon = displayMoonCheckBox.isSelected();
    chartPanel.repaint();
  }

  private void displayPlanetsCheckBox_actionPerformed(ActionEvent e)
  {
    displayPlanets = displayPlanetsCheckBox.isSelected();
    chartPanel.repaint();
  }

  private void displayStarsCheckBox_actionPerformed(ActionEvent e)
  {
    displayStars = displayStarsCheckBox.isSelected();
    chartPanel.repaint();
  }

  private void timeButton_actionPerformed(ActionEvent e)
  {
    readTimeValues();
    chartPanel.repaint();
  }
  
  private void readTimeValues()
  {
    try
    {
      year = Integer.parseInt(yearTextField.getText());
      month = monthComboBox.getSelectedIndex() + 1;
      day = Integer.parseInt(dayTextField.getText());
      hour = Integer.parseInt(hourTextField.getText());
      minute = Integer.parseInt(minuteTextField.getText());
      second = Float.parseFloat(secondTextField.getText());
      deltaT = Float.parseFloat(deltaTTextField.getText());      
      calculationRequired = true;
    }
    catch (NumberFormatException nfe)
    {
      nfe.printStackTrace();
    }
  }
  
  private void setCurrentTime()
  {
    String y = "", m = "", d = "", 
           h = "", mn = "", s = "";

    Date now = new Date();
    SimpleDateFormat dateFmt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
    String strNow = dateFmt.format(now);
//  System.out.println("Current Time:" + strNow);

    Date gmt = TimeUtil.getGMT();

    y =  (new SimpleDateFormat("yyyy")).format(gmt);
    m =  (new SimpleDateFormat("MM")).format(gmt);
    d =  (new SimpleDateFormat("d")).format(gmt);
    h =  (new SimpleDateFormat("HH")).format(gmt);
    mn = (new SimpleDateFormat("mm")).format(gmt);
    s =  (new SimpleDateFormat("ss")).format(gmt);
    
    yearTextField.setText(y);
    int mo = Integer.parseInt(m);
    monthComboBox.setSelectedIndex(mo - 1);
    dayTextField.setText(d);
    
    hourTextField.setText(h);
    minuteTextField.setText(mn);
    secondTextField.setText(s);
    
    deltaTTextField.setText(Float.toString(deltaT));
    
    readTimeValues();
  }

  private void nowButton_actionPerformed(ActionEvent e)
  {
    setCurrentTime();
    chartPanel.repaint();
  }

  private void realTimeCheckBox_actionPerformed(ActionEvent e)
  {
    if (realTimeCheckBox.isSelected())
    {
      timeButton.setEnabled(false);
      nowButton.setEnabled(false);
      Thread thread = new Thread()
        {
          public void run()
          {
            while (realTimeCheckBox.isSelected())
            {
              setCurrentTime();
              chartPanel.repaint();
              try { Thread.sleep(1000L); } catch (Exception ex) {}
            }
          }
        };
      thread.start();
    }
    else
    {
      timeButton.setEnabled(true);
      nowButton.setEnabled(true);
    }
  }

  public StarFinderEventListener getStarFinderEventListener()
  {
    return sfel;
  }

  public PositionPanel getPositionPanel()
  {
    return positionPanel;
  }

  private void hzCheckBox_actionPerformed(ActionEvent e)
  {
    displayHZ = hzCheckBox.isSelected();
    chartPanel.repaint();
  }

  private void zenithalGridCheckBox_actionPerformed(ActionEvent e)
  {
    displayZenithalGrid = zenithalGridCheckBox.isSelected();
    chartPanel.repaint();
  }

  private void cardinalGridCheckBox_actionPerformed(ActionEvent e)
  {
    displayCardinalGrid = cardinalGridCheckBox.isSelected();
    chartPanel.repaint();
  }

  private void horizonTransparencySlider_stateChanged(ChangeEvent e)
  {
    
    chartPanel.repaint();
  }

  class DisplayedBody
  {
    private Point p;
    private double z;
    private double alt;
    private String name;
    
    public DisplayedBody(Point p, String name, double z, double alt)
    {
      this.p = p;
      this.name = name;
      this.z = z;
      this.alt = alt;
    }

    public Point getP()
    {
      return p;
    }

    public void setZ(double z)
    {
      this.z = z;
    }

    public double getZ()
    {
      return z;
    }

    public void setAlt(double alt)
    {
      this.alt = alt;
    }

    public double getAlt()
    {
      return alt;
    }

    public void setName(String name)
    {
      this.name = name;
    }

    public String getName()
    {
      return name;
    }
  }
}

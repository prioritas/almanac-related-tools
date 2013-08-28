package app.sightreduction;


import app.almanac.AlmanacComputer;

import app.util.DateTimePanel;
import app.util.PositionPanel;

import astro.calc.GeoPoint;

import calculation.SightReductionUtil;
import calculation.TimeUtil;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import nauticalalmanac.Context;
import nauticalalmanac.Core;

import nmea.event.NMEAReaderListener;

import nmea.server.ctx.NMEAContext;
import nmea.server.ctx.NMEADataCache;

import ocss.nmea.parser.GeoPos;
import ocss.nmea.parser.UTCDate;

import user.util.GeomUtil;


//@MakeFrameAndInternalFrame(RadicalToUse)
public class SightReductionPanel
  extends JPanel
{
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private DateTimePanel datePanel = new DateTimePanel();
  private PositionPanel positionPanel = new PositionPanel(false, new Font("Tahoma", Font.PLAIN, 11));
  private DataPanel dataPanel = new DataPanel();
  private JButton srButton = new JButton();
  private JPanel outputPanel = new JPanel();
  private JScrollPane jScrollPane = new JScrollPane();
  private JEditorPane outputEditorPane = new JEditorPane();
  private BorderLayout borderLayout = new BorderLayout();
  private JSeparator jSeparator1 = new JSeparator();
  private JSeparator jSeparator2 = new JSeparator();

  private static DecimalFormat df22 = new DecimalFormat("#0.00"); // ("##0'°'00'\''");
  
  private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss 'UT'");
  private JPanel buttonPanel = new JPanel();
  private JButton reverseButton = new JButton();
  private JPanel correctionPanel = new JPanel();
  private JLabel hdLabel = new JLabel();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private JLabel hdValue = new JLabel();
  private JLabel sdLabel = new JLabel();
  private JLabel appAltLabel = new JLabel();
  private JLabel parallaxLabel = new JLabel();
  private JLabel refrLabel = new JLabel();
  private JLabel obsAltLabel = new JLabel();
  private JLabel sdValue = new JLabel();
  private JLabel appAltValue = new JLabel();
  private JLabel parallaxValue = new JLabel();
  private JLabel refrValue = new JLabel();
  private JLabel obsAltValue = new JLabel();
  private JPanel almanacPanel = new JPanel();
  private JLabel ghaLabel = new JLabel();
  private GridBagLayout gridBagLayout3 = new GridBagLayout();
  private JLabel ghaValue = new JLabel();
  private JLabel decLabel = new JLabel();
  private JLabel semDLabel = new JLabel();
  private JLabel hpLabel = new JLabel();
  private JLabel decValue = new JLabel();
  private JLabel semiDValue = new JLabel();
  private JLabel hpValue = new JLabel();
  private JLabel appCorrLabel = new JLabel();
  private JLabel appCorrValue = new JLabel();
  private JLabel totalCorrectionLabel = new JLabel();
  private JLabel totalCorrectionValue = new JLabel();
  private JLabel moonDistLabel = new JLabel();
  private JLabel lunarValue = new JLabel();
  private JPanel deadReckoningPanel = new JPanel();
  private JLabel observedAltitudeLabel = new JLabel();
  private GridBagLayout gridBagLayout4 = new GridBagLayout();
  private JLabel obsAltValueLabel = new JLabel();
  private JLabel zLabel = new JLabel();
  private JLabel estAltLabel = new JLabel();
  private JLabel intLabel = new JLabel();
  private JLabel zValueLabel = new JLabel();
  private JLabel estAltValueLabel = new JLabel();
  private JLabel intValueLabel = new JLabel();

  private transient NMEAReaderListener nmeaListener = null;
  private JPanel gpsDataPanel = new JPanel();
  private JButton getGPSDataButton = new JButton();
  private BorderLayout borderLayout1 = new BorderLayout();

  public SightReductionPanel()
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
    this.setLayout(gridBagLayout1);
    this.setSize(new Dimension(500, 639));
    this.setPreferredSize(new Dimension(500, 300));
    srButton.setText("Sight Reduction");
    srButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          srButton_actionPerformed(e);
        }
      });
    outputPanel.setLayout(borderLayout);
    outputPanel.setPreferredSize(new Dimension(451, 140));
    outputPanel.setMinimumSize(new Dimension(450, 140));
    reverseButton.setText("Reverse Sight");
    reverseButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          reverseButton_actionPerformed(e);
        }
      });
    correctionPanel.setLayout(gridBagLayout2);
    correctionPanel.setBorder(BorderFactory.createTitledBorder("Corrections"));
    hdLabel.setText("Horizon Dip:");
    hdValue.setText("0.0'");
    sdLabel.setText("Semi-diameter:");
    appAltLabel.setText("Apparent Altitude:");
    parallaxLabel.setText("Parallaxe:");
    refrLabel.setText("Refraction:");
    obsAltLabel.setText("Observed Altitude:");
    sdValue.setText("0.0'");
    appAltValue.setText("0 0.0'");
    appAltValue.setFont(new Font("Tahoma", 1, 11));
    parallaxValue.setText("0.0'");
    refrValue.setText("0.0'");
    obsAltValue.setText("0 0.0'");
    obsAltValue.setFont(new Font("Tahoma", 1, 11));
    almanacPanel.setBorder(BorderFactory.createTitledBorder("Almanac"));
    almanacPanel.setLayout(gridBagLayout3);
    ghaLabel.setText("GHA:");
    ghaValue.setText("000 00.00'");
    decLabel.setText("Dec:");
    semDLabel.setText("Semi-diameter:");
    hpLabel.setText("Horizontal Parallax:");
    decValue.setText("00 00.00 N");
    semiDValue.setText("0.0'");
    hpValue.setText("0.0'");
    appCorrLabel.setText("App. Corr:");
    appCorrValue.setText("0.0'");
    totalCorrectionLabel.setText("Total Corr:");
    totalCorrectionValue.setText("0.0'");
    moonDistLabel.setText("Lunar Dist:");
    lunarValue.setText("00 00.00'");
    deadReckoningPanel.setBorder(BorderFactory.createTitledBorder("Dead Reckoning"));
    deadReckoningPanel.setLayout(gridBagLayout4);
    observedAltitudeLabel.setText("Observed Altitude:");
    obsAltValueLabel.setText("-");
    zLabel.setText("Z:");
    estAltLabel.setText("Estimated Altitude:");
    intLabel.setText("Intercept:");
    zValueLabel.setText("-");
    estAltValueLabel.setText("-");
    intValueLabel.setText("-");
    gpsDataPanel.setLayout(borderLayout1);
    getGPSDataButton.setText("Get Data from GPS");
    getGPSDataButton.setToolTipText("If a GPS is connected, read its data (Position and UTC)");
    getGPSDataButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          getGPSDataButton_actionPerformed(e);
        }
      });
    this.add(datePanel, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
          new Insets(0, 0, 0, 0), 0, 0));
    this.add(positionPanel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
          new Insets(0, 0, 0, 0), 0, 0));
    this.add(dataPanel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
          new Insets(0, 0, 0, 0), 0, 0));

//  this.add(srButton, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(buttonPanel, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
          new Insets(0, 0, 0, 0), 0, 0));
    buttonPanel.add(srButton, null);

    buttonPanel.add(reverseButton, null);
    jScrollPane.getViewport().add(outputEditorPane, null);
    outputPanel.add(jScrollPane, BorderLayout.CENTER);
    this.add(outputPanel, new GridBagConstraints(0, 9, 2, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
          new Insets(3, 0, 0, 0), 0, 71));
    this.add(jSeparator1, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
          new Insets(3, 0, 3, 0), 0, 0));
    this.add(jSeparator2, new GridBagConstraints(0, 5, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
          new Insets(3, 0, 3, 0), 0, 0));

    correctionPanel.add(hdLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    correctionPanel.add(hdValue, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    correctionPanel.add(sdLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    correctionPanel.add(appAltLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    correctionPanel.add(parallaxLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    correctionPanel.add(refrLabel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    correctionPanel.add(obsAltLabel, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    correctionPanel.add(sdValue, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    correctionPanel.add(appAltValue, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    correctionPanel.add(parallaxValue, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    correctionPanel.add(refrValue, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    correctionPanel.add(obsAltValue, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    correctionPanel.add(appCorrLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    correctionPanel.add(appCorrValue, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    correctionPanel.add(totalCorrectionLabel, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    correctionPanel.add(totalCorrectionValue, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    this.add(correctionPanel, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
          new Insets(0, 0, 0, 0), 0, 0));
    almanacPanel.add(ghaLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    almanacPanel.add(ghaValue, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    almanacPanel.add(decLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    almanacPanel.add(semDLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    almanacPanel.add(hpLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    almanacPanel.add(decValue, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    almanacPanel.add(semiDValue, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    almanacPanel.add(hpValue, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    almanacPanel.add(moonDistLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    almanacPanel.add(lunarValue, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    this.add(almanacPanel, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
          new Insets(0, 0, 0, 0), 0, 0));
    deadReckoningPanel.add(observedAltitudeLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    deadReckoningPanel.add(obsAltValueLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    deadReckoningPanel.add(zLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    deadReckoningPanel.add(estAltLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    deadReckoningPanel.add(intLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    deadReckoningPanel.add(zValueLabel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    deadReckoningPanel.add(estAltValueLabel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    deadReckoningPanel.add(intValueLabel, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    this.add(deadReckoningPanel,
             new GridBagConstraints(0, 8, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
          new Insets(0, 0, 0, 0), 0, 0));
    gpsDataPanel.add(getGPSDataButton, BorderLayout.EAST);
    this.add(gpsDataPanel,
             new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                                    GridBagConstraints.HORIZONTAL,
                                    new Insets(0, 0, 0, 0), 0, 0));
    datePanel.setDate(TimeUtil.getGMT());
    datePanel.setBorder(BorderFactory.createTitledBorder("Date & Time"));
    dataPanel.setBorder(BorderFactory.createTitledBorder("Observation"));
    this.validate();
  }

  private final static boolean verbose = true;
  
  private void srButton_actionPerformed(ActionEvent e)
  {
    sr();
  }
  
  private void sr()
  {
    sr(false);
  }
  
  private void sr(boolean reverse)
  {
    obsAltValueLabel.setText("-");
    zValueLabel.setText("-");
    estAltValueLabel.setText("-");
    intValueLabel.setText("-");
    
    if (reverse)
      printout("-- Reverse Sight --");
    else
      printout("-- Sight Reduction --");
    //  printout("Sextant:" + dataPanel.getSextantAltitude());
    Date date = datePanel.getDate();
    double deltaT = datePanel.getDeltaT();
    GeoPoint gp = positionPanel.getPosition();

    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    
    AlmanacComputer.calculate(cal.get(Calendar.YEAR), 
                              cal.get(Calendar.MONTH) + 1, 
                              cal.get(Calendar.DAY_OF_MONTH), 
                              cal.get(Calendar.HOUR_OF_DAY), 
                              cal.get(Calendar.MINUTE), 
                              cal.get(Calendar.SECOND), 
                              deltaT);
    SightReductionUtil sru = new SightReductionUtil();
    
    sru.setL(gp.getL());
    sru.setG(gp.getG());
    
    double gha = 0d;
    double dec = 0d;
    double hp  = 0d;
    double sd  = 0d;
    double lunar = -1d;
    int limb         = dataPanel.getLimb();
    double eyeHeight = dataPanel.getEyeHeightInMeters();
    
    String body = dataPanel.getBody();
    if ("The Sun".equals(body))
    {
      gha = Context.GHAsun;
      dec = Context.DECsun;
      hp  = Context.HPsun / 3600d;
      sd  = Context.SDsun / 3600d;
      lunar = Context.LDist;
    }
    else if ("Moon".equals(body))
    {
      gha = Context.GHAmoon;
      dec = Context.DECmoon;
      hp  = Context.HPmoon / 3600d;
      sd  = Context.SDmoon / 3600d;
    }
    else if ("Venus".equals(body))
    {
      gha = Context.GHAvenus;
      dec = Context.DECvenus;
      hp  = Context.HPvenus / 3600d;
      sd  = Context.SDvenus / 3600d;
      lunar = Context.moonVenusDist;
    }
    else if ("Mars".equals(body))
    {
      gha = Context.GHAmars;
      dec = Context.DECmars;
      hp  = Context.HPmars / 3600d;
      sd  = Context.SDmars / 3600d;
      lunar = Context.moonMarsDist;
    }
    else if ("Jupiter".equals(body))
    {
      gha = Context.GHAjupiter;
      dec = Context.DECjupiter;
      hp  = Context.HPjupiter / 3600d;
      sd  = Context.SDjupiter / 3600d;
      lunar = Context.moonJupiterDist;
    }
    else if ("Saturn".equals(body))
    {
      gha = Context.GHAsaturn;
      dec = Context.DECsaturn;
      hp  = Context.HPsaturn / 3600d;
      sd  = Context.SDsaturn / 3600d;
      lunar = Context.moonSaturnDist;
    }
    else // Star
    {
      Core.starPos(body);
      gha = Context.GHAstar;
      dec = Context.DECstar;
      hp = 0d;
      sd = 0d;
      lunar = Context.starMoonDist;
    }
    
    sru.setAHG(gha);
    sru.setD(dec);    
    sru.calculate();     
    
    printout("From " + gp.toString());
    printout("On " + sdf.format(date));
    printout("For " + body);
    printout("GHA:" + GeomUtil.decToSex(gha, GeomUtil.SWING, GeomUtil.NONE));
    printout("Dec:" + GeomUtil.decToSex(dec, GeomUtil.SWING, GeomUtil.NS));
    
    printout(body + " Alt:" + GeomUtil.decToSex(sru.getHe(), GeomUtil.SWING, GeomUtil.NONE) + " (" + sru.getHe() + ")");
    printout(body + " Z  :" + GeomUtil.decToSex(sru.getZ(), GeomUtil.SWING, GeomUtil.NONE));
    printout("-----------------------------");
    double hi = -1;
    double hDip = 0;
    double refr = 0;
    double parallax = 0;
    double obsAlt = 0;
    double totalCorrection = 0d;
    if (!reverse)
    {
      hi = dataPanel.getSextantAltitude();
      printout("Sextant (instr.) altitude:" + GeomUtil.decToSex(hi, GeomUtil.SWING, GeomUtil.NONE));    
      obsAlt = SightReductionUtil.observedAltitude(hi, 
                                                   eyeHeight,
                                                   hp,    // Returned in seconds, sent in degrees
                                                   sd,    // Returned in seconds, sent in degrees
                                                   limb,
                                                   verbose);
  
      hDip = sru.getHorizonDip();
      refr = sru.getRefr();
      parallax = sru.getPa();
      
      totalCorrection = 0d;
      printout("For eye height " + df22.format(eyeHeight) + " m, horizon dip = " + df22.format(hDip) + "'");
      totalCorrection -= (hDip / 60D);
      printout("  - Total Corr. :" + df22.format(totalCorrection * 60d) + "'");
      printout("Refraction " + df22.format(refr) + "'");
      totalCorrection -= (refr / 60D);
      printout("  - Total Corr. :" + df22.format(totalCorrection * 60d) + "'");
      printout("For hp " + df22.format(hp * 60d) + "', parallax " + df22.format(parallax * 60d) + "'");
      totalCorrection += (parallax);
      printout("  - Total Corr. :" + df22.format(totalCorrection * 60d) + "'");
      printout("Semi-diameter: " + df22.format(sd * 60d) + "'");
      if (limb == SightReductionUtil.UPPER_LIMB)
        sd = -sd;
      else if (limb == SightReductionUtil.NO_LIMB)
        sd = 0;
      totalCorrection += sd;
      printout("  - Total Corr. :" + df22.format(totalCorrection * 60d) + "'");
      printout("-----------------------------");
      printout("For Hi :" + GeomUtil.decToSex(hi, GeomUtil.SWING, GeomUtil.NONE));
      printout("Obs Alt:" + GeomUtil.decToSex(obsAlt, GeomUtil.SWING, GeomUtil.NONE) + " (" + obsAlt + ")");
//    printout("-----------------------------");
      printout("-- Recap for " + body + " --");
      printout("Obs Alt:" + GeomUtil.decToSex(obsAlt, GeomUtil.SWING, GeomUtil.NONE) + " (" + obsAlt + ")");
      obsAltValueLabel.setText(GeomUtil.decToSex(obsAlt, GeomUtil.SWING, GeomUtil.NONE) + " (" + obsAlt + ")");
      printout(body + " estimated alt:" + GeomUtil.decToSex(sru.getHe(), GeomUtil.SWING, GeomUtil.NONE) + " (" + sru.getHe() + ")");
      estAltValueLabel.setText(GeomUtil.decToSex(sru.getHe(), GeomUtil.SWING, GeomUtil.NONE) + " (" + sru.getHe() + ")");
      printout(body + " Z  :" + GeomUtil.decToSex(sru.getZ(), GeomUtil.SWING, GeomUtil.NONE));
      zValueLabel.setText(GeomUtil.decToSex(sru.getZ(), GeomUtil.SWING, GeomUtil.NONE));
      double intercept = obsAlt - sru.getHe();
      printout("Intercept:" + df22.format(Math.abs(intercept) * 60d) + "' " + (intercept<0?"away from":"towards") + " " + body );
      intValueLabel.setText(df22.format(Math.abs(intercept) * 60d) + "' " + (intercept<0?"away from":"towards") + " " + body );
    }
    else
    {
      obsAlt = sru.getHe();
      hDip = SightReductionUtil.getHorizonDip(eyeHeight);
      // sd, we have already
      parallax = SightReductionUtil.getParallax(hp, obsAlt);
      refr = SightReductionUtil.getRefr(obsAlt - parallax);
      hi = obsAlt;
      if (limb == SightReductionUtil.UPPER_LIMB)
        sd = -sd;
      else if (limb == SightReductionUtil.NO_LIMB)
        sd = 0;
      totalCorrection = 0d;
      printout("For eye height " + df22.format(eyeHeight) + " m, horizon dip = " + df22.format(hDip) + "'");
      totalCorrection -= (hDip / 60D);
      printout("  - Total Corr. :" + df22.format(totalCorrection * 60d) + "'");
      printout("Refraction " + df22.format(refr) + "'");
      totalCorrection -= (refr / 60D);
      printout("  - Total Corr. :" + df22.format(totalCorrection * 60d) + "'");
      printout("For hp " + df22.format(hp * 60d) + "', parallax " + df22.format(parallax * 60d) + "'");
      totalCorrection += (parallax);
      printout("  - Total Corr. :" + df22.format(totalCorrection * 60d) + "'");
      printout("Semi-diameter: " + df22.format(sd * 60d) + "'");
      totalCorrection += sd;
      printout("  - Total Corr. :" + df22.format(totalCorrection * 60d) + "'");
      hi -= sd;
      hi += (hDip / 60d);
      hi -= parallax;
      hi += (refr / 60d);
      printout("Hi " + GeomUtil.decToSex(hi, GeomUtil.SWING, GeomUtil.NONE) + " (" + hi + ")");
      dataPanel.setSextantAltitude(hi);
    }
    hdValue.setText(df22.format(- hDip) + "'");
    sdValue.setText(df22.format(sd * 60d) + "'");
    
    parallaxValue.setText(df22.format(parallax * 60d) + "'");
    refrValue.setText(df22.format(- refr) + "'");
    
    obsAltValue.setText(GeomUtil.decToSex(obsAlt, GeomUtil.SWING, GeomUtil.NONE));
    appAltValue.setText(GeomUtil.decToSex(hi + sd - (hDip / 60d), GeomUtil.SWING, GeomUtil.NONE));
    
    ghaValue.setText(GeomUtil.decToSex(gha, GeomUtil.SWING, GeomUtil.NONE));
    decValue.setText(GeomUtil.decToSex(dec, GeomUtil.SWING, GeomUtil.NS));
    semiDValue.setText(df22.format(Math.abs(sd) * 60d) + "'");
    hpValue.setText(df22.format(hp * 60d) + "'");
    
    totalCorrectionValue.setText(df22.format(totalCorrection * 60d) + "'");
    appCorrValue.setText(df22.format((sd - (hDip / 60d)) * 60d) + "'");
    if (lunar != -1)
      lunarValue.setText(GeomUtil.decToSex(lunar, GeomUtil.SWING, GeomUtil.NONE));
    else
      lunarValue.setText("-");
  }
  
  private void printout(String s)
  {
    String orig = outputEditorPane.getText();
    if (orig.trim().length() > 0) orig += "\n";
    outputEditorPane.setText(orig + s);
    outputEditorPane.setCaretPosition(outputEditorPane.getText().length());
  }

  private void reverseButton_actionPerformed(ActionEvent e)
  {
    sr(true);
  }

  private boolean positionHasBeenRead = false;
  private boolean timeHasBeenRead = false;
  
  private void getGPSDataButton_actionPerformed(ActionEvent e)
  {
    positionHasBeenRead = false;
    timeHasBeenRead = false;
    
    if (nmeaListener == null)
    {
      nmeaListener = new NMEAReaderListener()
        {
          @Override
          public void manageNMEAString(String string)
          {
            if (!positionHasBeenRead)
            {
              GeoPos gps = (GeoPos)NMEAContext.getInstance().getCache().get(NMEADataCache.POSITION, true);
              if (gps != null)
              {
        //      System.out.println("Your fix:" + gps.toString());
                positionPanel.setPosition(gps.lat, gps.lng);
                positionHasBeenRead = true;
              }
              else
                System.out.println("No position yet...");
            }
            if (!timeHasBeenRead)
            {
              UTCDate utcDate = (UTCDate)NMEAContext.getInstance().getCache().get(NMEADataCache.GPS_DATE_TIME);      
              if (utcDate != null && utcDate.getValue() != null)
              {
                // Display it
                Date d = utcDate.getValue();
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                cal.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));                
                
                datePanel.setDate(cal);
                timeHasBeenRead = true;
              }
              else
                System.out.println("No date yet...");
            }
          }
        };
      NMEAContext.getInstance().addNMEAReaderListener(nmeaListener);
    }
//  else
//    NMEAContext.getInstance().removeNMEAReaderListener(nmeaListener);
  }
}


package app.clear;


import app.util.DateTimePanel;

import app.util.EyeHeightPanel;

import calculation.SightReductionUtil;
import calculation.TimeUtil;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import app.almanac.AlmanacComputer;

import nauticalalmanac.Context;
import nauticalalmanac.Core;

import user.util.GeomUtil;

public class ClearLunarPanel
  extends JPanel
{
  private boolean verbose = true;
  
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private DateTimePanel datePanel = new DateTimePanel();
  private MoonPanel moonPanel = new MoonPanel();
  private OtherBodyPanel otherBodyPanel = new OtherBodyPanel();
  private DistancePanel distancePanel = new DistancePanel();
  private JSeparator jSeparator1 = new JSeparator();
  private JButton calculateButton = new JButton();
  private EyeHeightPanel eyeHeightPanel = new EyeHeightPanel();
  private JSeparator jSeparator2 = new JSeparator();
  private JSeparator jSeparator3 = new JSeparator();
  private JSeparator jSeparator4 = new JSeparator();
  private OutputPanel outputPanel = new OutputPanel();

  public ClearLunarPanel()
  {
    try
    {
      jbInit();
      datePanel.setDeltaT(65.984);
      Date d = TimeUtil.getGMT();
      datePanel.setDate(d);
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
    this.setSize(new Dimension(498, 535));
    calculateButton.setText("Calculate");
    calculateButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          calculateButton_actionPerformed(e);
        }
      });
    jSeparator3.setOrientation(SwingConstants.VERTICAL);
    outputPanel.setPreferredSize(new Dimension(451, 100));
    this.add(datePanel, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(moonPanel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 0), 0, 0));
    this.add(otherBodyPanel, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 0), 0, 0));
    this.add(distancePanel, new GridBagConstraints(0, 4, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 0), 0, 0));
    this.add(jSeparator1, new GridBagConstraints(0, 5, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
    this.add(calculateButton, new GridBagConstraints(0, 6, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(eyeHeightPanel, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
    this.add(jSeparator2, new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jSeparator3, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 50, 0, 0), 0, 0));
    this.add(jSeparator4, new GridBagConstraints(0, 7, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 0), 0, 0));
    this.add(outputPanel, new GridBagConstraints(0, 8, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 0, 0), 0, 118));
  }

  private void calculateButton_actionPerformed(ActionEvent e)
  {
    double deltaT = datePanel.getDeltaT();
    Calendar cal = Calendar.getInstance();
    cal.setTime(datePanel.getDate());
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH) + 1;
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int hour = cal.get(Calendar.HOUR_OF_DAY);
    int minute = cal.get(Calendar.MINUTE);
    float second = cal.get(Calendar.SECOND);
    
    double moonAltitude = moonPanel.getAltitude();
    double bodyAltitude = otherBodyPanel.getAltitude();
    String otherBodyName = otherBodyPanel.getBodyName();
    int moonLimb = moonPanel.getLimb();
    int bodyLimb = otherBodyPanel.getLimb();
    double distance = distancePanel.getDistance();
    int distMoonLimb = distancePanel.getMoonLimb();
    int distBodyLimb = distancePanel.getBodyLimb();
    
    double eye = eyeHeightPanel.getEyeHeightInMeters();

    addToOutput("Moon with " + otherBodyName + ", " + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + " (dt:" + deltaT + ")");
    addToOutput("-----------------------------------------");
    if (verbose)
    {
      addToOutput("Horizon dip:" + SightReductionUtil.getHorizonDip(eye) + "'");
      addToOutput("-----------------------------------------");
    }
    if (verbose)
    {
      addToOutput("Moon Alt:" + GeomUtil.decToSex(moonAltitude, GeomUtil.SWING, GeomUtil.NONE) + "(" + moonAltitude + ")");
      addToOutput(otherBodyName + " Alt:" + GeomUtil.decToSex(bodyAltitude, GeomUtil.SWING, GeomUtil.NONE) + "(" + bodyAltitude + ")");
    }    
    AlmanacComputer.calculate(year, month, day, hour, minute, second, deltaT);
    
    // Moon
    int limb = (moonLimb==MoonPanel.LOWER_LIMB?SightReductionUtil.LOWER_LIMB:SightReductionUtil.UPPER_LIMB);
    double sdMoon = Context.SDmoon / 3600d;
    if (verbose)
      addToOutput("sd Moon:" + (sdMoon * 60d) + "'");
    double corr = SightReductionUtil.getAltitudeCorrection(moonAltitude, 
                                                           eye, 
                                                           Context.HPmoon / 3600d, // Returned in seconds, sent in degrees
                                                           sdMoon, 
                                                           limb, 
                                                           false, 
                                                           verbose);
    if (verbose)
      addToOutput("Moon Correction:" + (corr * 60) + "'");
    double moonObsAlt = moonAltitude + corr;
    // Apparent height, only corrected with horizon dip and semi-diameter
    double appCorr = - (SightReductionUtil.getHorizonDip(eye) / 60d);
    if (limb == SightReductionUtil.LOWER_LIMB)
      appCorr += (Context.SDmoon / 3600d);
    else
      appCorr -= (Context.SDmoon / 3600d);
    
    double appHMoon = moonAltitude + appCorr;
    if (verbose)
      addToOutput("App. Corr:" + (appCorr * 60) + "'");
    
    if (verbose)
    {
      addToOutput("Moon Apparent Altitude:" + GeomUtil.decToSex(appHMoon, GeomUtil.SWING, GeomUtil.NONE));
      addToOutput("Moon Observed Altitude:" + GeomUtil.decToSex(moonObsAlt, GeomUtil.SWING, GeomUtil.NONE));
    }
    // Other Body
    double hp = 0d;
    double sd = 0d;
    double calcDist = 0;
    
    if (otherBodyName.equals("The Sun"))
    {
      hp = Context.HPsun / 3600d;
      sd = Context.SDsun / 3600d;
      calcDist = Context.LDist;
    }
    else if (otherBodyName.equals("Venus"))
    {
      hp = Context.HPvenus / 3600d;
      sd = Context.SDvenus / 3600d;
      calcDist = Context.moonVenusDist;
    }
    else if (otherBodyName.equals("Mars"))
    {
      hp = Context.HPmars / 3600d;
      sd = Context.SDmars / 3600d;
      calcDist = Context.moonMarsDist;
    }
    else if (otherBodyName.equals("Jupiter"))
    {
      hp = Context.HPjupiter / 3600d;
      sd = Context.SDjupiter / 3600d;
      calcDist = Context.moonJupiterDist;
    }
    else if (otherBodyName.equals("Saturn"))
    {
      hp = Context.HPsaturn / 3600d;
      sd = Context.SDsaturn / 3600d;
      calcDist = Context.moonSaturnDist;
    }
    else
    {
      Core.starPos(otherBodyName);
      hp = 0d;
      sd = 0d;
      calcDist = Context.starMoonDist;
    }
    if (bodyLimb==OtherBodyPanel.LOWER_LIMB)
      limb = SightReductionUtil.LOWER_LIMB;
    else if (bodyLimb==OtherBodyPanel.UPPER_LIMB)
      limb = SightReductionUtil.UPPER_LIMB;
    else
      limb = SightReductionUtil.NO_LIMB;

    corr = SightReductionUtil.getAltitudeCorrection(bodyAltitude, 
                                                    eye, 
                                                    hp, 
                                                    sd, 
                                                    limb, 
                                                    false, 
                                                    verbose);
    addToOutput("-----------------------------------------");
    if (verbose)
    {
        addToOutput("sd " + otherBodyName + ":" + (sd * 60d) + "'");      
        addToOutput("Body Correction:" + (corr * 60) + "'");
    }
    double bodyObsAlt = bodyAltitude + corr;
    // Apparent height, only corrected with horizon dip and semi-diameter
    appCorr = - (SightReductionUtil.getHorizonDip(eye) / 60d);
    if (limb == SightReductionUtil.LOWER_LIMB)
      appCorr += sd;
    else if (limb == SightReductionUtil.UPPER_LIMB)
      appCorr -= sd;    
    double appHBody = bodyAltitude + appCorr;
    if (verbose)
      addToOutput("App. Corr:" + (appCorr * 60) + "'");
    if (verbose)
    {
      addToOutput(otherBodyName + " Apparent Altitude:" + GeomUtil.decToSex(appHBody, GeomUtil.SWING, GeomUtil.NONE));
      addToOutput(otherBodyName + " Observed Altitude:" + GeomUtil.decToSex(bodyObsAlt, GeomUtil.SWING, GeomUtil.NONE));
    }

    // Distance clearing
    double sdMoonAugmentation = 0.3 * Math.sin(Math.toRadians(moonObsAlt));
    sdMoon += (sdMoonAugmentation / 60d);

    double obsDist = distance;
    if (distMoonLimb == DistancePanel.NEAR_LIMB)
      obsDist += sdMoon;
    else
      obsDist -= sdMoon;
    if (distBodyLimb == DistancePanel.NEAR_LIMB)
      obsDist += sd;
    else
      obsDist -= sd;
    
    addToOutput("-----------------------------------------");
    addToOutput("Corrected Obs Dist (1):" + GeomUtil.decToSex(obsDist, GeomUtil.SWING, GeomUtil.NONE));

    addToOutput("hMoon   :" + GeomUtil.decToSex(moonObsAlt, GeomUtil.SWING, GeomUtil.NONE) + "\n" + 
                "appHMoon:" + GeomUtil.decToSex(appHMoon, GeomUtil.SWING, GeomUtil.NONE) + "\n" + 
                "hBody   :" + GeomUtil.decToSex(bodyObsAlt, GeomUtil.SWING, GeomUtil.NONE) + "\n" + 
                "appHBody:" + GeomUtil.decToSex(appHBody, GeomUtil.SWING, GeomUtil.NONE) + "\n" + 
                "Dist    :" + GeomUtil.decToSex(obsDist, GeomUtil.SWING, GeomUtil.NONE));
    double correctedDist = SightReductionUtil.clearLunarDistance(moonObsAlt, appHMoon, bodyObsAlt, appHBody, obsDist);
    addToOutput("Corrected Obs Dist (2):" + GeomUtil.decToSex(correctedDist, GeomUtil.SWING, GeomUtil.NONE));    
    addToOutput("----------------------");
    
    // Extrapolation
    addToOutput("Actual Dist           :" + GeomUtil.decToSex(calcDist, GeomUtil.SWING, GeomUtil.NONE));

    int hour_dist = hour;
    int minute_dist = minute;
    float second_dist = second;

    minute = 0;
    second = 0;
    AlmanacComputer.calculate(year, month, day, hour, minute, second, deltaT);
    double lower = getLunarDist(otherBodyName);
    addToOutput("Lower for [" + otherBodyName + "] on " + year + "-" + month + "-" + day + " " + 
                       hour + ":" + minute + ":" + second + " (dt:" + deltaT + ")" +
                       GeomUtil.decToSex(lower, GeomUtil.SWING, GeomUtil.NONE));

    hour += 1;
    minute = 0;
    second = 0;
    AlmanacComputer.calculate(year, month, day, hour, minute, second, deltaT);
    double higher = getLunarDist(otherBodyName);

    // Extrapolation - 2
    addToOutput("- Extrapolation -");
    double deltaOneHour = higher - lower;
    addToOutput("Delta (1 hour):" + (deltaOneHour * 60) + "'");

    double deltaDist = correctedDist - lower;
    addToOutput("Delta dist    :" + (deltaDist * 60) + "'");
    double deltaInHour = deltaDist / deltaOneHour;
    addToOutput("Distance shot at " + hour_dist + ":" + minute_dist + ":" + second_dist + " UT...");
    addToOutput("Interpoled time :" + GeomUtil.formatHMS(hour_dist + Math.abs(deltaInHour)) + " UT");

    double shotAt = hour_dist + (minute_dist / 60d) + (second_dist / 3600d);
    double extrapolated = hour_dist + Math.abs(deltaInHour);

    addToOutput("----------------------");
    double deltaTime = Math.abs(extrapolated - shotAt);
    addToOutput("Difference (interpolated): " + (deltaTime > 0? "+": "-") + GeomUtil.formatHMS(deltaTime));

    // Calculated
    deltaDist = correctedDist - calcDist;
    deltaInHour = deltaDist / deltaOneHour;
    addToOutput("Difference (calculated)  : " + (deltaInHour > 0? "+": "-") + GeomUtil.formatHMS(Math.abs(deltaInHour)));
    addToOutput("----------------------");

    addToOutput("Done");
    addToOutput("NB: 32s ~ 8'");    
  }
  
  private double getLunarDist(String bodyName)
  {
    double dist = 0d;
    if (bodyName.equals("The Sun"))
    {
      dist = Context.LDist;
    }
    else if (bodyName.equals("Venus"))
    {
      dist = Context.moonVenusDist;
    }
    else if (bodyName.equals("Mars"))
    {
      dist = Context.moonMarsDist;
    }
    else if (bodyName.equals("Jupiter"))
    {
      dist = Context.moonJupiterDist;
    }
    else if (bodyName.equals("Saturn"))
    {
      dist = Context.moonSaturnDist;
    }
    else
    {
      Core.starPos(bodyName);
      dist = Context.starMoonDist;
    }
    return dist;
  }
  
  private void addToOutput(String s)
  {
    String before = outputPanel.getText().trim();
    String str = before + (before.length()>0?"\n":"") + s;
    outputPanel.setText(str);
  }
}

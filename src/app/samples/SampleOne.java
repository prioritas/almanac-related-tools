package app.samples;

import app.almanac.AlmanacComputer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.text.DecimalFormat;

import java.text.NumberFormat;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.TimeZone;

import nauticalalmanac.Anomalies;
import nauticalalmanac.Context;
import nauticalalmanac.Core;
import nauticalalmanac.Jupiter;
import nauticalalmanac.Mars;
import nauticalalmanac.Moon;
import nauticalalmanac.Saturn;
import nauticalalmanac.Venus;

import user.util.GeomUtil;

public class SampleOne
{
  private static final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

  public static String userInput(String prompt)
  {
    String retString = "";
    System.err.print(prompt);
    try
    {
      retString = stdin.readLine();
    }
    catch(Exception e)
    {
      System.out.println(e);
      String s;
      try
      {
        s = userInput("<Oooch/>");
      }
      catch(Exception exception) 
      {
        exception.printStackTrace();
      }
    }
    return retString;
  }
  
  private static void askDate()
  {
    try 
    {
      year  = Integer.parseInt(userInput("Year : ").trim());
      month = Integer.parseInt(userInput("Month: ").trim());
      day   = Integer.parseInt(userInput("Day  : ").trim());

      hour   = Integer.parseInt(userInput("Hours  : ").trim());
      minute = Integer.parseInt(userInput("Minutes: ").trim());
      second = Float.parseFloat(userInput("Seconds: ").trim());

      String str = userInput("Delta T: [65.984] ");
      if (str.trim().length() == 0)
        str = "65.984";
      deltaT = Float.parseFloat(str.trim());
    }
    catch (Exception ex)
    {
      System.out.println("Oops:" + ex.toString());
    }
  }
  
  private static boolean ok = true;
  private static int year  = 0, 
                     month = 0, 
                     day = 0, 
                     hour = 0, 
                     minute = 0;
  private static float second = 0f, 
                       deltaT = 0f;
  // Main function
  public static void main2(String[] args)  
  {
    boolean interactive = false;
    if (interactive)
      askDate();
    else
    {
      year = 2004;
      month = 4;
      day = 26;
      hour = 20;
      minute = 0;
      second = 0;
      deltaT = 64.6f;
    }
              
    if (!ok)
      System.exit(1);
    System.out.println("For " + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + "  dT:" + deltaT);        
    long before = System.currentTimeMillis();
    calculate();
    long after = System.currentTimeMillis();
    output();
    System.out.println("--------- Computed in " + Long.toString(after - before) + "ms ---------");
    System.out.println();
  }

  public static void main(String[] args)
  {
    year = 2011;
    month = 12;
    day = 20;
    hour = 12;
    minute = 0;
    second = 0;
    deltaT = 66.4829f;

    System.out.println("For " + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + "  dT:" + deltaT);        
    calculate();
    System.out.println("Julian Date:" + Context.JD + ", " + Context.JD0h + ", " + Context.JDE);    
    System.out.println("EoT:" + eotInMinutesSeconds(Context.EoT));
    
    double lat = GeomUtil.sexToDec("37", "40"); // South San Francisco
    double[] rs = AlmanacComputer.sunRiseAndSet(lat, -122);
    if (Double.compare(rs[0], Double.NaN) == 0 || Double.compare(rs[1], Double.NaN) == 0)
      System.out.println("Latitude " + lat + ":No rise or set");
    else
    {
      System.out.println("Latitude " + lat + "\n - Sun rise:" + GeomUtil.formatHM(rs[0] - 8) + " Z:" + Integer.toString((int)Math.round(rs[2])) + 
                                             "\n - Sun set :" + GeomUtil.formatHM(rs[1] - 8) + " Z:" + Integer.toString((int)Math.round(rs[3])));
    }    
    SimpleDateFormat sdf = new SimpleDateFormat("Z");
    sdf.setTimeZone(TimeZone.getTimeZone("Pacific/Marquesas"));
    System.out.println("TZ:" + sdf.format(new Date()));

  }
  
  private static String eotInMinutesSeconds(double eot)
  {
    String str = "";
    double d = eot;
    if (d < 0) d = -d;
    NumberFormat mm = new DecimalFormat("00");
    NumberFormat ss = new DecimalFormat("00.0");
    str = mm.format(d) + "m " + ss.format((d - ((int)d)) * 60) + "s";
    
    return str;
  }
  public static void main3(String[] args)  
  {
    year = 2009;
    month = 12;
    day = 8;
    hour = 1;
    minute = 0;
    second = 0;
    deltaT = 65.984f;

    System.out.println("For " + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + "  dT:" + deltaT);        
    long before = System.currentTimeMillis();
    calculate();
    long after = System.currentTimeMillis();

    //Sun
    System.out.println("==================================");
    System.out.println("Sun");
    System.out.println("GHA:" + GeomUtil.decToSex(Context.GHAsun, DEGREE_OPTION, GeomUtil.NONE) + "\t(" + GeomUtil.formatDMS(Context.GHAsun, "\370") + ")");
    System.out.println("Dec:" + GeomUtil.decToSex(Context.DECsun, DEGREE_OPTION, GeomUtil.NS) + "\t(" + GeomUtil.formatDMS(Context.DECsun, "\370")  + ")");
    System.out.println("sd :" + df.format(Context.SDsun) + "\" (" + df.format(Context.SDsun / 60D) + "')");
    System.out.println("hp :" + df.format(Context.HPsun) + "\" (" + df.format(Context.HPsun / 60D) + "')");
    
    System.out.println("==================================");
    System.out.println("Moon");
    System.out.println("GHA:" + GeomUtil.decToSex(Context.GHAmoon, DEGREE_OPTION, GeomUtil.NONE) + "\t(" + GeomUtil.formatDMS(Context.GHAmoon, "\370") + ")");
    System.out.println("Dec:" + GeomUtil.decToSex(Context.DECmoon, DEGREE_OPTION, GeomUtil.NS) + "\t(" + GeomUtil.formatDMS(Context.DECmoon, "\370") + ")");
    System.out.println("sd :" + df.format(Context.SDmoon) + "\" (" + df.format(Context.SDmoon / 60D) + "')");
    System.out.println("hp :" + df.format(Context.HPmoon) + "\" (" + df.format(Context.HPmoon / 60D) + "')");

    System.out.println("==================================");
    System.out.println("Lunar Distance");
    System.out.println("Sun Moon Dist    :" + GeomUtil.decToSex(Context.LDist, DEGREE_OPTION, GeomUtil.NONE));
    System.out.println("Venus Moon Dist  :" + GeomUtil.decToSex(Context.moonVenusDist, DEGREE_OPTION, GeomUtil.NONE));
    System.out.println("Mars Moon Dist   :" + GeomUtil.decToSex(Context.moonMarsDist, DEGREE_OPTION, GeomUtil.NONE));
    System.out.println("Jupiter Moon Dist:" + GeomUtil.decToSex(Context.moonJupiterDist, DEGREE_OPTION, GeomUtil.NONE));
    System.out.println("Saturn Moon Dist :" + GeomUtil.decToSex(Context.moonSaturnDist, DEGREE_OPTION, GeomUtil.NONE));

    System.out.println("--------- Computed in " + Long.toString(after - before) + "ms ---------");
  }
    
  public static void main1(String[] args)  
  {
    year = 2004;
    month = 4;
    day = 26;
    hour = 20;
    minute = 0;
    second = 0;
    deltaT = 64.6f;

    System.out.println("For " + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + "  dT:" + deltaT);        
    long before = System.currentTimeMillis();
    calculate();
    long after = System.currentTimeMillis();

    //Sun
    System.out.println("==================================");
    System.out.println("Sun");
    System.out.println("GHA:" + GeomUtil.decToSex(Context.GHAsun, DEGREE_OPTION, GeomUtil.NONE) + "\t(" + GeomUtil.formatDMS(Context.GHAsun, "\370") + ")");
    System.out.println("Dec:" + GeomUtil.decToSex(Context.DECsun, DEGREE_OPTION, GeomUtil.NS) + "\t(" + GeomUtil.formatDMS(Context.DECsun, "\370")  + ")");
    System.out.println("sd :" + df.format(Context.SDsun) + "\" (" + df.format(Context.SDsun / 60D) + "')");
    System.out.println("hp :" + df.format(Context.HPsun) + "\" (" + df.format(Context.HPsun / 60D) + "')");
    
    System.out.println("==================================");
    System.out.println("Moon");
    System.out.println("GHA:" + GeomUtil.decToSex(Context.GHAmoon, DEGREE_OPTION, GeomUtil.NONE) + "\t(" + GeomUtil.formatDMS(Context.GHAmoon, "\370") + ")");
    System.out.println("Dec:" + GeomUtil.decToSex(Context.DECmoon, DEGREE_OPTION, GeomUtil.NS) + "\t(" + GeomUtil.formatDMS(Context.DECmoon, "\370") + ")");
    System.out.println("sd :" + df.format(Context.SDmoon) + "\" (" + df.format(Context.SDmoon / 60D) + "')");
    System.out.println("hp :" + df.format(Context.HPmoon) + "\" (" + df.format(Context.HPmoon / 60D) + "')");

    System.out.println("==================================");
    System.out.println("Sun-Lunar Distance");
    System.out.println("Dist:" + GeomUtil.decToSex(Context.LDist, DEGREE_OPTION, GeomUtil.NONE));

    System.out.println("--------- Computed in " + Long.toString(after - before) + "ms ---------");
    System.out.println();

    year = 2004;
    month = 4;
    day = 26;
    hour = 21;
    minute = 0;
    second = 0;
    deltaT = 64.6f;

    System.out.println("For " + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + "  dT:" + deltaT);        
    before = System.currentTimeMillis();
    calculate();
    after = System.currentTimeMillis();

    //Sun
    System.out.println("==================================");
    System.out.println("Sun");
    System.out.println("GHA:" + GeomUtil.decToSex(Context.GHAsun, DEGREE_OPTION, GeomUtil.NONE) + "\t(" + GeomUtil.formatDMS(Context.GHAsun, "\370") + ")");
    System.out.println("Dec:" + GeomUtil.decToSex(Context.DECsun, DEGREE_OPTION, GeomUtil.NS) + "\t(" + GeomUtil.formatDMS(Context.DECsun, "\370")  + ")");
    System.out.println("sd :" + df.format(Context.SDsun) + "\" (" + df.format(Context.SDsun / 60D) + "')");
    System.out.println("hp :" + df.format(Context.HPsun) + "\" (" + df.format(Context.HPsun / 60D) + "')");
    
    System.out.println("==================================");
    System.out.println("Moon");
    System.out.println("GHA:" + GeomUtil.decToSex(Context.GHAmoon, DEGREE_OPTION, GeomUtil.NONE) + "\t(" + GeomUtil.formatDMS(Context.GHAmoon, "\370") + ")");
    System.out.println("Dec:" + GeomUtil.decToSex(Context.DECmoon, DEGREE_OPTION, GeomUtil.NS) + "\t(" + GeomUtil.formatDMS(Context.DECmoon, "\370") + ")");
    System.out.println("sd :" + df.format(Context.SDmoon) + "\" (" + df.format(Context.SDmoon / 60D) + "')");
    System.out.println("hp :" + df.format(Context.HPmoon) + "\" (" + df.format(Context.HPmoon / 60D) + "')");

    System.out.println("==================================");
    System.out.println("Sun-Lunar Distance");
    System.out.println("Dist:" + GeomUtil.decToSex(Context.LDist, DEGREE_OPTION, GeomUtil.NONE));

    System.out.println("--------- Computed in " + Long.toString(after - before) + "ms ---------");
    System.out.println();
  }
    
  public static void calculate()
  {
    Core.julianDate(year, month, day, hour, minute, second, deltaT);
    Anomalies.nutation();
    Anomalies.aberration();

    Core.aries();
    Core.sun();
    Moon.compute();
    Venus.compute();
    Mars.compute();
    Jupiter.compute();
    Saturn.compute();
    Core.polaris();
    Core.moonPhase();
    Core.weekDay();
  }

  private final static DecimalFormat df = new DecimalFormat("###0.0000");

  private final static int DEGREE_OPTION = GeomUtil.SHELL;
  // Data output
  public static void output()
  {
    //Sun
    System.out.println("==================================");
    System.out.println("Sun");
    System.out.println("GHA:" + GeomUtil.decToSex(Context.GHAsun, DEGREE_OPTION, GeomUtil.NONE) + "\t(" + GeomUtil.formatDMS(Context.GHAsun, "\370") + ")");
    System.out.println("RA :" + GeomUtil.formatInHours(Context.RAsun));
    System.out.println("Dec:" + GeomUtil.decToSex(Context.DECsun, DEGREE_OPTION, GeomUtil.NS) + "\t(" + GeomUtil.formatDMS(Context.DECsun, "\370")  + ")");
    System.out.println("sd :" + df.format(Context.SDsun) + "\" (" + df.format(Context.SDsun / 60D) + "')");
    System.out.println("hp :" + df.format(Context.HPsun) + "\" (" + df.format(Context.HPsun / 60D) + "')");
  
    //Equation of time
    System.out.print("Equation of Time   ");
    String sign = "";
    if (Context.EoT < 0)
      sign = "-";
    else 
      sign = "+";
    Context.EoT = Math.abs(Context.EoT);
    int EOTmin = (int)Math.floor(Context.EoT);
    int EOTsec = (int)Math.round(600 * (Context.EoT - EOTmin)) / 10;
    if (EOTmin == 0)
      System.out.println(" " + sign + " " + EOTsec + "s");
    else 
      System.out.println(" " + sign + " " + EOTmin + "m " + EOTsec + "s");

    System.out.println("==================================");
    //Venus
    System.out.println("Venus");
    System.out.println("GHA:" + GeomUtil.decToSex(Context.GHAvenus, DEGREE_OPTION, GeomUtil.NONE) + "\t(" + GeomUtil.formatDMS(Context.GHAvenus, "\370") + ")");
    System.out.println("RA :" + GeomUtil.formatInHours(Context.RAvenus));
    System.out.println("Dec:" + GeomUtil.decToSex(Context.DECvenus, DEGREE_OPTION, GeomUtil.NS) + "\t(" + GeomUtil.formatDMS(Context.DECvenus, "\370") + ")");
    System.out.println("sd :" + df.format(Context.SDvenus) + "\"");
    System.out.println("hp :" + df.format(Context.HPvenus) + "\"");
    System.out.println("Illum " + Context.k_venus + "%");

    System.out.println("==================================");
    //Mars
    System.out.println("Mars");
    System.out.println("GHA:" + GeomUtil.decToSex(Context.GHAmars, DEGREE_OPTION, GeomUtil.NONE) + "\t(" + GeomUtil.formatDMS(Context.GHAmars, "\370") + ")");
    System.out.println("RA: " + GeomUtil.formatInHours(Context.RAmars));
    System.out.println("Dec:" + GeomUtil.decToSex(Context.DECmars, DEGREE_OPTION, GeomUtil.NS) + "\t(" + GeomUtil.formatDMS(Context.DECmars, "\370") + ")");
    System.out.println("sd :" + df.format(Context.SDmars) + "\"");
    System.out.println("hp :" + df.format(Context.HPmars) + "\"");
    System.out.println("Illum " + Context.k_mars + "%");

    System.out.println("==================================");
    //Jupiter
    System.out.println("Jupiter");
    System.out.println("GHA:" + GeomUtil.decToSex(Context.GHAjupiter, DEGREE_OPTION, GeomUtil.NONE) + "\t(" + GeomUtil.formatDMS(Context.GHAjupiter, "\370") + ")");
    System.out.println("RA: " + GeomUtil.formatInHours(Context.RAjupiter));
    System.out.println("Dec:" + GeomUtil.decToSex(Context.DECjupiter, DEGREE_OPTION, GeomUtil.NS) + "\t(" + GeomUtil.formatDMS(Context.DECjupiter, "\370") + ")");
    System.out.println("sd :" + df.format(Context.SDjupiter) + "\"");
    System.out.println("hp :" + df.format(Context.HPjupiter) + "\"");
    System.out.println("Illum " + Context.k_jupiter + "%");

    System.out.println("==================================");
    //Saturn
    System.out.println("Saturn");
    System.out.println("GHA:" + GeomUtil.decToSex(Context.GHAsaturn, DEGREE_OPTION, GeomUtil.NONE) + "\t(" + GeomUtil.formatDMS(Context.GHAsaturn, "\370") + ")");
    System.out.println("RA: " + GeomUtil.formatInHours(Context.RAsaturn));
    System.out.println("Dec:" + GeomUtil.decToSex(Context.DECsaturn, DEGREE_OPTION, GeomUtil.NS) + "\t(" + GeomUtil.formatDMS(Context.DECsaturn, "\370") + ")");
    System.out.println("sd :" + df.format(Context.SDsaturn) + "\"");
    System.out.println("hp :" + df.format(Context.HPsaturn) + "\"");
    System.out.println("Illum " + Context.k_saturn + "%");

    System.out.println("==================================");
    //Moon
    System.out.println("Moon");
    System.out.println("GHA:" + GeomUtil.decToSex(Context.GHAmoon, DEGREE_OPTION, GeomUtil.NONE) + "\t(" + GeomUtil.formatDMS(Context.GHAmoon, "\370") + ")");
    System.out.println("RA: " + GeomUtil.formatInHours(Context.RAmoon));
    System.out.println("Dec:" + GeomUtil.decToSex(Context.DECmoon, DEGREE_OPTION, GeomUtil.NS) + "\t(" + GeomUtil.formatDMS(Context.DECmoon, "\370") + ")");
    System.out.println("sd :" + df.format(Context.SDmoon) + "\" (" + df.format(Context.SDmoon / 60D) + "')");
    System.out.println("hp :" + df.format(Context.HPmoon) + "\" (" + df.format(Context.HPmoon / 60D) + "')");
    System.out.println("Illum " + Context.k_moon + "%" + Core.moonPhase());

    System.out.println("==================================");
    //Aries
    System.out.println("Aries");
    System.out.println(GeomUtil.decToSex(Context.GHAAtrue, DEGREE_OPTION, GeomUtil.NONE) + "\t(" + GeomUtil.formatDMS(Context.GHAAtrue, "\370") + ")");

    System.out.println("==================================");
    //Polaris
    System.out.println("Polaris");
    System.out.println("GHA:" + GeomUtil.decToSex(Context.GHApol, DEGREE_OPTION, GeomUtil.NONE) + "\t(" + GeomUtil.formatDMS(Context.GHApol, "\370") + ")");
    System.out.println("RA: " + GeomUtil.formatInHours(Context.RApol));
    System.out.println("Dec:" + GeomUtil.decToSex(Context.DECpol, DEGREE_OPTION, GeomUtil.NS) + "\t(" + GeomUtil.formatDMS(Context.DECpol, "\370") + ")");

    System.out.println("==================================");
    //Obliquity of Ecliptic
    System.out.println("Core Data");
    System.out.println("Mean Obl. of Ecl.:"+ GeomUtil.formatDMS(Context.eps0, "\370"));
    System.out.println("True Obl. of Ecl.:" + GeomUtil.formatDMS(Context.eps, "\370"));

    System.out.println("==================================");
    //Lunar Distance of Sun
    System.out.println("Sun-Lunar Distance");
    System.out.println("Dist:" + GeomUtil.decToSex(Context.LDist, DEGREE_OPTION, GeomUtil.NONE));
    
    System.out.println("==================================");
    System.out.println("Polaris -bis");    
    Core.starPos("Polaris");
    System.out.println("GHA:" + GeomUtil.decToSex(Context.GHAstar, DEGREE_OPTION, GeomUtil.NONE) + "\t(" + GeomUtil.formatDMS(Context.GHAstar, "\370") + ")");
    System.out.println("SHA:" + GeomUtil.decToSex(Context.SHAstar, DEGREE_OPTION, GeomUtil.NONE) + "\t(" + GeomUtil.formatDMS(Context.SHAstar, "\370") + ")");
    System.out.println("Dec:" + GeomUtil.decToSex(Context.DECstar, DEGREE_OPTION, GeomUtil.NS) + "\t(" + GeomUtil.formatDMS(Context.DECstar, "\370") + ")");
    System.out.println("Lunar dist:" + GeomUtil.decToSex(Context.LDist, DEGREE_OPTION, GeomUtil.NONE));    
    
    System.out.println("==================================");
    System.out.println("Arcturus");    
    Core.starPos("Arcturus");
    System.out.println("GHA:" + GeomUtil.decToSex(Context.GHAstar, DEGREE_OPTION, GeomUtil.NONE) + "\t(" + GeomUtil.formatDMS(Context.GHAstar, "\370") + ")");
    System.out.println("SHA:" + GeomUtil.decToSex(Context.SHAstar, DEGREE_OPTION, GeomUtil.NONE) + "\t(" + GeomUtil.formatDMS(Context.SHAstar, "\370") + ")");
    System.out.println("Dec:" + GeomUtil.decToSex(Context.DECstar, DEGREE_OPTION, GeomUtil.NS) + "\t(" + GeomUtil.formatDMS(Context.DECstar, "\370") + ")");
    System.out.println("Lunar dist:" + GeomUtil.decToSex(Context.LDist, DEGREE_OPTION, GeomUtil.NONE));    
    
    String[] dow = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
    
    System.out.println("\nIt's a " + dow[Core.weekDay()]);

    System.out.println("dpsi = " + Math.round(3600000 * Context.delta_psi) / 1000 + "\"");
    System.out.println("deps = " + Math.round(3600000 * Context.delta_eps) / 1000 + "\"");
    System.out.println("obliq = " + Context.OoE);
    System.out.println("trueobliq = " + Context.tOoE);
    System.out.println("JulianDay = " + Context.JD);
    System.out.println("JulianEphemDay = " + Context.JDE);
    
    System.out.println("-- Sun --");
    for (double lat=-55; lat<75; lat+=5)
    {
      double[] rs = AlmanacComputer.sunRiseAndSet(lat);
      if (Double.compare(rs[0], Double.NaN) == 0 || Double.compare(rs[1], Double.NaN) == 0)
        System.out.println("Latitude " + lat + ":No rise or set");
      else
      {
//      System.out.println(rs[0] + " - " + rs[1]);
        System.out.println("Latitude " + lat + ":Sun rise:" + GeomUtil.formatHM(rs[0]) + " Z:" + Integer.toString((int)Math.round(rs[2])) + ", Sun set :" + GeomUtil.formatHM(rs[1]) + " Z:" + Integer.toString((int)Math.round(rs[3])));
      }
    }
    System.out.println("-- Moon --");
    for (double lat=-55; lat<75; lat+=5)
    {
      double[] rs = AlmanacComputer.moonRiseAndSet(lat);
      if (Double.compare(rs[0], Double.NaN) == 0 || Double.compare(rs[1], Double.NaN) == 0)
        System.out.println("Latitude " + lat + ":No rise or set");
      else
      {
    //      System.out.println(rs[0] + " - " + rs[1]);
        System.out.println("Latitude " + lat + ":Moon rise:" + GeomUtil.formatHM(rs[0]) + ", Moon set :" + GeomUtil.formatHM(rs[1]));
      }
    }
  }
}

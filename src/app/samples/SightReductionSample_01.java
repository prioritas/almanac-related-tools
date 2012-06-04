package app.samples;

import calculation.SightReductionUtil;

import app.almanac.AlmanacComputer;

import nauticalalmanac.Anomalies;
import nauticalalmanac.Context;
import nauticalalmanac.Core;
import nauticalalmanac.Jupiter;
import nauticalalmanac.Moon;

import user.util.GeomUtil;

public class SightReductionSample_01
{
  private static double deltaT = 65.984;
  
  private static int year     = 2009;
  private static int month    =   10;
  private static int day      =   26;
  private static int hour     =   16;
  private static int minute   =   20;
  private static float second =    0f;
  
  private static double latitude  =   37d;
  private static double longitude = -123d;
  
  public static void main(String[] args)  
  {
    boolean verbose = false;
    // Sun
    try
    {
      System.out.println("Sun, " + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + " (dt:" + deltaT + ")");
      long before = System.currentTimeMillis();
      if (true)
        AlmanacComputer.calculate(year, month, day, hour, minute, second, deltaT);
      else
      {
        Core.julianDate(year, month, day, hour, minute, second, deltaT);
        Anomalies.nutation();
        Anomalies.aberration();
        
        Core.aries();
        Core.sun();
      }
      long after = System.currentTimeMillis();
      System.out.println("Almanac data calculated in " + Long.toString(after-before) + " ms");
      
      before = System.currentTimeMillis();
      SightReductionUtil sru = new SightReductionUtil();
     
      sru.setL(latitude);
      sru.setG(longitude);
      
      // Sun
      sru.setAHG(Context.GHAsun);
      sru.setD(Context.DECsun);    
      sru.calculate();          
      
      after = System.currentTimeMillis();
      System.out.println("Dead reckoning calculated in " + Long.toString(after-before) + " ms");

      System.out.println("Sun Alt:" + GeomUtil.decToSex(sru.getHe(), GeomUtil.SWING, GeomUtil.NONE) + " (" + sru.getHe() + ")");
      System.out.println("Sun Z  :" + GeomUtil.decToSex(sru.getZ(), GeomUtil.SWING, GeomUtil.NONE));
      
      double hi = 18.87;
      
      before = System.currentTimeMillis();
      double obsAlt = SightReductionUtil.observedAltitude(hi, 
                                                     1.8, 
                                                     Context.HPsun / 3600d,    // Returned in seconds, sent in degrees
                                                     Context.SDsun / 3600d,    // Returned in seconds, sent in degrees
          SightReductionUtil.LOWER_LIMB, 
                                                     verbose);
      
      after = System.currentTimeMillis();
      System.out.println("Altitude corrected in " + Long.toString(after-before) + " ms");

      System.out.println("For Hi :" + GeomUtil.decToSex(hi, GeomUtil.SWING, GeomUtil.NONE));
      System.out.println("Obs Alt:" + GeomUtil.decToSex(obsAlt, GeomUtil.SWING, GeomUtil.NONE) + " (" + obsAlt + ")");
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    System.out.println("----------------------");
    // Moon
    try
    {
      hour = 23;
      minute = 50;
      System.out.println("Moon, " + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + " (dt:" + deltaT + ")");
      long before = System.currentTimeMillis();
      if (true)
        AlmanacComputer.calculate(year, month, day, hour, minute, second, deltaT);
      else
      {
        Core.julianDate(year, month, day, hour, minute, second, deltaT);
        Anomalies.nutation();
        Anomalies.aberration();
        
        Core.aries();
        Moon.compute();
      }
      long after = System.currentTimeMillis();
      System.out.println("Almanac data calculated in " + Long.toString(after-before) + " ms");
      
      before = System.currentTimeMillis();
      SightReductionUtil dr = new SightReductionUtil();
     
      dr.setL(latitude);
      dr.setG(longitude);
      
      // Sun
      dr.setAHG(Context.GHAmoon);
      dr.setD(Context.DECmoon);    
      dr.calculate();          
      
      after = System.currentTimeMillis();
      System.out.println("Dead reckoning calculated in " + Long.toString(after-before) + " ms");

      System.out.println("Moon Alt:" + GeomUtil.decToSex(dr.getHe(), GeomUtil.SWING, GeomUtil.NONE) + " (" + dr.getHe() + ")");
      System.out.println("Moon Z  :" + GeomUtil.decToSex(dr.getZ(), GeomUtil.SWING, GeomUtil.NONE));
      
      double hi = 21.250;
      
      before = System.currentTimeMillis();
      double obsAlt = SightReductionUtil.observedAltitude(hi, 
                                                     1.8, 
                                                     Context.HPmoon / 3600d,  // Returned in seconds, sent in degrees
                                                     Context.SDmoon / 3600d,  // Returned in seconds, sent in degrees
          SightReductionUtil.LOWER_LIMB, 
                                                     verbose);
      after = System.currentTimeMillis();
      System.out.println("Altitude corrected in " + Long.toString(after-before) + " ms");

      System.out.println("For Hi :" + GeomUtil.decToSex(hi, GeomUtil.SWING, GeomUtil.NONE));
      System.out.println("Obs Alt:" + GeomUtil.decToSex(obsAlt, GeomUtil.SWING, GeomUtil.NONE) + " (" + obsAlt + ")");
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    System.out.println("----------------------");
    deltaT = 47.5;
    year = 1976;
    month = 8;
    day = 15;
    hour = 9;
    minute = 32;
    second = 16;
    latitude  =   GeomUtil.sexToDec("46", "38");
    longitude = - GeomUtil.sexToDec("04", "06");
    // Sun
    try
    {
      System.out.println("Sun, " + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + " (dt:" + deltaT + ")");
      long before = System.currentTimeMillis();
      if (true)
        AlmanacComputer.calculate(year, month, day, hour, minute, second, deltaT);
      else
      {
        Core.julianDate(year, month, day, hour, minute, second, deltaT);
        Anomalies.nutation();
        Anomalies.aberration();
        
        Core.aries();
        Core.sun();
      }      
      long after = System.currentTimeMillis();
      System.out.println("Almanac data calculated in " + Long.toString(after-before) + " ms");
      
      before = System.currentTimeMillis();
      SightReductionUtil dr = new SightReductionUtil();
     
      dr.setL(latitude);
      dr.setG(longitude);
      
      // Sun
      dr.setAHG(Context.GHAsun);
      dr.setD(Context.DECsun);    
      dr.calculate();          
      
      after = System.currentTimeMillis();
      System.out.println("Dead reckoning calculated in " + Long.toString(after-before) + " ms");

      System.out.println("Sun Alt:" + GeomUtil.decToSex(dr.getHe(), GeomUtil.SWING, GeomUtil.NONE) + " (" + dr.getHe() + ")");
      System.out.println("Sun Z  :" + GeomUtil.decToSex(dr.getZ(), GeomUtil.SWING, GeomUtil.NONE));
      
      double hi = GeomUtil.sexToDec("41", "55");
      
      before = System.currentTimeMillis();
      double obsAlt = SightReductionUtil.observedAltitude(hi, 
                                                     1.8, 
                                                     Context.HPsun / 3600d,    // Returned in seconds, sent in degrees
                                                     Context.SDsun / 3600d,    // Returned in seconds, sent in degrees
          SightReductionUtil.LOWER_LIMB, 
                                                     verbose);
      
      after = System.currentTimeMillis();
      System.out.println("Altitude corrected in " + Long.toString(after-before) + " ms");

      System.out.println("For Hi :" + GeomUtil.decToSex(hi, GeomUtil.SWING, GeomUtil.NONE));
      System.out.println("Obs Alt:" + GeomUtil.decToSex(obsAlt, GeomUtil.SWING, GeomUtil.NONE) + " (" + obsAlt + ")");
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    System.out.println("----------------------");
    // Altair
    hour = 19;
    minute = 32;
    second = 16;
    latitude  =   GeomUtil.sexToDec("46", "38");
    longitude = - GeomUtil.sexToDec("04", "16");
    try
    {
      System.out.println("Altair, " + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + " (dt:" + deltaT + ")");
      long before = System.currentTimeMillis();
      if (true)
        AlmanacComputer.calculate(year, month, day, hour, minute, second, deltaT);
      else
      {
        Core.julianDate(year, month, day, hour, minute, second, deltaT);
        Anomalies.nutation();
        Anomalies.aberration();
        
        Core.aries();
        Core.starPos("Altair");
      }
      long after = System.currentTimeMillis();
      System.out.println("Almanac data calculated in " + Long.toString(after-before) + " ms");
      
      before = System.currentTimeMillis();
      SightReductionUtil dr = new SightReductionUtil();
     
      dr.setL(latitude);
      dr.setG(longitude);
      
      // Altair
      dr.setAHG(Context.GHAstar);
      dr.setD(Context.DECstar);    
      dr.calculate();          
      
      after = System.currentTimeMillis();
      System.out.println("Dead reckoning calculated in " + Long.toString(after-before) + " ms");

      System.out.println("Altair Alt:" + GeomUtil.decToSex(dr.getHe(), GeomUtil.SWING, GeomUtil.NONE) + " (" + dr.getHe() + ")");
      System.out.println("Altair Z  :" + GeomUtil.decToSex(dr.getZ(), GeomUtil.SWING, GeomUtil.NONE));
      
      double hi = GeomUtil.sexToDec("36", "53");
      
      before = System.currentTimeMillis();
      double obsAlt = SightReductionUtil.observedAltitude(hi, 
                                                     1.8, 
                                                     0.0,
                                                     0.0, SightReductionUtil.NO_LIMB, 
                                                     verbose);
      
      after = System.currentTimeMillis();
      System.out.println("Altitude corrected in " + Long.toString(after-before) + " ms");

      System.out.println("For Hi :" + GeomUtil.decToSex(hi, GeomUtil.SWING, GeomUtil.NONE));
      System.out.println("Obs Alt:" + GeomUtil.decToSex(obsAlt, GeomUtil.SWING, GeomUtil.NONE) + " (" + obsAlt + ")");
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    System.out.println("----------------------");
    // Jupiter
    hour = 4;
    minute = 32;
    second = 16;
    latitude  =   GeomUtil.sexToDec("46", "38");
    longitude = - GeomUtil.sexToDec("04", "06");
    try
    {
      System.out.println("Jupiter, " + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + " (dt:" + deltaT + ")");
      long before = System.currentTimeMillis();
      if (true)
        AlmanacComputer.calculate(year, month, day, hour, minute, second, deltaT);
      else
      {
        Core.julianDate(year, month, day, hour, minute, second, deltaT);
        Anomalies.nutation();
        Anomalies.aberration();
        
        Core.aries();
        Jupiter.compute();
      }
      long after = System.currentTimeMillis();
      System.out.println("Almanac data calculated in " + Long.toString(after-before) + " ms");
      
      before = System.currentTimeMillis();
      SightReductionUtil dr = new SightReductionUtil();
     
      dr.setL(latitude);
      dr.setG(longitude);
      
      // Jupiter
      dr.setAHG(Context.GHAjupiter);
      dr.setD(Context.DECjupiter);    
      dr.calculate();          
      
      after = System.currentTimeMillis();
      System.out.println("Dead reckoning calculated in " + Long.toString(after-before) + " ms");

      System.out.println("Jupiter Alt:" + GeomUtil.decToSex(dr.getHe(), GeomUtil.SWING, GeomUtil.NONE) + " (" + dr.getHe() + ")");
      System.out.println("Jupiter Z  :" + GeomUtil.decToSex(dr.getZ(), GeomUtil.SWING, GeomUtil.NONE));
      
      double hi = GeomUtil.sexToDec("53", "11");
      
      before = System.currentTimeMillis();
      double obsAlt = SightReductionUtil.observedAltitude(hi, 
                                                     1.8, 
                                                     Context.HPjupiter / 3600d,    // Returned in seconds, sent in degrees
                                                     Context.SDjupiter / 3600d,    // Returned in seconds, sent in degrees
          SightReductionUtil.NO_LIMB, 
                                                     verbose);
      
      after = System.currentTimeMillis();
      System.out.println("Altitude corrected in " + Long.toString(after-before) + " ms");

      System.out.println("For Hi :" + GeomUtil.decToSex(hi, GeomUtil.SWING, GeomUtil.NONE));
      System.out.println("Obs Alt:" + GeomUtil.decToSex(obsAlt, GeomUtil.SWING, GeomUtil.NONE) + " (" + obsAlt + ")");
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    System.out.println("----------------------");
  }
}

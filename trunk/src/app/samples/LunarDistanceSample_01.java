package app.samples;

import calculation.SightReductionUtil;

import calculation.Util;

import app.almanac.AlmanacComputer;

import nauticalalmanac.Context;

import user.util.GeomUtil;

/**
 * From http://www.historicalatlas.com/lunars/easylun.html
 */
public class LunarDistanceSample_01
{
  private static boolean verbose = true;

  private static double deltaT = 64.6;

  private static int year = 2004;
  private static int month = 4;
  private static int day = 26;
  private static int hour = 20;
  private static int minute = 11;
  private static float second = 14f;

  private static String[] sunAlt = new String[]
    { "47", "48" };
  private static String[] moonAlt = new String[]
    { "47", "12.8" };

  private static String[] distDM = new String[]
    { "80", "8.6" };

  private static int[] sunHMS = new int[]
    { 20, 11, 14 };
  private static int[] distHMS = new int[]
    { 20, 16, 37 };
  private static int[] moonHMS = new int[]
    { 20, 17, 57 };

  private static double eye = Util.feetToMeters(10);

  public static void main(String[] args)
  {
    double hSun = 0d, appHSun = 0d;
    double hMoon = 0d, appHMoon = 0d;

    int hour_dist = distHMS[0];
    int minute_dist = distHMS[1];
    float second_dist = distHMS[2];

    // Sun
    try
    {
      hour = sunHMS[0];
      minute = sunHMS[1];
      second = sunHMS[2];
      System.out.println("Sun, " + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + " (dt:" + deltaT + ")");
      long before = System.currentTimeMillis();
      AlmanacComputer.calculate(year, month, day, hour, minute, second, deltaT);

      long after = System.currentTimeMillis();
      if (verbose)
        System.out.println("Almanac data calculated in " + Long.toString(after - before) + " ms");

      double hi = GeomUtil.sexToDec(sunAlt[0], sunAlt[1]); // 48 = 49 - 1
      if (verbose)
        System.out.println("Hi Sun:" + hi);

      before = System.currentTimeMillis();
      double corr = SightReductionUtil.getAltitudeCorrection(hi, 
                                                        eye, 
                                                        Context.HPsun / 3600d, // Returned in seconds, sent in degrees
                                                        Context.SDsun / 3600d, SightReductionUtil.LOWER_LIMB, 
                                                        false, 
                                                        verbose);
      if (verbose)
        System.out.println("Sun Correction:" + (corr * 60) + "'");
      double obsAlt = hi + corr;
      // Apparent height, only corrected with horizon dip and semi-diameter
      appHSun = hi - (SightReductionUtil.getHorizonDip(eye) / 60d) + (Context.SDsun / 3600d); // + : Lower Limb

      after = System.currentTimeMillis();
      if (verbose)
        System.out.println("Altitude corrected in " + Long.toString(after - before) + " ms");

      System.out.println("For Hi :" + GeomUtil.decToSex(hi, GeomUtil.SWING, GeomUtil.NONE));
      System.out.println("Obs Alt:" + GeomUtil.decToSex(obsAlt, GeomUtil.SWING, GeomUtil.NONE) + " (" + obsAlt + ")");
      hSun = obsAlt;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    System.out.println("----------------------");
    // Moon
    try
    {
      hour = moonHMS[0];
      minute = moonHMS[1];
      second = moonHMS[2];

      System.out.println("Moon, " + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + " (dt:" + deltaT + ")");
      long before = System.currentTimeMillis();
      AlmanacComputer.calculate(year, month, day, hour, minute, second, deltaT);

      long after = System.currentTimeMillis();
      if (verbose)
        System.out.println("Almanac data calculated in " + Long.toString(after - before) + " ms");

      double hi = GeomUtil.sexToDec(moonAlt[0], moonAlt[1]);
      if (verbose)
        System.out.println("Hi Moon:" + hi);

      before = System.currentTimeMillis();
      double corr = SightReductionUtil.getAltitudeCorrection(hi, 
                                                        eye, 
                                                        Context.HPmoon / 3600d, // Returned in seconds, sent in degrees
                                                        Context.SDmoon / 3600d, SightReductionUtil.UPPER_LIMB, 
                                                        false, 
                                                        verbose);
      if (verbose)
        System.out.println("Moon Correction:" + (corr * 60) + "'");
      double obsAlt = hi + corr;
      // Apparent height, only corrected with horizon dip and semi-diameter
      appHMoon = hi - (SightReductionUtil.getHorizonDip(eye) / 60d) - (Context.SDmoon / 3600d); // - : Upper Limb

      after = System.currentTimeMillis();
      if (verbose)
        System.out.println("Altitude corrected in " + Long.toString(after - before) + " ms");

      System.out.println("For Hi :" + GeomUtil.decToSex(hi, GeomUtil.SWING, GeomUtil.NONE));
      System.out.println("Obs Alt:" + GeomUtil.decToSex(obsAlt, GeomUtil.SWING, GeomUtil.NONE) + " (" + obsAlt + ")");
      hMoon = obsAlt;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    System.out.println("----------------------");
    // Lunar Dist calc.
    double obsDist = GeomUtil.sexToDec(distDM[0], distDM[1]); // Near Limb, @ 20:16:37 TU - Document: 80 9.3 (80 8.6 works better).
    System.out.println("App. Obs Dist         :" + GeomUtil.decToSex(obsDist, GeomUtil.SWING, GeomUtil.NONE));

    double sdSun  = Context.SDsun  / 60D; // in minutes
    double sdMoon = Context.SDmoon / 60D; // in minutes

    double sdMoonAugmentation = 0.3 * Math.sin(Math.toRadians(hMoon));
    sdMoon += sdMoonAugmentation;

    obsDist += ((sdMoon / 60d) + (sdSun / 60d)); // Near
    System.out.println("Corrected Obs Dist (1):" + GeomUtil.decToSex(obsDist, GeomUtil.SWING, GeomUtil.NONE));

    // Temp, for the document at http://www.historicalatlas.com/lunars/easylun.html.
    // I disagree with the corrected values...
    if (false)
    {
      hSun = GeomUtil.sexToDec("47", "12");
      hMoon = GeomUtil.sexToDec("46", "39.5");
    }
    if (verbose)
      System.out.println("hMoon   :" + GeomUtil.decToSex(hMoon, GeomUtil.SWING, GeomUtil.NONE) + "\n" + 
                         "appHMoon:" + GeomUtil.decToSex(appHMoon, GeomUtil.SWING, GeomUtil.NONE) + "\n" + 
                         "hSun    :" + GeomUtil.decToSex(hSun, GeomUtil.SWING, GeomUtil.NONE) + "\n" + 
                         "appHSun :" + GeomUtil.decToSex(appHSun, GeomUtil.SWING, GeomUtil.NONE) + "\n" + 
                         "Dist    :" + GeomUtil.decToSex(obsDist, GeomUtil.SWING, GeomUtil.NONE));
    double correctedDist = SightReductionUtil.clearLunarDistance(hMoon, appHMoon, hSun, appHSun, obsDist);
    System.out.println("Corrected Obs Dist (2):" + GeomUtil.decToSex(correctedDist, GeomUtil.SWING, GeomUtil.NONE));

    // Time of the dist observation
    hour = hour_dist;
    minute = minute_dist;
    second = second_dist;

    AlmanacComputer.calculate(year, month, day, hour, minute, second, deltaT);

    System.out.println("----------------------");
    double actualDist = Context.LDist;
    System.out.println("Actual Dist            :" + GeomUtil.decToSex(actualDist, GeomUtil.SWING, GeomUtil.NONE));
    double dec1 = Math.toRadians(Context.DECsun);
    double gha1 = Math.toRadians(Context.GHAsun);
    double dec2 = Math.toRadians(Context.DECmoon);
    double gha2 = Math.toRadians(Context.GHAmoon);
    double ld = Math.acos((Math.sin(dec1) * Math.sin(dec2)) + (Math.cos(dec1) * Math.cos(dec2) * Math.cos(Math.abs(gha2 - gha1))));
    ld = Math.toDegrees(ld);
    System.out.println("(Re-calculated Distance:" + GeomUtil.decToSex(ld, GeomUtil.SWING, GeomUtil.NONE) + ")");
    System.out.println("----------------------");
    // Delta
    hour = distHMS[0];
    minute = 0;
    second = 0;
    AlmanacComputer.calculate(year, month, day, hour, minute, second, deltaT);
    double lower = Context.LDist;
    if (verbose)
      System.out.println("Lower for [The Sun] on " + year + "-" + month + "-" + day + " " + 
                         hour + ":" + minute + ":" + second + " (dt:" + deltaT + ")" +
                         GeomUtil.decToSex(lower, GeomUtil.SWING, GeomUtil.NONE));
    
    hour = distHMS[0] + 1;
    minute = 0;
    second = 0;
    AlmanacComputer.calculate(year, month, day, hour, minute, second, deltaT);
    double higher = Context.LDist;

    // Extrapolation
    System.out.println("- Extrapolation -");
    double deltaOneHour = higher - lower;
    System.out.println("Delta (1 hour):" + (deltaOneHour * 60) + "'");

    double deltaDist = correctedDist - lower;
    System.out.println("Delta dist    :" + (deltaDist * 60) + "'");
    double deltaInHour = deltaDist / deltaOneHour;
    System.out.println("Distance shot at " + hour_dist + ":" + minute_dist + ":" + second_dist + " UT...");
    System.out.println("Interpoled time :" + GeomUtil.formatHMS(distHMS[0] + Math.abs(deltaInHour)) + " UT");

    double shotAt = hour_dist + (minute_dist / 60d) + (second_dist / 3600d);
    double extrapolated = distHMS[0] + Math.abs(deltaInHour);

    System.out.println("----------------------");
    double deltaT = Math.abs(extrapolated - shotAt);
    System.out.println("Difference (interpolated): " + (deltaT > 0? "+": "-") + GeomUtil.formatHMS(deltaT));

    // Calculated
    deltaDist = correctedDist - actualDist;
    deltaInHour = deltaDist / deltaOneHour;
    System.out.println("Difference (calculated)  : " + (deltaInHour > 0? "+": "-") + GeomUtil.formatHMS(Math.abs(deltaInHour)));
    System.out.println("----------------------");

    System.out.println("Done");
    System.out.println("NB: 32s ~ 8'");
  }
}

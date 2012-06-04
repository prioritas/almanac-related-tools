package app.samples;

import calculation.SightReductionUtil;

import calculation.Util;

import app.almanac.AlmanacComputer;

import nauticalalmanac.Context;

import user.util.GeomUtil;

/**
 * From Bruce Stark, 2
 */
public class LunarDistanceSample_03
{
  private static boolean verbose = true;

  private static double deltaT = 61.0;

  private static int year = 1996;
  private static int month = 10;
  private static int day = 31;
  private static int hour = 17;
  private static int minute = 18;
  private static float second = 52f;

  private static String[] sunAlt = new String[]
    { "21", "23.7" };
  private static String[] moonAlt = new String[]
    { "20", "2.8" };

  private static String[] distDM = new String[]
    { "118", "29.5" };

  private static int[] sunHMS = new int[]
    { 17, 18, 52 };
  private static int[] distHMS = sunHMS;
  private static int[] moonHMS = sunHMS;

  private static double eye = Util.feetToMeters(8);

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

      double hi = GeomUtil.sexToDec(sunAlt[0], sunAlt[1]);
      if (verbose)
        System.out.println("Hi Sun:" + hi);

      before = System.currentTimeMillis();
      double corr = SightReductionUtil.getAltitudeCorrection(hi, eye, Context.HPsun / 3600d, // Returned in seconds, sent in degrees
          Context.SDsun / 3600d, SightReductionUtil.LOWER_LIMB, false, verbose);
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
      double corr = SightReductionUtil.getAltitudeCorrection(hi, eye, Context.HPmoon / 3600d, // Returned in seconds, sent in degrees
          Context.SDmoon / 3600d, SightReductionUtil.LOWER_LIMB, false, verbose);
      if (verbose)
        System.out.println("Moon Correction:" + (corr * 60) + "'");
      double obsAlt = hi + corr;
      // Apparent height, only corrected with horizon dip and semi-diameter
      appHMoon = hi - (SightReductionUtil.getHorizonDip(eye) / 60d) + (Context.SDmoon / 3600d); // + : Lower Limb

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
    double obsDist = GeomUtil.sexToDec(distDM[0], distDM[1]); 
    System.out.println("App. Obs Dist         :" + GeomUtil.decToSex(obsDist, GeomUtil.SWING, GeomUtil.NONE));

    double sdSun  = Context.SDsun / 60D; // in minutes
    double sdMoon = Context.SDmoon / 60D; // in minutes

    double sdMoonAugmentation = 0.3 * Math.sin(Math.toRadians(hMoon)); // TASK See the same for the Sun ?
    sdMoon += sdMoonAugmentation;

    obsDist += ((sdMoon / 60d) + (sdSun / 60d)); // Near Limbs
    System.out.println("Corrected Obs Dist (1):" + GeomUtil.decToSex(obsDist, GeomUtil.SWING, GeomUtil.NONE));

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

    System.out.println("GHA Moon:" + GeomUtil.decToSex(Context.GHAmoon, GeomUtil.SWING, GeomUtil.NONE));
    System.out.println("GHA Sun :" + GeomUtil.decToSex(Context.GHAsun, GeomUtil.SWING, GeomUtil.NONE));
    System.out.println("Dec Moon:" + GeomUtil.decToSex(Context.DECmoon, GeomUtil.SWING, GeomUtil.NONE));
    System.out.println("Dec Sun :" + GeomUtil.decToSex(Context.DECsun, GeomUtil.SWING, GeomUtil.NONE));

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
    System.out.println("Interpoled time :" + GeomUtil.formatHMS(20 + Math.abs(deltaInHour)) + " UT");

    double shotAt = hour_dist + (minute_dist / 60d) + (second_dist / 3600d);
    double extrapolated = 20 + Math.abs(deltaInHour);

    System.out.println("----------------------");
    double deltaT = Math.abs(extrapolated - shotAt);
    System.out.println("Difference (interpolated): " + (deltaT > 0? "+": "-") + GeomUtil.formatHMS(deltaT));
    System.out.println("In miles:" + (deltaT * 3600d / 4d));

    // Calculated
    deltaDist = correctedDist - actualDist;
    deltaInHour = deltaDist / deltaOneHour;
    System.out.println("Difference (calculated)  : " + (deltaInHour > 0? "+": "-") + GeomUtil.formatHMS(Math.abs(deltaInHour)));
    System.out.println("In miles:" + (deltaInHour * 3600d / 4d));
    System.out.println("----------------------");

    System.out.println("Done");
    System.out.println("NB: 32s ~ 8'");
  }
}

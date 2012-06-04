package app.samples;

import calculation.SightReductionUtil;

import calculation.Util;

import app.almanac.AlmanacComputer;

import nauticalalmanac.Context;

import user.util.GeomUtil;

/**
 * From Bruce Stark, 1
 */
public class LunarDistanceSample_02
{
  private static boolean verbose = false;

  private static double deltaT = 61.0;

  private static int year = 1996;
  private static int month = 11;
  private static int day = 8;
  private static int hour = 14;
  private static int minute = 47;
  private static float second = 14f;

  private static String[] venusAlt = new String[]
    { "27", "52.3" };
  private static String[] moonAlt = new String[]
    { "23", "37.8" };

  private static String[] distDM = new String[]
    { "4", "18.9" };

  private static int[] moonHMS = new int[]
    { 14, 47, 14 };
  private static int[] distHMS = new int[]
    { 14, 47, 14 };
  private static int[] venusHMS = new int[]
    { 14, 47, 14 };

  private static double eye = Util.feetToMeters(8);

  public static void main(String[] args)
  {
    double hVenus = 0d, appHVenus = 0d;
    double hMoon = 0d, appHMoon = 0d;

    int hour_dist = distHMS[0];
    int minute_dist = distHMS[1];
    float second_dist = distHMS[2];

    // Venus
    try
    {
      hour = venusHMS[0];
      minute = venusHMS[1];
      second = venusHMS[2];
      System.out.println("Venus, " + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + " (dt:" + deltaT + ")");
      long before = System.currentTimeMillis();
      AlmanacComputer.calculate(year, month, day, hour, minute, second, deltaT);

      long after = System.currentTimeMillis();
      if (verbose)
        System.out.println("Almanac data calculated in " + Long.toString(after - before) + " ms");

      double hi = GeomUtil.sexToDec(venusAlt[0], venusAlt[1]); // 48 = 49 - 1
      if (verbose)
        System.out.println("Hi Venus:" + hi);

      before = System.currentTimeMillis();
      double corr = SightReductionUtil.getAltitudeCorrection(hi, 
                                                             eye, 
                                                             Context.HPvenus / 3600d, // Returned in seconds, sent in degrees
                                                             Context.SDvenus / 3600d, 
                                                             SightReductionUtil.LOWER_LIMB, 
                                                             false, 
                                                             verbose);
      if (verbose)
        System.out.println("Venus Correction:" + (corr * 60) + "'");
      double obsAlt = hi + corr;
      // Apparent height, only corrected with horizon dip and semi-diameter
      appHVenus = hi - (SightReductionUtil.getHorizonDip(eye) / 60d) + (Context.SDvenus / 3600d); // + : Lower Limb

      after = System.currentTimeMillis();
      if (verbose)
        System.out.println("Altitude corrected in " + Long.toString(after - before) + " ms");

      System.out.println("For Hi :" + GeomUtil.decToSex(hi, GeomUtil.SWING, GeomUtil.NONE));
      System.out.println("Obs Alt:" + GeomUtil.decToSex(obsAlt, GeomUtil.SWING, GeomUtil.NONE) + " (" + obsAlt + ")");
      hVenus = obsAlt;
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

    double sdVenus = Context.SDvenus / 60D; // in minutes
    double sdMoon  = Context.SDmoon / 60D;   // in minutes

    double sdMoonAugmentation = 0.3 * Math.sin(Math.toRadians(hMoon));
    sdMoon += sdMoonAugmentation;

    // Attention: Far limb!! Substract.
    obsDist -= ((sdMoon / 60d) + (sdVenus / 60d)); // Far 
    System.out.println("Corrected Obs Dist (1):" + GeomUtil.decToSex(obsDist, GeomUtil.SWING, GeomUtil.NONE));

    double correctedDist = SightReductionUtil.clearLunarDistance(hMoon, appHMoon, hVenus, appHVenus, obsDist);
    System.out.println("Corrected Obs Dist (2):" + GeomUtil.decToSex(correctedDist, GeomUtil.SWING, GeomUtil.NONE));

    // Time of the dist observation
    hour = hour_dist;
    minute = minute_dist;
    second = second_dist;

    AlmanacComputer.calculate(year, month, day, hour, minute, second, deltaT);
    System.out.println("----------------------");
    double actualDist = Context.moonVenusDist;
    System.out.println("Actual Dist            :" + GeomUtil.decToSex(actualDist, GeomUtil.SWING, GeomUtil.NONE));
    double dec1 = Math.toRadians(Context.DECvenus);
    double gha1 = Math.toRadians(Context.GHAvenus);
    double dec2 = Math.toRadians(Context.DECmoon);
    double gha2 = Math.toRadians(Context.GHAmoon);

    System.out.println("GHA Moon  :" + GeomUtil.decToSex(Context.GHAmoon, GeomUtil.SWING, GeomUtil.NONE));
    System.out.println("GHA Venus :" + GeomUtil.decToSex(Context.GHAvenus, GeomUtil.SWING, GeomUtil.NONE));
    System.out.println("Dec Moon  :" + GeomUtil.decToSex(Context.DECmoon, GeomUtil.SWING, GeomUtil.NONE));
    System.out.println("Dec Venus :" + GeomUtil.decToSex(Context.DECvenus, GeomUtil.SWING, GeomUtil.NONE));

    double ld = Math.acos((Math.sin(dec1) * Math.sin(dec2)) + (Math.cos(dec1) * Math.cos(dec2) * Math.cos(Math.abs(gha2 - gha1))));
    ld = Math.toDegrees(ld);
    System.out.println("(Re-calculated Distance:" + GeomUtil.decToSex(ld, GeomUtil.SWING, GeomUtil.NONE) + ")");
    System.out.println("----------------------");
    // Delta
    hour = distHMS[0];
    minute = 0;
    second = 0;
    AlmanacComputer.calculate(year, month, day, hour, minute, second, deltaT);
    System.out.println("-- Previous hour --");
    System.out.println("GHA Moon  :" + GeomUtil.decToSex(Context.GHAmoon, GeomUtil.SWING, GeomUtil.NONE));
    System.out.println("GHA Venus :" + GeomUtil.decToSex(Context.GHAvenus, GeomUtil.SWING, GeomUtil.NONE));
    System.out.println("Dec Moon  :" + GeomUtil.decToSex(Context.DECmoon, GeomUtil.SWING, GeomUtil.NONE));
    System.out.println("Dec Venus :" + GeomUtil.decToSex(Context.DECvenus, GeomUtil.SWING, GeomUtil.NONE));
    double lower = Context.moonVenusDist;

    hour = distHMS[0] + 1;
    minute = 0;
    second = 0;
    AlmanacComputer.calculate(year, month, day, hour, minute, second, deltaT);
    System.out.println("-- Following hour --");
    System.out.println("GHA Moon  :" + GeomUtil.decToSex(Context.GHAmoon, GeomUtil.SWING, GeomUtil.NONE));
    System.out.println("GHA Venus :" + GeomUtil.decToSex(Context.GHAvenus, GeomUtil.SWING, GeomUtil.NONE));
    System.out.println("Dec Moon  :" + GeomUtil.decToSex(Context.DECmoon, GeomUtil.SWING, GeomUtil.NONE));
    System.out.println("Dec Venus :" + GeomUtil.decToSex(Context.DECvenus, GeomUtil.SWING, GeomUtil.NONE));
    double higher = Context.moonVenusDist;

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

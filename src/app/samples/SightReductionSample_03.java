package app.samples;

import app.almanac.AlmanacComputer;

import calculation.SightReductionUtil;

import java.text.DecimalFormat;

import nauticalalmanac.Anomalies;
import nauticalalmanac.Context;
import nauticalalmanac.Core;
import nauticalalmanac.Jupiter;
import nauticalalmanac.Mars;
import nauticalalmanac.Moon;
import nauticalalmanac.Saturn;
import nauticalalmanac.Venus;

import user.util.GeomUtil;

/**
 * Calculated to Observed
 * 
 */
public class SightReductionSample_03
{
  private static final DecimalFormat df = new DecimalFormat("##0.000");

  public static void main(String[] args)
  {
    boolean verbose = true;

    String[] m = new String[]
      { "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec" };
    double deltaT = 65.984;

    int year = 2009;
    int month = 12;
    int day = 23;
    int hour = 23;
    int minute = 11;
    float second = 0f;

    System.out.println(year + "-" + m[month - 1] + "-" + day + " " + hour + ":" + minute + ":" + df.format(second));
    // Calculate everything
    AlmanacComputer.calculate(year, month, day, hour, minute, second, deltaT);

    // Sight Reduction
    double lat =   GeomUtil.sexToDec( "37", "39.856");
    double lng = - GeomUtil.sexToDec("122", "22.813");
    SightReductionUtil dr = new SightReductionUtil(Context.GHAsun, Context.DECsun, lat, lng);
    dr.calculate();
    Double he = dr.getHe();
    Double z = dr.getZ();
    System.out.println("Calc. Sun Obs. Alt:" + GeomUtil.decToSex(he, GeomUtil.SWING, GeomUtil.NONE));
    System.out.println("========================");

    double eye = 1.8;

    //  Context.SDsun = 16 * 60d;
    //  Context.HPsun = 0.1 * 60d;

    if (verbose)
      System.out.println("Semi-Diameter:" + df.format(Context.SDsun / 60d) + "'");
    double obs = he - (Context.SDsun / 3600d); // Lower Limb
    //  double newObs = DeadReckoning.observedToApparentAltitude(he.doubleValue(), Context.HPsun / 3600d, true);
    double pa = SightReductionUtil.getParallax(Context.HPsun / 3600d, he.doubleValue());
    if (verbose)
      System.out.println("Parallaxe:" + df.format(pa * 60d) + "'");
    obs -= pa;
    double ref = SightReductionUtil.getRefr(obs);
    if (verbose)
      System.out.println("Refraction:" + df.format(ref) + "'");
    obs += (ref / 60d);
    double hd = SightReductionUtil.getHorizonDip(eye) / 60d;
    if (verbose)
      System.out.println("Horizon Dip:" + df.format(hd * 60d) + "'");
    obs += hd;
    System.out.println("Apparent (sextant) Alt:" + GeomUtil.decToSex(obs, GeomUtil.SWING, GeomUtil.NONE));
    System.out.println("Z                     :" + Math.round(z) + "\272");
    System.out.println("========================");
    
    dr = new SightReductionUtil(Context.GHAmoon, Context.DECmoon, lat, lng);
    dr.calculate();
    he = dr.getHe();
    z = dr.getZ();
    System.out.println("Calc. Moon (LL) Obs. Alt:" + GeomUtil.decToSex(he, GeomUtil.SWING, GeomUtil.NONE));
    System.out.println("========================");

    if (verbose)
      System.out.println("Semi-Diameter:" + df.format(Context.SDmoon / 60d) + "'");
    obs = he - (Context.SDmoon / 3600d); // Lower Limb
    //  double newObs = DeadReckoning.observedToApparentAltitude(he.doubleValue(), Context.HPsun / 3600d, true);
    if (verbose)
      System.out.println("Hor. Par.:" + df.format(Context.HPmoon / 60d) + "'");
    pa = SightReductionUtil.getParallax(Context.HPmoon / 3600d, he.doubleValue());
    if (verbose)
      System.out.println("Parallaxe:" + df.format(pa * 60d) + "'");
    obs -= pa;
    ref = SightReductionUtil.getRefr(obs);
    if (verbose)
      System.out.println("Refraction:" + df.format(ref) + "'");
    obs += (ref / 60d);
    hd = SightReductionUtil.getHorizonDip(eye) / 60d;
    if (verbose)
      System.out.println("Horizon Dip:" + df.format(hd * 60d) + "'");
    obs += hd;
    System.out.println("Apparent (sextant) Alt:" + GeomUtil.decToSex(obs, GeomUtil.SWING, GeomUtil.NONE));
    System.out.println("Z                     :" + Math.round(z) + "\272");
    System.out.println("========================");
  }
}

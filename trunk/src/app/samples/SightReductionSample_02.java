package app.samples;

import calculation.SightReductionUtil;

import java.text.DecimalFormat;

import app.almanac.AlmanacComputer;

import nauticalalmanac.Anomalies;
import nauticalalmanac.Context;
import nauticalalmanac.Core;
import nauticalalmanac.Jupiter;
import nauticalalmanac.Mars;
import nauticalalmanac.Moon;

import nauticalalmanac.Saturn;
import nauticalalmanac.Venus;

import user.util.GeomUtil;

public class SightReductionSample_02
{
  private final static DecimalFormat df = new DecimalFormat("##0.000");
    
  public static void main(String[] args)
  {
    boolean verbose = false;

    String[] m = new String[] { "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec" };
    double deltaT = 65.984;
    
    int year     = 2009;
    int month    =   12;
    int day      =   10;
    int hour     =   20;
    int minute   =   11;
    float second =   14f;    

    System.out.println(year + "-" + m[month - 1] + "-" + day + " " + hour + ":" + minute + ":" + df.format(second));    
    // Calculate everything
    if (true)
      AlmanacComputer.calculate(year, month, day, hour, minute, second, deltaT);
    else
    {
      Core.julianDate(year, month, day, hour, minute, second, deltaT);
      Anomalies.nutation();
      Anomalies.aberration();
  
      Core.aries();
      Core.sun();
      Moon.compute(); // Important! Moon is used for lunar distances, by planets and stars.
      Venus.compute();
      Mars.compute();
      Jupiter.compute();
      Saturn.compute();
      Core.polaris();
      Core.moonPhase();
      Core.weekDay();
    }    
    // Sight Reduction
    SightReductionUtil dr = new SightReductionUtil(Context.GHAsun, Context.DECsun, 37d, -122d);
    dr.calculate();
    Double he = dr.getHe();
    Double z  = dr.getZ();
    System.out.println("Calc. Obs. Alt:" + GeomUtil.decToSex(he, GeomUtil.SWING, GeomUtil.NONE));
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
    System.out.println("Apparent Alt:" + GeomUtil.decToSex(obs, GeomUtil.SWING, GeomUtil.NONE));
    System.out.println("Z           :" + Math.round(z) + "\272");
    System.out.println("========================");
    // Reciproque:
    double back = SightReductionUtil.observedAltitude(obs, eye, Context.HPsun / 3600d, Context.SDsun / 3600d, SightReductionUtil.LOWER_LIMB, verbose);
    System.out.println("Reversed Alt:" + GeomUtil.decToSex(back, GeomUtil.SWING, GeomUtil.NONE));
    System.out.println("  (Where Refr:" + df.format(dr.getRefr()) + "')");
    System.out.println("Total Correction:" + df.format(Math.abs(back - obs) * 60d) + "'");
    double tc = SightReductionUtil.getAltitudeCorrection(obs, eye, Context.HPsun / 3600d, Context.SDsun / 3600d, SightReductionUtil.LOWER_LIMB, false, verbose);
    System.out.println("Calc Correction :" + df.format(tc * 60d) + "'");
  }
}

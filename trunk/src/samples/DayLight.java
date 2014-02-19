package samples;

import calculation.AstroComputer;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DayLight
{
  private final static double latitude =    8;
  private final static double longitude = -122;
  
  private final static SimpleDateFormat SDF  = new SimpleDateFormat("MMM-dd");
  static { SDF.setTimeZone(TimeZone.getTimeZone("Etc/UTC")); }
  private final static NumberFormat     DF22 = new DecimalFormat("#0.00");
  private final static NumberFormat     DF2  = new DecimalFormat("00");

  public static void main(String[] args)
  {
    Calendar utcCal = GregorianCalendar.getInstance();
    utcCal.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
    utcCal.getTime();

    AstroComputer.setDateTime(utcCal.get(Calendar.YEAR), 
                              utcCal.get(Calendar.MONTH) + 1, 
                              utcCal.get(Calendar.DAY_OF_MONTH), 
                              utcCal.get(Calendar.HOUR_OF_DAY), // 12 - (int)Math.round(AstroComputer.getTimeZoneOffsetInHours(TimeZone.getTimeZone(ts.getTimeZone()))), 
                              utcCal.get(Calendar.MINUTE), 
                              utcCal.get(Calendar.SECOND));
    AstroComputer.calculate();
    double[] rsSun  = AstroComputer.sunRiseAndSet(latitude, longitude);
    double daylight = (rsSun[AstroComputer.UTC_SET_IDX] - rsSun[AstroComputer.UTC_RISE_IDX]);
    System.out.println(SDF.format(utcCal.getTime()) +  ", Daylight:" + decimalHoursToHMS(daylight));
    System.out.println("========================================");
    boolean go = true;
    utcCal.set(Calendar.MONTH, Calendar.JANUARY);
    utcCal.set(Calendar.DAY_OF_MONTH, 1);
    utcCal.set(Calendar.HOUR_OF_DAY, 0);
    utcCal.set(Calendar.MINUTE, 0); 
    utcCal.set(Calendar.SECOND, 0);
    
    int year = utcCal.get(Calendar.YEAR);
    while (go)
    {
      AstroComputer.setDateTime(utcCal.get(Calendar.YEAR), 
                                utcCal.get(Calendar.MONTH) + 1, 
                                utcCal.get(Calendar.DAY_OF_MONTH), 
                                utcCal.get(Calendar.HOUR_OF_DAY), // 12 - (int)Math.round(AstroComputer.getTimeZoneOffsetInHours(TimeZone.getTimeZone(ts.getTimeZone()))), 
                                utcCal.get(Calendar.MINUTE), 
                                utcCal.get(Calendar.SECOND));
      AstroComputer.calculate();
      rsSun  = AstroComputer.sunRiseAndSet(latitude, longitude);
      daylight = (rsSun[AstroComputer.UTC_SET_IDX] - rsSun[AstroComputer.UTC_RISE_IDX]);
      System.out.println(SDF.format(utcCal.getTime()) +  ", Daylight:" + decimalHoursToHMS(daylight));
      utcCal.add(Calendar.DAY_OF_MONTH, 1);
      go = (year == utcCal.get(Calendar.YEAR));
    }   
  }
  
  private static String decimalHoursToHMS(double diff)
  {
    double dh = Math.abs(diff);
    String s = "";
    if (dh >= 1)
      s += (DF2.format((int)dh) + "h ");
    double remainder = dh - ((int)dh);
    double minutes = remainder * 60;
    if (s.trim().length() > 0 || minutes >= 1)
      s += (DF2.format((int)minutes) + "m ");
    remainder = minutes - (int)minutes;
    double seconds = remainder * 60;
    s += (DF2.format((int)seconds) + "s");
    if (diff < 0)
      s = "- " + s;
    else
      s = "+ " + s;
    return s.trim();
  }  
}

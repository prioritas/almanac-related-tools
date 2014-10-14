package util;


import java.util.Date;
import java.util.HashMap;

import java.util.Map;

import ocss.nmea.parser.GeoPos;
import ocss.nmea.parser.OverGround;
import ocss.nmea.parser.RMC;
import ocss.nmea.parser.SVData;
import ocss.nmea.parser.StringParsers;
import ocss.nmea.parser.UTC;
import ocss.nmea.parser.UTCDate;
import ocss.nmea.parser.Wind;

public class NMEACache
{
  private Map<Integer, SVData> satellites = null;
  private Date gpsDate = null;
  private double bsp = 0d;
  private double cog = 0d;
  private double sog = 0d;
  private double lat = 0d;
  private double lng = 0;
  
  private int hdg    = 0;
  private double aws = 0d;
  private int awa    = 0;
  private double wt  = 0d;
  private double dbt = 0d;
  private double bigLog   = 0d;
  private double smallLog = 0d;
  
  private long started = 0L;
  private long running = 0L; // Has been running for X ms.
  
  private static NMEACache instance = null;

  private NMEACache()
  {
    
  }

  public synchronized static NMEACache getInstance()
  {
    if (instance == null)
      instance = new NMEACache();
    return instance;
  }

  public void dispatch(String nmeaPayload)
  {
 // System.out.println("[" + nmeaPayload + "]");  
    if (nmeaPayload != null && nmeaPayload.length() > 6  && StringParsers.validCheckSum(nmeaPayload))
    {
      String k = nmeaPayload.substring(3, 6);
      if (k.equals("GLL"))
      {
        Object[] gll = StringParsers.parseGLL(nmeaPayload);
        GeoPos gp = (GeoPos)gll[StringParsers.GP_in_GLL];
        if (gp != null)
        {
          setLat(gp.lat);
          setLng(gp.lng);
        }
        Date date = (Date)gll[StringParsers.DATE_in_GLL];
        if (date != null)
        {
          setGpsDate(date);
        }
      }
      else if (k.equals("VTG"))
      {
        OverGround og = StringParsers.parseVTG(nmeaPayload);
        if (og != null)
        {
          setCog(og.getCourse());
          setSog(og.getSpeed());
        }
      }
      else if (k.equals("RMC"))
      {
        RMC rmc = StringParsers.parseRMC(nmeaPayload);
        if (rmc != null)
        {
          setCog(rmc.getCog());
          setSog(rmc.getSog());
          if (rmc.getGp() != null)
          {
            setLat(rmc.getGp().lat);
            setLng(rmc.getGp().lng);
          }
          setGpsDate(rmc.getRmcDate());
        }
      }
      else if (k.equals("ZDA"))
      {
        UTC utc = StringParsers.parseZDA(nmeaPayload);
        if (utc != null)
        {
          setGpsDate(utc.getDate());
        }
      }
      else if (k.equals("GSV"))
      {
        Map<Integer, SVData> map = StringParsers.parseGSV(nmeaPayload);
        satellites = map;
//      System.out.println(Integer.toString(map.size()) + " satellite(s) in view.");
      }
      else if (k.equals("VHW")) // Boat speed and Heading
      {
        setBsp(StringParsers.parseVHW(nmeaPayload)[StringParsers.BSP_in_VHW]);
      }
      else if (k.equals("MWV")) // Wind Speed and Angle
      {
        Wind wind = StringParsers.parseMWV(nmeaPayload);
        setAwa(wind.angle);
        setAws(wind.speed);
      }
      else if (k.equals("HDG"))
      {
        setHdg((int)StringParsers.parseHDG(nmeaPayload)[StringParsers.HDG_in_HDG]);
      }
      else if (k.equals("HDM"))
      {
        setHdg(StringParsers.parseHDM(nmeaPayload));
      }
      else if (k.equals("HDT"))
      {
        setHdg(StringParsers.parseHDT(nmeaPayload));
      }
      else if (k.equals("MTW")) // Water Temp
      {
        setWt(StringParsers.parseMTW(nmeaPayload));
      }
      else if (k.equals("VLW")) // Log
      {
        double[] la = StringParsers.parseVLW(nmeaPayload);
        setBigLog(la[0]);
        setSmallLog(la[1]);
      }
      else if (k.equals("DBT"))  // Depth
      {
        setDbt(StringParsers.parseDBT(nmeaPayload, StringParsers.DEPTH_IN_METERS));
      }
      else if (k.equals("DPT"))  // Depth
      {
        setDbt(StringParsers.parseDPT(nmeaPayload, StringParsers.DEPTH_IN_METERS));
      }
//    else
//      System.out.println("Not managed yet:[" + nmeaPayload + "]");   
      
      if (started == 0L)
        started = System.currentTimeMillis();
      running = System.currentTimeMillis() - started;
    }
  }
  
  public long getRunning()
  {
    return this.running;  
  }
  
  public void setBsp(double bsp)
  {
    this.bsp = bsp;
  }

  public double getBsp()
  {
    return bsp;
  }

  public void setSog(double sog)
  {
    this.sog = sog;
  }

  public double getSog()
  {
    return sog;
  }

  public void setLat(double lat)
  {
    this.lat = lat;
  }

  public double getLat()
  {
    return lat;
  }

  public void setLng(double lng)
  {
    this.lng = lng;
  }

  public double getLng()
  {
    return lng;
  }

  public void setCog(double cog)
  {
    this.cog = cog;
  }

  public double getCog()
  {
    return cog;
  }

  public void setGpsDate(Date gpsDate)
  {
    this.gpsDate = gpsDate;
  }

  public Date getGpsDate()
  {
    return gpsDate;
  }

  public void setSatellites(HashMap<Integer, SVData> satellites)
  {
    this.satellites = satellites;
  }

  public Map<Integer, SVData> getSatellites()
  {
    return satellites;
  }

  public void setHdg(int hdg)
  {
    this.hdg = hdg;
  }

  public int getHdg()
  {
    return hdg;
  }

  public void setAws(double aws)
  {
    this.aws = aws;
  }

  public double getAws()
  {
    return aws;
  }

  public void setAwa(int awa)
  {
    this.awa = awa;
  }

  public int getAwa()
  {
    return awa;
  }

  public void setWt(double wt)
  {
    this.wt = wt;
  }

  public double getWt()
  {
    return wt;
  }

  public void setDbt(double dbt)
  {
    this.dbt = dbt;
  }

  public double getDbt()
  {
    return dbt;
  }

  public void setBigLog(double bigLog)
  {
    this.bigLog = bigLog;
  }

  public double getBigLog()
  {
    return bigLog;
  }

  public void setSmallLog(double smallLog)
  {
    this.smallLog = smallLog;
  }

  public double getSmallLog()
  {
    return smallLog;
  }
}

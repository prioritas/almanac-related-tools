package app.starfinder.ctx;

import astro.calc.GeoPoint;

import java.util.List;
import java.util.EventListener;

import ocss.nmea.parser.SVData;

public abstract class StarFinderEventListener implements EventListener 
{
  public void requestChartPanelRepaint() {}
  public void headingHasChanged(int hdg) {}
  public void positionHasChanged(GeoPoint gp) {}
  public void setGPSSatellites(List<SVData> svData) {}
  
  public void internalFrameClosed() {}
}

package app.starfinder.ctx;

import astro.calc.GeoPoint;

import java.util.ArrayList;

import java.util.List;

import ocss.nmea.parser.SVData;

import oracle.xml.parser.v2.DOMParser;

public class SFContext
{
  private static SFContext context = null;
  private List<StarFinderEventListener> applicationListeners = null;
  private DOMParser parser = null;
  
  private SFContext()
  {
    applicationListeners = new ArrayList<StarFinderEventListener>(2); // 2: Initial Capacity
    parser = new DOMParser();
  }
  
  public static synchronized SFContext getInstance()
  {
    if (context == null)
      context = new SFContext();
    return context;
  }
  
  public void release()
  {
    context = null;
    parser = null;
    System.gc();
  }

  public List<StarFinderEventListener> getListeners()
  {
    return applicationListeners;
  }
  
  public synchronized void addApplicationListener(StarFinderEventListener l)
  {
    if (!this.getListeners().contains(l))
    {      
      this.getListeners().add(l);
  //    System.out.println("Now having " + Integer.toString(this.getListeners().size()) + " listener(s)");
    }
  }

  public synchronized void removeApplicationListener(StarFinderEventListener l)
  {
    this.getListeners().remove(l);
  }
  
  public void fireRequestChartPanelRepaint()
  {
    for (int i=0; i < this.getListeners().size(); i++)
    {
      StarFinderEventListener l = this.getListeners().get(i);
      l.requestChartPanelRepaint();
    }    
  }    
  
  public void fireHeadingHasChanged(int hdg)
  {
    for (int i=0; i < this.getListeners().size(); i++)
    {
      StarFinderEventListener l = this.getListeners().get(i);
      l.headingHasChanged(hdg);
    }    
  }    
  
  public void firePositionHasChanged(GeoPoint gp)
  {
    for (int i=0; i < this.getListeners().size(); i++)
    {
      StarFinderEventListener l = this.getListeners().get(i);
      l.positionHasChanged(gp);
    }    
  }    
    
  public void fireSetGPSSatellites(List<SVData> svData)
  {
    for (int i=0; i < this.getListeners().size(); i++)
    {
      StarFinderEventListener l = this.getListeners().get(i);
      l.setGPSSatellites(svData);
    }    
  }   
  
  public void fireInternalFrameClosed()
  {
    for (int i=0; i < this.getListeners().size(); i++)
    {
      StarFinderEventListener l = this.getListeners().get(i);
      l.internalFrameClosed();
    }    
  }    
  
  public DOMParser getParser()
  {
    return parser;
  }
}

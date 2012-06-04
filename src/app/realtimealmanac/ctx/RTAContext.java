package app.realtimealmanac.ctx;

import app.sightreduction.ctx.SREventListener;

import java.util.ArrayList;
import java.util.List;

public class RTAContext
{
  private static RTAContext applicationContext = null;
  private transient List<RTAEventListener> rtaListeners = null;

  private RTAContext()
  {
    rtaListeners = new ArrayList<RTAEventListener>(2); // 2: Initial Capacity
  }

  public static synchronized RTAContext getInstance()
  {
    if (applicationContext == null)
      applicationContext = new RTAContext();
    return applicationContext;
  }

  public List<RTAEventListener> getListeners()
  {
    return rtaListeners;
  }

  public synchronized void addRTAListener(RTAEventListener l)
  {
    if (!rtaListeners.contains(l))
    {
      rtaListeners.add(l);
    }
  }

  public synchronized void removeRTAListener(RTAEventListener l)
  {
    rtaListeners.remove(l);
  }

  public void fireInternalFrameClosed()
  {
    for (int i = 0; i < rtaListeners.size(); i++)
    {
      RTAEventListener l = rtaListeners.get(i);
      l.internalFrameClosed();
    }
  }
}

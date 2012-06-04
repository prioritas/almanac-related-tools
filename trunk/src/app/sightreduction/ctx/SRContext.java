package app.sightreduction.ctx;

import java.util.ArrayList;
import java.util.List;

public class SRContext
{
  private static SRContext applicationContext = null;
  private transient List<SREventListener> srListeners = null;

  private SRContext()
  {
    srListeners = new ArrayList<SREventListener>(2); // 2: Initial Capacity
  }

  public static synchronized SRContext getInstance()
  {
    if (applicationContext == null)
      applicationContext = new SRContext();
    return applicationContext;
  }

  public List<SREventListener> getListeners()
  {
    return srListeners;
  }

  public synchronized void addSRListener(SREventListener l)
  {
    if (!srListeners.contains(l))
    {
      srListeners.add(l);
    }
  }

  public synchronized void removeSRListener(SREventListener l)
  {
    srListeners.remove(l);
  }

  public void fireInternalFrameClosed()
  {
    for (int i = 0; i < srListeners.size(); i++)
    {
      SREventListener l = srListeners.get(i);
      l.internalFrameClosed();
    }
  }
}

package app.almanac.ctx;

import java.util.ArrayList;

import java.util.List;

import oracle.xml.parser.v2.DOMParser;

public class APContext
{
  private static APContext applicationContext = null;
  private transient List<APEventListener> apListeners = null;
  private transient DOMParser parser = null;

  private APContext()
  {
    apListeners = new ArrayList<APEventListener>(2); // 2: Initial Capacity
    parser = new DOMParser();
  }

  public static synchronized APContext getInstance()
  {
    if (applicationContext == null)
      applicationContext = new APContext();
    return applicationContext;
  }

  public List<APEventListener> getListeners()
  {
    return apListeners;
  }

  public synchronized void addAPListener(APEventListener l)
  {
    if (!apListeners.contains(l))
    {
      apListeners.add(l);
    }
  }

  public synchronized void removeAPListener(APEventListener l)
  {
    apListeners.remove(l);
  }

  public DOMParser getParser()
  {
    return parser;
  }

  public void fireInternalFrameClosed()
  {
    for (int i = 0; i < apListeners.size(); i++)
    {
      APEventListener l = apListeners.get(i);
      l.internalFrameClosed();
    }
  }
}

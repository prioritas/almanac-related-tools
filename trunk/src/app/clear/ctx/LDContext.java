package app.clear.ctx;


import java.util.ArrayList;
import java.util.List;

public class LDContext
{
  private static LDContext applicationContext = null;  
  private transient List<LDEventListener> ldListeners = null;
  
  private LDContext()
  {
    ldListeners = new ArrayList<LDEventListener>(2); // 2: Initial Capacity
  }
    
  public static synchronized LDContext getInstance()
  {
    if (applicationContext == null)
      applicationContext = new LDContext();
    return applicationContext;
  }
    
  public List<LDEventListener> getListeners()
  {
    return ldListeners;
  }    

  public synchronized void addLDListener(LDEventListener l)
  {
    if (!ldListeners.contains(l))
    {
      ldListeners.add(l);
    }
  }

  public synchronized void removeLDListener(LDEventListener l)
  {
    ldListeners.remove(l);
  }

  public void fireInternalFrameClosed()
  {
    for (int i=0; i<ldListeners.size(); i++)
    {
      LDEventListener l = ldListeners.get(i);
      l.internalFrameClosed();
    }
  }
}

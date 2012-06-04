package app.realtimealmanac;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class RealTimeFrame
  extends JFrame
{
  JPanel realTimePanel = new RealTimeAlmanacPanel();
  
  public RealTimeFrame()
  {
    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private void jbInit()
    throws Exception
  {
    this.getContentPane().setLayout( new BorderLayout() );
    this.setSize(new Dimension(300, 470));    
    this.setTitle( "Real Time Almanac" );
    this.getContentPane().add(realTimePanel, BorderLayout.CENTER);
  }
}

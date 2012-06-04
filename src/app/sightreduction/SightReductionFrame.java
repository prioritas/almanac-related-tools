package app.sightreduction;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class SightReductionFrame
  extends JFrame
{
  private SightReductionPanel srp = new SightReductionPanel();
  
  public SightReductionFrame()
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
    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(srp, BorderLayout.CENTER);
    this.setSize(new Dimension(546, 681));
    this.setTitle( "Sight Reduction" );
  }
}

package app.planetarium;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class PlanetariumFrame
  extends JFrame
{
  public PlanetariumFrame()
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
    this.setSize(new Dimension(466, 498));
    this.setTitle( "Planetarium" );
    this.getContentPane().add(new PlanetariumPanel(), BorderLayout.CENTER);
  }
}

package app.almanac.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class AlmanacPublisherFrame
  extends JFrame
{
  private BorderLayout borderLayout1 = new BorderLayout();
  private AlmanacPublisherPanel publisherPanel = new AlmanacPublisherPanel();

  public AlmanacPublisherFrame()
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
    this.getContentPane().setLayout(borderLayout1);
    this.setSize(new Dimension(405, 344));
    this.setTitle( "Almanac Publisher" );
    this.getContentPane().add(publisherPanel, BorderLayout.CENTER);
  }
}

package app.realtimealmanac;

import app.realtimealmanac.ctx.RTAContext;

import app.sightreduction.SightReductionPanel;
import app.sightreduction.ctx.SRContext;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class RealTimeAlmanacInternalFrame
  extends JInternalFrame
{
  private RealTimeAlmanacPanel srp = new RealTimeAlmanacPanel();

  public RealTimeAlmanacInternalFrame()
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
    this.setSize(new Dimension(300, 470));
    this.setTitle("Real Time Almanac");
//  try { this.setFrameIcon(new ImageIcon(this.getClass().getResource("sextant.gif"))); } catch (Exception ignore) {}
    this.addInternalFrameListener(new InternalFrameAdapter()
      {
        public void internalFrameClosed(InternalFrameEvent e)
        {
          this_internalFrameClosed(e);
        }
      });
    this.validate();
  }

  private void this_internalFrameClosed(InternalFrameEvent e)
  {
    RTAContext.getInstance().fireInternalFrameClosed();
  }
}

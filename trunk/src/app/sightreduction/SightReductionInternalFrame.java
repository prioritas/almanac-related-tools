package app.sightreduction;

import app.sightreduction.ctx.SRContext;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class SightReductionInternalFrame
  extends JInternalFrame
{
  private SightReductionPanel srp = new SightReductionPanel();

  public SightReductionInternalFrame()
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
    this.setSize(new Dimension(546, 687));
    this.setTitle("Sight Reduction");
    try { this.setFrameIcon(new ImageIcon(this.getClass().getResource("sextant.gif"))); } catch (Exception ignore) {}
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
    SRContext.getInstance().fireInternalFrameClosed();
  }
}

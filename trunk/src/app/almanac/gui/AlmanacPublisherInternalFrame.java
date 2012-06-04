package app.almanac.gui;

import app.almanac.ctx.APContext;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class AlmanacPublisherInternalFrame
  extends JInternalFrame
{
  private BorderLayout borderLayout1 = new BorderLayout();
  private AlmanacPublisherPanel publisherPanel = new AlmanacPublisherPanel();

  public AlmanacPublisherInternalFrame()
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
    this.setSize(new Dimension(409, 343));
    this.setTitle("Almanac Publisher");    
    try { this.setFrameIcon(new ImageIcon(this.getClass().getResource("sextant.gif"))); } catch (Exception ignore) {}
    this.addInternalFrameListener(new InternalFrameAdapter()
      {
        public void internalFrameClosed(InternalFrameEvent e)
        {
          this_internalFrameClosed(e);
        }
      });
    this.getContentPane().add(publisherPanel, BorderLayout.CENTER);
  }

  private void this_internalFrameClosed(InternalFrameEvent e)
  {
    APContext.getInstance().fireInternalFrameClosed();
  }
}

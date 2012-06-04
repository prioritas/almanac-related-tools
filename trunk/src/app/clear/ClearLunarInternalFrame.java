package app.clear;


import app.clear.ctx.LDContext;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class ClearLunarInternalFrame
  extends JInternalFrame
{
  private ClearLunarPanel clp = new ClearLunarPanel();

  public ClearLunarInternalFrame()
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
    this.setSize(new Dimension(500, 540));
    this.getContentPane().add(clp, BorderLayout.CENTER);
    this.setTitle(" Clear Lunar Distance");
    try { this.setFrameIcon(new ImageIcon(this.getClass().getResource("sextant.gif"))); } catch (Exception ignore) {}
    this.addInternalFrameListener(new InternalFrameAdapter()
      {
        public void internalFrameClosed(InternalFrameEvent e)
        {
          this_internalFrameClosed(e);
        }
      });
  }

  private void this_internalFrameClosed(InternalFrameEvent e)
  {
    LDContext.getInstance().fireInternalFrameClosed();
  }
}

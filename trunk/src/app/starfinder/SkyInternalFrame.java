package app.starfinder;

import app.starfinder.ctx.SFContext;

import astro.calc.GeoPoint;

import java.awt.BorderLayout;
import java.awt.Dimension;

import java.io.PrintWriter;

import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class SkyInternalFrame
  extends JInternalFrame
{
  private SkyPanel sp = null;
  
  public SkyInternalFrame()
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
    this.setSize(new Dimension(970, 665));
    sp = new SkyPanel();
    this.add(sp, BorderLayout.CENTER);
    this.setTitle("Star Finder");
    try { this.setFrameIcon(new ImageIcon(this.getClass().getResource("star.png"))); } catch (Exception ignore) {}
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
    try
    {
      GeoPoint gp = sp.getPositionPanel().getPosition();
      Properties posProp = new Properties();
      posProp.setProperty("last.latitude", Double.toString(gp.getL()));
      posProp.setProperty("last.longitude", Double.toString(gp.getG()));
      PrintWriter pw = new PrintWriter(SkyPanel.POSITION_PROPRTIES_FILE_NAME);
      posProp.list(pw);
      pw.close();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    SFContext.getInstance().fireInternalFrameClosed();
    SFContext.getInstance().removeApplicationListener(sp.getStarFinderEventListener());
  }
}

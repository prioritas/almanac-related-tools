package app.sightreduction;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class SightReduction
{
  public SightReduction()
  {
    JFrame frame = new SightReductionFrame();
    try { frame.setIconImage(new ImageIcon(this.getClass().getResource("sextant.gif")).getImage()); } catch (Exception ignore) {}
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height)
    {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width)
    {
      frameSize.width = screenSize.width;
    }
    frame.setLocation( ( screenSize.width - frameSize.width ) / 2, ( screenSize.height - frameSize.height ) / 2 );
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    frame.setVisible(true);
  }

  public static void main(String[] args)
  {
    try
    {
      if (System.getProperty("swing.defaultlaf") == null)
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    new SightReduction();
  }
}

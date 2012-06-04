package app.starfinder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class SkyFrame
  extends JFrame
{
  public SkyFrame()
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
    this.setSize(new Dimension(970, 665));
    this.add(new SkyPanel(), BorderLayout.CENTER);
    this.setTitle( "Star Finder" );
    try { this.setIconImage(new ImageIcon(this.getClass().getResource("star.png")).getImage()); } catch (Exception ignore) {}

  }
  
}

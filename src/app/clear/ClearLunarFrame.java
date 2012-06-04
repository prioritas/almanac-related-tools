package app.clear;


import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;


public class ClearLunarFrame
  extends JFrame
{
  private ClearLunarPanel clp = new ClearLunarPanel();
  
  public ClearLunarFrame()
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
  }

}

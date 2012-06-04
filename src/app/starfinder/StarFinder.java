package app.starfinder;

import coreutilities.CheckForUpdateThread;

import coreutilities.ctx.CoreContext;

import coreutilities.ctx.CoreEventListener;

import java.awt.Dimension;
import java.awt.Toolkit;

import java.io.File;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.UIManager;

import oracle.xml.parser.v2.DOMParser;

public class StarFinder
{
  public StarFinder()
  {
    JFrame frame = new SkyFrame();
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
    checkForUpdate();
  }

  private static boolean proceed = false;
  public static void checkForUpdate()
  {
    // Checking for update
    proceed = true;
    try
    {
      Thread checkForUpdate = 
        new CheckForUpdateThread("star_finder", 
                                 new DOMParser(), 
                                 "." + File.separator + "config" + File.separator + "structure.xml" , 
                                 proceed);
      // Add listener
      CoreContext.getInstance().addApplicationListener(new CoreEventListener()
       {
         public void updateCompleted(ArrayList<String> fList)
         {
           System.out.println("Update Completed by the Core Context");
           if (fList != null && fList.size() > 0)
           {
             String downloadMess = "";
             for (String s : fList)
               downloadMess += (s + "\n");
             // Display file list
  //         sendPing("Software update requested for:\n" + downloadMess);
             System.out.println("Updated " + downloadMess);  
           }
         }
       });
      checkForUpdate.start();
    }
    catch (Exception ignore) {}
  }

  public static void main(String[] args)
  {
    String lnf = System.getProperty("swing.defaultlaf");
//  System.out.println("LnF:" + lnf);
    if (lnf == null) // Let the -Dswing.defaultlaf do the job.
    {
      try
      {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }
    JFrame.setDefaultLookAndFeelDecorated(true);
    new StarFinder();
  }
}

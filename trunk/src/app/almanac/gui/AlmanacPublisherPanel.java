package app.almanac.gui;

import app.almanac.AlmanacComputer;

import app.almanac.ctx.APContext;

import app.util.FileChooserPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileInputStream;
import java.io.InputStream;

import java.io.InputStreamReader;

import java.text.DecimalFormat;

import java.text.SimpleDateFormat;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;

import java.util.GregorianCalendar;

import java.util.List;
import java.util.TimeZone;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import oracle.xml.parser.v2.DOMParser;
import oracle.xml.parser.v2.NSResolver;
import oracle.xml.parser.v2.XMLDocument;
import oracle.xml.parser.v2.XMLElement;

import org.w3c.dom.NodeList;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class AlmanacPublisherPanel
  extends JPanel
{
  private final static SimpleDateFormat SDF     = new SimpleDateFormat("dd MMM yyyy");
  private final static SimpleDateFormat SDF_UTC = new SimpleDateFormat("dd MMM yyyy 'UTC'");

  private final static Color PURPLE = new Color(165, 0, 165);
  
  private GridBagLayout gridBagLayout = new GridBagLayout();
  private JComboBox typeComboBox = new JComboBox();
  private JLabel typeLabel = new JLabel();
  private JLabel languageLabel = new JLabel();
  private JComboBox languageComboBox = new JComboBox();
  private JRadioButton periodRadioButton = new JRadioButton();
  private JRadioButton intervalRadioButton = new JRadioButton();
  private PeriodPanel periodPanel = new PeriodPanel();
  private IntervalPanel intervalPanel = new IntervalPanel();
  private ButtonGroup bg = new ButtonGroup();
  
  private final static String[] pubType = {"Almanac with Stars", "Almanac", "Lunar Distances" };
  private final static String[] lang    = {"English", "French" };
  private JButton publishButton = new JButton();
  private JLabel deltaTLabel = new JLabel();
  private JFormattedTextField deltaTFormattedTextField = new JFormattedTextField(new DecimalFormat("#00.000"));
  private JLabel fileLabel = new JLabel();
  private FileChooserPanel filePanel = new FileChooserPanel();
  private JProgressBar progressBar = new JProgressBar();
  private JPanel leapSecLinkPanel = new JPanel();
  private JLabel leapSecLinkOne   = new JLabel();
  private JLabel leapSecLinkTwo   = new JLabel();
  private JLabel leapSecLinkThree = new JLabel();
  private JLabel leapSecLinkFour  = new JLabel();
  private GenerateOrReusePanel gorPanel = new GenerateOrReusePanel(this);

  public AlmanacPublisherPanel()
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
    this.setLayout(gridBagLayout);
    this.setSize(new Dimension(400, 329));
    typeLabel.setText("Type:");
    languageLabel.setText("Language:");
    periodRadioButton.setText("Period");
    intervalRadioButton.setText("Interval");
    intervalRadioButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          intervalRadioButton_actionPerformed(e);
        }
      });
    this.add(typeComboBox, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(typeLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(languageLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(languageComboBox, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(periodRadioButton, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(intervalRadioButton, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(periodPanel, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

    this.add(intervalPanel, new GridBagConstraints(1, 4, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(publishButton, new GridBagConstraints(0, 7, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
    this.add(deltaTLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 10, 0), 0, 0));
    this.add(deltaTFormattedTextField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 10, 0), 0, 0));
    this.add(fileLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(filePanel, new GridBagConstraints(1, 5, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(progressBar, new GridBagConstraints(0, 8, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 0), 0, 0));
    this.add(leapSecLinkPanel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 10, 0), 0, 0));
    this.add(gorPanel, new GridBagConstraints(0, 6, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
    typeComboBox.removeAllItems();
    for (int i = 0; i < pubType.length; i++)
      typeComboBox.addItem(pubType[i]);
    languageComboBox.removeAllItems();
    for (int i = 0; i < lang.length; i++)
      languageComboBox.addItem(lang[i]);

    bg.add(periodRadioButton);
    bg.add(intervalRadioButton);
    periodRadioButton.setSelected(true);
    periodRadioButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          periodRadioButton_actionPerformed(e);
        }
      });
    periodPanel.setEnabled(true);
    intervalPanel.setEnabled(false);
    publishButton.setText("Publish");
    publishButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(final ActionEvent e)
        {
          Thread publishThread = new Thread()
            {
              public void run()
              {
                publishButton_actionPerformed(e);      
              }
            };
          publishThread.start();
        }
      });
    deltaTLabel.setText("Delta T:");
    deltaTFormattedTextField.setPreferredSize(new Dimension(60, 20));
    deltaTFormattedTextField.setHorizontalAlignment(JTextField.CENTER);
    deltaTFormattedTextField.setText(System.getProperty("deltaT", "65.984"));

    fileLabel.setText("File to generate:");
    progressBar.setIndeterminate(false);
    progressBar.setStringPainted(true);
    progressBar.setEnabled(false);
    progressBar.setString("Ready");
    leapSecLinkOne.setText("Check it on the web ");
    leapSecLinkTwo.setText("<html><u>Here</u></html>");
    leapSecLinkTwo.setForeground(Color.blue);
    leapSecLinkTwo.addMouseListener(new MouseAdapter()
      {
        public void mouseClicked(MouseEvent e)
        {
//        String url = "http://aa.usno.navy.mil/data/docs/celnavtable.php"; // "http://maia.usno.navy.mil/"
          String url = "http://maia.usno.navy.mil/";

          try { openInBrowser(url); }
          catch (Exception ex) { ex.printStackTrace(); }
        }
        public void mouseEntered(MouseEvent e)
        {
          leapSecLinkTwo.setForeground(PURPLE);
          leapSecLinkTwo.repaint();
        }
        public void mouseExited(MouseEvent e)
        {
          leapSecLinkTwo.setForeground(Color.blue);
          leapSecLinkTwo.repaint();
        }
      });
    leapSecLinkThree.setText(" or ");
    leapSecLinkFour.setText("<html><u>Here</u></html>");
    leapSecLinkFour.setForeground(Color.blue);
    leapSecLinkFour.addMouseListener(new MouseAdapter()
      {
        public void mouseClicked(MouseEvent e)
        {
    //        String url = "http://aa.usno.navy.mil/data/docs/celnavtable.php"; // "http://maia.usno.navy.mil/"
          String url = "http://maia.usno.navy.mil/ser7/deltat.data";

          try { openInBrowser(url); }
          catch (Exception ex) { ex.printStackTrace(); }
        }
        public void mouseEntered(MouseEvent e)
        {
          leapSecLinkFour.setForeground(PURPLE);
          leapSecLinkFour.repaint();
        }
        public void mouseExited(MouseEvent e)
        {
          leapSecLinkFour.setForeground(Color.blue);
          leapSecLinkFour.repaint();
        }
      });
    leapSecLinkPanel.add(leapSecLinkOne, null);
    leapSecLinkPanel.add(leapSecLinkTwo, null);
    leapSecLinkPanel.add(leapSecLinkThree, null);
    leapSecLinkPanel.add(leapSecLinkFour, null);
    
    periodPanel.setDate(new Date());
    intervalPanel.initDate();
  }

  private void periodRadioButton_actionPerformed(ActionEvent e)
  {
    periodPanel.setEnabled(periodRadioButton.isSelected());
    intervalPanel.setEnabled(!intervalRadioButton.isSelected());
  }

  private void intervalRadioButton_actionPerformed(ActionEvent e)
  {
    periodPanel.setEnabled(!intervalRadioButton.isSelected());
    intervalPanel.setEnabled(intervalRadioButton.isSelected());
  }

  private void publishButton_actionPerformed(ActionEvent e)
  {
    publishButton.setEnabled(false);
    boolean ok = true;
    String mess = "You requested:\n" +
                  (String)typeComboBox.getSelectedItem() + " in " + (String)languageComboBox.getSelectedItem() + "\n" +
                  "for ";
    if (gorPanel.getOption() == GenerateOrReusePanel.GENERATE)
    {
      if (periodRadioButton.isSelected())
      {
        if (periodPanel.getPeriod() == PeriodPanel.YEAR)
          mess += "one year\n";
        else if (periodPanel.getPeriod() == PeriodPanel.MONTH)
          mess += "one month\n";
        else if (periodPanel.getPeriod() == PeriodPanel.DAY)
          mess += "one day\n";
      }
      else if (intervalRadioButton.isSelected())
      {
        mess += "a period from ";
        mess += (SDF.format(intervalPanel.getFromDate()));
        mess += " to ";
        mess += (SDF.format(intervalPanel.getToDate()));
        mess += "\n";
      }
    }
    else
    { // Existing file
      String dataFile = filePanel.getFileName();
      final File f = new File(dataFile);
      if (f.exists())
      {
        final AlmanacHandler ah = new AlmanacHandler();
        try
        {
          SAXParserFactory factory = SAXParserFactory.newInstance();
          SAXParser saxParser = factory.newSAXParser();
          
          InputSource is = new InputSource(new FileInputStream(f));
          is.setEncoding("ISO-8859-1");
//        TimeZone.setDefault(TimeZone.getTimeZone("127")); // for UTC display

          progressBar.setIndeterminate(true);
          progressBar.setStringPainted(true);
          progressBar.setEnabled(true);
          progressBar.setString("Parsing...");    
          
          saxParser.parse(is, ah);       
          
          progressBar.setIndeterminate(false);
          progressBar.setStringPainted(true);
          progressBar.setEnabled(false);
          progressBar.setString("Ready");
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
          mess = "Parsing file [" + dataFile + "] :\n";
          mess += ex.toString();
          mess += "\n\nMake sure you picked the right XML file.";
          JOptionPane.showMessageDialog(this, mess, "Reusing Data", JOptionPane.ERROR_MESSAGE);
        }
        
        mess += ("an existing data file that contains data for " + Integer.toString(ah.getNbDay()) + " day(s).");
        mess += "\n";
        if (ah.getNbDay() == 1)
        {
          mess += ("- " + SDF_UTC.format(ah.getFirstDate().getTime())); 
        }
        else
        {
          mess += ("From " + SDF_UTC.format(ah.getFirstDate().getTime())); 
          mess += "\n";
          mess += ("To   " + SDF_UTC.format(ah.getLastDate().getTime())); 
        }            
      }
      else
      {
        // File does not exist
        ok = false;
        mess = "Requested file [" + dataFile + "] not found";
        JOptionPane.showMessageDialog(this, mess, "File to reuse not found", JOptionPane.ERROR_MESSAGE);
      }
    }
    if (ok)
    {
      if (gorPanel.getOption() == GenerateOrReusePanel.GENERATE)
      {
        String fileRadical = filePanel.getFileName();
        if (fileRadical.trim().length() == 0)
          fileRadical = System.getProperty("user.dir") + File.separator + "data";
        else
          fileRadical = removeExtension(fileRadical);
        
        mess += ("into " + fileRadical + " (xml and pdf)\n");
      }
      
      mess += "\nDo we proceed?";
      int response = JOptionPane.showConfirmDialog(this, mess, "Almanac Publisher", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
      if (response == JOptionPane.OK_OPTION)
      {
        String type = "";
        String fileRadical = filePanel.getFileName();
        if (fileRadical.trim().length() == 0)
          fileRadical = System.getProperty("user.dir") + File.separator + "data";
        else
          fileRadical = removeExtension(fileRadical);
        
        if (gorPanel.getOption() == GenerateOrReusePanel.GENERATE)
        {
          List<String> prms = new ArrayList<String>(2);
          if (intervalRadioButton.isSelected())
          {
            type = "from-to";
            Date from = intervalPanel.getFromDate();
            Date to   = intervalPanel.getToDate();
            Calendar calFrom = Calendar.getInstance();
            Calendar calTo   = Calendar.getInstance();
            calFrom.setTime(from);
            calTo.setTime(to);
            prms.add("-from-year");
            prms.add(Integer.toString(calFrom.get(Calendar.YEAR)));
            prms.add("-from-month");
            prms.add(Integer.toString(calFrom.get(Calendar.MONTH) + 1));
            prms.add("-from-day");
            prms.add(Integer.toString(calFrom.get(Calendar.DAY_OF_MONTH)));
            prms.add("-to-year");
            prms.add(Integer.toString(calTo.get(Calendar.YEAR)));
            prms.add("-to-month");
            prms.add(Integer.toString(calTo.get(Calendar.MONTH) + 1));
            prms.add("-to-day");
            prms.add(Integer.toString(calTo.get(Calendar.DAY_OF_MONTH)));
          }
          else
          {
            type = "continuous";              
            prms.add("-year");
            prms.add(Integer.toString(periodPanel.getYear()));
            if (periodPanel.getPeriod() == PeriodPanel.MONTH ||
                periodPanel.getPeriod() == PeriodPanel.DAY)
            {
              prms.add("-month");
              prms.add(Integer.toString(periodPanel.getMonth()));
            }
            if (periodPanel.getPeriod() == PeriodPanel.DAY)
            {
              prms.add("-day");
              prms.add(Integer.toString(periodPanel.getDay()));
            }
          }
          // 1 - Generate Data
          prms.add("-out"); 
          prms.add(fileRadical + ".xml");
          prms.add("-type");
          prms.add(type);
          System.setProperty("deltaT", deltaTFormattedTextField.getText());
          String[] args = new String[prms.size()];
          args = prms.toArray(args);
          for (int i=0; i<args.length; i++)
            System.out.println(args[i]);
          
          progressBar.setIndeterminate(true);
          progressBar.setEnabled(true);
          progressBar.setString("Calculating...");
          
          publishButton.setEnabled(false);
          publishButton.setText("Calculating...");
          
          AlmanacComputer.main(args);
          System.out.println("Calculation Completed...");
        }
        progressBar.setString("Publishing...");
        publishButton.setText("Publishing...");
        // 2 - Transform
        System.out.println("OS:" + System.getProperty("os.name"));
        if (typeComboBox.getSelectedItem().equals("Almanac"))
        {
          // Call publishalmanac.bat FR|EN true|false data.xml data.pdf
          try
          {
            String lang = "EN";
            if (languageComboBox.getSelectedItem().equals("French"))
              lang = "FR";
            String cmd = "cmd /k start pub" + File.separator + "publishalmanac.bat " + lang + " " + "false" + " \"" + fileRadical + ".xml\"" +  " \"" + fileRadical + ".pdf\"";
            if (System.getProperty("os.name").indexOf("Linux") > -1)
              cmd = "." + File.separator + "pub" + File.separator + "publishalmanac " + lang + " " + "false" + " " + fileRadical + ".xml" +  " " + fileRadical + ".pdf";
            System.out.println("Command:" + cmd);
            Process p = Runtime.getRuntime().exec(cmd);
            afterExec(p);
          }
          catch (Exception ex)
          {
            ex.printStackTrace();
          }
        }
        else if (typeComboBox.getSelectedItem().equals("Almanac with Stars"))
        {
          // Call publishalmanac.bat FR|EN true|false data.xml data.pdf
          try
          {
            String lang = "EN";
            if (languageComboBox.getSelectedItem().equals("French"))
              lang = "FR";
            String cmd = "cmd /k start pub" + File.separator + "publishalmanac.bat " + lang + " " + "true" + " \"" + fileRadical + ".xml\"" +  " \"" + fileRadical + ".pdf\"";
            if (System.getProperty("os.name").indexOf("Linux") > -1)
              cmd = "." + File.separator + "pub" + File.separator + "publishalmanac " + lang + " " + "true" + " " + fileRadical + ".xml" +  " " + fileRadical + ".pdf";
            System.out.println("Command:" + cmd);
            Process p = Runtime.getRuntime().exec(cmd);
            afterExec(p);
          }
          catch (Exception ex)
          {
            ex.printStackTrace();
          }
        }
        else
        {
          // Call publishalmanac.bat FR|EN data.xml data.pdf
          try
          {
            String lang = "EN";
            if (languageComboBox.getSelectedItem().equals("French"))
              lang = "FR";
            String cmd = "cmd /k start pub" + File.separator + "publishlunar.bat " + lang + " \"" + fileRadical + ".xml\"" +  " \"" + fileRadical + ".pdf\"";
            if (System.getProperty("os.name").indexOf("Linux") > -1)
              cmd = "." + File.separator + "pub" + File.separator + "publishlunar " + lang + " " + fileRadical + ".xml" +  " " + fileRadical + ".pdf";
            System.out.println("Command:" + cmd);
            Process p = Runtime.getRuntime().exec(cmd);
            afterExec(p);
          }
          catch (Exception ex)
          {
            ex.printStackTrace();
          }
        }
      }
    }
    progressBar.setIndeterminate(false);
    progressBar.setEnabled(false);
    progressBar.setString("Ready");
    publishButton.setEnabled(true);
    publishButton.setText("Publish");
  }
  
  private void afterExec(Process p) throws Exception 
  {
    if (true)
    {
 //   try { p.waitFor(); } catch (InterruptedException ie) {}
      BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
      BufferedReader es = new BufferedReader(new InputStreamReader(p.getErrorStream()));    
      
      String line = "";
      System.out.println("=== Output ===");
      boolean loop = true;
      while (loop && is.ready())
      {
        line = is.readLine();
        if (line == null)
          loop = false;
//      else if (line.trim().length() == 0)
//        loop = false;
        else
          System.out.println(line);
      }
      line = "";
      loop = true;
      System.out.println("=== Error ===");
      while (loop && es.ready())
      {
        line = es.readLine();
        if (line == null)
          loop = false;
//      else if (line.trim().length() == 0)
//        loop = false;
        else
          System.out.println(line);
      }
//    is.close();
//    es.close();
      System.out.println("==============");
    }
  }
  
  private static String removeExtension(String str)
  {
    String s = str;
    if (s.indexOf(File.separator) > -1)
    {
      String fr = s.substring(s.lastIndexOf(File.separator));
      if (fr.indexOf(".") > -1 && (fr.toLowerCase().endsWith(".pdf") || fr.toLowerCase().endsWith(".xml")))
      {
        int extLen = fr.length() - fr.lastIndexOf(".");
        s = s.substring(0, s.length() - extLen);
      }
    }
    return s; 
  }
  
  public static void openInBrowser(String page) throws Exception
  {
    String os = System.getProperty("os.name");
    if (os.indexOf("Windows") > -1)
    {
      String cmd = "";
      if (page.indexOf(" ") != -1)
        cmd = "cmd /k start \"" + page + "\"";
      else
        cmd = "cmd /k start " + page + "";
      System.out.println("Command:" + cmd);
      Runtime.getRuntime().exec(cmd); // Can contain blanks...
    }
    else if (os.indexOf("Linux") > -1) // Assuming htmlview
      Runtime.getRuntime().exec("htmlview " + page);
    else
    {
      throw new RuntimeException("OS [" + os + "] not supported yet");
    }
  }
  
  public void generateData(boolean b)
  {
    periodPanel.setEnabled(b && periodRadioButton.isSelected());
    periodRadioButton.setEnabled(b);
    intervalPanel.setEnabled(b && intervalRadioButton.isSelected());
    intervalRadioButton.setEnabled(b);
  }  

  public static class AlmanacHandler extends DefaultHandler
  {
    
    public AlmanacHandler()
    { 
    }

    private GregorianCalendar firstDate = null;
    private GregorianCalendar lastDate  = null;
    
    private int nbDay = 0;
    
    private int year  = 0;
    private int month = 0;
    private int day   = 0;
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
      throws SAXException
    {
  //  super.startElement(uri, localName, qName, attributes);
      if (uri.equals("urn:nautical-almanac") && qName.equals("day"))
      {
        day = Integer.parseInt(attributes.getValue("", "value"));
        nbDay++;
      }
      if (uri.equals("urn:nautical-almanac") && qName.equals("month"))
      {
        month = Integer.parseInt(attributes.getValue("", "value"));
      }
      if (uri.equals("urn:nautical-almanac") && qName.equals("year"))
      {
        year = Integer.parseInt(attributes.getValue("", "value"));
      }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
      throws SAXException
    {
      super.endElement(uri, localName, qName);
      if (uri.equals("urn:nautical-almanac") && qName.equals("day"))
      {
        GregorianCalendar gc = new GregorianCalendar(year, month - 1, day);
        if (firstDate == null)
          firstDate = gc;
        else
          lastDate = gc;
      }
    }

    public GregorianCalendar getFirstDate()
    {
      return firstDate;
    }

    public GregorianCalendar getLastDate()
    {
      return lastDate;
    }

    public int getNbDay()
    {
      return nbDay;
    }
  }
}

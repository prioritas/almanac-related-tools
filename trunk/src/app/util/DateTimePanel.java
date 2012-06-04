package app.util;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.Insets;

import java.awt.event.FocusAdapter;

import java.awt.event.FocusEvent;

import java.text.DecimalFormat;

import java.util.Calendar;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

import util.SwingUtil;

public class DateTimePanel
  extends JPanel
{
  private final static MonthObject[] months = new MonthObject[] 
    {
      new MonthObject( 1, "Jan"),                                                
      new MonthObject( 2, "Feb"),
      new MonthObject( 3, "Mar"),
      new MonthObject( 4, "Apr"),
      new MonthObject( 5, "May"),
      new MonthObject( 6, "Jun"),
      new MonthObject( 7, "Jul"),
      new MonthObject( 8, "Aug"),
      new MonthObject( 9, "Sep"),
      new MonthObject(10, "Oct"),
      new MonthObject(11, "Nov"),
      new MonthObject(12, "Dec")
    };
  
  private GridBagLayout gridBagLayout = new GridBagLayout();
  private JLabel yearLabel = new JLabel();
  private JFormattedTextField yearTextField = new JFormattedTextField(new DecimalFormat("0000"));
  private JLabel monthLabel = new JLabel();
  private JComboBox monthComboBox = new JComboBox();
  private JLabel dayLabel = new JLabel();
  private JFormattedTextField dayTextField = new JFormattedTextField(new DecimalFormat("00"));
  private JLabel hmsLabel = new JLabel();
  private JFormattedTextField hourTextField = new JFormattedTextField(new DecimalFormat("00"));
  private JLabel sepOneLabel = new JLabel();
  private JFormattedTextField minuteTextField = new JFormattedTextField(new DecimalFormat("00"));
  private JLabel sepTwoLabel = new JLabel();
  private JFormattedTextField secondTextField = new JFormattedTextField(new DecimalFormat("00"));
  private JLabel deltaTLabel = new JLabel();
  private JFormattedTextField deltaTTextField = new JFormattedTextField(new DecimalFormat("00.000"));

  public DateTimePanel()
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
    this.setSize(new Dimension(532, 192));
    yearLabel.setText("Year:");
    yearTextField.setPreferredSize(new Dimension(50, 20));
    yearTextField.setHorizontalAlignment(JTextField.CENTER);
    yearTextField.setText("2009");
    yearTextField.addFocusListener(new FocusAdapter()
      {
        public void focusGained(FocusEvent e)
        {
          yearTextField_focusGained(e);
        }
      });
    monthLabel.setText("Month:");
    monthComboBox.setMinimumSize(new Dimension(50, 20));
    monthComboBox.setSize(new Dimension(50, 20));
    monthComboBox.setPreferredSize(new Dimension(50, 20));
    monthComboBox.removeAllItems();
    for (int i=0; i<months.length; i++)
      monthComboBox.addItem(months[i]);
    dayLabel.setText("Day:");
    dayTextField.setText("12");
    dayTextField.setSize(new Dimension(30, 20));
    dayTextField.setPreferredSize(new Dimension(30, 20));
    dayTextField.setMinimumSize(new Dimension(30, 20));
    dayTextField.setHorizontalAlignment(JTextField.CENTER);
    dayTextField.addFocusListener(new FocusAdapter()
      {
        public void focusGained(FocusEvent e)
        {
          dayTextField_focusGained(e);
        }
      });
    hmsLabel.setText("HMS:");
    hourTextField.setPreferredSize(new Dimension(30, 20));
    hourTextField.setHorizontalAlignment(JTextField.CENTER);
    hourTextField.setText("00");
    hourTextField.addFocusListener(new FocusAdapter()
      {
        public void focusGained(FocusEvent e)
        {
          hourTextField_focusGained(e);
        }
      });
    sepOneLabel.setText(":");
    minuteTextField.setPreferredSize(new Dimension(30, 20));
    minuteTextField.setHorizontalAlignment(JTextField.CENTER);
    minuteTextField.setText("00");
    minuteTextField.addFocusListener(new FocusAdapter()
      {
        public void focusGained(FocusEvent e)
        {
          minuteTextField_focusGained(e);
        }
      });
    sepTwoLabel.setText(":");
    secondTextField.setPreferredSize(new Dimension(30, 20));
    secondTextField.setHorizontalAlignment(JTextField.CENTER);
    secondTextField.setText("00");
    secondTextField.addFocusListener(new FocusAdapter()
      {
        public void focusGained(FocusEvent e)
        {
          secondTextField_focusGained(e);
        }
      });
    deltaTLabel.setText("DeltaT:");
    deltaTTextField.setPreferredSize(new Dimension(50, 20));
    deltaTTextField.setHorizontalAlignment(JTextField.CENTER);
    deltaTTextField.setText(System.getProperty("deltaT", "65.584"));
    deltaTTextField.addFocusListener(new FocusAdapter()
      {
        public void focusGained(FocusEvent e)
        {
          deltaTTextField_focusGained(e);
        }
      });
    this.add(yearLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(yearTextField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(monthLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    this.add(monthComboBox, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(dayLabel, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    this.add(dayTextField, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(hmsLabel, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    this.add(hourTextField, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(sepOneLabel, new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(minuteTextField, new GridBagConstraints(9, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(sepTwoLabel, new GridBagConstraints(10, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(secondTextField, new GridBagConstraints(11, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(deltaTLabel, new GridBagConstraints(12, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    this.add(deltaTTextField, new GridBagConstraints(13, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
  }
  
  public void setDate(Date d)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(d);
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);    
    int hour = cal.get(Calendar.HOUR_OF_DAY);
    int min = cal.get(Calendar.MINUTE);
    int sec = cal.get(Calendar.SECOND);
    yearTextField.setText(((NumberFormatter)yearTextField.getFormatter()).getFormat().format(year));
    monthComboBox.setSelectedIndex(month);
    dayTextField.setText(((NumberFormatter)dayTextField.getFormatter()).getFormat().format(day));
    hourTextField.setText(((NumberFormatter)hourTextField.getFormatter()).getFormat().format(hour));
    minuteTextField.setText(((NumberFormatter)minuteTextField.getFormatter()).getFormat().format(min));
    secondTextField.setText(((NumberFormatter)secondTextField.getFormatter()).getFormat().format(sec));
  }
  
  public Date getDate()
  {
    Date d = null;
    try
    {
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.YEAR, Integer.parseInt(yearTextField.getText()));
      cal.set(Calendar.MONTH, monthComboBox.getSelectedIndex());
      cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayTextField.getText()));
      cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourTextField.getText()));
      cal.set(Calendar.MINUTE, Integer.parseInt(minuteTextField.getText()));
      cal.set(Calendar.SECOND, Integer.parseInt(secondTextField.getText()));
      d = cal.getTime();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return d;
  }
  
  public double getDeltaT()
  {
    double d = 0d;
    try
    {
      d = Double.parseDouble(deltaTTextField.getText());
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return d;
  }
  
  public void setDeltaT(double d)
  {
    Object o = deltaTTextField.getFormatter();
    if (o instanceof NumberFormatter)
    {
      NumberFormatter nf = (NumberFormatter)o;
      deltaTTextField.setText(nf.getFormat().format(d));
    }
    else
      System.out.println("Formatter is a :" + o.getClass().getName());
  }

  private void yearTextField_focusGained(FocusEvent e)
  {
    SwingUtil.selectField((JTextField)e.getSource());
  }

  private void dayTextField_focusGained(FocusEvent e)
  {
    SwingUtil.selectField((JTextField)e.getSource());
  }

  private void hourTextField_focusGained(FocusEvent e)
  {
    SwingUtil.selectField((JTextField)e.getSource());
  }

  private void minuteTextField_focusGained(FocusEvent e)
  {
    SwingUtil.selectField((JTextField)e.getSource());
  }

  private void secondTextField_focusGained(FocusEvent e)
  {
    SwingUtil.selectField((JTextField)e.getSource());
  }

  private void deltaTTextField_focusGained(FocusEvent e)
  {
    SwingUtil.selectField((JTextField)e.getSource());
  }

  public static class MonthObject
  {
    private int val;
    private String name;
    
    public MonthObject(int value, String name)
    {
      this.val = value;
      this.name = name;
    }
    public String toString() { return name; }
    public int getValue() { return val; }
  }
}

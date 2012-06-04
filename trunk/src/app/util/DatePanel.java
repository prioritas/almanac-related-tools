package app.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.text.DecimalFormat;

import java.util.Calendar;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

public class DatePanel
  extends JPanel
{
  private static final DateTimePanel.MonthObject[] months = new DateTimePanel.MonthObject[]
    { 
      new DateTimePanel.MonthObject( 1, "Jan"), 
      new DateTimePanel.MonthObject( 2, "Feb"), 
      new DateTimePanel.MonthObject( 3, "Mar"), 
      new DateTimePanel.MonthObject( 4, "Apr"), 
      new DateTimePanel.MonthObject( 5, "May"), 
      new DateTimePanel.MonthObject( 6, "Jun"), 
      new DateTimePanel.MonthObject( 7, "Jul"), 
      new DateTimePanel.MonthObject( 8, "Aug"), 
      new DateTimePanel.MonthObject( 9, "Sep"),
      new DateTimePanel.MonthObject(10, "Oct"), 
      new DateTimePanel.MonthObject(11, "Nov"), 
      new DateTimePanel.MonthObject(12, "Dec") 
    };

  private GridBagLayout gridBagLayout = new GridBagLayout();
  private JLabel yearLabel = new JLabel();
  private JFormattedTextField yearTextField = new JFormattedTextField(new DecimalFormat("0000"));
  private JLabel monthLabel = new JLabel();
  private JComboBox monthComboBox = new JComboBox();
  private JLabel dayLabel = new JLabel();
  private JFormattedTextField dayTextField = new JFormattedTextField(new DecimalFormat("00"));

  public DatePanel()
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
    monthLabel.setText("Month:");
    monthComboBox.setMinimumSize(new Dimension(50, 20));
    monthComboBox.setSize(new Dimension(50, 20));
    monthComboBox.setPreferredSize(new Dimension(50, 20));
    monthComboBox.removeAllItems();
    for (int i = 0; i < months.length; i++)
      monthComboBox.addItem(months[i]);
    dayLabel.setText("Day:");
    dayTextField.setText("12");
    dayTextField.setSize(new Dimension(30, 20));
    dayTextField.setPreferredSize(new Dimension(30, 20));
    dayTextField.setMinimumSize(new Dimension(30, 20));
    dayTextField.setHorizontalAlignment(JTextField.CENTER);
    this.add(yearLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(yearTextField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(monthLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    this.add(monthComboBox, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(dayLabel, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    this.add(dayTextField, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
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
    yearTextField.setText(((NumberFormatter) yearTextField.getFormatter()).getFormat().format(year));
    monthComboBox.setSelectedIndex(month);
    dayTextField.setText(((NumberFormatter) dayTextField.getFormatter()).getFormat().format(day));
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
      d = cal.getTime();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return d;
  }

  public void setEnabled(boolean b)
  {
    Component[] comp = this.getComponents();
    for (int i=0; i<comp.length; i++)
      comp[i].setEnabled(b);
  }  
}

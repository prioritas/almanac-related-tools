package app.almanac.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.text.DecimalFormat;

import java.util.Calendar;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class PeriodPanel
  extends JPanel
{
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private JRadioButton yearRadioButton = new JRadioButton();
  private JRadioButton monthRadioButton = new JRadioButton();
  private JRadioButton dayRadioButton = new JRadioButton();
  private JFormattedTextField yearFormattedTextField = new JFormattedTextField(new DecimalFormat("0000"));
  private JFormattedTextField dayFormattedTextField = new JFormattedTextField(new DecimalFormat("00"));
  private JComboBox monthComboBox = new JComboBox();
  private ButtonGroup bg = new ButtonGroup();

  public final static String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
  public final static int YEAR  = 0;
  public final static int MONTH = 1;
  public final static int DAY   = 2;

  public PeriodPanel()
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
    this.setLayout(gridBagLayout1);
    yearRadioButton.setText("For one Year");
    monthRadioButton.setText("For one Month");
    monthRadioButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          monthRadioButton_actionPerformed(e);
        }
      });
    dayRadioButton.setText("For one Day");
    dayRadioButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          dayRadioButton_actionPerformed(e);
        }
      });
    yearFormattedTextField.setPreferredSize(new Dimension(40, 20));
    yearFormattedTextField.setHorizontalAlignment(JTextField.CENTER);
    dayFormattedTextField.setHorizontalAlignment(JTextField.CENTER);
    dayFormattedTextField.setPreferredSize(new Dimension(40, 20));
    monthComboBox.setPreferredSize(new Dimension(55, 20));
    this.add(yearRadioButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(monthRadioButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(dayRadioButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(yearFormattedTextField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(dayFormattedTextField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(monthComboBox, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    monthComboBox.removeAllItems();
    for (int i=0; i<MONTHS.length; i++)
      monthComboBox.addItem(MONTHS[i]);
    bg.add(yearRadioButton);
    bg.add(monthRadioButton);
    bg.add(dayRadioButton);
    yearRadioButton.setSelected(true);
    monthComboBox.setEnabled(false);
    dayFormattedTextField.setEnabled(false);                                    
    yearRadioButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          yearRadioButton_actionPerformed(e);
        }
      });
  }

  private void yearRadioButton_actionPerformed(ActionEvent e)
  {
    tuneUp();
  }

  private void monthRadioButton_actionPerformed(ActionEvent e)
  {
    tuneUp();
  }

  private void dayRadioButton_actionPerformed(ActionEvent e)
  {
    tuneUp();
  }
  
  public void setEnabled(boolean b)
  {
    Component[] comp = this.getComponents();
    for (int i=0; i<comp.length; i++)
      comp[i].setEnabled(b);
    if (b) // tune up
      tuneUp();
  }
  
  private void tuneUp()
  {
    if (yearRadioButton.isSelected())
    {
      monthComboBox.setEnabled(false);
      dayFormattedTextField.setEnabled(false);
    }
    else if (monthRadioButton.isSelected())
    {
      monthComboBox.setEnabled(true);
      dayFormattedTextField.setEnabled(false);
    }
    else if (dayRadioButton.isSelected())
    {
      monthComboBox.setEnabled(true);
      dayFormattedTextField.setEnabled(true);
    }
  }
  
  public int getPeriod()
  {
    int p = -1;
    if (yearRadioButton.isSelected())
      p = YEAR;
    else if (monthRadioButton.isSelected())
      p = MONTH;
    else
      if (dayRadioButton.isSelected())
      p = DAY;
    return p;
  }  
  public int getDay()
  {
    int d = -1;
    try
    {
      d = Integer.parseInt(dayFormattedTextField.getText());
    }
    catch (Exception ex)
    {
      System.err.println("PediodPanel:" + ex.toString());
    }
    return d;
  }
  public int getMonth()
  {
    return monthComboBox.getSelectedIndex() + 1;
  }
  public int getYear()
  {
    int y = 0;
    try
    {
      y = Integer.parseInt(yearFormattedTextField.getText());
    }
    catch (Exception ex)
    {
      System.err.println("PeriodPanel (2) : " + ex.toString());
    }
    return y;
  }
  
  public void setDate(Date d)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(d);
    yearFormattedTextField.setText(Integer.toString(cal.get(Calendar.YEAR)));
    monthComboBox.setSelectedIndex(cal.get(Calendar.MONTH));
    dayFormattedTextField.setText(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
  }
}

package app.almanac.gui;

import app.util.DatePanel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.Insets;

import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class IntervalPanel
  extends JPanel
{
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private DatePanel fromPanel = new DatePanel();
  private DatePanel toPanel = new DatePanel();
  private JLabel fromLabel = new JLabel();
  private JLabel toLabel = new JLabel();

  public IntervalPanel()
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
    fromLabel.setText("From");
    toLabel.setText("To");
    this.add(fromPanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(toPanel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(fromLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0));
    this.add(toLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0));
  }
  
  public void initDate()
  {
    Date now = new Date();
    fromPanel.setDate(now);
    toPanel.setDate(now);
  }
  
  public Date getFromDate()
  {
    return fromPanel.getDate();
  }
  
  public Date getToDate()
  {
    return toPanel.getDate();
  }

  public void setEnabled(boolean b)
  {
    Component[] comp = this.getComponents();
    for (int i=0; i<comp.length; i++)
      comp[i].setEnabled(b);
  }  
}

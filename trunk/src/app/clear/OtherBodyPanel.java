package app.clear;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.text.DecimalFormat;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nauticalalmanac.Star;

public class OtherBodyPanel
  extends JPanel
{
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private JLabel altitudeLabel = new JLabel();
  private JFormattedTextField degreeTextField = new JFormattedTextField(new DecimalFormat("00"));
  private JLabel degreeSymbol = new JLabel();
  private JFormattedTextField minuteTextField = new JFormattedTextField(new DecimalFormat("00.00"));
  private JLabel minuteSymbol = new JLabel();
  private JLabel limbLabel = new JLabel();
  private JComboBox limbComboBox = new JComboBox();
  private JLabel otherBodyLabel = new JLabel();

  public static final int LOWER_LIMB = 0;
  public static final int UPPER_LIMB = 1;
  public static final int NO_LIMB    = 2;

  private static final String[] limbs = new String[]
    { "Lower", "Upper", "None" };
  private JComboBox bodyComboBox = new JComboBox();

  public OtherBodyPanel()
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
    altitudeLabel.setText("Sextant Altitude:");
    degreeTextField.setPreferredSize(new Dimension(30, 20));
    degreeTextField.setHorizontalAlignment(JTextField.CENTER);
    degreeTextField.setText("00");
    degreeSymbol.setText("°");
    minuteTextField.setPreferredSize(new Dimension(40, 20));
    minuteTextField.setHorizontalAlignment(JTextField.CENTER);
    minuteTextField.setText("0.0");
    minuteSymbol.setText("'");
    limbLabel.setText("Limb:");
    limbComboBox.removeAllItems();
    for (int i = 0; i < limbs.length; i++)
      limbComboBox.addItem(limbs[i]);
    otherBodyLabel.setText("Other Body:");
    otherBodyLabel.setFont(new Font("Tahoma", 1, 11));

    bodyComboBox.removeAllItems();
    bodyComboBox.addItem("The Sun");
    bodyComboBox.addItem("Venus");
    bodyComboBox.addItem("Mars");
    bodyComboBox.addItem("Jupiter");
    bodyComboBox.addItem("Saturn");
    // Stars
    for (int i=0; i<Star.getCatalog().length; i++)
      bodyComboBox.addItem(Star.getCatalog()[i].getStarName());
    
    this.add(altitudeLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(degreeTextField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(degreeSymbol, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 2, 0, 4), 0, 0));
    this.add(minuteTextField, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(minuteSymbol, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));
    this.add(limbLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(limbComboBox, new GridBagConstraints(1, 2, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(otherBodyLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 10, 0), 0, 0));
    this.add(bodyComboBox, new GridBagConstraints(1, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 10, 0), 0, 0));
  }

  public double getAltitude()
  {
    double a = 0d;
    try
    {
      double d = Double.parseDouble(degreeTextField.getText());
      double m = Double.parseDouble(minuteTextField.getText());
      a = d + (m / 60d);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return a;
  }

  public int getLimb()
  {
    return limbComboBox.getSelectedIndex();
  }
  
  public String getBodyName()
  {
    return bodyComboBox.getSelectedItem().toString();
  }
}

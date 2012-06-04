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

public class DistancePanel
  extends JPanel
{
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private JLabel altitudeLabel = new JLabel();
  private JFormattedTextField degreeTextField = new JFormattedTextField(new DecimalFormat("000"));
  private JLabel degreeSymbol = new JLabel();
  private JFormattedTextField minuteTextField = new JFormattedTextField(new DecimalFormat("00.00"));
  private JLabel minuteSymbol = new JLabel();
  private JLabel moonLimbLabel = new JLabel();
  private JComboBox moonLimbComboBox = new JComboBox();
  private JComboBox bodyLimbComboBox = new JComboBox();
  private JLabel distanceLabel = new JLabel();

  public static final int NEAR_LIMB = 0;
  public static final int FAR_LIMB = 1;

  private static final String[] limbs = new String[]
    { "Near", "Far" };
  private JLabel bodyLimbLabel = new JLabel();

  public DistancePanel()
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
    altitudeLabel.setText("Sextant Distance");
    degreeTextField.setPreferredSize(new Dimension(30, 20));
    degreeTextField.setHorizontalAlignment(JTextField.CENTER);
    degreeTextField.setText("00");
    degreeSymbol.setText("°");
    minuteTextField.setPreferredSize(new Dimension(40, 20));
    minuteTextField.setHorizontalAlignment(JTextField.CENTER);
    minuteTextField.setText("0.0");
    minuteSymbol.setText("'");
    moonLimbLabel.setText("Moon Limb");
    moonLimbComboBox.setPreferredSize(new Dimension(80, 20));
    moonLimbComboBox.removeAllItems();
    for (int i = 0; i < limbs.length; i++)
      moonLimbComboBox.addItem(limbs[i]);
    distanceLabel.setText("Lunar Distance");
    distanceLabel.setFont(new Font("Tahoma", 1, 11));
    bodyLimbLabel.setText("Body Limb");
    bodyLimbComboBox.setPreferredSize(new Dimension(80, 20));
    bodyLimbComboBox.removeAllItems();
    for (int i = 0; i < limbs.length; i++)
      bodyLimbComboBox.addItem(limbs[i]);
    this.add(altitudeLabel, new GridBagConstraints(1, 1, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(degreeTextField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(degreeSymbol, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 2, 0, 4), 0, 0));
    this.add(minuteTextField, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(minuteSymbol, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));
    this.add(moonLimbLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(moonLimbComboBox, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
    this.add(distanceLabel, new GridBagConstraints(0, 0, 6, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 10, 0), 0, 0));
    this.add(bodyLimbLabel, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(bodyLimbComboBox, new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
  }

  public double getDistance()
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

  public int getMoonLimb()
  {
    return moonLimbComboBox.getSelectedIndex();
  }
  public int getBodyLimb()
  {
    return bodyLimbComboBox.getSelectedIndex();
  }
}

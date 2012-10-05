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
import javax.swing.text.NumberFormatter;

public class MoonPanel
  extends JPanel
{
  private final static DecimalFormat DF22 = new DecimalFormat("00.00"); 

  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private JLabel altitudeLabel = new JLabel();
  private JFormattedTextField degreeTextField = new JFormattedTextField(new DecimalFormat("00"));
  private JLabel degreeSymbol = new JLabel();
  private JFormattedTextField minuteTextField = new JFormattedTextField(DF22);
  private JLabel minuteSymbol = new JLabel();
  private JLabel limbLabel = new JLabel();
  private JComboBox limbComboBox = new JComboBox();
  private JLabel moonLabel = new JLabel();
  
  public final static int LOWER_LIMB = 0;
  public final static int UPPER_LIMB = 1;
  
  private final static String[] limbs = new String[] { "Lower", "Upper" };

  public MoonPanel()
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
    minuteTextField.setText(((NumberFormatter)minuteTextField.getFormatter()).getFormat().format(0.0));
    minuteSymbol.setText("'");
    limbLabel.setText("Limb:");
    limbComboBox.removeAllItems();
    for (int i=0; i<limbs.length; i++)
      limbComboBox.addItem(limbs[i]);
    moonLabel.setText("Moon");
    moonLabel.setFont(new Font("Tahoma", 1, 11));
    this.add(altitudeLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(degreeTextField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(degreeSymbol, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 2, 0, 4), 0, 0));
    this.add(minuteTextField, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(minuteSymbol, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));
    this.add(limbLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(limbComboBox, new GridBagConstraints(1, 2, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(moonLabel, new GridBagConstraints(0, 0, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 10, 0), 0, 0));
  }
  
  public double getAltitude()
  {
    double a = 0d;
    try
    {
//    System.out.println("formatter is a " + minuteTextField.getFormatter().getClass().getName() );
      double d = DF22.parse(degreeTextField.getText()).doubleValue(); // Double.parseDouble(degreeTextField.getText());
      double m = DF22.parse(minuteTextField.getText()).doubleValue(); // Double.parseDouble(minuteTextField.getText());
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
}

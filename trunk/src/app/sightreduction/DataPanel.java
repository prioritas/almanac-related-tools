package app.sightreduction;

import app.util.EyeHeightPanel;

import calculation.SightReductionUtil;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.Insets;

import java.awt.event.FocusAdapter;

import java.awt.event.FocusEvent;

import java.text.DecimalFormat;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.JTextField;

import javax.swing.text.NumberFormatter;

import nauticalalmanac.Star;

import util.SwingUtil;

public class DataPanel
  extends JPanel
{
  private final static DecimalFormat DF22 = new DecimalFormat("00.00"); 

  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private JLabel sextantAltitudeLabel = new JLabel();
  private JFormattedTextField degreeFormattedTextField = new JFormattedTextField(new DecimalFormat("00"));
  private JLabel degreeLabel = new JLabel();
  private DecimalFormat df22 = DF22;
  private JFormattedTextField minuteFormattedTextField = new JFormattedTextField(df22);
  private JLabel minuteLabel = new JLabel();
  private JLabel bodyLabel = new JLabel();
  private JLabel limbLabel = new JLabel();
  private JComboBox bodyComboBox = new JComboBox();
  private JComboBox limbComboBox = new JComboBox();
  private EyeHeightPanel eyeHeightPanel = new EyeHeightPanel();

  public DataPanel()
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
    this.setSize(new Dimension(400, 126));
    sextantAltitudeLabel.setText("Sextant Altitude:");
    degreeFormattedTextField.setPreferredSize(new Dimension(40, 20));
    degreeFormattedTextField.setHorizontalAlignment(JTextField.CENTER);
    degreeFormattedTextField.setText("00");
    degreeFormattedTextField.addFocusListener(new FocusAdapter()
      {
        public void focusGained(FocusEvent e)
        {
          degreeFormattedTextField_focusGained(e);
        }
      });
    degreeLabel.setText(" ° ");
    minuteFormattedTextField.setPreferredSize(new Dimension(50, 20));
    minuteFormattedTextField.setHorizontalAlignment(JTextField.CENTER);
    minuteFormattedTextField.setText(((NumberFormatter)minuteFormattedTextField.getFormatter()).getFormat().format(0.0));
    minuteFormattedTextField.addFocusListener(new FocusAdapter()
      {
        public void focusGained(FocusEvent e)
        {
          minuteFormattedTextField_focusGained(e);
        }
      });
    minuteLabel.setText(" '");
    bodyLabel.setText("Body:");
    limbLabel.setText("Limb:");
    this.add(sextantAltitudeLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(degreeFormattedTextField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(degreeLabel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(minuteFormattedTextField, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(minuteLabel, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(bodyLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 2, 0), 0, 0));
    this.add(limbLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));
    this.add(bodyComboBox, new GridBagConstraints(1, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 2, 0), 0, 0));
    this.add(limbComboBox, new GridBagConstraints(1, 2, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 0, 0), 0, 0));
    this.add(eyeHeightPanel, new GridBagConstraints(0, 3, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
    
    // Combo boxes
    bodyComboBox.removeAllItems();
    bodyComboBox.addItem("The Sun");
    bodyComboBox.addItem("Moon");
    bodyComboBox.addItem("Venus");
    bodyComboBox.addItem("Mars");
    bodyComboBox.addItem("Jupiter");
    bodyComboBox.addItem("Saturn");
    for (int i=0; i<Star.getCatalog().length; i++)
      bodyComboBox.addItem(Star.getCatalog()[i].getStarName());
    
    limbComboBox.removeAllItems();
    limbComboBox.addItem("Lower Limb");
    limbComboBox.addItem("No Limb");
    limbComboBox.addItem("Upper Limb");
  }
  
  public double getSextantAltitude()
  {
    double d = 0;
    try
    {
      double deg = ((DecimalFormat)((NumberFormatter)degreeFormattedTextField.getFormatter()).getFormat()).parse(degreeFormattedTextField.getText()).doubleValue(); // Double.parseDouble(degreeFormattedTextField.getText());
      double min = ((DecimalFormat)((NumberFormatter)minuteFormattedTextField.getFormatter()).getFormat()).parse(minuteFormattedTextField.getText()).doubleValue(); // Double.parseDouble(minuteFormattedTextField.getText());
      d = deg + (min / 60d);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return d;
  }
  
  public void setSextantAltitude(double h)
  {
    int d = (int)h;
    degreeFormattedTextField.setText(Integer.toString(d));
    double m = Math.abs(h - d) * 60d;
    minuteFormattedTextField.setText(df22.format(m));
  }
  
  public String getBody()
  {
    return (String)bodyComboBox.getSelectedItem();
  }
  
  public final static int LOWER_LIMB = 0;
  public final static int NO_LIMB    = 1;
  public final static int UPPER_LIMB = 2;

  public int getLimb()
  {
    int l = -1;
    if (((String)limbComboBox.getSelectedItem()).equals("Lower Limb"))
      l = SightReductionUtil.LOWER_LIMB;
    else if (((String)limbComboBox.getSelectedItem()).equals("No Limb"))
      l = SightReductionUtil.NO_LIMB;
    else if (((String)limbComboBox.getSelectedItem()).equals("Upper Limb"))
      l = SightReductionUtil.UPPER_LIMB;
    return l;
  }
  
  public double getEyeHeightInMeters()
  {
    return eyeHeightPanel.getEyeHeightInMeters();
  }

  private void degreeFormattedTextField_focusGained(FocusEvent e)
  {
    SwingUtil.selectField((JTextField)e.getSource());
  }

  private void minuteFormattedTextField_focusGained(FocusEvent e)
  {
    SwingUtil.selectField((JTextField)e.getSource());
  }
}

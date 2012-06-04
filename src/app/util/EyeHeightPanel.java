package app.util;

import calculation.Util;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.Insets;

import java.awt.event.FocusAdapter;

import java.awt.event.FocusEvent;

import java.text.DecimalFormat;

import javax.swing.ButtonGroup;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import util.SwingUtil;

public class EyeHeightPanel
  extends JPanel
{
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private JLabel eyeHeightLabel = new JLabel();
  private JFormattedTextField eyeHeightTextField = new JFormattedTextField(new DecimalFormat("00.00"));
  private JRadioButton metersRadioButton = new JRadioButton();
  private JRadioButton feetRadioButton = new JRadioButton();
  private ButtonGroup group = new ButtonGroup();

  public EyeHeightPanel()
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
    eyeHeightLabel.setText("Eye Height:");
    eyeHeightTextField.setPreferredSize(new Dimension(40, 20));
    eyeHeightTextField.setText("01.80");
    eyeHeightTextField.setHorizontalAlignment(JTextField.CENTER);
    eyeHeightTextField.addFocusListener(new FocusAdapter()
      {
        public void focusGained(FocusEvent e)
        {
          eyeHeightTextField_focusGained(e);
        }
      });
    metersRadioButton.setText("meters");
    metersRadioButton.setSelected(true);
    feetRadioButton.setText("feet");
    group.add(metersRadioButton);
    group.add(feetRadioButton);
    this.add(eyeHeightLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(eyeHeightTextField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(metersRadioButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    this.add(feetRadioButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
  }
  
  public double getEyeHeightInMeters()
  {
    double h = 0d;
    try
    {
      h = Double.parseDouble(eyeHeightTextField.getText());
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    if (feetRadioButton.isSelected())
      h = Util.feetToMeters(h);
    return h;
  }

  private void eyeHeightTextField_focusGained(FocusEvent e)
  {
    SwingUtil.selectField(eyeHeightTextField);
  }
}

package app.almanac.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class GenerateOrReusePanel
  extends JPanel
{
  private AlmanacPublisherPanel parent = null;
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private JRadioButton generateRadioButton = new JRadioButton();
  private JRadioButton reuseRadioButton = new JRadioButton();
  private ButtonGroup group = new ButtonGroup();
  
  public final static int GENERATE = 0;
  public final static int REUSE    = 1;

  public GenerateOrReusePanel(AlmanacPublisherPanel parent)
  {
    this.parent = parent;
    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public int getOption()
  {
    if (generateRadioButton.isSelected())
      return GENERATE;
    else
      return REUSE;
  }

  private void jbInit()
    throws Exception
  {
    this.setLayout(gridBagLayout1);
    this.setSize(new Dimension(400, 115));
    generateRadioButton.setText("Generate new Data File");
    reuseRadioButton.setText("Reuse Data File");
    reuseRadioButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          reuseRadioButton_actionPerformed(e);
        }
      });
    group.add(generateRadioButton);
    group.add(reuseRadioButton);
    generateRadioButton.setSelected(true);
    generateRadioButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          generateRadioButton_actionPerformed(e);
        }
      });
    this.add(generateRadioButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(reuseRadioButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
  }

  private void generateRadioButton_actionPerformed(ActionEvent e)
  {
    parent.generateData(true);
  }

  private void reuseRadioButton_actionPerformed(ActionEvent e)
  {
    parent.generateData(false);
  }
}

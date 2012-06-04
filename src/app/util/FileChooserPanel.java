package app.util;

import coreutilities.Utilities;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import util.SwingUtil;

public class FileChooserPanel
  extends JPanel
{
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private JTextField fileNameTextField = new JTextField();
  private JButton browseButton = new JButton();

  public FileChooserPanel()
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
    fileNameTextField.setPreferredSize(new Dimension(150, 20));
    browseButton.setText("...");
    browseButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          browseButton_actionPerformed(e);
        }
      });
    this.add(fileNameTextField, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(browseButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
  }

  private void browseButton_actionPerformed(ActionEvent e)
  {
    String fName = SwingUtil.chooseFile(this, 
                                        JFileChooser.FILES_ONLY,
                                        null,
                                        "Data Files",
                                        ".",
                                        "Select", 
                                        "Output File");
    if (fName.trim().length() > 0)
    {
      fileNameTextField.setText(fName);
    }
  }
  
  public String getFileName()
  {
    return fileNameTextField.getText();
  }
}

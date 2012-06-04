package app.clear;

import java.awt.BorderLayout;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class OutputPanel
  extends JPanel
{
  private BorderLayout borderLayout = new BorderLayout();
  private JScrollPane jScrollPane = new JScrollPane();
  private JEditorPane jEditorPane = new JEditorPane();

  public OutputPanel()
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
    this.setLayout(borderLayout);
    jScrollPane.getViewport().add(jEditorPane, null);
    this.add(jScrollPane, BorderLayout.CENTER);
  }
  
  public String getText()
  {
    return jEditorPane.getText();
  }
  
  public void setText(String s)
  {
    jEditorPane.setText(s);
  }
}

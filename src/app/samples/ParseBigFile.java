package app.samples;

import java.io.File;

import oracle.xml.parser.v2.DOMParser;
import oracle.xml.parser.v2.NSResolver;
import oracle.xml.parser.v2.XMLDocument;

import org.w3c.dom.NodeList;

public class ParseBigFile
{
  public static void main(String[] args)
  {
    try
    {
      DOMParser parser = new DOMParser();
      long before = System.currentTimeMillis();
      parser.parse(new File("data.xmlx").toURI().toURL());
      long after = System.currentTimeMillis();
      System.out.println("Parsed in " + Long.toString(after - before) + " ms.");
      before = System.currentTimeMillis();
      XMLDocument doc = parser.getDocument();
      after = System.currentTimeMillis();
      System.out.println("Got document in " + Long.toString(after - before) + " ms.");
      
      final String ns = "urn:nautical-almanac";
      String xPathExpr = "//ns:day";
      before = System.currentTimeMillis();
      NodeList nl = doc.selectNodes(xPathExpr, new NSResolver()
                                    {
                                      public String resolveNamespacePrefix(String string)
                                      {
                                        return ns;
                                      }
                                    });
      after = System.currentTimeMillis();
      System.out.println("Found " + nl.getLength() + " node(s), in " + Long.toString(after - before) + " ms.");
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      System.out.println("Bye.");
    }
  }
}

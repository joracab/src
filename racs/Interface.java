package racs;

import javax.swing.UIManager;
import java.awt.*;

public class Interface {
  boolean packFrame = false;
  Frame frame;

  /*
   Constructor : generate the Frame.
  */
  public Interface() {
     frame = new Frame();

    //Validate frames that have preset sizes
    //Pack frames that have useful preferred size info, e.g. from their layout
    if (packFrame)
    {
      frame.pack();
    }
    else
    {
      frame.validate();
    }
    //Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    frame.setVisible(true);
  };

}; // end-of-class
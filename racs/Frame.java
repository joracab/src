package racs;

import java.lang.Double;

// interface libraries
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.table.TableModel.*;
import com.borland.jbcl.layout.*;


public class Frame extends JFrame {

  // listener object
  Listeners listen;
  // interface components
  JPanel contentPane;
  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu jMenuFile = new JMenu();
  JMenuItem jMenuFileExit = new JMenuItem();
  JMenu jMenuHelp = new JMenu();
  JMenuItem jMenuHelpAbout = new JMenuItem();
  XYLayout xYLayout1 = new XYLayout();
  JTabbedPane Loader = new JTabbedPane();
  JPanel Experimenter = new JPanel();
  XYLayout xYLayout2 = new XYLayout();
  JLabel Times = new JLabel();
  JLabel Fold = new JLabel();
  JTextField Text_Times = new JTextField();
  //JTextField Text_Fold = new JTextField();
  JRadioButton RB_Average = new JRadioButton();
  JRadioButton RB_Desviation = new JRadioButton();
  JButton Run = new JButton();
  JButton SaveAs = new JButton();
  JPanel Controller = new JPanel();
  XYLayout xYLayout3 = new XYLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  TitledBorder titledBorder1;
  JTextArea jTextArea1 = new JTextArea();
  JPanel Botonera = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  JButton B_Clear = new JButton();
  JTextField Text_Folds = new JTextField();
  JPanel Loaded_Files = new JPanel();
  XYLayout xYLayout4 = new XYLayout();
  JLabel jDataSet = new JLabel();
  JLabel jMetricSpace = new JLabel();
  JLabel jSamples = new JLabel();
  JTextField jTextField_DataSet = new JTextField();
  JTextField jTextField_MetricSpace = new JTextField();
  JTextField jTextField_Samples = new JTextField();
  JPanel jPanel2 = new JPanel();
  XYLayout xYLayout5 = new XYLayout();
  JPanel jPanel_FileInform1 = new JPanel();
  JMenuItem jMenuItem1 = new JMenuItem();
  JMenuItem jMenuOpenFile = new JMenuItem();
  JComboBox jComboBox_splitting = new JComboBox();
  JComboBox jComboBox_classify = new JComboBox();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  ButtonGroup buttonGroup1 = new ButtonGroup();
  JComboBox jComboBox_Algorithm = new JComboBox();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JTextField jTextField_num_children = new JTextField();
  JRadioButton jRadioButton_Trace = new JRadioButton();
  JPanel jPanel4 = new JPanel();
  XYLayout xYLayout7 = new XYLayout();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel6 = new JLabel();
  JMenuItem jMenuItem2 = new JMenuItem();
  JList jList1 = new JList();
  TitledBorder titledBorder2;
  JPanel jPanel_FileInform2 = new JPanel();
  TitledBorder titledBorder3;
  TitledBorder titledBorder4;
  XYLayout xYLayout8 = new XYLayout();
  BorderLayout borderLayout1 = new BorderLayout();
  JScrollPane jScrollPane_TableContainer = new JScrollPane();
  JScrollPane jScrollPane_TableContainer2 = new JScrollPane();
  TitledBorder titledBorder5;
  TitledBorder titledBorder6;
  //
  // dynamic swing components
  //
  JComboBox methods= new JComboBox();
  JTable table,table2;
  /*
   Constructor: launch the frame.
  */
  public Frame()
  {
    listen = new Listeners();
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  //Component initialization
  private void jbInit() throws Exception  {
    contentPane = (JPanel) this.getContentPane();
    titledBorder1 = new TitledBorder("Selecting Prototype Calculation Method");
    titledBorder2 = new TitledBorder("Class Distribution");
    titledBorder3 = new TitledBorder("");
    titledBorder4 = new TitledBorder("");
    titledBorder5 = new TitledBorder("");
    titledBorder6 = new TitledBorder("Prototype Computation Method");
    contentPane.setLayout(xYLayout1);
    this.setSize(new Dimension(719, 509));
    this.setTitle("Enviroment");
    jMenuFile.setText("File");
    jMenuFileExit.setText("Exit");
    jMenuFileExit.addActionListener(new Frame1_jMenuFileExit_ActionAdapter(this));
    jMenuHelp.setText("Help");
    jMenuHelpAbout.setText("About");
    jMenuHelpAbout.addActionListener(new Frame1_jMenuHelpAbout_ActionAdapter(this));
    Experimenter.setLayout(xYLayout2);
    Times.setFont(new java.awt.Font("SansSerif", 1, 12));
    Times.setText("Times");
    Fold.setFont(new java.awt.Font("SansSerif", 1, 12));
    Fold.setText("Folds");
    Text_Times.setForeground(Color.blue);
    Text_Times.setText("10");
    Text_Times.setHorizontalAlignment(SwingConstants.LEFT);
    Text_Folds.setForeground(Color.blue);
    Text_Folds.setText("10");
    RB_Average.setFont(new java.awt.Font("SansSerif", 1, 12));
    RB_Average.setText("Average/Fold");
    RB_Average.setVerticalAlignment(SwingConstants.CENTER);
    RB_Average.setVerticalTextPosition(SwingConstants.CENTER);
    //RB_Average.addActionListener(new Frame_RB_Average_actionAdapter(this));
    RB_Desviation.setFont(new java.awt.Font("SansSerif", 1, 12));
    RB_Desviation.setText("Mistakes/ Fold");
    Run.setFont(new java.awt.Font("Serif", 1, 12));
    Run.setBorder(BorderFactory.createRaisedBevelBorder());
    Run.setMargin(new Insets(2, 12, 2, 12));
    Run.setText("Run");
    //Run.addMouseListener(new Frame_Run_mouseAdapter(this));
    Run.addActionListener(new Frame1_Run_actionAdapter(this));
    SaveAs.setFont(new java.awt.Font("Serif", 1, 12));
    SaveAs.setBorder(BorderFactory.createRaisedBevelBorder());
    SaveAs.setMargin(new Insets(2, 12, 2, 12));
    SaveAs.setText("Save As");
    SaveAs.addActionListener(new Frame_SaveAs_actionAdapter(this));
    Controller.setLayout(xYLayout3);
    Controller.setBorder(BorderFactory.createRaisedBevelBorder());
    Botonera.setBorder(BorderFactory.createRaisedBevelBorder());
    Botonera.setLayout(gridLayout1);
    B_Clear.setFont(new java.awt.Font("Serif", 1, 12));
    B_Clear.setBorder(BorderFactory.createRaisedBevelBorder());
    B_Clear.setMargin(new Insets(2, 12, 2, 12));
    B_Clear.setText("Clear");
    B_Clear.addActionListener(new Frame_B_Clear_actionAdapter(this));
    //Text_Folds.setText("0");
    Loaded_Files.setLayout(xYLayout4);
    jDataSet.setFont(new java.awt.Font("SansSerif", 1, 12));
    jDataSet.setText("Data set");
    jMetricSpace.setFont(new java.awt.Font("SansSerif", 1, 12));
    jMetricSpace.setText("Metric Space");
    jSamples.setFont(new java.awt.Font("SansSerif", 1, 12));
    jSamples.setText("Samples");
    jTextField_DataSet.setFont(new java.awt.Font("MS Sans Serif", 0, 11));
    jTextField_DataSet.setForeground(Color.blue);
    jTextField_DataSet.setEditable(false);
    jTextField_DataSet.setText("<path>");
    //jTextField_DataSet.addActionListener(new Frame_jTextField_DataSet_actionAdapter(this));
    jTextField_MetricSpace.setForeground(Color.blue);
    jTextField_MetricSpace.setEditable(false);
    jTextField_MetricSpace.setText("<path>");
    jTextField_Samples.setBackground(SystemColor.inactiveCaptionText);
    jTextField_Samples.setForeground(Color.blue);
    jTextField_Samples.setEditable(false);
    jTextField_Samples.setText("0");
    jPanel2.setBorder(BorderFactory.createRaisedBevelBorder());
    jPanel2.setLayout(xYLayout5);
    jPanel_FileInform1.setBorder(BorderFactory.createRaisedBevelBorder());
    jPanel_FileInform1.setLayout(borderLayout1);
    jMenuOpenFile.setText("Open file");
    jMenuOpenFile.addActionListener(new Frame_jMenuOpenFile_actionAdapter(this));
    jMenuItem1.setText("Close file");
    jLabel1.setFont(new java.awt.Font("SansSerif", 1, 12));
    jLabel1.setText("Splitting criteria");
    jLabel2.setFont(new java.awt.Font("SansSerif", 1, 12));
    jLabel2.setText("Classify by");
    //jComboBox_classify.addActionListener(new Frame_jComboBox_classify_actionAdapter(this));
    //jComboBox_splitting.addActionListener(new Frame_jComboBox_splitting_actionAdapter(this));
    jComboBox_splitting.setForeground(Color.blue);
    jComboBox_classify.setForeground(Color.blue);
    Experimenter.setForeground(Color.blue);
    jLabel4.setFont(new java.awt.Font("SansSerif", 1, 12));
    jLabel4.setText("Algorithm");
    jLabel3.setFont(new java.awt.Font("SansSerif", 1, 12));
    jLabel3.setText("Children");
    jTextField_num_children.setForeground(Color.blue);
    jTextField_num_children.setText("2");
    jRadioButton_Trace.setFont(new java.awt.Font("SansSerif", 1, 12));
    jRadioButton_Trace.setText("Trace");
    jPanel4.setBorder(BorderFactory.createRaisedBevelBorder());
    jPanel4.setLayout(xYLayout7);
    jLabel5.setFont(new java.awt.Font("SansSerif", 1, 12));
    jLabel5.setText("DBDT SETTINGS");
    jLabel6.setFont(new java.awt.Font("SansSerif", 1, 12));
    jLabel6.setForeground(Color.black);
    jLabel6.setText("EXPERIMENT SETTINGS");
    jComboBox_Algorithm.setForeground(Color.blue);
    jComboBox_Algorithm.addActionListener(new
        Frame_jComboBox_Algorithm_actionAdapter(this));
    //jComboBox_Algorithm.addItemListener(new Frame_jComboBox_Algorithm_itemAdapter(this));
    jMenuItem2.setText("Save Ms file from arff");
    jMenuItem2.addActionListener(new Frame_jMenuItem2_actionAdapter(this));
    jPanel_FileInform2.setBorder(titledBorder4);
    jPanel_FileInform2.setLayout(xYLayout8);
    jScrollPane_TableContainer.getViewport().setBackground(SystemColor.inactiveCaptionText);
    jScrollPane_TableContainer.setForeground(Color.blue);
    jScrollPane_TableContainer.setBorder(titledBorder2);
    jScrollPane_TableContainer2.getViewport().setBackground(SystemColor.inactiveCaptionText);
    jScrollPane_TableContainer2.setForeground(Color.blue);
    jScrollPane_TableContainer2.setBorder(titledBorder6);
    RB_AUC.setFont(new java.awt.Font("SansSerif", 1, 12));
    RB_AUC.setText("AUC");
    RB_Acc.setText("Accuracy");
    RB_Acc.setFont(new java.awt.Font("SansSerif", 1, 12));
    jLabel7.setText("KNN Neighbors");

    text_neighborhs.setText("0");
    jPanel_FileInform2.add(jTextField_Samples,  new XYConstraints(70, 9, 52, -1));
    jPanel_FileInform2.add(jSamples, new XYConstraints(7, 11, -1, -1));
    jPanel_FileInform2.add(jScrollPane_TableContainer,      new XYConstraints(7, 53, 274, 176));
    jPanel_FileInform2.add(jScrollPane_TableContainer2,                new XYConstraints(332, 58, 272, 167));
    jPanel2.add(jDataSet,   new XYConstraints(8, 7, -1, -1));
    jPanel2.add(jTextField_MetricSpace,          new XYConstraints(90, 41, 540, -1));
    jPanel2.add(jMetricSpace, new XYConstraints(7, 43, -1, -1));
    jPanel2.add(jTextField_DataSet,   new XYConstraints(90, 5, 539, -1));
    Loader.add(Experimenter,  "Experimenter");
    Loaded_Files.add(jPanel_FileInform1,         new XYConstraints(10, 120, 659, 249));
    Loaded_Files.add(jPanel2,      new XYConstraints(11, 17, 661, 73));
    jMenuFile.add(jMenuOpenFile);
    jMenuFile.add(jMenuItem2);
    jMenuFile.add(jMenuItem1);
    jMenuFile.add(jMenuFileExit);
    jMenuHelp.add(jMenuHelpAbout);
    jMenuBar1.add(jMenuFile);
    jMenuBar1.add(jMenuHelp);
    contentPane.add(Loader,new XYConstraints(8, 15, 697, 453));
    //Controller.add(Text_Folds,       new XYConstraints(57, 56, 20, 14));
    Controller.add(jLabel4,  new XYConstraints(5, 141, -1, -1));
    Controller.add(RB_Desviation,     new XYConstraints(5, 87, -1, -1));
    Controller.add(RB_Average,   new XYConstraints(5, 61, 102, -1));
    Controller.add(jRadioButton_Trace,   new XYConstraints(5, 113, -1, -1));
    Controller.add(Times, new XYConstraints(5, 35, -1, -1));
    Controller.add(Text_Folds, new XYConstraints(138, 33, 20, 21));
    Controller.add(Fold, new XYConstraints(91, 35, -1, -1));
    Controller.add(Text_Times, new XYConstraints(52, 33, 20, 21));
    Controller.add(jComboBox_Algorithm,       new XYConstraints(71, 140, 109, 17));
    Controller.add(jLabel6,  new XYConstraints(46, 3, -1, -1));
    Controller.add(jList1,  new XYConstraints(178, 84, -1, -1));
    Controller.add(text_neighborhs, new XYConstraints(100, 159, -1, -1));
    Controller.add(jLabel7, new XYConstraints(8, 163,-1, -1));

    Controller.add(RB_AUC, new XYConstraints(134, 61, -1, -1));
    Controller.add(RB_Acc,   new XYConstraints(134, 87, -1, -1));
    Experimenter.add(Botonera, new XYConstraints(28, 353, 188, 37));
    Experimenter.add(jPanel4,       new XYConstraints(3, 216, 246, 126));
    Experimenter.add(jScrollPane1, new XYConstraints(253, 8, 430, 397));
    Experimenter.add(Controller, new XYConstraints( -1, 10, 247, 203));
    jScrollPane1.getViewport().add(jTextArea1, null);
    Botonera.add(Run, null);
    Botonera.add(SaveAs, null);
    Botonera.add(B_Clear, null);
    Loader.add(Loaded_Files,  "Loaded Files");
    // EXTRA CODE
    jComboBox_classify.addItem("Proximity");
    jComboBox_classify.addItem("Density");
    //
    // EXTRA CODE
    jComboBox_splitting.addItem("Information Gain");
    jComboBox_splitting.addItem("Gain Ratio");
    //
    // EXTRA CODE
    jComboBox_Algorithm.addItem("DBDT");
    jComboBox_Algorithm.addItem("ID3");
    jComboBox_Algorithm.addItem("C4.5");
    jComboBox_Algorithm.addItem("KNN w-pound");
    jComboBox_Algorithm.addItem("KNN Metric");
    //
    // EXTRA CODE
    methods.addItem("Median");
    methods.addItem("Center");
    methods.setSelectedIndex(0);
    methods.setEditable(false);
    //methods.addItemListener(new Frame_jComboBox_Methods_itemAdapter(this));
    //
    // EXTRA CODE
    //table2.getModel().addTableModelListener(new Frame_jTable_Table2_Listener(this));
    //
    jPanel4.add(jLabel5,  new XYConstraints(72, 2, 98, -1));
    jPanel4.add(jComboBox_classify,  new XYConstraints(98, 28, 133, 16));
    jPanel4.add(jLabel2, new XYConstraints(5, 28, 73, 19));
    jPanel4.add(jTextField_num_children,  new XYConstraints(97, 90, 21, -1));
    jPanel4.add(jLabel3, new XYConstraints(4, 92, -1, -1));
    jPanel4.add(jLabel1, new XYConstraints(4, 63, -1, 17));
    jPanel4.add(jComboBox_splitting,  new XYConstraints(97, 62, 133, 18));
    jPanel_FileInform1.add(jPanel_FileInform2, BorderLayout.CENTER);
    Loader.add(Data_Base,   "Data_Base");
    Loader.add(Networking,   "Networking");
    this.setJMenuBar(jMenuBar1);
  }
  //File | Exit action performed
  public void jMenuFileExit_actionPerformed(ActionEvent e) {
    System.exit(0);
  }
  //Help | About action performed
  public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
  }
  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      jMenuFileExit_actionPerformed(null);
    }
  }


  /*
    Press over the Run_button. It launchs the experiment.
  */
  void Run_actionPerformed(ActionEvent e)
  {
    int times, folds, children, nb;
    boolean  average,desviation,trace, AUC, Acc;

    times = Integer.valueOf(Text_Times.getText()).intValue();
    folds = Integer.valueOf(Text_Folds.getText()).intValue();
    children = Integer.valueOf(jTextField_num_children.getText()).intValue();
    average=RB_Average.isSelected();
    desviation=RB_Desviation.isSelected();
    trace = jRadioButton_Trace.isSelected();
    AUC = RB_AUC.isSelected();
    Acc = RB_Acc.isSelected();
    // knn neighbor
    nb=  Integer.valueOf( text_neighborhs.getText()).intValue();
    listen.Experimenter_RunButton(times, folds, average, desviation, children, trace, AUC, Acc,nb,
                                  jComboBox_classify, jComboBox_splitting, jComboBox_Algorithm, jTextArea1, this);
  };

  /*
    Mouse click action over the Run_button.
  */
  /*void Run_mouseClicked(MouseEvent e)
  {

  };*/

  void B_Clear_actionPerformed(ActionEvent e)
  {
    listen.Experimenter_ClearButton(jTextArea1);
  };


  /*
   Press over the "Save As" button. It saves the content of the TextPane.
  */
  void SaveAs_actionPerformed(ActionEvent e)
  {
    listen.Experimenter_SaveAsButton(jTextArea1);
  };


  /*
    Menu option (file open) selected.
  */
  void jMenuOpenFile_actionPerformed(ActionEvent e)
  {
     listen.Menu_OpenFile(this, jTextField_DataSet, jTextField_MetricSpace, jTextField_Samples, jScrollPane_TableContainer, jScrollPane_TableContainer2,methods,table,table2,new Frame_jTable_Table2_Listener(this));
  };

  /*void jTextField_DataSet_actionPerformed(ActionEvent e) {

  }

  void jComboBox_classify_actionPerformed(ActionEvent e) {

  }

  void jComboBox_splitting_actionPerformed(ActionEvent e) {

  }

  void RB_Average_actionPerformed(ActionEvent e) {

  }*/

  /*
    MENU OPTION: Save Ms from arff file.
    ACTION: Save a metric space  from a *.arff file.
  */
  void jMenuItem2_actionPerformed(ActionEvent e)
  {
    listen.Menu_SaveMs(this);
  }

  /*void jComboBox_Algorithm_itemStateChanged(ItemEvent e) {

  }*/

  /*
  */

 /* void jComboBox_Methods_itemStateChanged(ItemEvent e)
  {
    System.out.println("hola hoita hoa ");
    listen.Loaded_Files_ComboBox_Methods(methods);
  };
  */


  /*
     One cell (table2), which referrers to the compute center method, has changed.
  */
  void jTable_table2_tableChanged(TableModelEvent e)
  {
    int row,column;
    TableModel table2Model;
    String new_value;
    row = e.getLastRow();
    column = e.getColumn();
    System.out.println("fila "+row+" columna "+column);
    table2Model=(TableModel) e.getSource();
    new_value= (String)table2Model.getValueAt(row,column);
    System.out.println("valor "+new_value);
    listen.Loaded_Files_Table2_Cell_Changed(row,new_value);

  };
  JPanel Data_Base = new JPanel();
  JPanel Networking = new JPanel();
  JRadioButton RB_AUC = new JRadioButton();
  JRadioButton RB_Acc = new JRadioButton();
  javax.swing.JLabel jLabel7 = new JLabel();

  javax.swing.JTextField text_neighborhs = new JTextField();


  public void jComboBox_Algorithm_actionPerformed(java.awt.event.ActionEvent e) {

  }

};//end-of-class-Frame
class Frame_jComboBox_Algorithm_actionAdapter
    implements java.awt.event.ActionListener {
  private Frame adaptee;
  Frame_jComboBox_Algorithm_actionAdapter(Frame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(java.awt.event.ActionEvent e) {
    adaptee.jComboBox_Algorithm_actionPerformed(e);
  }
}





/********************************************************************************/
/*                C L A S S       A D A P T E R S /L I S T E N E R S            */
/********************************************************************************/

class Frame1_jMenuFileExit_ActionAdapter implements ActionListener {
  Frame adaptee;

  Frame1_jMenuFileExit_ActionAdapter(Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuFileExit_actionPerformed(e);
  }
}

class Frame1_jMenuHelpAbout_ActionAdapter implements ActionListener {
  Frame adaptee;

  Frame1_jMenuHelpAbout_ActionAdapter(Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuHelpAbout_actionPerformed(e);
  }
}

class Frame1_Run_actionAdapter implements java.awt.event.ActionListener {
  Frame adaptee;

  Frame1_Run_actionAdapter(Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.Run_actionPerformed(e);
  }
}

/*class Frame_Run_mouseAdapter extends java.awt.event.MouseAdapter {
  Frame adaptee;

  Frame_Run_mouseAdapter(Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void mouseClicked(MouseEvent e) {
    adaptee.Run_mouseClicked(e);
  }
}*/

class Frame_B_Clear_actionAdapter implements java.awt.event.ActionListener {
  Frame adaptee;

  Frame_B_Clear_actionAdapter(Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.B_Clear_actionPerformed(e);
  }
}

class Frame_SaveAs_actionAdapter implements java.awt.event.ActionListener {
  Frame adaptee;

  Frame_SaveAs_actionAdapter(Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.SaveAs_actionPerformed(e);
  }
}

class Frame_jMenuOpenFile_actionAdapter implements java.awt.event.ActionListener {
  Frame adaptee;

  Frame_jMenuOpenFile_actionAdapter(Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuOpenFile_actionPerformed(e);
  }
}

/*class Frame_jTextField_DataSet_actionAdapter implements java.awt.event.ActionListener {
  Frame adaptee;

  Frame_jTextField_DataSet_actionAdapter(Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jTextField_DataSet_actionPerformed(e);
  }
}*/

/*class Frame_jComboBox_classify_actionAdapter implements java.awt.event.ActionListener {
  Frame adaptee;

  Frame_jComboBox_classify_actionAdapter(Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jComboBox_classify_actionPerformed(e);
  }
}*/

/*class Frame_jComboBox_splitting_actionAdapter implements java.awt.event.ActionListener {
  Frame adaptee;

  Frame_jComboBox_splitting_actionAdapter(Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jComboBox_splitting_actionPerformed(e);
  }
}*/

/*class Frame_RB_Average_actionAdapter implements java.awt.event.ActionListener {
  Frame adaptee;

  Frame_RB_Average_actionAdapter(Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.RB_Average_actionPerformed(e);
  }
}*/

class Frame_jMenuItem2_actionAdapter implements java.awt.event.ActionListener {
  Frame adaptee;

  Frame_jMenuItem2_actionAdapter(Frame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItem2_actionPerformed(e);
  }
}; // end-of-class

/*class Frame_jComboBox_Methods_itemAdapter implements java.awt.event.ItemListener
{
  Frame adaptee;

  Frame_jComboBox_Methods_itemAdapter(Frame adaptee)
  {
    this.adaptee = adaptee;
  }
  public void itemStateChanged(ItemEvent e)
  {
    adaptee.jComboBox_Methods_itemStateChanged(e);
  }
}; //end-of-class
*/

/*
  Table listener.
  Not Java generated
*/
class Frame_jTable_Table2_Listener implements javax.swing.event.TableModelListener
{
   Frame adaptee;

   Frame_jTable_Table2_Listener(Frame adaptee)
   {
     this.adaptee = adaptee;
   };

   public void tableChanged(TableModelEvent e)
   {
     adaptee.jTable_table2_tableChanged(e);
   };
}; //end-of-class

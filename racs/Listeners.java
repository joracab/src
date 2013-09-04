package racs;

import weka.core.*;

import java.util.*;
import java.util.regex.Pattern;
import java.io.*;
//import java.awt.Dimension;

// interface libraries
import javax.swing.*;
import javax.swing.table.TableColumn;

public class Listeners
{

   Instances dataset;
   MetricSpace Ms;
   //boolean median;
   int attributes_method[]; // method appliyed to each attribute to compute the center, prototype, etc.
   static String current_path;
  /*
   Constructor: null
  */
  public Listeners()
  {
    current_path=null;
  };

 /*
  Launch the experiment.
  A warning window is showed if the parameters <times> and <folds> are negative values.
 */
public void Experimenter_RunButton(int times, int folds, boolean average, boolean mistakes, int children, boolean trace, boolean AUC, boolean Acc,int neighbors, JComboBox jComboBox_classify, JComboBox jComboBox_splitting, JComboBox jComboBox_Algorithm, JTextArea TextArea, JFrame f)
 {
   Testing test;
   String message,header;
   int classify_criterion,algorithm_selection,splitting_criterion;

   header="";
   if (dataset==null || Ms==null)
   {
     message = "Data set and metric space files have not been loaded.";
     javax.swing.JOptionPane.showMessageDialog(f, (Object) message, "Warning",2);
   }
   else if (times < 1 || folds <= 1)
   {
     message = "The field <Times> must be greater than zero.\n The field <Folds> must be greater than one.";
     javax.swing.JOptionPane.showMessageDialog(f, (Object) message, "Note",2);
   }
   else if (children<=1)
   {
     message = "The number of <children> must be greater than 1.\n";
     javax.swing.JOptionPane.showMessageDialog(f, (Object) message, "Note",2);
   }
   else
   {
     algorithm_selection=jComboBox_Algorithm.getSelectedIndex();
     test = new Testing(times, folds, dataset);
     header+=times+"x"+folds+" ";
     if (algorithm_selection==0) // running RACS method
     {
       header+="RACS (children="+children+") ";
       classify_criterion=jComboBox_classify.getSelectedIndex();
       splitting_criterion=jComboBox_splitting.getSelectedIndex();
       if (splitting_criterion==0) header+="IG";
       else if (splitting_criterion==1) header+="GR";
       test.Checking_RACS(Ms, children, classify_criterion, splitting_criterion, trace, AUC, Acc, attributes_method);


     }
     else if (algorithm_selection==1)  // running ID3 method
     {
       header+="ID3 ";
       test.Checking_ID3(trace,AUC,Acc);
     }
     else if (algorithm_selection==2)  // running ID3 method  //running c4.5 method
     {
       header+="C4.5 ";
       test.Checking_C45(trace,AUC,Acc);
     }

     else if (algorithm_selection==3)
     {
     header+="KNN";
     classify_criterion=jComboBox_classify.getSelectedIndex();
    splitting_criterion=jComboBox_splitting.getSelectedIndex();
     test.Checking_KNN(Ms, children, classify_criterion, splitting_criterion, trace, AUC, Acc,neighbors, attributes_method);
     }
     
     else 
     {
    	 header+="KNN Metric";
         classify_criterion=jComboBox_classify.getSelectedIndex();
        splitting_criterion=jComboBox_splitting.getSelectedIndex();
         test.Checking_KNNMetric(Ms, children, classify_criterion, splitting_criterion, trace, AUC, Acc,neighbors, attributes_method);
         	 
     }
       // running ID3 method
     // output

     View.viewPerformance(average, mistakes, test.getRightMatrix(), test.getAccuracyMatrix(), TextArea);
     View.viewGeneralPerformance(header,AUC,test.getMeanAUC(),test.getDesvAUC(),Acc,test.getMeanAccuracy(),test.getDesviation(), TextArea);
   }
 };


 /*
  Clear the Text area component. A confirmation is needed.
 */
 public void Experimenter_ClearButton(JTextArea j)
 {
   String message;
   int answer;

   message="Are you sure?" ;
   answer=javax.swing.JOptionPane.showConfirmDialog(null,(Object) message,"Confirmation",2);
   if (answer==0) j.setText("");
 };


 /*
   Save the TextPane content in a choosen file.
 */
 public void Experimenter_SaveAsButton(JTextArea j)
 {
   JFileChooser choose;
   int user_action;
   FileWriter writer;
   BufferedWriter out;
   StringTokenizer tokenizer;
   String p;

   choose= new JFileChooser(current_path);
   user_action=choose.showSaveDialog(null);
   if (user_action==JFileChooser.APPROVE_OPTION)
   {
     try
     {
       writer = new FileWriter(choose.getSelectedFile());
       out = new BufferedWriter(writer);
       tokenizer = new StringTokenizer(j.getText(),"\n");
       while (tokenizer.hasMoreTokens())
       {
         p = tokenizer.nextToken();
         out.write(p);
         out.newLine();
       }
       out.flush();
       out.close();
       writer.close();
     }catch (java.io.IOException e) {
       System.out.println("Error: "+e.getMessage());};
   }
 };


 /*
   Load a data set along with its metric space.
   Only one *.arff and metric space file are allowed.
   The metric space file will be always referenced as "metric_space".
 */
 public void Menu_OpenFile(JFrame f, JTextField jTextField_DataSet, JTextField jTextField_MetricSpace, JTextField jTextField_Samples, JScrollPane jTableContainer, JScrollPane jTableContainer2,JComboBox methods, JTable table, JTable table2, Frame_jTable_Table2_Listener listener)
 {

   JFileChooser choose;
   //JTable table,table2;
   TableColumn method;
   int user_action,i,total,total_class;
   Object [][] class_distribution,attributes_type;
   File file;
   FileReader reader;
   String message, file_name, path;
   String [] tableHead ={"CLASS","N. INSTANCES","%"};
   String [] tableHead2 = {"ATTRIBUTE","TYPE","METHOD"};
   Enumeration instances,class_label,attributes;
   Attribute att;

   file_name="";
   i=0;
   total=-1; total_class=0;
   choose = new JFileChooser(current_path);
   user_action=choose.showOpenDialog(null);

   if (user_action==JFileChooser.APPROVE_OPTION)
   {
       file = choose.getSelectedFile();
       file_name = file.getName();
       path=file.getParent();
       current_path=path;

       if (!Pattern.matches(".+arff+$", file_name)) // testing the name file format
       {
         message="Sorry, the selected file must be a <*.arff> file." ;
         javax.swing.JOptionPane.showMessageDialog(f, message, "Note", 2);
       }
       else
       {
           try
           {

            reader = new FileReader(path+"\\"+file_name);
            dataset = new Instances(reader);
            dataset.setClassIndex(dataset.numAttributes() - 1); // stablishing which attribute is the class attribute

            attributes_method=new int[dataset.numAttributes()];
            // updating the interface (data set path)
            jTextField_DataSet.setText(path+"\\"+file_name);
            jTextField_Samples.setText(""+dataset.numInstances());

            // loading data set statistics in a JTable (class distribution)

            // To Do cambiar codigo



            instances= dataset.enumerateInstances();
            class_distribution = new Object[dataset.numClasses()][3];
            class_label=dataset.attribute(dataset.numAttributes()-1).enumerateValues();
            while (class_label.hasMoreElements())
            {
              class_distribution[i][0] = (String) class_label.nextElement();
              class_distribution[i][1]= new Integer(0);
              class_distribution[i++][2]=null;
            }
            while (instances.hasMoreElements())
            {
              Instance inst = (Instance)instances.nextElement();
              class_distribution[(int) inst.classValue()][1]=
                   new Integer (((Integer)class_distribution[(int)inst.classValue()][1]).intValue()+1); // object Integer is increased in 1
            }
            for (int k=0;k<class_distribution.length;k++)
            {
              total=dataset.numInstances();
              total_class=((Integer)class_distribution[k][1]).intValue();
              class_distribution[k][2]=new Double (100*((double) total_class) / ((double) total));
            }
            table = new JTable (class_distribution,tableHead);
            jTableContainer.getViewport().add(table,null);

            // loading types of attributes and prototype computation method (median, center, etc.)
            i=0;
            attributes_type = new Object[dataset.numAttributes()][3];
            attributes = dataset.enumerateAttributes();
            while (attributes.hasMoreElements())
            {
              att = (Attribute) attributes.nextElement();
              attributes_type[i][0]=att.name();
              if (att.isNominal()) attributes_type[i][1]="Nom/Est.";
              
              else 
            	  if (att.isString()) attributes_type[i][1]="String";
            	  else
            	  attributes_type[i][1]="Numerical";
               
              attributes_type[i++][2]=null;
            }
            table2 = new JTable(attributes_type,tableHead2);
            method = table2.getColumnModel().getColumn(2);
            method.setCellEditor(new DefaultCellEditor(methods));
            table2.getModel().addTableModelListener(listener);
            jTableContainer2.getViewport().add(table2,null);
           }
           catch (java.io.IOException file_name_error)
                 {javax.swing.JOptionPane.showMessageDialog(f,(Object) file_name_error.getMessage(),"Note",2);};
           try
           {
             // loading metric space
             Ms = new MetricSpace();
             String str= path+"\\"+file_name.substring(0,file_name.indexOf(".arff"))+"_metric_space.txt";
             Ms.getSpaceFromFile(str);
             jTextField_MetricSpace.setText(str);
           }
           catch (java.io.FileNotFoundException error)
                 {
                   javax.swing.JOptionPane.showMessageDialog(f,(Object) error.getMessage(),"Warning",2);};
       }
   }
 };

 /*
    "Save Ms from arff file" response.
 */
 public void Menu_SaveMs(JFrame f)
 {
   JFileChooser choose;
   int user_action;
   File file;
   FileReader reader;
   String message, file_name, path;

   file_name="";
   choose = new JFileChooser();
   user_action=choose.showOpenDialog(null);
   try
   {
     if (user_action == JFileChooser.APPROVE_OPTION) {
       file = choose.getSelectedFile();
       file_name = file.getName();
       path = file.getParent();
       racsUtils.MsFromArff(path, file_name);
     }
   }
   catch(java.io.FileNotFoundException error_file)
        {javax.swing.JOptionPane.showMessageDialog(f,(Object) error_file.getMessage(),"Warning",2);}
   catch(java.io.IOException error_io)
        {javax.swing.JOptionPane.showMessageDialog(f,(Object) error_io.getMessage(),"Warning",2);};
 };


/*
  Change the prototype computation method (Median, Center, etc.).
*/
/*public void Loaded_Files_ComboBox_Methods(JComboBox methods)
{
  if (methods.getSelectedIndex()==0) median=true;
  else median=false;
};
*/

/*
  Change the prototype computation method (Median, Center, etc.).
*/
public void Loaded_Files_Table2_Cell_Changed(int num_attribute,String new_value)
{
    int index;
    index=0;
    //System.out.println("hla de nou");

    if (new_value.equals("Median")) index=0;
    else if (new_value.equals("Center")) index=1;
    attributes_method[num_attribute]=index;
    //for (int i=0; i<attributes_method.length; i++) System.out.println(attributes_method[i]);
};

};// end-of-class-Listeners




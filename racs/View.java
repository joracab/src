/*
  View items (matrices, data set, etc.) by standard output.
*/
package racs;

import weka.core.*;
import java.util.*;

// interface libraries
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;
import com.borland.jbcl.layout.*;
import javax.swing.border.*;

public class View {

	
	
	private static String tabula="";
 /*
   Null constructor.
 */
 public View() {};


 /*
  Print a data set by standard output.
 */
 public static void viewData(Instances data)
 {
   Enumeration enume= data.enumerateInstances();
   while (enume.hasMoreElements()) {
     Instance inst = (Instance) enume.nextElement();
     System.out.println(inst);
   }
 };


 /*
  Print a partition by standard output.
 */
 public static void viewPartition(String message,Instances[] partition) throws IOException
 {
	 
	 String arbol="C:\\Users\\admin\\Desktop\\dspruebas\\dspruebas\\dspruebas\\arbol.txt";
     
	 File fichero=new File(arbol);

	 FileReader Treefile;
	  BufferedReader input_file;
	  FileWriter Fw = null;
	  BufferedWriter output_file;
	  String line, set_of_values, row;
	  StringTokenizer getTokens;
	  int num_of_values;
		if(fichero.exists())
		{
			
			Fw=new FileWriter(fichero,true); 
		
		}else{
			
		  fichero.createNewFile();
		  Fw=new FileWriter(fichero,true);
			
		}
	
	  output_file = new BufferedWriter(Fw);
	  output_file.write(tabula+message+"\n");
      output_file.newLine();
 
	 
   System.out.println(tabula+message+"\n");
   for(int i=0;i<partition.length;i++)
   {
     System.out.println(tabula+"Branch "+i);
     output_file.write(tabula+"Branch "+i+"\n");
     output_file.newLine();
     Enumeration enume= partition[i].enumerateInstances();

       Instance inst = (Instance) enume.nextElement();
       System.out.println(inst.stringValue(0));
       output_file.write(tabula+inst.stringValue(0)+"\n");
       output_file.newLine();  
 
   }
   output_file.close();
   tabula+=" |->";
 };


 
 public static void ViewTree(String message, String tree) throws IOException
 {
	 Date dat=new Date();
	 java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("dd/MM/yyyy");
	 String fecha = sdf.format(dat);
	 
	 String arbol="C:\\Users\\Jose\\tesis\\arboles\\arbol.txt";
     
	 File fichero=new File(arbol);

	 FileReader Treefile;
	  BufferedReader input_file;
	  FileWriter Fw = null;
	  BufferedWriter output_file;
	  String line, set_of_values, row;
	  StringTokenizer getTokens;
	  int num_of_values;
		if(fichero.exists())
		{
			
			Fw=new FileWriter(fichero,true); 
		
		}else{
			
		  fichero.createNewFile();
		  Fw=new FileWriter(fichero,true);
			
		}
	
	  output_file = new BufferedWriter(Fw);
	  output_file.write(message+"\n"+tree.toString());
      output_file.newLine();
 
	 
//   System.out.println(tabula+message+"\n");
   
    // System.out.println(tabula+"Branch "+i);
     //  output_file.write(tabula+inst.stringValue(0)+"\n");
     //  output_file.newLine();  
 
   //}
   output_file.close();
  // tabula+=" |->";
 };

 /*
   Print a distance matrix by standard output.
 */
 public static void viewMatrix(double [][]m)
 {
   System.out.println("");
   System.out.println("Printing matrix ...");
   for (int i=0;i<m.length;i++)
   {
     System.out.println("");
     for (int j = 0; j < m[i].length; j++) System.out.print(" " + m[i][j]);
   }
   System.out.println("");
 };


 /*
   Print all the distance matrices by standard output.
 */
 public static void viewMetricSpace(MetricSpace ms)
 {
   for (int i=0; i< (ms.getMetricSpace()).size();i++)
   {
     System.out.println("Metric Space associated to attribute "+i);
     viewMatrix((double [][]) (ms.getMetricSpace()).elementAt(i));
   }
 };


 /*
   Print the candidates, to be prototypes, by standard output.
 */
 public static void viewCandidates(String message, Candidates c )
 {
   int i;

   System.out.println("Candidates ...("+message+")\n");
   for (i=0; i< c.getNumberofCandidates();i++)
     System.out.println(c.getInstance(i)+" , "+c.getMeanDistance(i));
   System.out.println("-------------------------------------\n");
 };

 /*
  Print the a set of prototypes by standard output.
 */
 public static void viewPrototypes(String message, Instance [] protos)
 {
   System.out.println(message+"\n");
   for (int i=0;i<protos.length; i++) System.out.println(protos[i]+"\n");
 };

 public static void viewKNN(String message,JTextArea output)
  {
    System.out.println(message+"\n");

 };

 /*
    View the performance of the classifier for a given fold.
    right = number of instances correctly classified.
    diference = Cardinality(fold) - right
    Acc, Mist = show or not the accuarcy and the mistakes for this fold.
    Accuracy = the accuracy reached by the classifier
  */
  public static void viewPerformance(boolean average, boolean mistakes, int [][] RightMatrix, double [][] AccuracyMatrix, JTextArea output)
  {
    int wrongs=0;
    if (average || mistakes)
    {
      for (int i = 0; i < RightMatrix.length; i++)
        for (int j = 0; j < RightMatrix[i].length; j++)
        {
          output.append("Fold " + i + " , Times " + j+"\n");
          if (mistakes)
          {
            output.append("Rights : " + RightMatrix[i][j]+"\n");
            wrongs = (int) ((RightMatrix[i][j] / AccuracyMatrix[i][j]) - RightMatrix[i][j]);
            output.append("Wrongs : " + wrongs+"\n");
          }
          if (average) output.append("Accuracy " + AccuracyMatrix[i][j]+"\n");
          output.append("--------------------------------------------------------\n");
        }
    }
  };


  /*
    View the general performance of one given classifier
  */
  public static void viewGeneralPerformance(String message,boolean AUC, double cAUC, double dcAUC, boolean Acc, double acc, double desv, JTextArea output)
  {
    String a_acc,a_desv,a_cAUC,d_cAUC;

   output.append("*********************************************\n");
   output.append(message+"\n");
   if (Acc)
   {
     a_acc = Double.toString(acc * 100);
     a_desv = Double.toString(desv * 100);
     output.append("Mean Accuracy : " + a_acc + "±" + a_desv + "\n");
   };
   if (AUC)
   {
     a_cAUC = Double.toString(cAUC).substring(0, 3);
     //d_cAUC = Double.toString(dcAUC).substring(0, 3);
     output.append("AUC : " + cAUC + "±" + dcAUC+ "\n");
   }
  };

}; // end-of-class

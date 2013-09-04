/*
 Utilities for the RACS implementation.
*/
package racs;

import java.util.*;
import java.lang.Double;
import java.lang.Class;
import java.io.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import weka.core.*;


public class racsUtils {
  public racsUtils() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  static int [] original_class_distribution;

  /*
   Null constructor.
  */
  public racsUtils(Instances data)
  {
    original_class_distribution=getCardinalityClass(data);
  };

  /*
   Return the minimum of three given integer numbers.
  */
  public static int min(int a, int b, int c)
  {
    if ( a<=b & a<=c) return a;
    else if ( b<=a & b<=c) return b;
    return c;
  };

  /*
   Return the minimum of three given float numbers.
  */
  public static float min(float a, float b, float c)
  {
    if ( a<=b & a<=c) return a;
    else if ( b<=a & b<=c) return b;
    return c;
  };

  /*
   Return the minimum of three given double numbers.
  */
  public static double min(double a, double b, double c)
  {
    if ( a<=b & a<=c) return a;
    else if ( b<=a & b<=c) return b;
    return c;
  };

  /*
    Return a VECTOR with the different class values from the set "data".
  */
  public static Vector getDataClassValues(Instances data)
  {

    Vector collec=new Vector();

    Enumeration enume=data.enumerateInstances();
    while (enume.hasMoreElements())
    {
      Instance item = (Instance) enume.nextElement();
      if (!collec.contains(new Double(item.classValue()))) collec.add(new Double(item.classValue()));
    }
    return collec;
  };


  /*
   Return the class cardinality for a given sample.
  */
  public static int [] getCardinalityClass(Instances data)
  {
     int [] counter;
     Enumeration enume;
     Instance ins;

     counter = new int[data.numClasses()];
     enume = data.enumerateInstances();
     while (enume.hasMoreElements())
     {
       ins = (Instance) enume.nextElement();
       //System.out.println("class values "+ins.classValue());
       counter[(int) ins.classValue()]++;
     }
     return counter;
   };


  /*
   Return the NUMBER of different values of an attribute "a"
   in the set "data".
  */
  public static int getNumberAttributeValues(Instances data, Attribute a)
  {
    int disvalues=0;
    Enumeration enume;
    Vector bag= new Vector();
    Instance inst;

    enume=data.enumerateInstances();
    while (enume.hasMoreElements())
    {
      inst=(Instance) enume.nextElement();
      if (!bag.contains(new Double(inst.value(a))))
      {
        bag.add(new Double(inst.value(a)));
        disvalues++;
      }
    }
    return disvalues;
  };

 /*
   Get the metric space from an *.arff file and save it in a *.txt file.
 */
public static void MsFromArff(String path, String arff) throws java.io.FileNotFoundException , java.io.IOException
{
  FileReader arff_file;
  BufferedReader input_arff_file;
  FileWriter metric_space;
  BufferedWriter output_metric_space;
  String line, set_of_values, row;
  StringTokenizer getTokens;
  int num_of_values;

  arff_file = new FileReader(path+"\\"+arff);
  input_arff_file = new BufferedReader(arff_file);
  metric_space = new FileWriter(path+"\\"+arff.substring(0,arff.length()-5)+"_metric_space.txt");
  output_metric_space = new BufferedWriter(metric_space);

  while ( (line = input_arff_file.readLine()) != null)
   {
      //if (line.length()==0) System.out.println("Excellent");
     // System.out.println("hi boy  "+line+" "+line.length());
       line=racsUtils.removeLeft(' ',line);
       line=racsUtils.removeRight(' ',line);
       //System.out.println("hi pretty boy "+line);
     if (line.length()>0 && line.charAt(0)!='%')
     {
      
       if (line.startsWith("@attribute"))
       { 
         line=line.trim();
         if (line.endsWith("real") || line.indexOf("numeri")>0)
         {
             output_metric_space.write("# -1");
             output_metric_space.newLine();
         }
         else if (line.indexOf("string")>0)
         {

             // Get the number of attributes and create the matrix of distances according  to the number of attributes//
             line=line.substring(line.indexOf("{")+1,line.indexOf("}"));
             //System.out.println("what a shit "+line);
             //line=tableModel.removeFrequentValues(line);
            // System.out.println("what a shit "+line);
               getTokens = new StringTokenizer(line,","); // line = "@attribute at01 {red,blue,yellow}"
               num_of_values=getTokens.countTokens();
               output_metric_space.write("# "+num_of_values);
               output_metric_space.newLine();

               while(getTokens.hasMoreTokens())
               {


                 //remove repeat elements in the data set

                 /*System.out.println("The elements are: " + getTokens.nextElement())*/;
                 row="";
                String actual=getTokens.nextElement().toString();


                 StringTokenizer newTokens=new StringTokenizer(line,",");
                 while(newTokens.hasMoreTokens())
                 {
                   String next=newTokens.nextElement().toString();
                   if (next.equals(actual))
                    /* pruebas con 10.000 datos*/

                    row+="0 ";
                   else
                     row+=tableModel.getLevenshteinDistance(actual,next)+" ";

                 }

                 output_metric_space.write(row);
                 output_metric_space.newLine();
               }
             }
         else // writting the distance matrix
         {
           //System.out.println("what a shit "+line);
           getTokens = new StringTokenizer(line,","); // line = "@attribute at01 {red,blue,yellow}"
           num_of_values=getTokens.countTokens();
           output_metric_space.write("# "+num_of_values);
           output_metric_space.newLine();

            for (int i=0; i<num_of_values; i++){
             row="";
           }
           /*;MAtriz de distancia  por MEAN distance */
            for (int i=0; i<num_of_values; i++)
           {
             row="";
             for (int j = 0; j < num_of_values; j++)
             {
               if (j==i) row+="0 ";
               else row+="1 ";
             }
             output_metric_space.write(row);
             output_metric_space.newLine();
           }
         }
       }
      }
   }
  arff_file.close();
  input_arff_file.close();
  output_metric_space.close();
  metric_space.close();
};




 /*
   Given an input as "acbcd   ", removeRight(' ',"abcd  ")
   returns "abcd".
 */
  public static String removeRight(char c, String line)
  {
    int index,len;

    len=line.length();
    index=len-1;

    if (len==0) return "";
    while (line.charAt(index)=='c') index--;
    return line.substring(0,index+1);
  };

  /*
   Given an input as "   acbcd  ", removeLeft(' ',"   abcd")
   returns "abcd".
 */
  public static String removeLeft(char c, String line)
  {
    int index;

    index=0;

    if (line.equals("")) return "";
    while (line.charAt(index)=='c') index++;
    return line.substring(index);
  }

  private void jbInit() throws Exception {
  };

}; // end-of-class

class tableModel extends AbstractTableModel
{
  private String [] tableHead={"ATTRIBUTE","TYPE","METHOD"};
  private Object [][] data;
  private JComboBox methodList;

  public tableModel(Enumeration attributes, int num_attributes)
  {
    Attribute att;
    int i;

    i=0;
    methodList = new JComboBox();
    methodList.addItem("From Distribution");
    methodList.addItem("Invented");
    data = new Object[num_attributes][2];
    while (attributes.hasMoreElements())
    {
      att=(Attribute) attributes.nextElement();
      data[i][0]=att.toString();
      if (att.isNominal()) data[i++][1]="Nominal";
      else
      if(att.isString()) data[i++][1]="String";
      else data[i++][1]="Numerical";
      
    };
  };
    // implementing methods
    public int getColumnCount()
    {
      return tableHead.length;
    };
    public int getRowCount()
    {
      return data.length;
    };
    public String getColumnName(int col)
    {
      return tableHead[col];
    };
    public Object getValueAt(int row, int col)
    {
      return data[row][col];
    };
    public Class getColumnClass(int col)
    {
      return getValueAt(0,col).getClass();
    };
    public static int getLevenshteinDistance (String s, String t) {
     if (s == null || t == null) {
       throw new IllegalArgumentException("Strings must not be null");
     }

     /*
       The difference between this impl. and the previous is that, rather
        than creating and retaining a matrix of size s.length()+1 by t.length()+1,
        we maintain two single-dimensional arrays of length s.length()+1.  The first, d,
        is the 'current working' distance array that maintains the newest distance cost
        counts as we iterate through the characters of String s.  Each time we increment
        the index of String t we are comparing, d is copied to p, the second int[].  Doing so
        allows us to retain the previous cost counts as required by the algorithm (taking
        the minimum of the cost count to the left, up one, and diagonally up and to the left
        of the current cost count being calculated).  (Note that the arrays aren't really
        copied anymore, just switched...this is clearly much better than cloning an array
        or doing a System.arraycopy() each time  through the outer loop.)

        Effectively, the difference between the two implementations is this one does not
        cause an out of memory condition when calculating the LD over two very large strings.
     */

     int n = s.length(); // length of s
     int m = t.length(); // length of t

     if (n == 0) {
       return m;
     } else if (m == 0) {
       return n;
     }

     int p[] = new int[n+1]; //'previous' cost array, horizontally
     int d[] = new int[n+1]; // cost array, horizontally
     int _d[]; //placeholder to assist in swapping p and d

     // indexes into strings s and t
     int i; // iterates through s
     int j; // iterates through t

     char t_j; // jth character of t

     int cost; // cost

     for (i = 0; i<=n; i++) {
        p[i] = i;
     }

     for (j = 1; j<=m; j++) {
        t_j = t.charAt(j-1);
        d[0] = j;

        for (i=1; i<=n; i++) {
           cost = s.charAt(i-1)==t_j ? 0 : 1;
           // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
           d[i] = Math.min(Math.min(d[i-1]+1, p[i]+1),  p[i-1]+cost);
        }

        // copy current distance counts to 'previous row' distance counts
        _d = p;
        p = d;
        d = _d;
     }

     // our last action in the above loop was to switch d and p, so p now
     // actually has the most recent cost counts
     return p[n];
   };
/**
 * removeFrequentValues
 *
 * @param A StringTokenizer
 * @return StringTokenizer
 */
public static String removeFrequentValues(String line){
StringTokenizer A=new StringTokenizer(line,",");
String newline="";

 while(A.hasMoreTokens()){
          String str=A.nextToken();
 System.out.println(newline.indexOf(str));
if(newline.indexOf(str)==-1){
    newline+=str+",";
}
else
{
String substr=newline.substring(newline.indexOf(str),newline.lastIndexOf(","));
  if (substr.length()!=str.length())newline+=str+",";
       System.out.println(str);
       System.out.println(newline);
 }
 };
 newline=newline.substring(0,newline.length()-1);
 return   newline;

   };

}; // end-of-class tableModel

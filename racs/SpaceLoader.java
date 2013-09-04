/* Loading a metric space from disk to memory ...
   Remember the right format for a file is the next one

   # 2 --> dimension of the matrix
   0 3 --> the (2x2) distance matrix
   3 0

   # 3
   0 2 4
   2 0 5
   4 5 0
   ...
   # -1 --> numerical attributes will not need a distance matrix.
   ...
*/

package racs;

import java.io.*;
import java.util.*;

public class SpaceLoader {

  private FileReader fr;
  private BufferedReader input;

 /*
   Constructor: Inicialise the FileReader and the BufferedReader
 */
 public SpaceLoader(String fileName) throws java.io.FileNotFoundException
 {
     fr = new FileReader(fileName);
     input = new BufferedReader(fr);
 };


 /*
   List the read file by standard output.
 */
 public void list()
 {
   String line;
   StringTokenizer getTokens;
   try
   {
     while ( (line = input.readLine()) != null)
     {
       getTokens = new StringTokenizer(line);
       while (getTokens.hasMoreElements()) System.out.println(getTokens.nextToken());
     }
   } catch(Exception e){
     System.out.println("Error: "+e.toString());};
 };


 /*
   Load the matrices file in memory (memory = Vector of matrices)
 */
 public void loadMetricSpaces(Vector Spaces)
 {
   String line;
   int row, colum, state, dim;
   double matrix[][],item;

   row=0; colum=0; state=0; dim=0;
   matrix=new double[1][1];

   try
   {
     while ( (line = input.readLine()) != null)
     {
       StringTokenizer getTokens = new StringTokenizer(line);
       if (getTokens.hasMoreElements())
       {
         if (state == 0) // reading the dimension of the matrix
         {
           getTokens.nextToken();
           dim = (new Integer(getTokens.nextToken())).intValue();
           if (dim >0)
           {
             matrix = new double[dim][dim];
             state=1;
             row=0;
           }
           else Spaces.add(null); // this is the case for numerical attributes
         }
         else // reading the matrix
         {
           colum = 0;
           while (getTokens.hasMoreElements())
           {
             item = (Double.valueOf(getTokens.nextToken())).doubleValue();
             matrix[row][colum++] = item;
           }
           row++;
           if (row == dim)
           {
             state = 0;
             Spaces.add(matrix);
           }
         }
       }
     }
   } catch(Exception e){
     System.out.println("Error: "+e.toString());};
 };

}; // end-of-class

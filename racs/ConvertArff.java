/*
  Convert an arff file into a metric_arff file
*/

package racs;

import java.io.*;
import java.util.*;

public class ConvertArff
{
  private FileReader arff;
  private BufferedReader input_buffer;
  private String ppath;
  /*
   Connect the arff file which we want to convert.
  */
  public ConvertArff(String path, String file) throws java.io.FileNotFoundException
  {
    arff = new FileReader(path+"\\"+file);
    input_buffer = new BufferedReader(arff);
    ppath=path;
  };


}; // end-of-class

package racs;

import weka.core.*;
import weka.classifiers.trees.Id3;
import weka.classifiers.trees.j48.J48;

import java.util.*;
import java.io.*;

public class racs {

  public static void main(String[] args)
  {

    /*FileReader reader;
    Instances dataset,dataset2;
    Id3 Tree2=new Id3();
    MetricSpace Ms=new MetricSpace();
    Id3Metric Tree=new Id3Metric();
    J48 Treec45 = new J48();
    Testing test;*/
    Interface inter;
    inter = new Interface(); // launch the interface

    /*Ms.getSpaceFromFile("./conjunt de prova/espai_metric.txt");
    try{
      reader = new FileReader("./conjunt de prova/cadenes_metric.arff");
      dataset = new Instances(reader);
      dataset.setClassIndex(dataset.numAttributes() - 1); // stablishing which attribute is the class attribute


      System.out.println("El número de atributs "+dataset.numAttributes());


      test=new Testing(2,2,dataset,true,true);
      test.Checking(Tree,Ms,3,false);
      System.out.println("La mitjana es..."+test.getMeanAccuracy());

     }catch (Exception erro){System.out.println("Problemas al abrir el archivo "+erro.getMessage());};*/

} // end-of-main


   //Treec45.buildClassifier(dataset);
   //System.out.println(Tree2.toString());

    //Tree.buildClassifier(dataset,Ms,10);
     //test=new Testing(2,2,dataset);
     //test.Checking(Tree,Ms,3);
   //System.out.println("La mitjana es..."+test.getMeanAccuracy());
  //Tree.buildClassifier(dataset, Ms,3);
 //System.out.println("hola ");
   //reader = new FileReader("./DataSetsMRDM/splice/splice_id3_594.arff");
   //dataset2 = new Instances(reader);
   //dataset2.setClassIndex(dataset2.numAttributes() - 1);
    //Tree2.buildClassifier(dataset2);



}; // racs end-of-class
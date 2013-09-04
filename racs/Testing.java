/*
  K-Cross Validation for TESTING a classifier.
  The results will be showed by PaneText.
*/

package racs;

import java.util.*;
import java.awt.BorderLayout;
import java.io.*;

import weka.core.*;
import weka.classifiers.trees.Id3;
import weka.classifiers.trees.j48.*;
import weka.classifiers.lazy.IBk.*;
import weka.classifiers.evaluation.*;
import weka.classifiers.Classifier;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;
import racs.KNN;




public class Testing
{

 private int folds, rep;
 public Instances dataSet,dataSet1;
 private double [][] Accuracy;
 private int [][] Rights;
 private double [][] cAUC;
 /*
   Constructor: Inicialise the number of folds, repetitions, etc.
 */
 public Testing(int numRep, int numFolds, Instances dSet)
 {
   folds=numFolds;
   rep=numRep;
   dataSet=new Instances(dSet);
   /* testing de prueba del dataset*/
   dataSet1=new Instances(dSet);
   Accuracy= new double[rep][folds];
   Rights= new int[rep][folds];
   cAUC=new double[rep][folds];
 };

 public void Checking_KNN(MetricSpace ms, int maxBranches, int classify_criterion, int splitting_criterion, boolean trace, boolean AUC, boolean Acc, int neighbors,int [] attributes_method)
 {


   Random random;
   int seed;
   double real_class, classified_class,classified_class_knn,right;
   Instances train, test;
   Enumeration enume;
   Instance instance;
   Id3Metric tree;
   weka.classifiers.lazy.IBk Knn;
   racsUtils rUtils;

   seed=1;
   tree= new Id3Metric();
   Knn = new weka.classifiers.lazy.IBk(5);
   rUtils = new racsUtils(dataSet); // setting the origanl class distribution (pre prunning)
//dataSet1.firstInstance();

   try
   {
     for (int i_rep = 0; i_rep < rep; i_rep++) {
       random = new Random(seed);
      
       dataSet1.randomize(random); // shuffle the dataset
       //View.viewDatda(dataSet1);

       double total = 0.0;
       for (int j_fold = 0; j_fold < folds; j_fold++) {
         // folding ...
         train = dataSet1.trainCV(folds, j_fold);
         test = dataSet1.testCV(folds, j_fold);
         if (trace) {
           System.out.println("Data for training ...");
           View.viewData(train);
         }
         /* aprendizaje del KNN*/
         Knn.setDistanceWeighting(new SelectedTag(Knn.WEIGHT_INVERSE,
                                                  Knn.TAGS_WEIGHTING));
         Knn.buildClassifier(train);

         
         //Evaluation eval= new Evaluation(train)
         //eval.crossValidateModel(Knn,train,10);
         //System.out.println(Knn.toString()+" "+Knn.getOptions().toString()+" "+" distance weigth "+Knn.getDistanceWeighting().getSelectedTag().getReadable());
         //System.out.println( " Accuracy "+(eval.correct()/train.numInstances())+" Correctas"+ eval.correct());

         //System.out.println(""+ eval.toSummaryString("pruebas",false));
         // tree.buildClassifier(train, ms, maxBranches,splitting_criterion,attributes_method);
         if (trace) {
           System.out.println(tree.toString());

         }
         ;
         System.out.println("clasificador de KNN    " + Knn.toString());

         if (Acc) {
           if (trace) {
             System.out.println("Data for testing ...");
             View.viewData(test);
           }
           enume = test.enumerateInstances();
           right = 0.0;
           while (enume.hasMoreElements()) {
             instance = (Instance) enume.nextElement();
             classified_class = Knn.classifyInstance(instance);
             real_class = instance.classValue();
             if (classified_class == real_class) {
               right++;
               if (trace)
                 System.out.println(instance + " right");
             }
             else if (trace)
               System.out.println(instance + " mistake");
           }
           Accuracy[i_rep][j_fold] = (right / test.numInstances());
           Rights[i_rep][j_fold] = (int) right;

         }

         // AUC evaluation...
         if (AUC) cAUC[i_rep][j_fold] = computeAUC(Knn, test);
         if ((i_rep==(rep-1)) && (j_fold==rep-1))
         {
       	  for (int ir = 0; ir < rep; ir++){
          System.out.println("  AUC"+cAUC[ir][ir] + " Accuracy"+Accuracy[ir][ir]+ "\n");
       	  }
         }

         System.out.println("AUC" + cAUC[i_rep][j_fold] + "\n");

       }
       ;
     }
     ;


   } catch(Exception e) {
     System.out.println("Error "+e.getMessage());
   };
 };

 
 public void Checking_KNNMetric(MetricSpace ms, int maxBranches, int classify_criterion, int splitting_criterion, boolean trace, boolean AUC, boolean Acc, int neighbors,int [] attributes_method)
 {

	 
   Random random;
   int seed;
   double real_class, classified_class,classified_class_knn,right;
   Instances train, test;
   Enumeration enume;
   Instance instance;
  // Id3Metric tree;
   IBkMetric Knn;
   racsUtils rUtils;

   seed=1;
   //tree= new Id3Metric();
   Knn = new IBkMetric(5,ms);
   
   Knn.m_Original=dataSet1;
   rUtils = new racsUtils(dataSet); // setting the origanl class distribution (pre prunning)
//dataSet1.firstInstance();

   try
   {
     for (int i_rep = 0; i_rep < rep; i_rep++) {
      
    	 java.util.Date ahorainicio = new java.util.Date();
         System.out.println("Contador rep inicio"+i_rep+ "  "+ ahorainicio.toString());
         
    	 random = new Random(seed);
      
      //ms.getDistanceMatrix(i_rep)
      
       dataSet1.randomize(random); // shuffle the dataset
       //View.viewData(dataSet1);

       double total = 0.0;
       for (int j_fold = 0; j_fold < folds; j_fold++) {
         // folding ...
         train = dataSet1.trainCV(folds, j_fold);
         test = dataSet1.testCV(folds, j_fold);
         if (trace) {
           System.out.println("Data for training ...");
           View.viewData(train);
         }
         /* aprendizaje del KNN*/
         Knn.setDistanceWeighting(new SelectedTag(Knn.WEIGHT_INVERSE,
                                                  Knn.TAGS_WEIGHTING));
         Knn.buildClassifier(train);

          //System.out.println(Knn.getOptions().toString());
         if (trace) {
           //System.out.println(tree.toString());

         }
         ;
         //System.out.println("clasificador de KNN    " + Knn.toString());

         if (Acc) {
           if (trace) {
             System.out.println("Data for testing ...");
             View.viewData(test);
           }
           enume = test.enumerateInstances();
           right = 0.0;
           while (enume.hasMoreElements()) {
             instance = (Instance) enume.nextElement();
             classified_class = Knn.classifyInstance(instance);
             real_class = instance.classValue();
             if (classified_class == real_class) {
               right++;
               if (trace)
                 System.out.println(instance + " right");
             }
             else if (trace)
               System.out.println(instance + " mistake");
           }
           Accuracy[i_rep][j_fold] = (right / test.numInstances());
           Rights[i_rep][j_fold] = (int) right;

         }

         // AUC evaluation...
         if (AUC) cAUC[i_rep][j_fold] = computeAUC(Knn, test);

         System.out.println("AUC" + cAUC[i_rep][j_fold] + "\n");
         if ((i_rep==rep-1) && (j_fold==rep-1))
         {
       	  for (int ir = 0; ir < rep; ir++){
          System.out.println("  AUC"+cAUC[ir][ir] + " Accuracy"+Accuracy[ir][ir]);
       	  }
         }

       }
       ;
     }
     ;


   } catch(Exception e) {
     System.out.println(e);
   };
 };

 
/* public void Checking_RACS(MetricSpace ms, int maxBranches, int classify_criterion, int splitting_criterion, boolean trace, boolean AUC, boolean Acc, int neighbors,int [] attributes_method)
 {
   Random random;
   int seed;
   double real_class, classified_class,classified_class_knn,right;
   Instances train, test;
   Enumeration enume;
   Instance instance;
   Id3Metric tree;
   weka.classifiers.lazy.IBk Knn;
   racsUtils rUtils;

   seed=1;
   tree= new Id3Metric();
   Knn = new weka.classifiers.lazy.IBk(10);
   rUtils = new racsUtils(dataSet); // setting the origanl class distribution (pre prunning)
dataSet1.firstInstance();
   try
   {
     for (int i_rep = 0; i_rep < rep; i_rep++)
     {
       random = new Random(seed);
       dataSet1.randomize(random); // shuffle the dataset
       View.viewData(dataSet1);
       for (int j_fold = 0; j_fold < folds; j_fold++)
       {
         // folding ...
         train = dataSet1.trainCV(folds, j_fold);
         test = dataSet1.testCV(folds, j_fold);
         if (trace)
         {
           System.out.println("Data for training ...");
           View.viewData(train);
         }
         // learning ...
        // aprendizaje del KNN
         Knn.buildClassifier(train);

         Knn.setCrossValidate(true);
         Knn.buildClassifier(test);
         System.out.println(Knn.toString()+" "+Knn.getOptions().toString()+" "+" ");
         tree.buildClassifier(train, ms, maxBranches,splitting_criterion,attributes_method);
         
         if (trace) {
           System.out.println(tree.toString());

         };


         /// evaluaar con KNN




         // Accuracy evaluation
         if (Acc)
         {
           if (trace) {
             System.out.println("Data for testing ...");
             View.viewData(test);
           }
           enume = test.enumerateInstances();
           right = 0.0;
           int pcount=0;
           while (enume.hasMoreElements()) {

           pcount=pcount+1;
              System.out.println(" "+pcount+"\n ");
             instance = (Instance) enume.nextElement();
             if (classify_criterion == 0){
               classified_class = tree.classifyInstance(instance, ms); // proximity criterion

             }
             else
               classified_class = tree.classifyInstanceDensity(instance, ms); // density criterion

             real_class = instance.classValue();
             if (classified_class == real_class) {
               right++;
               if (trace)
                 System.out.println(instance + " right");
             }
             else if (trace)
               System.out.println(instance + " mistake");
           }
           Accuracy[i_rep][j_fold] = (right / test.numInstances());
           Rights[i_rep][j_fold] = (int) right;

         }
         // AUC evaluation...
       ///  if (AUC) cAUC[i_rep][j_fold]=computeAUC(tree,test);
         //System.out.println(""+cAUC[i_rep][j_fold] +"\n");

       };
     };
   /**  pruebas KNN
     for (int i_rep = 0; i_rep < rep; i_rep++)
     {
       random = new Random(seed);
       dataSet.randomize(random); // shuffle the dataset
       View.viewData(dataSet);
       for (int j_fold = 0; j_fold < folds; j_fold++)
       {
         // folding ...
         train = dataSet.trainCV(folds, j_fold);
         test = dataSet.testCV(folds, j_fold);
         if (trace)
         {
           System.out.println("Data for training ...");
           View.viewData(train);
         }
         // learning ...
         tree.buildClassifier(train, ms, maxBranches,splitting_criterion,attributes_method);
         if (trace) System.out.println(tree.toString());

         //  testing...
         // Accuracy evaluation
         if (Acc)
         {
           if (trace) {
             System.out.println("Data for testing ...");
             View.viewData(test);
           }
           enume = test.enumerateInstances();
           right = 0.0;
           while (enume.hasMoreElements()) {
             instance = (Instance) enume.nextElement();
             if (classify_criterion == 0)
               classified_class = tree.classifyInstance(instance, ms); // proximity criterion
             else
               classified_class = tree.classifyInstanceDensity(instance, ms); // density criterion
             real_class = instance.classValue();
             if (classified_class == real_class) {
               right++;
               if (trace)
                 System.out.println(instance + " right");
             }
             else if (trace)
               System.out.println(instance + " mistake");
           }
           Accuracy[i_rep][j_fold] = right / test.numInstances();
           Rights[i_rep][j_fold] = (int) right;
         }
         // AUC evaluation...
         //if (AUC) cAUC[i_rep][j_fold]=computeAUC(tree,test);
         //System.out.println(""+cAUC[i_rep][j_fold] +"\n");
       };
     };
   } catch(Exception e) {
     System.err.println("Error "+e.getMessage()+  e.getCause());};
 };
*/
  public void Checking_RACS(MetricSpace ms, int maxBranches, int classify_criterion, int splitting_criterion, boolean trace, boolean AUC, boolean Acc,int [] attributes_method)
   {

      java.util.Date ahorainicio = new java.util.Date();
     System.out.println("Contador rep inicio"+ ahorainicio.toString());
         Random random;
         int seed;
         double real_class, classified_class,right;
         Instances train, test;
         Enumeration enume;
         Instance instance;
         Id3Metric tree;
         racsUtils rUtils;

         seed=1;
         tree= new Id3Metric();
        
         rUtils = new racsUtils(dataSet); // setting the origanl class distribution (pre prunning)

         try
         {
           for (int i_rep = 0; i_rep < rep; i_rep++)
           {
             random = new Random(seed);
             dataSet.randomize(random); // shuffle the dataset
             //View.viewData(dataSet);
             for (int j_fold = 0; j_fold < folds; j_fold++)
             {
               // folding ...
               train = dataSet.trainCV(folds, j_fold);
               test = dataSet.testCV(folds, j_fold);
               if (trace)
               {
            //     System.out.println("Data for training ...");
              //   View.viewData(train);
               }
               // learning ...
               tree.buildClassifier(train, ms, maxBranches,splitting_criterion,attributes_method);
              
               if (trace) View.ViewTree("arbol de decision ",tree.toString());
               //  testing...
               // Accuracy evaluation
               if (Acc)
               {
            	   
            	   
                 if (trace) {
                   //System.out.println("Data for testing ...");
                   View.viewData(test);
                 }
                 enume = test.enumerateInstances();
                 System.out.println(test.numInstances());
                 right = 0.0;
                 while (enume.hasMoreElements()) {
                   instance = (Instance) enume.nextElement();
                   double[] testInst;
				if (classify_criterion == 0){
                	   
                	 // testInst=tree.distributionForInstance(instance);
                	   
                     classified_class =	tree.classifyInstance(instance, ms); // proximity criterion);
					
				
                     
                   
				}else
                     {
					classified_class = tree.classifyInstanceDensity(instance, ms); // density criterion
                     }
                   real_class = instance.classValue();
                   if (classified_class == real_class) {
             //   	   System.out.println("class="+classified_class+" real ="+real_class);
                     right++;
                     if (trace)
                       System.out.println(instance + " right");
                   }
                   else if (trace)
                     System.out.println(instance + " mistake");
                 }
                 

                   if(i_rep==9){
                	   i_rep=i_rep;
                   }
                 Accuracy[i_rep][j_fold] = (right) / test.numInstances();
                 Rights[i_rep][j_fold] = (int) right;
               }
               // AUC evaluation...
               if (AUC) cAUC[i_rep][j_fold]=computeAUC(tree,test);
              
              if (i_rep==(rep-1) && (j_fold==rep-1))
              {
            	  for (int ir = 0; ir < rep; ir++){
               System.out.println(ir+"  AUC"+cAUC[ir][ir] + " Accuracy"+Accuracy[ir][ir]);
            	  }
              }
             };

             java.util.Date ahora = new java.util.Date();
            System.out.println("Contador rep "+ i_rep +" "+ ahora.toString());
           };


         } catch(Exception e) {System.err.println("Error "+e.getMessage()+e.getCause().toString()+" traza \n"+e.getStackTrace().toString());};
       };

       
     
 /*
   Check the accuracy of id3 method
 */
 public void Checking_ID3(boolean trace, boolean AUC, boolean Acc)
 {
   Random random;
   int seed;
   double real_class, classified_class,right;
   Instances train, test;
   Enumeration enume;
   Instance instance;
   Id3 tree;

   seed=1;
   tree = new Id3();

   try
   {
     for (int i_rep = 0; i_rep < rep; i_rep++)
     {
       random = new Random(seed);
       dataSet.randomize(random); // shuffle the dataset
       for (int j_fold = 0; j_fold < folds; j_fold++)
       {
         //folding ...
         train = dataSet.trainCV(folds, j_fold);
         test = dataSet.testCV(folds, j_fold);
         // learning ...
         tree.buildClassifier(train);
         if (trace) System.out.println(tree.toString());
         // testing...
         // Accuracy evaluation
         if (Acc)
         {
           enume = test.enumerateInstances();
           right = 0.0;
           while (enume.hasMoreElements()) {
             instance = (Instance) enume.nextElement();
             classified_class = tree.classifyInstance(instance);
             real_class = instance.classValue();
             if (classified_class == real_class) {
               right++;
               if (trace)
                 System.out.println(instance + " right");
             }
             else if (trace)
               System.out.println(instance + " mistake");
           };
           
           if(Acc){
            System.out.println("ACC"+right /(double)test.numInstances());
           }
           Accuracy[i_rep][j_fold] = right / test.numInstances();
           Rights[i_rep][j_fold] = (int) right;
         }
         // AUC evaluation...
         
         System.out.println("rep "+i_rep+"fold "+j_fold+"val"+cAUC[i_rep][j_fold]);
         if (AUC) cAUC[i_rep][j_fold]=computeAUC(tree,test);
         //System.out.println(""+cAUC[i_rep][j_fold]+ "\n");
       };
     };
   } catch(Exception e) {
     System.err.println("Error "+e.getMessage());};
 };


 /*
   Check the accuracy of C4.5 method
 */
 public void Checking_C45(boolean trace, boolean AUC, boolean Acc)
 {
   Random random;
   int seed;
   double real_class, classified_class,right;
   Instances train, test;
   Enumeration enume;
   Instance instance;
   J48 tree;

   seed=1;
   tree = new J48();

   try
   {
     for (int i_rep = 0; i_rep < rep; i_rep++)
     {
       random = new Random(seed);
       dataSet.randomize(random); // shuffle the dataset
       for (int j_fold = 0; j_fold < folds; j_fold++)
       {
         //folding ...
         train = dataSet.trainCV(folds, j_fold);
         test = dataSet.testCV(folds, j_fold);
         // learning ...
         tree.setUnpruned(true);
         tree.buildClassifier(train);
         if (trace) System.out.println(tree.toString());
         // testing...
         // Accuracy evaluation ...
         if (Acc)
         {
           enume = test.enumerateInstances();
           right = 0.0;
           while (enume.hasMoreElements()) {
             instance = (Instance) enume.nextElement();
             classified_class = tree.classifyInstance(instance);
             real_class = instance.classValue();
             if (classified_class == real_class) {
               right++;
               if (trace)
                 System.out.println(instance + " rigth");
             }
             else if (trace)
               System.out.println(instance + " mistake");
           }
           ;
           Accuracy[i_rep][j_fold] = right / test.numInstances();
           Rights[i_rep][j_fold] = (int) right;
         }
         // AUC evaluation...
         if (AUC) cAUC[i_rep][j_fold]=computeAUC(tree,test);
         
         System.out.println("Mean AUc"+getMeanAUC());
         
         if ((i_rep==(rep-1)) && (j_fold==rep-1))
         {
       	  for (int ir = 0; ir <= rep; ir++){
          System.out.println("  AUC"+cAUC[ir][ir] + " Accuracy"+Accuracy[ir][ir]);
          
       	  }
         }
        
       };
     };
   } catch(Exception e) {
     System.err.println("Error "+e.getMessage());};
 };


 /*
   Compute AUC
 */
 private double computeAUC(Classifier cl, Instances test) throws Exception
 {
   Instances tinst,tinst1;
   EvaluationUtils eu;
   FastVector fpredictions;
   ThresholdCurve tc;

   test.numInstances();
   
   tinst=null;
   eu = new EvaluationUtils();
   eu.setSeed(10);
   fpredictions = new FastVector();
 
   //fpredictions =eu.getTestPredictions(cl, test) ;
   fpredictions =getTestPredictionswithStrings(cl, test);
   tc = new ThresholdCurve();
  System.out.println(fpredictions.toString());
   tinst=tc.getCurve(fpredictions);
   
 // if (tc.getROCArea(tinst)<0.5){
	//return 0.5;
	// }else
	 //return tc.getROCArea(tinst);
   getROCArea1(tinst,tc);
   System.out.println("tinst"+getROCArea1(tinst,tc));
   
   System.out.println(" accuracy "+calculateAccuracy(fpredictions));
 // return tc.getROCArea(tinst);
  return getROCArea1(tinst,tc);
   //return 0.5;
 };

/* private double computeAUC(Id3Metric cl, Instances test,MetricSpace ms) throws Exception
 {
   Instances tinst,tinst1;
   EvaluationUtils eu;
   FastVector fpredictions;
   ThresholdCurve tc;

   test.numInstances();
   
   tinst=null;
   eu = new EvaluationUtils();
   //eu.setSeed(10);
   fpredictions = new FastVector();
 
   fpredictions =eu.getTestPredictions(cl, test);//getTestPredictionswithStrings(cl, test);
   tc = new ThresholdCurve();
   tinst=tc.getCurve(fpredictions);
   double thi=tc.getThresholdInstance(dataSet, 0.95);
   
   //tinst1=tc.getCurve(fpredictions,(int)test.classIndex());

   System.out.println("Thresholdinst"+" accuracy "+calculateAccuracy(fpredictions)+ "AUC  "+tc.getROCArea(tinst)+" ");
   System.out.print(tc.getThresholdInstance(tinst,2));
   
  double var=tc.getROCArea(tinst);
   
   
   return tc.getROCArea(tinst);
   //return 0.5;
 };*/
 @SuppressWarnings("unused")
private double computeAUC(Classifier cl, Instances test, Instances train) throws Exception
 {
   Instances tinst,tinst1;
   EvaluationUtils eu;
   FastVector fpredictions;
   ThresholdCurve tc;

   test.numInstances();
   
   tinst=null;
   eu = new EvaluationUtils();
   eu.setSeed(10);
   fpredictions = new FastVector();
 
   fpredictions =eu.getTrainTestPredictions(cl,train,test);//eu.getTestPredictions(cl, test) ;//getTestPredictionswithStrings(cl, test);
   tc = new ThresholdCurve();
   tinst=tc.getCurve(fpredictions);
   //tinst1=tc.getCurve(fpredictions,(int)test.classIndex());
   //System.out.print(tinst1.toSummaryString());
   System.out.println(" accuracy "+calculateAccuracy(fpredictions));
   return tc.getROCArea(tinst);
   //return 0.5;
 };
 
 private double computeAUC(knnClassifier cl, Instances test) throws Exception
 {
   Instances tinst;
   EvalUtils eu;
   FastVector fpredictions;
   ThresholdCurve tc;

   tinst=null;
   eu = new EvalUtils();
   fpredictions = new FastVector();
   fpredictions = eu.getTestPredictions(cl, test);
   tc = new ThresholdCurve();
   tinst=tc.getCurve(fpredictions);
 
   return tc.getROCArea(tinst);
   //return 0.5;
 };

 
 public static double calculateAccuracy(FastVector predictions) {
     double correct = 0;
     
     for (int i = 0; i < predictions.size(); i++) {
         NominalPrediction np = (NominalPrediction) predictions.elementAt(i);
         if (np.predicted() == np.actual()) {
             correct++;
         }
     }
     
     return  correct / predictions.size();
 } 
 
 public FastVector getTestPredictionswithStrings(Id3Metric classifier, 
         Instances test, MetricSpace ms) 
throws Exception{   
	FastVector predictions = new FastVector();
	for (int i = 0; i < test.numInstances(); i++) {
		if (!test.instance(i).classIsMissing()) {
		predictions.addElement(getPredictionStridm(classifier, test.instance(i),ms));
			}
		}
	return predictions;
 }
 
 public Prediction getPredictionStridm(Id3Metric classifier, Instance test,MetricSpace ms)
		    throws Exception {

	 		 
	 		 double actual = test.classValue();
		    
		    ms.getDistanceMatrix((int) test.classValue());
		    
		    double distUno=classifier.classifyInstance(test, ms);
		    
		    double [] dist={0,0,0};
		    dist[(int) test.classValue()]=distUno;
		 
		    if (test.classAttribute().isNominal()) 
		    {
		    	return new NominalPrediction(actual, dist);
		    } 
		    if (test.classAttribute().isNumeric())
		    {
		    	//System.out.println("numeric");
		     return new NumericPrediction(actual, dist[0], test.weight());
		    }
		    else
		     { // devuelve numerico
		    //	System.out.println("String");
		       return new NumericPrediction(actual, dist[0], test.weight());
		    }  
		   
		    }

 
 
 public FastVector getTestPredictionswithStrings(Classifier classifier, 
                                      Instances test) 
		    throws Exception{   
		    FastVector predictions = new FastVector();
		    for (int i = 0; i < test.numInstances(); i++) {
		       if (!test.instance(i).classIsMissing()) {
		         predictions.addElement(getPredictionStr(classifier, test.instance(i)));
		      }
		   }
		     return predictions;
  }
 
 public Prediction getPredictionStr(Classifier classifier, Instance test)
    throws Exception {

    double actual = test.classValue();
    double [] dist = classifier.distributionForInstance(test);
    if (test.classAttribute().isNominal()) 
    {
    	return new NominalPrediction(actual, dist,test.weight());
    } 
    if (test.classAttribute().isNumeric())
    {
    	//System.out.println("numeric");
     return new NumericPrediction(actual, dist[0], test.weight());
    }
    else
     { // devuelve numerico
    //	System.out.println("String");
       return new NumericPrediction(actual, dist[0], test.weight());
    }  
   
    }
 

		 
 
 public  double getROCArea1(Instances tcurve, ThresholdCurve tc) {

	    final int n = tcurve.numInstances();
	    if (!tc.RELATION_NAME.equals(tcurve.relationName()) 
	        || (n == 0)) {
	      return Double.NaN;
	    }
	    final int tpInd = tcurve.attribute(tc.TRUE_POS_NAME).index();
	    final int fpInd = tcurve.attribute(tc.FALSE_POS_NAME).index();
	    final double [] tpVals = tcurve.attributeToDoubleArray(tpInd);
	    final double [] fpVals = tcurve.attributeToDoubleArray(fpInd);

	    double area = 0.0, cumNeg = 0.0;
	    final double totalPos = tpVals[0];
	    final double totalNeg = fpVals[0];
	    for (int i = 0; i < n; i++) {
		double cip, cin;
		if (i < (n - 1)) {
		    cip = tpVals[i] - tpVals[i + 1];
		    cin = fpVals[i] - fpVals[i + 1];
		} else {
		    cip = tpVals[n - 1];
		    cin = fpVals[n - 1];
		}
		area += cip * (cumNeg + (0.5 * cin));
		cumNeg += cin;
	    }
	    area /= (totalNeg * totalPos);

	    return area;
	  }
 
 
 /*
   Return AUC value
 */
 public double getMeanAUC()
 {
   double s=0.0;
   for (int i=0;i<rep;i++) for(int j=0;j<folds;j++) s+=cAUC[i][j];
   return s/(rep*folds);

 };

 /*
   Return AUC desviation standard value
 */
 public double getDesvAUC()
 {
   double s=0.0,aver;
   aver=getMeanAUC();
   for (int i=0;i<rep;i++) for(int j=0;j<folds;j++) s += Math.pow(cAUC[i][j] - aver, 2);
   //System.out.println(""+s+"\n");
   return Math.sqrt(s/(rep*folds-1));

 };


 /*
   Return the mean accuracy.
 */
 public double getMeanAccuracy()
 {
   double s=0;
   for (int i=0;i<rep;i++) for(int j=0;j<folds;j++) {s+=Accuracy[i][j];
 //  System.out.println("Accuracy["+i+"");
   }
 
   return s/(rep*folds);
 };

 /*
   Return the standard desviation.
 */
 public double getDesviation()
 {
   double s=0,aver;
   aver=getMeanAccuracy();
   for (int i=0;i<rep;i++) for(int j=0;j<folds;j++) s += Math.pow(Accuracy[i][j] - aver, 2);

   return Math.sqrt(s/(rep*folds-1));
 };

 /*
   Return the Right matrix: number of right classified instances in each fold and repetition
 */
 public int [][] getRightMatrix()
 {
   return Rights;
 };


 /*
   Return the Accuracy matrix: reached accuracy of each fold and repetition.
 */
  public double [][] getAccuracyMatrix()
  {
    return Accuracy;
  };

}; // end-of-class



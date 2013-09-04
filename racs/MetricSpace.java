/*
  Offer support for managing the metric space  (compute prototypes,
  compute distances, compute mean distances,  ...)
*/
package racs;

import java.util.*;
import java.lang.Double;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation.*;
import weka.core.*;

public class MetricSpace {

  private Vector SetOfMatrices;
  private SpaceLoader Loader;
  static int part=0;
  /*
    First constructor: construct a metric space from a vector of
    distance matrices.
  */
  public MetricSpace(Vector Matrices)
  {
    SetOfMatrices=Matrices;
  };


  /*
    Second constructor: construct an empty metric space.
  */
  public MetricSpace()
  {
    SetOfMatrices=new Vector();
  };


  /*
    Add a distance matrix to the metric space.
  */
  public void AddSpace(double s[][])
  {
    SetOfMatrices.add(s);
  };


  /*
     Return the distance matrix of an attribute
     according to the an index value identifier.
  */
  public double[][] getDistanceMatrix(int index)
  {
    return (double [][])SetOfMatrices.elementAt(index);
  };


 /*
   Return the distance matrix of an attribute
   according to the atributte.
 */
 public double[][] getDistanceMatrix(Attribute a)
 {
   return (double[][]) SetOfMatrices.elementAt(a.index());
 };


 /*
  Return the metric space.
 */
 public Vector getMetricSpace()
 {
   return SetOfMatrices;
 };


 /*
  Load a set of distance matrices from a file (in a proper format)
  to the memory.
 */
 public void getSpaceFromFile(String file) throws java.io.FileNotFoundException
 {
   Loader=new SpaceLoader(file);
   Loader.loadMetricSpaces(SetOfMatrices);
 };


 /*
  Return a set of intance-prototypes according to the attribute "a".
  m = the maximum number of prototypes to be selected.
  median = strategy to compute the prototype. Center just in the opposite case.
  Return an "double" array which means the true value for numerical attributes
  and index matrix value for the rest of the types (nominal or structured).
 */
 public double [] getPrototypes(Instances data, Attribute a, int m, int [] attributes_method)
 {

   int children; // the real number of prototypes will be selected
   int numberOfAttributeValues;
   Candidates [] prototypes;
   Vector classValues;

   classValues=new Vector();
   classValues=racsUtils.getDataClassValues(data);
   numberOfAttributeValues=racsUtils.getNumberAttributeValues(data, a);
   children=racsUtils.min(classValues.size(), numberOfAttributeValues, m);
   prototypes= new Candidates[classValues.size()];
   System.out.println( attributes_method[a.index()]+" "+ a.isNominal());
   if (attributes_method[a.index()]==0 || a.isNominal())
   {
     //System.out.println("particio "+part++);
     for (int i = 0; i < classValues.size(); i++) { // for each class value get N good candidates
       prototypes[i] = getNBestPrototypesClass(data, a, children,
                                               ( (Double) classValues.
                                                elementAt(i)).doubleValue());
       //View.viewCandidates("Printing candidates ...",prototypes[i]);
     }
     //View.viewPrototypes("Choosing the best",getNBestPrototypes(prototypes, a, children));
     return getNBestPrototypes(prototypes, a, children);
   }
   else
    { //System.out.println(" A PER EL CENTRE PERDUT ..."+part++);
      double [] c = getCenters(data, a, classValues.size());
      //for (int i=0;i<c.length;i++) System.out.println(c[i]);
      return c;
    }
 };


 /*
  Return the best (probably) N prototypes from the set "data" according to a given
  class (classIdentifier).
 */
 private Candidates getNBestPrototypesClass(Instances data, Attribute a, int N, double classIdentifier)
 {
   double md; // mean distance
   int i=0;
   Candidates candidates; // candidates to be the best prototypes for this class (classIdentifier)
   Instance inst;
   Enumeration enume;
   Vector valueUsed;

   enume=data.enumerateInstances();
   candidates = new Candidates(N);
   valueUsed = new Vector();

   while(enume.hasMoreElements())
   {
     inst = (Instance) enume.nextElement();

    double v= inst.value(a);
     if (!valueUsed.contains(new Double(inst.value(a))) &&
         inst.classValue() == classIdentifier)
     {
       valueUsed.add(new Double(inst.value(a)));
       md = meanDistance(inst, data, a);
       if (i < N) {
         candidates.add(inst, md);
         if (i == (N - 1)) { //View.viewCandidates("abans d'ordenar",candidates);
           candidates.sort();
           //View.viewCandidates("després d'ordenar", candidates);
         }
       }
       else
       {
           if (md < candidates.getMeanDistance(N - 1))
           {
             candidates.update(inst, md, N - 1);
             candidates.sort();
           }
        }
        i++;
      }
    }
    return candidates;
 };


 /*
   Return, maybe, the N best prototypes.
 */
 private  double [] getNBestPrototypes(Candidates [] candidates, Attribute a, int N)
 {
   Vector prototypes, usedValue, usedClass;
   Double thisClass, thisValue;
   double md, best_md;
   Instance chosen_candidate,inst;
   double [] a_prototypes; // prototypes values

   prototypes = new Vector();
   usedValue= new Vector();
   usedClass = new Vector();

   int i;

   i=0;
   while (i<N)
   {
     // Remember that each different Candidate object
     // is associated to a different class
     chosen_candidate=null;
     best_md=Double.MAX_VALUE;
     for (int j=0;j<candidates.length;j++)
     {
       // Remember that the first Instance, in every Candidate object,
       // is always the best one (the most compact).
       inst = candidates[j].getInstance(0);
       md = candidates[j].getMeanDistance(0);

       thisClass = new Double(inst.classValue());
       thisValue = new Double(inst.value(a));

       if (!usedClass.contains(thisClass) && !usedValue.contains(thisValue) && md < best_md )
       {
         best_md=md;
         chosen_candidate = inst;
       }
     }
     if (chosen_candidate !=null)
     {
       usedValue.add(new Double(chosen_candidate.value(a)));
       usedClass.add(new Double(chosen_candidate.classValue()));
       prototypes.add((Instance) chosen_candidate);
     }
     i++;
   }
   // make the Vector of prototypes into an ARRAY of prototypes
   a_prototypes=new double[prototypes.size()];
   for (int k=0; k<prototypes.size(); k++)
       a_prototypes[k]=((Instance) prototypes.elementAt(k)).value(a);
   return a_prototypes;
 };


 /*
   Get the center of a given attribute.
   n stands for the number of different classes in data.
 */
 public double [] getCenters(Instances data, Attribute att, int n)
 {
   /*if (att.isNumeric())*/ return getNumericCenters(data,att,n);
   //return null;
 };


 /*
   Return the centers of a given numeric attribute.
   By the moment, only one center for each class is calculated.
   n = number of different classes in data.
 */
 public double[] getNumericCenters(Instances data, Attribute att, int n)
 {
   Enumeration instances;
   Instance inst;
   double [] centers,sum_values,cardinality;
   int [] indexes;
   int i;
   Vector usedClass;

   instances=data.enumerateInstances();
   centers = new double[n]; sum_values= new double[n]; cardinality= new double[n];
   indexes = new int[data.numClasses()];
   usedClass = new Vector();
   i=0;
   View.viewData(data);
   while (instances.hasMoreElements())
   {
     inst=(Instance) instances.nextElement();
     if (!usedClass.contains(new Double(inst.classValue())))
     {
       System.out.println("trobe una classe del tipus "+inst.value(inst.classAttribute()));
       usedClass.add(new Double(inst.classValue()));
       indexes[(int) inst.classValue()]=i;
       i++;
     }
     sum_values[indexes[(int) inst.classValue()]]+=inst.value(att);
     cardinality[indexes[(int) inst.classValue()]]++;
   }
   for (int j=0;j<n;j++) centers[j]=sum_values[j]/cardinality[j];
   return centers;
 };


 /*
  Return the TOTAL distance between an instance and the rest of instances
  from "data" according to the attribute "a".
 */
 private double distance(Instance inst, Instances data, Attribute a)
 {
   double d;
   Enumeration enume;

   d=0.0;
   enume=data.enumerateInstances();
   while (enume.hasMoreElements())
   {
     Instance item = (Instance) enume.nextElement();
     if (item.classValue()==inst.classValue()) // both of them belonging to the same class
     {
       if (a.isNumeric()) d += Math.abs(inst.value(a)-item.value(a)); // there is no matrix distance for numerical attributes
       else d += ( (double[][]) SetOfMatrices.elementAt(a.index()))[ (int) inst.value(a)][ (int) item.value(a)];
     }
   };
   return d;
 };


 /*
    Return the MEAN distance between an instance and the rest of instances
    from "data" according to the attribute "a".
 */
 private double meanDistance(Instance inst, Instances data, Attribute a)
 {
   double d;
   int cardinality;
   Enumeration enume;
   int cont=0;
   d=0.0; cardinality=0;
   enume=data.enumerateInstances();
   while (enume.hasMoreElements())
   {

     Instance item = (Instance) enume.nextElement();
     String it=item.toString(a.index());
     
     String name= a.name();
     
     if (item.classValue()==inst.classValue()) // b1oth of them belonging to the same class
     {
    	 
    	 cont++;
    	 
    	// System.out.println(""+name+"  "+cont+"  "+a.index()
    			 /*+"   item"+item+"  "+inst.value(a)+"  "+item.value(a)); */
	    
    
       int idx=a.index();
       if (a.isNumeric()) d+=Math.abs(inst.value(a)-item.value(a)); // there is no matrix distance for numerical attributes
      //inst.value(a)

       else
       {
    	   
    	   
         double val = ( (double[][]) SetOfMatrices.elementAt(a.index()))[ (int)
             inst.value(a)][ (int) item.value(a)];
         d += ( (double[][]) SetOfMatrices.elementAt(a.index()))[ (int) inst.
             value(a)][ (int) item.value(a)];
         cardinality++;

     }
     }
   }
   if (cardinality >0) return d/cardinality;
   else return -1;
 };

};// end-of-class

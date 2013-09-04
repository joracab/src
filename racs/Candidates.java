/*
  Instances which are candidates to be a prototype.
  Note that for each class we get the N best candidates to become in prototypes.
  For this reason, the attributes are arrays, since the number of candidates
  per class could be more than one.
*/
package racs;

import weka.core.*;

public class Candidates {

  private Instance [] instances;
  private double [] meanDistance;
  private int [] indexes;
  private int free;

  /*
    Constructor
  */
  public Candidates(int n)
  {
    instances = new Instance [n];
    meanDistance = new double [n];
    indexes = new int[n];
    free=0;
  };


  /*
    Add a new instance and its meanDistance in a
    free position.
  */
  public void add(Instance instance, double mdist)
  {
    instances[free]=instance;
    meanDistance[free]=mdist;
    indexes[free]=free;
    free=(free+1)%(instances.length);
  };


  /*
    Update a position with a new instance and a meandistance.
  */
  public void update(Instance instance, double mdist, int position)
  {
    instances[indexes[position]]=instance;
    meanDistance[indexes[position]]=mdist;
  };


  /*
    Sort, in increasing order, the instances and the meanDistance arrays.
  */
  public void sort()
  {
    indexes=Utils.sort(meanDistance);
  };


  /*
     Return the meandistance from a given position.
  */
  public double getMeanDistance(int position)
  {
    return meanDistance[indexes[position]];
  };


 /*
  Return the value from a given position.
 */
 public Instance getInstance(int position)
 {
   return instances[indexes[position]];
 };


 /*
   Return the number of candidates
 */
  public int getNumberofCandidates()
  {
    return instances.length;
  };

}; //end-of-class

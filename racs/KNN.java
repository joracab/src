package racs;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
import racs.Frame;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import weka.core.*;
/**
 * This is the root class of jKNN and includes the core
 * of the algorithm.
 * @author Paul Lammertsma
 */
public class KNN implements KnnConstants {

        private static final long serialVersionUID = -1660494050190503761L;

       // public int size;
        public String[] KNN_FEATURE_LIST={};
        public static boolean doEuclidean = KNN_DISTANCE_EUCLIDEAN;
        public static boolean doRescale = KNN_DISTANCE_SCALED;
        public static boolean doScaleOutputs = KNN_SCALE_OUTPUTS;

        /**
         * Default constructor for jKNN.
         */
        public KNN() {

        }

        /**
         * Evaluates a situation to classify any unknown variables.
         * @param situation A {@link Situation} containing one or
         * 					more unknown {@link eValue}s.
         */
        public void evaluate(Situation situation,Instances ds, int nb) {
                clear();

                busy(true);

                ///.out.println("Gathering " + KNN_NEIGHBORS + " nearest neighbors to:\n  " + situation + "\n");

//// revisar 1como pasar los atributos a cadena pappoder clasificarlos
                String[] nearest = getNearest(situation, nb,ds);

                ///.out.println();

                KnnValue[] properties = situation.getProperties();
                KnnValue[] newProperties = new KnnValue[properties.length];
              /*  for (int =0; <properties.length; ++) {
                        if (properties[].isUnknown()) {
                                double sum = 0;
                                Map votes = new HashMap();
                                int maxVotes = 0;
                                double maxVoteIndex = 0.0f;
                                String set1 = "", set2 = "";
                                for (int j=0; j<nearest.length; j++) {
                                        if (doScaleOutputs) {
                                                sum += nearest[j].getProperty().getIndex();
                                        } else {
                                                double thisVote = nearest[j].getProperty().getIndex();
                                                int value = 0;
                                                if (votes.get(thisVote) != null){ value = votes.hashCode();
                                                value=value+1;}
                                                votes.put(thisVote, value);
                                                if (maxVotes < value) {
                                                        maxVotes = value;
                                                        maxVoteIndex = thisVote;
                                                }
                                        }
                                        if (j>0) {
                                                set1 += ", ";
                                                set2 += " + ";
                                        }
                                        set1 += nearest[j].getProperty().toString();
                                        set2 += nearest[j].getProperty().getIndex();
                                }
                                char chars[] = new char[KNN_FEATURE_LIST[].length()];
                                Arrays.fill(chars, ' ');
                                String placeholder = new String(chars);
                                //Feature feature;
                                if (doScaleOutputs) {
                                        ///.out.println(KNN_FEATURE_LIST[] + " = { " + set1 + " } / " + nearest.length);
                                        ///.out.println(placeholder + " = (" + set2 + ") / " + nearest.length);
                                        sum /= nearest.length;
                                        //feature = new Feature(sum, KNN_VALUE_LIST[]);
                                        ///.out.println(placeholder + " = " + sum);
                                } else {
                                        ///.out.println(KNN_FEATURE_LIST[] + " = MAX( " + set1 + " )");
                                       // feature = new Feature(maxVoteIndex, KNN_VALUE_LIST[]);
                                }
                                ///.out.println(placeholder + " = " + feature);
                                //newProperties[] = feature.getValue();
                        } else {
                                newProperties[] = properties[];
                        }
                }

                Situation newSituation = new Situation(newProperties);
                */

                ///.out.println("\nExpected situation result:\n  " + newSituation);

                busy(false);
        }

        /**
         * Determines nearest <em>n</em> neighbors of a given situation.
         * @param situation {@link Situation} to compare
         * @param knn_neighbors Number of neighbors to return
         * @return Nearest neighboring situations
         */
   //     @SuppressWarnings("unchecked")
        private String[] getNearest(Situation situation, int knn_neighbors,Instances ds){
                String[] nearest = new String[knn_neighbors];
                Map distances = new HashMap();

                /////.out.println("Distance to from " + situation + " to...");
                for (int i=0; i<ds.numInstances(); i++) {
                        distances.put(i, measureDistance(ds.firstInstance(), ds.instance(i)));
                        /////.out.println("..." + KnnSituation.getInput(i) + ": " + distances.get(i));
                }

                for (int n=0; n<knn_neighbors; n++) {
                        double shortestDistance = -1.0f;
                        int shortestIndex = 0;
                        // Cycle through all distances
                        Iterator it = distances.entrySet().iterator();
                        while (it.hasNext()) {
                                Entry distance = (Entry) it.next();
                                if (distance.getValue().equals(shortestDistance) || shortestDistance<0) {
                                        shortestDistance = distance.getValue().hashCode();
                                        shortestIndex = distance.getKey().hashCode();
                                }
                        }
                        highlight(shortestIndex);
                        if (KNN_LOG_NEAREST) {
                            System.out.println("Neighbor #" + (n+1) + ": "  + ":\n  distance = " + shortestDistance + "\n  " + ds.instance(shortestIndex).toString());
                        }
                        // Remove the current distance from the Map
                        nearest[n] = ds.instance(shortestIndex).toString();
                        distances.remove(shortestIndex);
                }
                return nearest;
        }

        /**
         * Calculates either the Euclidean or absolute distance
         * between two situations by calculating the distances
         * between the individual values.
         * @param situation1
         * @param situation2
         * @return Distance between the situations
         */
        private static double measureDistance(Instance situation1, Instance situation2) {
                double result = 0.0f;

                for (int i=0; i<situation1.dataset().numAttributes(); i++) {
                        if (!situation1.attribute(i).isString()) {
                              //  if (doEuclidean) {
                                        // Euclidean distance


                                        result += Math.pow((situation1.value(i)-situation2.value(i)), 2);
                                        //result +=(1/(Math.pow((situation1.getProperty(i).getIndex() - situation2.getProperty(i).getIndex(doRescale)), 2)))*( Math.pow((situation1.getProperty(i).getIndex() - situation2.getProperty(i).getIndex(doRescale)), 2));
                              //  } else {
                                        // Absolute ditance
                                    //    result += Math.abs((situation1.getProperty(i).getIndex() - situation2.getProperty(i).getIndex(doRescale)));
                               // }
                        }
                }
                result = Math.pow(result, 0.5);
                return result;
        }

        /**
         * Notifies jKNN that the application is busy.
         * @param isBusy Whether or not the process is busy
         */
        public void busy(boolean isBusy) {
                ///.out.progressSetVisible(isBusy);
        }

        /**
         * Notifies jKNN that the application is busy.
         * @param progress Progress of the process (0 to 100)
         */
        public void busy(int progress) {
  //              ///.out.updateProgress(progress);
        }

        /**
         * Clears any log information.
         */
        public void clear() {
    //            ///.out.clear();
        }

        /**
         * Highlights a situation for visual comparison.
         * @param index Index of situation in the dataset
         */
        public void highlight(int index) {}

       public String[] resizeArray(String[] array, int size)
       {
       String[] newarray= new String[size];

       System.arraycopy(array,0,newarray,0,array.length);
       return newarray;
       }

        public void setFeatureSize(int pos, String Value){

        KNN_FEATURE_LIST[pos]=Value;
        }

}

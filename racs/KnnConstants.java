package racs;


public interface KnnConstants {


  public final static int KNN_NEIGHBORS = 3;
      public final static boolean KNN_DISTANCE_EUCLIDEAN = true;
      public final static boolean KNN_DISTANCE_SCALED = true;
      public final static boolean KNN_SCALE_OUTPUTS = false;

      public final static boolean KNN_LOG_NEAREST = true;

      // Feature structure

      /**
       * KnnSituation.id represents the array of input IDs
       */
      public final static String KNN_DATASET_INPUTS = "Days";

      public final static String KNN_DATASET_INPUT = "Day ";

      /**
       * Format of feature list as it should be understood
       * by KNN.
       */

      /**
       * Format of feature list as it should be understood
       * by KNN, containing arrays of {@link KnnValue}s.
       */
      public KnnValue[][] KNN_VALUE_LIST = {

      };

      // Defaults

      /**
       * Default data set, following the format of features
       * as defined in <em>KNN_FEATURE_LIST</em>, containing
       * data entries for each data set. Each data set is
       * represented by an array of {@link KnnValue}s.
       */
      public KnnValue[][] dataSetSituations = {

      };

      /**
       * Default situation1 for evaluation, following the
       * format of features as defined in <em>KNN_FEATURE_LIST</em>.
       */
      public final static KnnValue[] situationString1 = {
            //  Outlook.SUNNY, Temperature.COOL, Humidity.HIGH, Wind.STRONG, PlayTennis.UNKNOWN
      };
      /**
       * Default situation2 for evaluation, following the
       * format of features as defined in <em>KNN_FEATURE_LIST</em>.
       */
      public final static KnnValue[] situationString2 = {
             //Outlook.RAIN, Temperature.HOT, Humidity.NORMAL, Wind.WEAK, PlayTennis.UNKNOWN
      };

      // Set up the situations from above

      public static Situation situation1 = new Situation(situationString1);
      public static Situation situation2 = new Situation(situationString2);

      public  Situation[] dataSet = new Situation[dataSetSituations.length];

}

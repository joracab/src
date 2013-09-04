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
/**
 * Representation of a situation.
 * <p>A situation contains a series of {@link Feature}s as
 * defined in <em>KNN_FEATURE_LIST</em>. These features need
 * not necessarily be defined as a {@link KnnValue} can
 * take on the value <em>UNKNOWN</em> (-1).</p>

 */
public class Situation implements KnnConstants {

        private KnnValue[] properties;

        /**
         * Creates a new {@link Situation} containing the passed
         * properties.
         * @param properties An array of {@link KnnValue}s.
         */
        public Situation(KnnValue[] properties) {
                this.properties = properties;
        }

        /**
         * Returns a single property.
         * @param index Index of the desired property.
         * @return Property
         */
        public KnnValue getProperty(int index) {
                return this.properties[index];
        }

        /**
         * Returns all properties.
         * @return Array of properties
         */
        public KnnValue[] getProperties() {
                return this.properties;
        }

        /**
         * Returns the identifying string of a single property
         * @param index Index of the desired property ID.
         * @return Identifying string of the property
         */
        public String getID(int index) {
                return this.properties[index].getID();
        }

        /**
         * Returns the descriptor of the data set (e.g. "Entries").
         * @return Descriptor
         */
        public static String getInput() {
                return KNN_DATASET_INPUTS;
        }

        /**
         * Returns the descriptor of an entry in the data set
         * (e.g. "Entry 1").
         * @param index Index of the entry in the data set
         * @return Descriptor of the entry
         */
        public static String getInput(int index) {
                return KNN_DATASET_INPUT;
        }

        /**
         * Replaces the properties of this {@link Situation}
         * instance.
         * @param properties Replacing array of {@link KnnValue}s
         */
        public void setProperties(KnnValue[] properties) {
                this.properties = properties;
        }

        /**
         * Returns a string representation of a {@link Situation}.
         */
        public String toString() {
                String result = "";
                for (int i=0; i<this.properties.length; i++) {
                        if (i>0) result.concat(", ");
                        if (properties[i].isUnknown()) {
                                result.concat("??");
                        } else {
                                result= result+ this.properties[i].getID();
                        }
                }
                return "(" + result + ")";
        }

        /**
         * Calculates the scaled values of the {@link KnnValue}s of the
         * data set.
         * @see knn.KnnValue#calculateScaled(double, double)
         * @see #getMean(int, Situation[])
         * @see #getSigma(int, Situation[], double)
         * @param dataSet
         */
        public void createScales(Situation[] dataSet) {
                for (int i=0; i<properties.length; i++) {
                        double mean = getMean(i, dataSet);
                        properties[i].calculateScaled(mean, getSigma(i, dataSet, mean));
                }
        }

        /**
         * Calculate the mean of the data set over feature <em>j</em>:
         * <p><em>X</em><sub>mean</sub> = 1/<em>N</em> * SUM(<em>X</em><sub><em>i</em>,<em>j</em></sub>)
         * <br />Where <em>x</em> is the data set {<em>X</em><sub><em>0</em>,<em>j</em></sub>,...,<em>X</em><sub><em>N</em>,<em>j</em></sub>} and <em>j</em> is a feature.</p>
         * @see #getSigma(int, Situation[], double)
         * @param index The feature to use for calculation (<em>j</em> in the calculation above)
         * @param dataSet The data set to calculate over (<em>X</em> in the calculation above)
         * @return Mean of the data set over feature <em>j</em>:
         */
        public double getMean(int index, Situation[] dataSet) {
                double mean = 0;
                for (int i=0; i<dataSet.length; i++) {
                        mean += dataSet[i].getProperty(index).getIndex();
                }
                return mean / dataSet.length;
        }

        /**
         * Calculate the standard deviation's sigma value of the data set over feature <em>j</em>:
         * <p>sigma = ( 1/<em>N</em> * SUM(<em>X</em><sub><em>i</em>,<em>j</em></sub> - <em>X</em><sub>mean</sub>)<sup>2</sup> )<sup>0.5</sup>
         * <br />Where <em>x</em> is the data set {<em>X</em><sub><em>0</em>,<em>j</em></sub>,...,<em>X</em><sub><em>N</em>,<em>j</em></sub>} and <em>j</em> is a feature.</p>
         * @see #getMean(int, Situation[])
         * @param index The feature to use for calculation (<em>j</em> in the calculation above)
         * @param dataSet The data set to calculate over (<em>X</em> in the calculation above)
         * @param mean The mean of the data set (<em>X</em><sub>mean</sub> in the calculation above)
         * @return Sigma value of the data set over feature <em>j</em>.
         */
        public double getSigma(int index, Situation[] dataSet, double mean) {
                double sigma = 0;
                for (int i=0; i<dataSet.length; i++) {
                        sigma += Math.pow(dataSet[i].getProperty(index).getIndex() - mean, 2);
                }
                return Math.pow(sigma / dataSet.length, 0.5);
        }
}

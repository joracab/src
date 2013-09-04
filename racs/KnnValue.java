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

public class KnnValue {
        private double value, scaledValue;
        private String id;

        /**
         * Creates an instance of a feature value without an
         * accompanying identifying string.
         * @param value
         */
        public KnnValue(double value) {
                this.value = value;
                this.id = null;
        }

        /**
         * Creates an instance of a feature value with an
         * accompanying identifying string.
         * @param value
         * @param id
         */
        public KnnValue(double value, String id) {
                this.value = value;
                this.id = id;
        }

        /**
         * Returns the numeric value of the {@link KnnValue}.
         * @return Value
         */
        public double getIndex() {
                return getIndex(false);
        }

        /**
         * Returns the (scaled) value of the {@link KnnValue}.
         * @param scaled Whether or not to return the scaled value
         * @return Value
         */
        public double getIndex(boolean scaled) {
                if (scaled) return scaledValue;
                return value;
        }

        /**
         * Returns the identifying string of the {@link KnnValue}.
         * @return Identifying string
         */
        public String getID() {
                return id;
        }

        /**
         * Calculates the scaled value from the original value
         * using the mean and sigma of the data set.
         * @param mean Mean value of the data set.
         * @param sigma Sigma value of the data set (from the standard deviation).
         */
        public void calculateScaled(double mean, double sigma) {
                scaledValue = (value - mean) / sigma;
        }

        /**
         * Returns whether or not this value is the reserved
         * <em>UNKNOWN</em> (-1) identifier.
         * @return Whether or not this value is unknown.
         */
        public boolean isUnknown() {
                return value<0;
        }

        /**
         * Returns a string representation of {@link KnnValue}.
         */
        public String toString() {
                return id;
        }
}

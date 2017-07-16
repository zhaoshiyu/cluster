package com.zhshyu.math;

/**
 * distance measures of n-dimensional vectors.
 * 
 * @author Zhao Shiyu
 *
 */
public class Distance {

	/**
	 * Calculates the Canberra distance between two points.
	 * 
	 * @param pointA
	 * @param pointB
	 * @return
	 */
	public static double canberraDistance(double[] pointA, double[] pointB) {
		checkMatrixDimensions(pointA, pointB);
		double sum = 0;
        for (int i = 0; i < pointA.length; i++) {
            final double num = Math.abs(pointA[i] - pointB[i]);
            final double denom = Math.abs(pointA[i]) + Math.abs(pointB[i]);
            sum += num == 0.0 && denom == 0.0 ? 0.0 : num / denom;
        }
        return sum;
	}
	
	/**
	 * Calculates the L<sub>&infin;</sub> (max of abs) distance between two points.
	 * 
	 * @param pointA
	 * @param pointB
	 * @return
	 */
	public static double chebyshevDistance(double[] pointA, double[] pointB) {
		checkMatrixDimensions(pointA, pointB);
		double max = 0;
        for (int i = 0; i < pointA.length; i++) {
            max = Math.max(max, Math.abs(pointA[i] - pointB[i]));
        }
        return max;
	}
	
	/**
	 * Calculates the Earh Mover's distance (also known as Wasserstein metric) between two distributions.
	 * 
	 * @param pointA
	 * @param pointB
	 * @return
	 */
	public static double earthMoversDistance(double[] pointA, double[] pointB) {
		checkMatrixDimensions(pointA, pointB);
		double lastDistance = 0;
        double totalDistance = 0;
        for (int i = 0; i < pointA.length; i++) {
            final double currentDistance = (pointA[i] + lastDistance) - pointB[i];
            totalDistance += Math.abs(currentDistance);
            lastDistance = currentDistance;
        }
        return totalDistance;
	}
	
	/**
	 * Calculates the L<sub>2</sub> (Euclidean) distance between two points.
	 * 
	 * @param pointA
	 * @param pointB
	 * @return
	 */
	public static double euclideanDistance(double[] pointA, double[] pointB) {
		checkMatrixDimensions(pointA, pointB);
		double sum = 0;
        for (int i = 0; i < pointA.length; i++) {
            final double dp = pointA[i] - pointB[i];
            sum += dp * dp;
        }
        return Math.sqrt(sum);
	}
	
	/**
	 * Calculates the L<sub>1</sub> (sum of abs) distance between two points.
	 * 
	 * @param pointA
	 * @param pointB
	 * @return
	 */
	public static double manhattanDistance(double[] pointA, double[] pointB) {
		checkMatrixDimensions(pointA, pointB);
		double sum = 0;
        for (int i = 0; i < pointA.length; i++) {
            sum += Math.abs(pointA[i] - pointB[i]);
        }
        return sum;
	}

	/**
	 * 
	 * @param pointA
	 * @param pointB
	 */
	private static void checkMatrixDimensions(double[] pointA, double[] pointB) {
		if (pointA.length != pointB.length) {
			throw new IllegalArgumentException("dimensions must agree.");
		}
	}

}

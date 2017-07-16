package com.zhshyu.math;

/**
 * 
 * 常用函数操作类
 * 
 * @author Zhao Shiyu
 *
 */
public class Function {
	
	/**
	 * standard gaussian
	 * @param x
	 * @return double
	 */
	public static double standardGaussian(double x) {
        return Math.exp(-x * x / 2) / Math.sqrt(2 * Math.PI);
    }
	
	/**
	 * gaussian
	 * @param x variable
	 * @param mu expectation
	 * @param sigma standard deviation
	 * @return double
	 */
    public static double gaussian(double x, double mu, double sigma) {
        return standardGaussian((x - mu) / sigma) / sigma;
    }
    
    /**
     * gaussian
     * @param distance distance
     * @param sigma standard deviation
     * @return double
     */
	public static double gaussian(double distance, double sigma) {
		return standardGaussian(distance / sigma) / sigma;
	}
	
//    /**
//     * gaussian
//     * @param distance distance
//     * @param sigma standard deviation
//     * @return double
//     */
//	public static double gaussian(double distance, double sigma) {
//		return Math.exp(-0.5 * Math.pow(distance / sigma, 2)) / (sigma * Math.sqrt(2 * Math.PI));
//	}
//	
//	/**
//	 * gaussian
//	 * @param variable variable
//	 * @param expectation expectation
//	 * @param standardDeviation standard deviation
//	 * @return double
//	 */
//	public static double gaussian(double variable, double expectation, double standardDeviation) {
//		return gaussian(variable - expectation, standardDeviation);
//		//return (1.0 / (standardDeviation * Math.sqrt(2 * Math.PI))) * Math.exp(-0.5 * Math.pow((variable - expectation) / standardDeviation, 2));
//	}
	
	/**
	 * sigmoid
	 * @param k
	 * @param alpha
	 * @param beta
	 * @param x
	 * @return double
	 */
	public static double sigmoid(double k, double alpha, double beta, double x) {
		return k / (1.0 + alpha * Math.exp(-(beta * x)));
	}
	
	/**
	 * sigmoid
	 * @param alpha
	 * @param beta
	 * @param x
	 * @return double
	 */
	public static double sigmoid(double alpha, double beta, double x) {
		return sigmoid(1.0, alpha, beta, x);
	}
	
	/**
	 * sigmoid
	 * @param beta
	 * @param x
	 * @return double
	 */
	public static double sigmoid(double beta, double x) {
		return sigmoid(1.0, beta, x);
	}
	
	/**
	 * sigmoid
	 * @param x
	 * @return double
	 */
	public static double sigmoid(double x) {
		return sigmoid(1.0, x);
	}
	
	/**
	 * tanh
	 * @param x
	 * @return double
	 */
	public static double tanh(double x) {
		return (Math.exp(x) - Math.exp(-x)) / (Math.exp(x) + Math.exp(-x));
	}

}

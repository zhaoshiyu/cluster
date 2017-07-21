package com.zhshyu.ml.cluster.core;

/**
 * 
 * @author Zhao Shiyu
 *
 */
public interface Clusterable {
	
	double[] getValue();
	
	PointStatus getStatus();
	
	void setStatus(PointStatus status);
	
	Clusterable clone();
	
	

}

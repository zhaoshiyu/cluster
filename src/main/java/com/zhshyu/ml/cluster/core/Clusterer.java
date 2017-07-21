package com.zhshyu.ml.cluster.core;

import com.zhshyu.math.Distance;

/**
 * 
 * @author Zhao Shiyu
 *
 * @param <T>
 */
public abstract class Clusterer<T extends Clusterable> implements Clustering<T> {
	
	protected double distance(final Clusterable PointA, final Clusterable pointB) {
        return Distance.euclideanDistance(PointA.getValue(), pointB.getValue());
    }

}

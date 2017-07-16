package com.zhshyu.ml.cluster.core;

import com.zhshyu.math.Distance;

/**
 * 
 * @author Zhao Shiyu
 *
 */
public abstract class Clusterer implements clustering {
	
	protected double distance(final Point PointA, final Point pointB) {
        return Distance.euclideanDistance(PointA.getValue(), pointB.getValue());
    }

}

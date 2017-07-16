package com.zhshyu.ml.cluster.core;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Zhao Shiyu
 *
 */
public class Cluster implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2900667976735008884L;
	private final List<Point> points;
	
    public Cluster() {
        points = new LinkedList<Point>();
    }
    
    void addPoint(final Point point) {
        points.add(point);
    }
    
    List<Point> getPoints() {
        return points;
    }

}

package com.zhshyu.ml.cluster.core;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Zhao Shiyu
 *
 * @param <T>
 */
public class Cluster<T extends Clusterable>  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2900667976735008884L;
	private final List<T> points;
	
    public Cluster() {
        this.points = new LinkedList<T>();
    }
    
    public Cluster(final T point) {
        this.points = new LinkedList<T>();
        this.points.add(point);
    }
    
    public Cluster(final Collection<T> points) {
        this.points = new LinkedList<T>();
        this.points.addAll(points);
    }
    
    void addPoint(final T point) {
        this.points.add(point);
    }
    
    List<T> getPoints() {
        return points;
    }

}

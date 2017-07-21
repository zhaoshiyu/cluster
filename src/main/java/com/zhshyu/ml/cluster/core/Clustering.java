package com.zhshyu.ml.cluster.core;

import java.util.Collection;
import java.util.List;

/**
 * 
 * @author Zhao Shiyu
 *
 * @param <T>
 */
public interface Clustering<T extends Clusterable> {
	
	List<Cluster<T>> cluster(Collection<T> points);
	
}

package com.zhshyu.ml.cluster.core;

import java.util.Collection;
import java.util.List;

/**
 * 
 * @author Zhao Shiyu
 *
 */
public interface clustering {
	
	List<Cluster> cluster(Collection<Point> points);
	
}

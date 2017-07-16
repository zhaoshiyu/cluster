package com.zhshyu.ml.cluster.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DBSCAN cluster algorithm
 * 
 * @author Zhao Shiyu
 *
 */
public class DBSCAN extends Clusterer {

	private final int minPoints;

	private final double epsilon;

	public DBSCAN(int minPoints, double epsilon) {
		this.minPoints = minPoints;
		this.epsilon = epsilon;
	}

	public int getMinPoints() {
		return minPoints;
	}

	public double getEpsilon() {
		return epsilon;
	}

	private enum PointStatus {
		NOISE,
		PART_OF_CLUSTER
	}

	@Override
	public List<Cluster> cluster(Collection<Point> points) {
		final List<Cluster> clusters = new LinkedList<Cluster>();
		final Map<Point, PointStatus> visited = new HashMap<Point, PointStatus>();

		for (final Point point : points) {
			if (visited.get(point) != null) {
				continue;
			}
			final List<Point> neighbors = getNeighbors(point, points);
			if (neighbors.size() >= minPoints) {
				final Cluster cluster = new Cluster();
				clusters.add(expandCluster(cluster, point, neighbors, points, visited));
			} else {
				visited.put(point, PointStatus.NOISE);
			}
		}

		return clusters;
	}

	private Cluster expandCluster(final Cluster cluster, final Point point, final List<Point> neighbors,
			final Collection<Point> points, final Map<Point, PointStatus> visited) {
		cluster.addPoint(point);
		visited.put(point, PointStatus.PART_OF_CLUSTER);

		List<Point> seeds = new ArrayList<Point>(neighbors);
		int index = 0;
		while (index < seeds.size()) {
			final Point current = seeds.get(index);
			PointStatus pStatus = visited.get(current);
			// only check non-visited points
			if (pStatus == null) {
				final List<Point> currentNeighbors = getNeighbors(current, points);
				if (currentNeighbors.size() >= minPoints) {
					seeds = merge(seeds, currentNeighbors);
				}
			}

			if (pStatus != PointStatus.PART_OF_CLUSTER) {
				visited.put(current, PointStatus.PART_OF_CLUSTER);
				cluster.addPoint(current);
			}

			index++;
		}
		return cluster;
	}
	
	private List<Point> getNeighbors(final Point point, final Collection<Point> points) {
		final List<Point> neighbors = new ArrayList<Point>();
		for (final Point neighbor : points) {
			if (point != neighbor && distance(neighbor, point) <= epsilon) {
				neighbors.add(neighbor);
			}
		}
		return neighbors;
	}
	
	List<Point> merge(final List<Point> one, final List<Point> two) {
		final Set<Point> oneSet = new HashSet<Point>(one);
		for (Point item : two) {
			if (!oneSet.contains(item)) {
				one.add(item);
			}
		}
		return one;
	}

}

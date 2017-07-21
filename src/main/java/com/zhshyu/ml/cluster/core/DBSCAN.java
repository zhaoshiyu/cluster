package com.zhshyu.ml.cluster.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * DBSCAN cluster algorithm
 * 
 * @author Zhao Shiyu
 *
 * @param <T>
 */
public class DBSCAN<T extends Clusterable> extends Clusterer<T> {

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

	@Override
	public List<Cluster<T>> cluster(Collection<T> points) {
		final List<Cluster<T>> clusters = new LinkedList<Cluster<T>>();

		for (final T point : points) {
			if (point.getStatus() != PointStatus.ORIGINAL) {
				continue;
			}
			final List<T> neighbors = neighbors(point, points);
			if (neighbors.size() >= minPoints) {
				final Cluster<T> cluster = new Cluster<T>();
				clusters.add(expandCluster(cluster, point, neighbors, points));
			} else {
				point.setStatus(PointStatus.NOISE);
			}
		}

		return clusters;
	}

	private Cluster<T> expandCluster(final Cluster<T> clusterTerm, final T point, final List<T> neighbors, final Collection<T> points) {
		clusterTerm.addPoint(point);
		point.setStatus(PointStatus.PART_OF_CLUSTER);

		List<T> seeds = new ArrayList<T>(neighbors);
		int index = 0;
		while (index < seeds.size()) {
			final T current = seeds.get(index);
			PointStatus pStatus = current.getStatus();
			// only check non-visited points
			if (pStatus == PointStatus.ORIGINAL) {
				final List<T> currentNeighbors = neighbors(current, points);
				if (currentNeighbors.size() >= minPoints) {
					seeds = merge(seeds, currentNeighbors);
				}
			}

			if (pStatus != PointStatus.PART_OF_CLUSTER) {
				current.setStatus(PointStatus.PART_OF_CLUSTER);
				clusterTerm.addPoint(current);
			}

			index++;
		}
		return clusterTerm;
	}

	/**
	 * Returns a list of density-reachable neighbors of a {@code point}.
	 *
	 * @param point
	 *            the point to look for
	 * @param points
	 *            possible neighbors
	 * @return the List of neighbors
	 */
	private List<T> neighbors(final T point, final Collection<T> points) {
		final List<T> neighbors = new ArrayList<T>();
		for (final T neighbor : points) {
			if (point != neighbor && distance(neighbor, point) <= epsilon) {
				neighbors.add(neighbor);
			}
		}
		return neighbors;
	}

	/**
	 * Merges two lists together.
	 *
	 * @param one
	 *            first list
	 * @param two
	 *            second list
	 * @return merged lists
	 */
	private List<T> merge(final List<T> one, final List<T> two) {
		final Set<T> oneSet = new HashSet<T>(one);
		for (T item : two) {
			if (!oneSet.contains(item)) {
				one.add(item);
			}
		}
		return one;
	}

}

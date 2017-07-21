package com.zhshyu.ml.cluster.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * MeanShift cluster algorithm
 * 
 * @author Zhao Shiyu
 *
 * @param <T>
 */
public class MeanShift<T extends Clusterable> extends Clusterer<T> {
	
	private final static double MIN_DISTANCE = 0.000001;
	private final static double GROUP_DISTANCE_TOLERANCE = 0.1;
	private final static int TOTAL_STEP = 200000;
		
	private final double kernelBandwidth;
	
	public MeanShift(double kernelBandwidth) {
		this.kernelBandwidth = kernelBandwidth;
	}
		
	public double getKernelBandwidth() {
		return kernelBandwidth;
	}
	
	@Override
	public List<Cluster<T>> cluster(Collection<T> points) {
		return cluster((List<T>)points);
	}
	
	
	private List<Cluster<T>> cluster(List<T> points) {
		int size = points.size();
		//final List<T> shiftPoints = new ArrayList<T>(size);
		final List<T> shiftPoints = deepCopy(points);
		double maxMinDist = 1;
		int iterationNumber = 0;
		while(maxMinDist > MIN_DISTANCE && iterationNumber <= TOTAL_STEP) {
			if(iterationNumber == TOTAL_STEP) System.err.println("MeanShift达到最大迭代上限：" + TOTAL_STEP + ",\tmaxMinDist: " + maxMinDist);
			maxMinDist = 0;
			++iterationNumber;
			for(int i = 0; i < size; ++i) {
				if(shiftPoints.get(i).getStatus() != PointStatus.ORIGINAL) {
					continue;
				}
				T pNew = shiftPoints.get(i);
				T pNewStart = (T) pNew.clone();
				//T pNewStart = points.get(i);
				pNew = shiftPoint(pNew, points);
				double dist = distance(pNew, pNewStart);
				if(dist > maxMinDist) maxMinDist = dist;
				if(dist < MIN_DISTANCE) {
					pNew.setStatus(PointStatus.VISITED);
					//data.set(i, pNewStart);
				}
				shiftPoints.set(i, pNew);
				
			}
			//System.out.println("STEP: " + iterationNumber + "\tmaxMinDist: " + maxMinDist);
			if((iterationNumber % 20000) == 0) System.out.println("STEP: " + iterationNumber + "\tmaxMinDist: " + maxMinDist);
		}
		return groupPoints(points, shiftPoints);
	}

	
	private T shiftPoint(T point, List<T> points) {
		int size = points.size();
		int dataDim = point.getValue().length;
		double[] distances = new double[size];
		
		for(int i = 0; i < size; ++i) {
			distances[i] = distance(point, points.get(i));
		}
		double[] pointWeights = gaussian(distances);
		double denominator = 0;
		double[] shiftedPointValue = new double[dataDim];
		for(int i = 0; i < size; ++i) {
			denominator += pointWeights[i];
			for(int j = 0; j < dataDim; ++j) {
				shiftedPointValue[j] += pointWeights[i] * points.get(i).getValue()[j];
			}
		}
		for(int j = 0; j < dataDim; ++j) {
			point.getValue()[j] = shiftedPointValue[j] / denominator;
		}
		return point;
	}
	
	private double gaussian(double distance, double bandwidth) {
		return Math.exp(-0.5 * Math.pow(distance / bandwidth, 2)) / (bandwidth * Math.sqrt(2 * Math.PI));
	}
	
	private double[] gaussian(double[] distances) {
		int len = distances.length;
		double[] ret = new double[len];
		for(int i = 0; i < len; ++i) {
			ret[i] = gaussian(distances[i], kernelBandwidth);
		}
		return ret;
	}
	
	private List<Cluster<T>> groupPoints(List<T> shiftPoints) {
		if(shiftPoints == null) return null;
		int size = shiftPoints.size();
		//int[] groupAssignment = new int[size];
		Map<Integer, Cluster<T>> groups = new HashMap<Integer, Cluster<T>>();
		int groupIndex = 0;
		Cluster<T> cluster;
		for(int i = 0; i < size; ++i) {
			T meanPoint = shiftPoints.get(i);
			Integer nearestGroupIndex = determineNearestGroup(meanPoint, groups);
			if(nearestGroupIndex == null) {
				cluster = new Cluster<T>();
				cluster.addPoint(meanPoint);
				groups.put(groupIndex, cluster);
				//groupAssignment[i] = groupIndex;
				++groupIndex;
			} else {
				cluster = groups.get(nearestGroupIndex);
				cluster.addPoint(meanPoint);
				groups.put(nearestGroupIndex, cluster);
				//groupAssignment[i] = nearestGroupIndex;
			}
		}
		
		List<Cluster<T>> clusters = new ArrayList<Cluster<T>>(groups.size());
		for(Map.Entry<Integer, Cluster<T>> entry : groups.entrySet()) {
			clusters.add(entry.getValue());
		}
		return clusters;
	}
	
	private List<Cluster<T>> groupPoints(List<T> points, List<T> shiftPoints) {
		if(shiftPoints == null) return null;
		int size = shiftPoints.size();
		//int[] groupAssignment = new int[size];
		Map<Integer, Cluster<T>> groups = new HashMap<Integer, Cluster<T>>();
		List<Cluster<T>> clusters = new LinkedList<Cluster<T>>();
		int groupIndex = 0;
		for(int i = 0; i < size; ++i) {
			T meanPoint = shiftPoints.get(i);
			Integer nearestGroupIndex = determineNearestGroup(meanPoint, groups);
			if(nearestGroupIndex == null) {
				clusters.add(groupIndex, new Cluster<T>(points.get(i)));
				
				groups.put(groupIndex, new Cluster<T>(meanPoint));
				
				++groupIndex;
			} else {
				//Cluster<T> cluster = new Cluster<T>();
				clusters.get(nearestGroupIndex).addPoint(points.get(i));
				//cluster.addPoint(points.get(i));
				
				groups.get(nearestGroupIndex).addPoint(points.get(i));
				
			}
			
		}
		return clusters;
	}
		
	private Integer determineNearestGroup(T meanPoint, Map<Integer, Cluster<T>> groups) {
		Integer nearestGroupIndex = null;
		int index = 0;
		for(Map.Entry<Integer, Cluster<T>> entry : groups.entrySet()) {
			double minDistance = distance2Group(meanPoint, entry.getValue());
			if(minDistance < GROUP_DISTANCE_TOLERANCE) nearestGroupIndex = index;
			++index;			
		}
		return nearestGroupIndex;
	}
	
	private double distance2Group(T meanPoint, Cluster<T> cluster) {
		double minDistance = Double.MAX_VALUE;
		for(T pt : cluster.getPoints()) {
			double dist = distance(meanPoint, pt);
			if(dist < minDistance) minDistance = dist;
		}
		return minDistance;
	}
	
	private void checkDimensions(double[] pointA, double[] pointB) {
		if (pointA.length != pointB.length) {
			throw new IllegalArgumentException("Dimensions must agree.");
		}
	}
	
	//private <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {  
	private <T> List<T> deepCopy(List<T> src) {
		List<T> dest = null;
	    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();  
	    ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(byteOut);
			out.writeObject(src);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	  
	    ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());  
	    ObjectInputStream in;
		try {
			in = new ObjectInputStream(byteIn);
			dest = (List<T>) in.readObject();  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	    //@SuppressWarnings("unchecked")  
	    //List<T> dest = (List<T>) in.readObject();  
	    return dest;  
	} 
	
//	public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {  
//	    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();  
//	    ObjectOutputStream out = new ObjectOutputStream(byteOut);  
//	    out.writeObject(src);  
//	  
//	    ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());  
//	    ObjectInputStream in = new ObjectInputStream(byteIn);  
//	    @SuppressWarnings("unchecked")  
//	    List<T> dest = (List<T>) in.readObject();  
//	    return dest;  
//	} 
	
		
}

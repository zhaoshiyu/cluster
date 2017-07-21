package com.zhshyu.ml.cluster.core;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 
 * @author Zhao Shiyu
 *
 */
public class Point implements Clusterable, Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1768954550612374057L;
	
	/**
	 * 一个样本数据的值，数组长度对应数据维数
	 */
	private final double[] value;
	
	private PointStatus status;
	
	public Point(double[] value) {
		this(value, PointStatus.ORIGINAL);
	}
	
	public Point(double[] value, PointStatus status) {
		this.value = value;
		this.status = status;
	}

	@Override
	public double[] getValue() {
		return value;
	}
	
	@Override
	public PointStatus getStatus() {
		return status;
	}
	
	@Override
	public void setStatus(PointStatus status) {
		this.status = status;
	}

	@Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }
	
	@Override
	public String toString() {
		return Arrays.toString(value);
	}
	
	public Point clone() {
		int len = value.length;
		double[] newValue = new double[len];
		for(int i = 0; i < len; ++i) {
			newValue[i] = value[i];
		}
		return new Point(newValue, status);
	}

}

package com.zhshyu.ml.cluster.core;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 
 * @author Zhao Shiyu
 *
 */
public class Point implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1768954550612374057L;
	
	/**
	 * 一个样本数据的值，数组长度对应数据维数
	 */
	private final double[] value;
	
	public Point(double[] value) {
		this.value = value;
	}
	
	public double[] getValue() {
		return value;
	}
	
	@Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }
	
	@Override
	public String toString() {
		return Arrays.toString(value);
	}

}

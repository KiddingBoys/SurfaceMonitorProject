package com.whut.surfacemonitorproject_wjj.utils;

import android.util.Log;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 过滤器
 * @author wujiajun
 */
public class WeightFilter {
	private final double[] w = {0.2, 0.3, 0.5};
	private Queue<Double> mHistory = new LinkedList<Double>();
	
	public WeightFilter() {
		clear();
	}
	
	public boolean parse(boolean in) {
		mHistory.poll();
		if (in) mHistory.offer(1.0);
		else    mHistory.offer(-1.0);
		
		Iterator<Double> it = mHistory.iterator();
		double ret = 0;
		int index = 0;
		while(it.hasNext()) {
			double temp = it.next();
			Log.e("", "ret temp : " + temp);
			ret += temp * w[index];
			index++;
		}
		// bigger than 0.0, means the final result is bigger with history param
		Log.e("", "ret : " + ret);
		return (ret >= 0.0);
	}
	
	public void clear() {
		mHistory.clear();
		mHistory.offer(1.0);
		mHistory.offer(1.0);
		mHistory.offer(1.0);
	}
}

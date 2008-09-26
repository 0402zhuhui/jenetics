package org.jenetics;

import javax.measure.Measure;
import javax.measure.unit.SI;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id: Timer.java,v 1.1 2008-09-26 21:36:33 fwilhelm Exp $
 */
public class Timer {
	private final String _label;
	private long _start = 0;
	private long _sum = 0;
	
	public Timer(final String lable) {
		_label = lable;
	}
	
	public void start() {
		_start = System.currentTimeMillis();
	}
	
	public void stop() {
		_sum += System.currentTimeMillis() - _start;
	}
	
	@Override
	public String toString() {
		return _label + ": " + Measure.valueOf(_sum, SI.MILLI(SI.SECOND)).toString();
	}
	
}

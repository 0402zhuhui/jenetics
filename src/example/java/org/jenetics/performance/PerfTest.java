/*
 * Java Genetic Algorithm Library (@!identifier!@).
 * Copyright (c) @!year!@ Franz Wilhelmstötter
 *  
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Author:
 *     Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 *     
 */
package org.jenetics.performance;

import static java.util.FormattableFlags.LEFT_JUSTIFY;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formattable;
import java.util.Formatter;
import java.util.List;

import javax.measure.Measurable;
import javax.measure.Measure;
import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.jenetics.stat.Variance;
import org.jenetics.util.Accumulator;
import org.jenetics.util.Accumulators.MinMax;
import org.jenetics.util.Timer;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
public abstract class PerfTest {
	
	private final String _group;
	
	private final List<Timer> _timers = new ArrayList<Timer>();
	private final List<Variance<Long>> _variances = new ArrayList<Variance<Long>>();
	private final List<MinMax<Long>> _minmax = new ArrayList<MinMax<Long>>();
	
	public PerfTest(final String group) {
		_group = group;
	}
	
	protected abstract int calls();
	
	protected Timer newTimer(final String name) {
		final Variance<Long> variance = new Variance<Long>();
		final MinMax<Long> minmax = new MinMax<Long>();
		_variances.add(variance);
		_minmax.add(minmax);
		
		final Timer timer = new Timer(name);
		timer.setAccumulator(new Accumulator<Long>() {
			@Override public void accumulate(final Long value) {
				variance.accumulate(value);
				minmax.accumulate(value);
			}
		});
		
		_timers.add(timer);
		
		return timer;
	}
	
	protected abstract PerfTest measure();	
	
	public void print(final PrintStream out) {
		out.print(this);
	}
	
	@Override
	public String toString() {
		final int[] columns = new int[]{37, 16, 16, 16}; 
		final String hhline = hline(columns, '=');
		final String hline = hline(columns, '-');
		
		final String header = String.format(
				"| %%-%ds | %%-%ds | %%-%ds | %%-%ds |",
				columns[0] - 2, columns[1] - 2, columns[2] - 2, columns[3] - 2
			);
		final String row = String.format(
				"| %%-%ds | %%%d.5s | %%%d.5s | %%%d.5s |",
				columns[0] - 2, columns[1] - 2, columns[2] - 2, columns[3] - 2
			);
		
		final StringBuilder out = new StringBuilder();
		out.append(hhline).append('\n');
		out.append(String.format(header, _group, "Mean", "Min", "Max")).append("\n");
		out.append(hhline).append('\n');
		
		for (int i = 0, n = _timers.size(); i < n; ++i) {
			final Timer timer = _timers.get(i);
			final Variance<Long> variance = _variances.get(i);
			final MinMax<Long> minmax = _minmax.get(i);
			
			out.append(String.format(
					row, 
					timer.getLabel(), 
					FormattableDuration(variance.getMean()),
					FormattableDuration(minmax.getMin()),
					FormattableDuration(minmax.getMax())
				));
			out.append("\n");
			out.append(hline).append('\n');
		}

		return out.toString();
	}
	
	private static String hline(final int[] columns, final char c) {
		final StringBuilder out = new StringBuilder();
		out.append('+');
		for (int i = 0; i < columns.length; ++i) {
			for (int j = 0; j < columns[i]; ++j) {
				out.append(c);
			}
			out.append('+');
		}
		
		return out.toString();
	}
	
	private static Formattable FormattableDuration(final double nanos) {
		return new Formattable() {
			
			private final Measurable<Duration> 
			_duration = Measure.valueOf(nanos, SI.NANO(SI.SECOND));
			
			@Override
			public void formatTo(
				final Formatter formatter, 
				final int flags, 
				final int width,
				final int precision
			) {
				final double nanos = _duration.doubleValue(SI.NANO(SI.SECOND));
				final double micros = _duration.doubleValue(SI.MICRO(SI.SECOND));
				final double millis = _duration.doubleValue(SI.MILLI(SI.SECOND));
				final double seconds = _duration.doubleValue(SI.SECOND);
				
				final NumberFormat nf = NumberFormat.getNumberInstance();
				nf.setMinimumFractionDigits(precision);
				nf.setMaximumFractionDigits(precision);
				
				String unit = "";
				String value = "";
				if ((long)seconds > 0) {
					unit = "s ";
					value = nf.format(seconds);
				} else if ((long)millis > 0) {
					unit = "ms";
					value = nf.format(millis);
				} else if ((long)micros > 0) {
					unit = "µs";
					value = nf.format(micros);
				} else {
					unit = "ns";
					value = nf.format(nanos);
				}
			
				String result = value + " " + unit;
				if (result.length() < width) {
					if ((flags & LEFT_JUSTIFY) == LEFT_JUSTIFY) {
						result = result + padding(width - result.length());
					} else {
						result = padding(width - result.length()) + result;
					}
				}
				
				formatter.format(result);
				
			}
			
			private String padding(final int width) {
				final char[] chars = new char[width];
				Arrays.fill(chars, ' ');
				return new String(chars);
			}
			
		};
	}
	
	public static void main(final String[] args) {
		//System.getProperties().list(System.out);
		new ArrayTest().measure().print(System.out);
		new PopulationTest().measure().print(System.out);
		new ChromosomeTest().measure().print(System.out);
	}
	
}



















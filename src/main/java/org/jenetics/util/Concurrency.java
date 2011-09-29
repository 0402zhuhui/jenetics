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
package org.jenetics.util;

import java.util.Properties;

import javolution.context.ConcurrentContext;
import javolution.context.LogContext;
import javolution.lang.Configurable;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
public final class Concurrency implements AutoCloseable {

	private static final String 
	KEY_CONTEXT = "javolution.context.ConcurrentContext#DEFAULT";
	
	private static final String 
	KEY_CONCURRENTCY = "javolution.context.ConcurrentContext#MAXIMUM_CONCURRENCY";
	
	private Concurrency() {
		ConcurrentContext.enter();
	}
	
	public static void setConcurrency(final int concurrency) {
		if (concurrency > ConcurrentContext.getConcurrency()) {
			final Properties properties = new Properties();
			properties.put(KEY_CONCURRENTCY, concurrency);
			setProperties(properties);
		}
		
		ConcurrentContext.setConcurrency(concurrency);
	}
	
	public static void setContext(final Class<? extends ConcurrentContext> type) {
		final Properties properties = new Properties();
		properties.put(KEY_CONTEXT, type);
		setProperties(properties);
	}
	
	private static void setProperties(final Properties properties) {
		LogContext.enter(LogContext.NULL);
		try {
			Configurable.read(properties);
		} finally {
			LogContext.exit();
		}
	}
	
	public static void setDefaultContext() {
		setContext(ConcurrentContext.DEFAULT.get());
	}
	
	@SuppressWarnings("unchecked")
	public static Class<ConcurrentContext> getContext() {
		return (Class<ConcurrentContext>)ConcurrentContext.getCurrent().getClass();
	}
	
	public static Concurrency start() {
		return new Concurrency();
	}
	
	public void execute(final Runnable task) {
		ConcurrentContext.execute(task);
	}
	
	@Override
	public void close() {
		ConcurrentContext.exit();
	}
	
	public static void main(final String[] args) {
		try (Concurrency c = Concurrency.start()) {
			c.execute(new Runnable() {
				@Override public void run() {
				}
			});
		}
		
		ConcurrentContext.enter();
		try {
			ConcurrentContext.execute(new Runnable() {
				@Override public void run() {
				}
			});
		} finally {
			ConcurrentContext.exit();
		}
	}

}

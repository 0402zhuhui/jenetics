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

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id: TimerTest.java,v 1.2 2010-01-15 11:26:46 fwilhelm Exp $
 */
public class TimerTest {

	@Test
	public void create() {
		Timer timer = new Timer("New Timer");
		Assert.assertEquals(timer.getLabel(), "New Timer");
	}
	
	@Test
	public void equals() {
		Timer t1 = new Timer();
		Timer t2 = new Timer();
		Assert.assertEquals(t1, t2);
		Assert.assertNotSame(t1, t2);
		Assert.assertEquals(t1.compareTo(t2), 0);
	}
	
	@Test 
	void timerClone() throws InterruptedException {
		Timer timer = new Timer("ASDFASDF");
		timer.start();
		Thread.sleep(100);
		timer.stop();
				
		Timer clone = timer.clone();
		Assert.assertEquals(clone, timer);
		Assert.assertNotSame(clone, timer);
		Assert.assertEquals(clone.compareTo(timer), 0);
	}
	
}






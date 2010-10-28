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

import static org.jenetics.util.Predicates.nil;
import static org.jenetics.util.Predicates.not;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
public class ArrayTest {

	@Test
	public void create1() {
		final Array<Integer> a1 = new Array<Integer>(Arrays.asList(1, 2, 3, 4, 5));
		final Array<Integer> a2 = new Array<Integer>(Arrays.asList(6, 7, 8, 9, 10, 11, 12, 13));
		final Array<Integer> a3 = new Array<Integer>(a1, a2);
		
		Assert.assertEquals(a3.length(), a1.length() + a2.length());
		for (int i = 0; i < a1.length() + a2.length(); ++i) {
			Assert.assertEquals(a3.get(i), new Integer(i + 1));
		}
	}
	
	@Test
	public void create2() {
		final Array<Integer> a1 = new Array<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));
		final Array<Integer> a2 = new Array<Integer>(Arrays.asList(6, 7, 8, 9, 10, 11, 12, 13));
		final Array<Integer> a3 = new Array<Integer>(a1.subArray(0, 6), a2);

		Assert.assertEquals(a3.length(), a1.length() + a2.length() - 2);
		for (int i = 0; i < a1.length() + a2.length() - 2; ++i) {
			Assert.assertEquals(a3.get(i), new Integer(i));
		}
	}
	
	@Test
	public void create3() {
		final Array<Integer> a1 = new Array<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));
		final Array<Integer> a2 = new Array<Integer>(Arrays.asList(6, 7, 8, 9, 10, 11, 12, 13));
		final Array<Integer> a3 = new Array<Integer>(a1.subArray(1, 6), a2);
		
		Assert.assertEquals(a3.length(), a1.length() + a2.length() - 3);
		for (int i = 1; i < a1.length() + a2.length() - 2; ++i) {
			Assert.assertEquals(a3.get(i - 1), new Integer(i));
		}
	}
	
	@Test
	public void create4() {
		final Array<Integer> a1 = new Array<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));
		final Array<Integer> a2 = new Array<Integer>(Arrays.asList(6, 7, 8, 9, 10, 11, 12, 13));
		final Array<Integer> a3 = new Array<Integer>(a1, a2.subArray(2, 7));
		
		Assert.assertEquals(a3.length(), a1.length() + a2.length() - 3);
		for (int i = 0; i < a1.length() + a2.length() - 3; ++i) {
			Assert.assertEquals(a3.get(i), new Integer(i));
		}
	}
	
	@Test
	public void equals() {
		final Array<Integer> a1 = new Array<Integer>(20);
		final Array<Integer> a2 = new Array<Integer>(20);
		for (int i = 0; i < 10; ++i) {
			a1.set(i, i);
		}
		for (int i = 0; i < 10; ++i) {
			a2.set(i + 10, i);
		}
		
		Assert.assertEquals(a1.subArray(0, 5), a2.subArray(10, 15));
		Assert.assertEquals(a1.subArray(0, 5), a2.subArray(6, 15).subArray(4, 9));
		Assert.assertEquals(a1.subArray(0, 5).copy(), a2.subArray(6, 15).subArray(4, 9).copy());
		Assert.assertFalse(a1.equals(a2));
	}
	
	@Test
	public void filter() {
		final Array<Integer> array = new Array<Integer>(20);
		array.fill(100);
		array.set(18, null);
		array.set(19, null);
		
		final Array<Integer> filtered = array.filter(not(nil()));
		Assert.assertEquals(filtered.length(), array.length() - 2);
	}
	
	@Test
	public void fill1() {
		final Array<Integer> array = new Array<Integer>(10).fill(10).seal();
		Assert.assertEquals(array.length(), 10);
		Assert.assertTrue(array.isSealed());
		for (Integer i : array) {
			Assert.assertEquals(i, new Integer(10));
		}
	}
	
	@Test
	public void fill2() {
		final Array<Integer> array = new Array<Integer>(10).fill(0);
		Assert.assertEquals(array.length(), 10);
		Assert.assertFalse(array.isSealed());
		
		final AtomicInteger integer = new AtomicInteger(0);
		array.fill(new Factory<Integer>() {
			@Override
			public Integer newInstance() {
				return integer.getAndIncrement();
			}
		});
		
		for (int i = 0; i < array.length(); ++i) {
			Assert.assertEquals(array.get(i), new Integer(i));
		}
	}
	
	@Test(expectedExceptions = UnsupportedOperationException.class)
	public void seal1() {
		final Array<Integer> array = new Array<Integer>(10).fill(10).seal();
		array.set(0, 10);
	}
	
	@Test(expectedExceptions = UnsupportedOperationException.class)
	public void seal2() {
		new Array<Integer>(10).seal().fill(1);
	}
	
	@Test(expectedExceptions = UnsupportedOperationException.class)
	public void seal3() {
		final Array<Integer> array = new Array<Integer>(10).fill(10).seal();
		for (ListIterator<Integer> it = array.iterator(); it.hasNext();) {
			it.next();
			it.set(4);
		}
	}
	
	@Test(expectedExceptions = UnsupportedOperationException.class)
	public void seal4() {
		new Array<Integer>(10).seal().subArray(0, 4).set(0, 1);
	}
	
	@Test
	public void foreach() {
		final Array<Integer> array = new Array<Integer>(10).fill(123).seal();
		final AtomicInteger count = new AtomicInteger(0);
		int value = array.foreach(new Predicate<Integer>() {
			@Override public boolean evaluate(Integer object) {
				Assert.assertEquals(object, new Integer(123));
				count.addAndGet(1);
				return true;
			}
		});
		
		Assert.assertEquals(value, -1);
		Assert.assertEquals(count.get(), 10);
		
		count.set(0);
		value = array.foreach(new Predicate<Integer>() {
			@Override public boolean evaluate(Integer object) {
				Assert.assertEquals(object, new Integer(123));
				return count.addAndGet(1) != 5;
			}
		});
		
		Assert.assertEquals(count.get(), 5);
		Assert.assertEquals(value, 4);
	}
	
	@Test
	public void append1() {
		final Array<Integer> a1 = new Array<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5));
		final Array<Integer> a2 = new Array<Integer>(Arrays.asList(6, 7, 8, 9, 10));
		final Array<Integer> a3 = a1.append(a2);
		
		Assert.assertEquals(a3.length(), 11);
		Assert.assertEquals(a3, 
				new Array<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
			);
	}
	
	@Test
	public void append2() {
		final Array<Integer> a1 = new Array<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5));
		final Array<Integer> a3 = a1.append(Arrays.asList(6, 7, 8, 9, 10));
		
		Assert.assertEquals(a3.length(), 11);
		Assert.assertEquals(a3, 
				new Array<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
			);
	}
	
	@Test
	public void append3() {
		final Array<Integer> a1 = new Array<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5));
		final Array<Integer> a2 = a1.append(6);
		final Array<Integer> a3 = a1.append(6);
		
		Assert.assertEquals(a2.length(), a1.length() + 1);
		Assert.assertEquals(a3.length(), a1.length() + 1);
		Assert.assertNotSame(a2, a3);
		Assert.assertEquals(a2, a3);
	}
	
	@Test
	public void indexOf() {
		final Array<Integer> array = new Array<Integer>(20);
		for (int i = 0; i < 10; ++i) {
			array.set(i, i);
		}
		for (int i = 10; i < 20; ++i) {
			array.set(i, i - 10);
		}
		
		int index = array.indexOf(5);
		Assert.assertEquals(index, 5);
		
		index = array.lastIndexOf(5);
		Assert.assertEquals(index, 15);
		
		index = array.lastIndexOf(25);
		Assert.assertEquals(index, -1);
		
		index = array.indexOf(-1);
		Assert.assertEquals(index, -1);
		
		index = array.indexOf(Integer.MIN_VALUE);
		Assert.assertEquals(index, -1);
		
		index = array.indexOf(Integer.MAX_VALUE);
		Assert.assertEquals(index, -1);
	}
	
	@Test
	public void copy() {
		final Array<Integer> array = new Array<Integer>(10);
		for (int i = 0; i < array.length(); ++i) {
			array.set(i, i);
		}
		
		final Array<Integer> copy = array.subArray(3, 8).copy();
		Assert.assertEquals(copy.length(), 5);
		for (int i = 0; i < 5; ++i) {
			Assert.assertEquals(copy.get(i), new Integer(i + 3));
		}
	}
	
	@Test
	public void subArray() {
		final Array<Integer> array = new Array<Integer>(10);
		for (int i = 0; i < array.length(); ++i) {
			array.set(i, i);
		}
		
		final Array<Integer> sub = array.subArray(3, 8);
		Assert.assertEquals(sub.length(), 5);
		for (int i = 0; i < 5; ++i) {
			Assert.assertEquals(sub.get(i), new Integer(i + 3));
			sub.set(i, i + 100);
		}
		
		for (int i = 3; i < 8; ++i) {
			Assert.assertEquals(array.get(i), new Integer(i + 97));
		}
		
		final Array<Integer> copy = sub.copy();
		Assert.assertEquals(copy.length(), 5);
		for (int i = 0; i < 5; ++i) {
			Assert.assertEquals(sub.get(i), new Integer(i + 100));
		}
		
		int count = 0;
		for (Integer i : sub) {
			Assert.assertEquals(i, new Integer(count + 100));
			++count;
		}
		Assert.assertEquals(count, 5);
		
//		System.out.println(sub);
//		System.out.println(array);
	}
	
	@Test
	public void toArray1() {
		final Array<Integer> array = new Array<Integer>(10);
		for (int i = 0; i < array.length(); ++i) {
			array.set(i, i);
		}
		
		Object[] oa = array.toArray();
		Assert.assertEquals(oa.length, array.length());
		Assert.assertEquals(oa.getClass(), Object[].class);
		for (int i = 0; i < oa.length; ++i) {
			Assert.assertEquals(oa[i], array.get(i));
		}
	}
	
	@Test
	public void toArray2() {
		final Array<Integer> array = new Array<Integer>(10);
		for (int i = 0; i < array.length(); ++i) {
			array.set(i, i);
		}
		
		Integer[] oa = array.toArray(new Integer[0]);
		Assert.assertEquals(oa.length, array.length());
		Assert.assertEquals(oa.getClass(), Integer[].class);
		for (int i = 0; i < oa.length; ++i) {
			Assert.assertEquals(oa[i], array.get(i));
		}
		Assert.assertEquals(new Array<Integer>(oa), array);
	}
	
	@Test
	public void cloning() {
		final Array<Integer> array = new Array<Integer>(10);
		for (int i = 0; i < array.length(); ++i) {
			array.set(i, i);
		}
		
		Array<Integer> clone = array.clone();
		Assert.assertNotSame(clone, array);
		Assert.assertEquals(clone, array);
	}
	
	@Test
	public void serialize() throws IOException, ClassNotFoundException {
		final Array<Integer> array = new Array<Integer>(10);
		for (int i = 1; i < array.length(); ++i) {
			array.set(i, i);
		}
		
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bout);
		out.writeObject(array);
		out.flush();
		out.close();
		
		ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
		ObjectInputStream in = new ObjectInputStream(bin);
		Object object = in.readObject();
		
		Assert.assertEquals(object, array);
	}
	
}







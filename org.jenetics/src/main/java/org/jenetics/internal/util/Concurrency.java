/*
 * Java Genetic Algorithm Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 */
package org.jenetics.internal.util;

import static java.lang.Math.max;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.jenetics.util.arrays;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version 2.0 &mdash; <em>$Date: 2014-04-05 $</em>
 * @since 2.0
 */
public abstract class Concurrency implements Executor, AutoCloseable {

	public static final int CORES = Runtime.getRuntime().availableProcessors();

	public static final ForkJoinPool DEFAULT = new ForkJoinPool(max(CORES - 1, 1));

	public static final Concurrency SERIAL_EXECUTOR = new Concurrency() {
		@Override
		public void execute(final Runnable command) {
			command.run();
		}
		@Override
		public void execute(final List<? extends Runnable> runnables) {
			for (final Runnable runnable : runnables) {
				runnable.run();
			}
		}
		@Override
		public void close() {
		}
	};

	public void execute(final List<? extends Runnable> runnables) {
		final int[] parts = arrays.partition(
			runnables.size(),
			CORES == 1 ? 1 : CORES + 1
		);

		for (int i = 0; i < parts.length - 1; ++i) {
			final int part = i;
			execute(new Runnable() { @Override public void run() {
				for (int j = parts[part]; j < parts[part + 1]; ++j) {
					runnables.get(j).run();
				}
			}});
		}
	}

	@Override
	public abstract void close();

	public static Concurrency withDefault() {
		return with(DEFAULT);
	}

	public static Concurrency with(final Executor executor) {
		if (executor instanceof ForkJoinPool) {
			return new ScopedForkJoinPool((ForkJoinPool)executor);
		} else if (executor instanceof ExecutorService) {
			return new ScopedExecutorService((ExecutorService)executor);
		} else {
			return new ScopedExecutor(executor);
		}
	}

	private static final class ScopedForkJoinPool extends Concurrency {
		private final Stack<ForkJoinTask<?>> _tasks = new Stack<>();
		private final ForkJoinPool _pool;

		ScopedForkJoinPool(final ForkJoinPool pool) {
			_pool = requireNonNull(pool);
		}

		@Override
		public void execute(final Runnable runnable) {
			_tasks.push(_pool.submit(runnable));
		}

		@Override
		public void execute(final List<? extends Runnable> runnables) {
			_tasks.push(_pool.submit(new RunnablesAction(runnables)));
		}

		@Override
		public void close() {
			for (ForkJoinTask<?> t = _tasks.pop(); t != null; t = _tasks.pop()) {
				t.join();
			}
		}
	}

	private static final class ScopedExecutorService extends Concurrency {
		private final Stack<Future<?>> _futures = new Stack<>();
		private final ExecutorService _service;

		ScopedExecutorService(final ExecutorService service) {
			_service = requireNonNull(service);
		}

		@Override
		public void execute(final Runnable command) {
			_futures.push(_service.submit(command));
		}

		@Override
		public void close() {
			try {
				for (Future<?> f = _futures.pop(); f != null; f = _futures.pop()) {
					f.get();
				}
			} catch (InterruptedException|ExecutionException e) {
				throw new CancellationException(e.getMessage());
			}
		}
	}

	private static final class ScopedExecutor extends Concurrency {
		private final Stack<FutureTask<?>> _tasks = new Stack<>();
		private final Executor _executor;

		ScopedExecutor(final Executor executor) {
			_executor = requireNonNull(executor);
		}

		@Override
		public void execute(final Runnable command) {
			final FutureTask<?> task = new FutureTask<>(command, null);
			_tasks.push(task);
			_executor.execute(task);
		}

		@Override
		public void close() {
			try {
				for (FutureTask<?> t = _tasks.pop(); t != null; t = _tasks.pop()) {
					t.get();
				}
			} catch (InterruptedException|ExecutionException e) {
				throw new CancellationException(e.getMessage());
			}
		}
	}

}

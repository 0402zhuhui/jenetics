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
package org.jenetix.random;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static org.jenetics.internal.util.Equality.eq;

import java.io.Serializable;
import java.util.Random;

import org.jenetics.internal.util.Equality;
import org.jenetics.internal.util.Hash;

import org.jenetics.util.Random32;
import org.jenetics.util.math;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__! &mdash; <em>$Date: 2014-07-19 $</em>
 */
public class MRG2Random extends Random32 {

	private static final long serialVersionUID = 1L;

	private static final long MODULUS = 0xFFFFFFFFL;

	public static final class Param implements Serializable {
		private static final long serialVersionUID = 1L;

		/**
		 * LEcuyer 1 parameters: a = 1498809829; b = 1160990996
		 */
		public static final Param LECUYER1 = new Param(1498809829, 1160990996);

		/**
		 * LEcuyer 2 parameters: a = 46325; b = 1084587
		 */
		public static final Param LECUYER2 = new Param(46325, 1084587);

		/**
		 * The default PRNG parameters: LECUYER1
		 */
		public static final Param DEFAULT = LECUYER1;

		public final long a;
		public final long b;

		public Param(final int a, final int b) {
			this.a = a;
			this.b = b;
 		}

		@Override
		public int hashCode() {
			return Hash.of(getClass())
				.and(a)
				.and(b).value();
		}

		@Override
		public boolean equals(final Object obj) {
			return Equality.of(this, obj).test(param ->
				eq(a, param.a) &&
				eq(b, param.b)
			);
		}

		@Override
		public String toString() {
			return format("Param[%d, %d", a, b);
		}
	}

	private static final class State implements Serializable {
		private static final long serialVersionUID = 1L;

		int r1;
		int r2;

		State(final long seed) {
			setSeed(seed);
		}

		void setSeed(final long seed) {
			long t = seed%MODULUS;
			if (t < 0) t += MODULUS;

			r1 = (int)t;
			r2 = 1;
		}
	}

	private final Param _param;
	private final State _state;

	public MRG2Random(final Param param, final long seed) {
		_param = requireNonNull(param);
		_state = new State(seed);
	}

	public MRG2Random(final Param param) {
		this(param, math.random.seed());
	}

	public MRG2Random(final long seed) {
		this(Param.DEFAULT, seed);
	}

	public MRG2Random() {
		this(Param.DEFAULT, math.random.seed());
	}

	@Override
	public int nextInt() {
		step();
		return _state.r1;
	}

	public void step() {
		final long t = _param.a*_state.r1 + _param.b*_state.r2;

		_state.r2 = _state.r1;
		_state.r1 = (int)(t% MODULUS);
	}

	@Override
	public void setSeed(final long seed) {
		if (_state != null) _state.setSeed(seed);
	}

	public static void main(final String[] args) {
		final Random random = new MRG2Random(32344);
		for (int i = 0; i < 20; ++i) {
			System.out.println(random.nextInt());
		}
	}
}

/*
#=============================================================================#
# Testing: org.jenetix.random.MRG2Random (2014-07-19 08:52)                   #
#=============================================================================#
#=============================================================================#
# Linux 3.13.0-32-generic (amd64)                                             #
# java version "1.8.0_11"                                                     #
# Java(TM) SE Runtime Environment (build 1.8.0_11-b12)                        #
# Java HotSpot(TM) 64-Bit Server VM (build 25.11-b03)                         #
#=============================================================================#
#=============================================================================#
#            dieharder version 3.31.1 Copyright 2003 Robert G. Brown          #
#=============================================================================#
   rng_name    |rands/second|   Seed   |
stdin_input_raw|  3.24e+07  |1958677204|
#=============================================================================#
        test_name   |ntup| tsamples |psamples|  p-value |Assessment
#=============================================================================#
   diehard_birthdays|   0|       100|     100|0.45162587|  PASSED
      diehard_operm5|   0|   1000000|     100|0.89931000|  PASSED
  diehard_rank_32x32|   0|     40000|     100|0.98558757|  PASSED
    diehard_rank_6x8|   0|    100000|     100|0.87992933|  PASSED
   diehard_bitstream|   0|   2097152|     100|0.98242971|  PASSED
        diehard_opso|   0|   2097152|     100|0.00003609|   WEAK
        diehard_oqso|   0|   2097152|     100|0.36890261|  PASSED
         diehard_dna|   0|   2097152|     100|0.08581447|  PASSED
diehard_count_1s_str|   0|    256000|     100|0.89143693|  PASSED
diehard_count_1s_byt|   0|    256000|     100|0.72627479|  PASSED
 diehard_parking_lot|   0|     12000|     100|0.79600924|  PASSED
    diehard_2dsphere|   2|      8000|     100|0.12211107|  PASSED
    diehard_3dsphere|   3|      4000|     100|0.65067039|  PASSED
     diehard_squeeze|   0|    100000|     100|0.05515571|  PASSED
        diehard_sums|   0|       100|     100|0.75716674|  PASSED
        diehard_runs|   0|    100000|     100|0.67730004|  PASSED
        diehard_runs|   0|    100000|     100|0.60484673|  PASSED
       diehard_craps|   0|    200000|     100|0.44618590|  PASSED
       diehard_craps|   0|    200000|     100|0.00949146|  PASSED
 marsaglia_tsang_gcd|   0|  10000000|     100|0.00000000|  FAILED
 marsaglia_tsang_gcd|   0|  10000000|     100|0.00000000|  FAILED
         sts_monobit|   1|    100000|     100|0.00588390|  PASSED
            sts_runs|   2|    100000|     100|0.03494785|  PASSED
          sts_serial|   1|    100000|     100|0.00823705|  PASSED
          sts_serial|   2|    100000|     100|0.65214038|  PASSED
          sts_serial|   3|    100000|     100|0.09830315|  PASSED
          sts_serial|   3|    100000|     100|0.08520313|  PASSED
          sts_serial|   4|    100000|     100|0.42140687|  PASSED
          sts_serial|   4|    100000|     100|0.19089630|  PASSED
          sts_serial|   5|    100000|     100|0.81291242|  PASSED
          sts_serial|   5|    100000|     100|0.91045371|  PASSED
          sts_serial|   6|    100000|     100|0.03436634|  PASSED
          sts_serial|   6|    100000|     100|0.13604199|  PASSED
          sts_serial|   7|    100000|     100|0.05166980|  PASSED
          sts_serial|   7|    100000|     100|0.33104605|  PASSED
          sts_serial|   8|    100000|     100|0.00318581|   WEAK
          sts_serial|   8|    100000|     100|0.71294691|  PASSED
          sts_serial|   9|    100000|     100|0.14785313|  PASSED
          sts_serial|   9|    100000|     100|0.47298324|  PASSED
          sts_serial|  10|    100000|     100|0.95630550|  PASSED
          sts_serial|  10|    100000|     100|0.70276134|  PASSED
          sts_serial|  11|    100000|     100|0.22953484|  PASSED
          sts_serial|  11|    100000|     100|0.30980928|  PASSED
          sts_serial|  12|    100000|     100|0.99039110|  PASSED
          sts_serial|  12|    100000|     100|0.17396317|  PASSED
          sts_serial|  13|    100000|     100|0.27880443|  PASSED
          sts_serial|  13|    100000|     100|0.43185846|  PASSED
          sts_serial|  14|    100000|     100|0.53613456|  PASSED
          sts_serial|  14|    100000|     100|0.35195743|  PASSED
          sts_serial|  15|    100000|     100|0.08837131|  PASSED
          sts_serial|  15|    100000|     100|0.04706621|  PASSED
          sts_serial|  16|    100000|     100|0.90196487|  PASSED
          sts_serial|  16|    100000|     100|0.02283210|  PASSED
         rgb_bitdist|   1|    100000|     100|0.73606611|  PASSED
         rgb_bitdist|   2|    100000|     100|0.47857244|  PASSED
         rgb_bitdist|   3|    100000|     100|0.08642860|  PASSED
         rgb_bitdist|   4|    100000|     100|0.58435707|  PASSED
         rgb_bitdist|   5|    100000|     100|0.66588489|  PASSED
         rgb_bitdist|   6|    100000|     100|0.42356477|  PASSED
         rgb_bitdist|   7|    100000|     100|0.73686042|  PASSED
         rgb_bitdist|   8|    100000|     100|0.85771610|  PASSED
         rgb_bitdist|   9|    100000|     100|0.96969570|  PASSED
         rgb_bitdist|  10|    100000|     100|0.87154645|  PASSED
         rgb_bitdist|  11|    100000|     100|0.95901694|  PASSED
         rgb_bitdist|  12|    100000|     100|0.52537770|  PASSED
rgb_minimum_distance|   2|     10000|    1000|0.16887545|  PASSED
rgb_minimum_distance|   3|     10000|    1000|0.46074187|  PASSED
rgb_minimum_distance|   4|     10000|    1000|0.63177420|  PASSED
rgb_minimum_distance|   5|     10000|    1000|0.58415031|  PASSED
    rgb_permutations|   2|    100000|     100|0.06725441|  PASSED
    rgb_permutations|   3|    100000|     100|0.44710851|  PASSED
    rgb_permutations|   4|    100000|     100|0.09999759|  PASSED
    rgb_permutations|   5|    100000|     100|0.89681851|  PASSED
      rgb_lagged_sum|   0|   1000000|     100|0.06755149|  PASSED
      rgb_lagged_sum|   1|   1000000|     100|0.02925234|  PASSED
      rgb_lagged_sum|   2|   1000000|     100|0.00029097|   WEAK
      rgb_lagged_sum|   3|   1000000|     100|0.33426943|  PASSED
      rgb_lagged_sum|   4|   1000000|     100|0.27069294|  PASSED
      rgb_lagged_sum|   5|   1000000|     100|0.21406228|  PASSED
      rgb_lagged_sum|   6|   1000000|     100|0.02283466|  PASSED
      rgb_lagged_sum|   7|   1000000|     100|0.44666962|  PASSED
      rgb_lagged_sum|   8|   1000000|     100|0.18296164|  PASSED
      rgb_lagged_sum|   9|   1000000|     100|0.17722894|  PASSED
      rgb_lagged_sum|  10|   1000000|     100|0.12117042|  PASSED
      rgb_lagged_sum|  11|   1000000|     100|0.35584913|  PASSED
      rgb_lagged_sum|  12|   1000000|     100|0.00000042|  FAILED
      rgb_lagged_sum|  13|   1000000|     100|0.08624099|  PASSED
      rgb_lagged_sum|  14|   1000000|     100|0.33551547|  PASSED
      rgb_lagged_sum|  15|   1000000|     100|0.01963136|  PASSED
      rgb_lagged_sum|  16|   1000000|     100|0.00499651|   WEAK
      rgb_lagged_sum|  17|   1000000|     100|0.05651952|  PASSED
      rgb_lagged_sum|  18|   1000000|     100|0.06711449|  PASSED
      rgb_lagged_sum|  19|   1000000|     100|0.23630521|  PASSED
      rgb_lagged_sum|  20|   1000000|     100|0.00002537|   WEAK
      rgb_lagged_sum|  21|   1000000|     100|0.00034285|   WEAK
      rgb_lagged_sum|  22|   1000000|     100|0.00051909|   WEAK
      rgb_lagged_sum|  23|   1000000|     100|0.00091037|   WEAK
      rgb_lagged_sum|  24|   1000000|     100|0.27174947|  PASSED
      rgb_lagged_sum|  25|   1000000|     100|0.00000003|  FAILED
      rgb_lagged_sum|  26|   1000000|     100|0.25115930|  PASSED
      rgb_lagged_sum|  27|   1000000|     100|0.14137543|  PASSED
      rgb_lagged_sum|  28|   1000000|     100|0.02582455|  PASSED
      rgb_lagged_sum|  29|   1000000|     100|0.01982600|  PASSED
      rgb_lagged_sum|  30|   1000000|     100|0.02134834|  PASSED
      rgb_lagged_sum|  31|   1000000|     100|0.51786339|  PASSED
      rgb_lagged_sum|  32|   1000000|     100|0.05797835|  PASSED
     rgb_kstest_test|   0|     10000|    1000|0.02416749|  PASSED
     dab_bytedistrib|   0|  51200000|       1|0.00000000|  FAILED
             dab_dct| 256|     50000|       1|0.18471019|  PASSED
Preparing to run test 207.  ntuple = 0
        dab_filltree|  32|  15000000|       1|0.00000007|  FAILED
        dab_filltree|  32|  15000000|       1|0.00000000|  FAILED
Preparing to run test 208.  ntuple = 0
       dab_filltree2|   0|   5000000|       1|0.00000000|  FAILED
       dab_filltree2|   1|   5000000|       1|0.00000000|  FAILED
Preparing to run test 209.  ntuple = 0
        dab_monobit2|  12|  65000000|       1|1.00000000|  FAILED
#=============================================================================#
# Runtime: 0:39:00                                                            #
#=============================================================================#
*/

package ITSecurity.BigInteger;

import java.util.concurrent.ThreadLocalRandom;

public final class BigIntUtils {
	public static BigInt add(BigInt a, BigInt b) {
		BigInt c;
		if (a.positive && b.positive) {
			c = a.clone();
			c.add(b, true);
		} else if (!a.positive && b.positive) {
			if (a.compareToIgnoringSign(b) <= 0) {
				c = b.clone();
				c.sub(a, true);
			} else {
				c = a.clone();
				c.sub(b, false);
			}
		} else if (a.positive && !b.positive) {
			if (a.compareToIgnoringSign(b) <= 0) {
				c = b.clone();
				c.sub(a, false);
			} else {
				c = a.clone();
				c.sub(b, true);
			}
		} else {
			c = a.clone();
			c.add(b, false);
		}
		// zero is always positive
		if (c.spart == 0) {
			c.positive = true;
		}
		return c;
	}

	public static BigInt sub(BigInt a, BigInt b) {
		BigInt c;
		if (a.positive && b.positive) {
			if (a.compareToIgnoringSign(b) <= 0) {
				c = b.clone();
				c.sub(a, false);
			} else {
				c = a.clone();
				c.sub(b, true);
			}
		} else if (!a.positive && b.positive) {
			c = a.clone();
			c.add(b, false);
		} else if (a.positive && !b.positive) {
			c = a.clone();
			c.add(b, true);
		} else {
			if (a.compareToIgnoringSign(b) <= 0) {
				c = b.clone();
				c.sub(a, true);
			} else {
				c = a.clone();
				c.sub(b, false);
			}
		}
		// zero is always positive
		if (c.spart == 0) {
			c.positive = true;
		}
		c.reduce();
		return c;
	}
	
	/**
	 * Returns an array. First element is the GCD, second and third elements are the factors u und a such that
	 * 
	 * gcd(a,b) = a*u + b*v
	 * 
	 * If a and b are coprime (gcd(a,b) = 1), then x is the inverse of a and y is the inverse of b. 
	 * @param a
	 * @param b
	 * @return
	 */
	public static BigInt[] egcd(BigInt a, BigInt b) {
		if (a.spart == 0) {
			return new BigInt[] {
					b.clone(),
					new BigInt(true, 0, a.size),
					new BigInt(true, 1, a.size),
			};
		} else {
			BigInt[] result = egcd(divMod(b.clone(), a), a);
			BigInt gcd = result[0];
			BigInt x = result[1];
			BigInt y = result[2];
			BigInt bClone = b.clone();
			div(bClone,a);
			bClone.mul(x, bClone.positive == x.positive);
			return new BigInt[] {
				gcd.clone(),
				sub(y, bClone),
				x.clone()
			};
		}
	}
	
	/**
	 * Returns the multiplicative inverse of 'a' such that a*a_inverse mod m = 1 mod m.
	 * @param a
	 * @param m
	 * @return
	 */
	public static BigInt inverse(BigInt a, BigInt m) {
		BigInt[] result = egcd(a, m);
		BigInt inverse = result[1];
		if (inverse.positive) {			
			return inverse;
		} else {
			return add(inverse, m);
		}
	}

	/**
	 * Divides a by b. Modifies a and returns the remainder. The remainder is
	 * corrected to be positive.
	 * 
	 * @param a The dividend. Will be modified.
	 * @param b The divisor.
	 * @return The remainder.
	 */
	public static BigInt divMod(BigInt a, BigInt b) {
		return a.divMod(b, a.positive == b.positive);
	}

	/**
	 * Divides a by b. Doesn't correct the remainder to be positive.
	 * 
	 * @param a
	 * @param b
	 */
	public static void div(BigInt a, BigInt b) {
		a.div(b, a.positive == b.positive);
	}

	public static Cell estimateAlter(Cell cUpper, Cell cLower, Cell divisor) {
		Cell2 dividend = new Cell2(cUpper);
		if (dividend.value < divisor.value) {
			dividend.setLower(cLower);
			dividend.setUpper(cUpper);
		}
		if (divisor.value == 0) {
			return new Cell(0);
		}
		return new Cell(Integer.divideUnsigned(dividend.value, divisor.value));
	}

	/**
	 * Returns a pseudo-random BigInt with the given size. The bit at the
	 * size-position will be 1. Uses a linear congruential generator.
	 * 
	 * @param size The size. Must be >0.
	 * @return
	 */
	public static BigInt getRandomOdd(int size) {
		if (size <= 0) {
			throw new RuntimeException("RandomOdd size must be >0.");
		}
		int maxNum = Integer.MAX_VALUE;
		int m = ThreadLocalRandom.current().nextInt(0, maxNum);
		int y = ThreadLocalRandom.current().nextInt(0, maxNum);
		int a = ThreadLocalRandom.current().nextInt(0, m);
		int b = ThreadLocalRandom.current().nextInt(0, m);

		BigInt result = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		for (int i = 0; i < size; i++) {
			y = getPsudoRandomInt(a, b, m, y);
			byte randomBit = (byte)(y & 1); // 0x00 or 0x01
			int cellBit = i % Cell.CELL_BASE;
			int cellIndex = i / Cell.CELL_BASE;
			result.cells[cellIndex].value |= (randomBit << cellBit);
		}
		result.spart = Math.max(1, size / Cell.CELL_BASE);
		
		result.cells[0].value |= 1; // make odd
		
		int msbBit = size % Cell.CELL_BASE;
		int msbIndex = size / Cell.CELL_BASE;
		result.cells[msbIndex].value |= (1 << msbBit); // set most significant bit to 1
		
		return result;
	}

	/**
	 * yNew = (a * yPrev + b) MOD m
	 * 
	 * Only use the last bit of the return value for "true pseudorandomness" ;)
	 * 
	 * @param a
	 * @param b
	 * @param m
	 * @param yPrev
	 * @return
	 */
	private static int getPsudoRandomInt(int a, int b, int m, int yPrev) {
		return (a * yPrev + b) % m;
	}
}

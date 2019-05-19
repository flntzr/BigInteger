package ITSecurity.BigInteger;

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
		return c;
	}

	public static BigInt divMod(BigInt a, BigInt b) {
		return a.divMod(b, a.positive == b.positive);
	}

	public static Cell estimateAlter(Cell cUpper, Cell cLower, Cell divisor) {
		Cell2 dividend = new Cell2(cUpper);
		if (dividend.value < divisor.value) {
			dividend.setLower(cLower);
			dividend.setUpper(cUpper);
		}
		return new Cell(Integer.divideUnsigned(dividend.value, divisor.value));
	}
}

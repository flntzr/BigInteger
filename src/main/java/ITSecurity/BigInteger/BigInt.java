package ITSecurity.BigInteger;

public class BigInt extends BigNumber {
	public static final int DEFAULT_BIG_INT_SIZE = 4096 / Cell.CELL_BASE;

	public BigInt(BigInt bigInt, int size) {
		super(size, bigInt);
	}

	public BigInt(boolean positive, int value, int size) {
		super(size, positive, value);
	}

	private String extractPositive(String str) {
		this.positive = true;
		if (str.startsWith("+")) {
			str = str.substring(1);
		} else if (str.startsWith("-")) {
			str = str.substring(1);
			this.positive = false;
		}
		return str;
	}

	public void fromHexString(String str) {
		this.clearCells();
		str = this.extractPositive(str);
		if (str.startsWith("0x")) {
			str = str.substring(2);
		}

		int subStrSize = Cell.CELL_BASE / 4;
		// 1 hex digit equals 4 bits => fit CELL_BASE / 4 characters into a cell
		int strLength = str.length();
		this.spart = (int) Math.ceil(strLength / subStrSize);
		int j = 0;
		for (int i = 0; i < strLength; i += subStrSize) {
			int startIdx = Math.max(0, strLength - i - subStrSize);
			int endIdx = strLength - i;
			String subStr = str.substring(startIdx, endIdx);
			this.cells[j++] = new Cell2(Integer.parseUnsignedInt(subStr, 16));
		}
		this.reduce();
	}

	public void fromOctString(String str) {
		this.clearCells();
		str = this.extractPositive(str);
		int len = str.length();
		for (int i = 0; i < len; i++) {
			int num = Integer.parseUnsignedInt("" + str.charAt(i), 8);
			BigInt a = new BigInt(this.positive, num, this.size);
			this.shiftLeft(3);
			this.add(a, positive);
			this.reduce();
		}
	}

	public void fromDecString(String str) {
		this.clearCells();
		str = this.extractPositive(str);
		int len = str.length();
		for (int i = 0; i < len; i++) {
			int num = Integer.parseUnsignedInt("" + str.charAt(i), 10);
			BigInt a = new BigInt(this.positive, num, this.size);
			this.mul10();
			this.add(a, positive);
			this.reduce();
		}
	}

	public void add(BigInt a, boolean positive) {
		Cell2 tmp = new Cell2(0);
		Cell2 over = new Cell2(0);
		BigNumberUtils.sameSize(this, a);
		this.spart++;
		for (int i = 0; i < this.spart; i++) {
			tmp.value = this.cells[i].value + a.cells[i].value + over.value;
			this.cells[i].value = tmp.getLower();
			over.value = tmp.getUpper();
		}
		if (over.value != 0) {
			this.cells[this.spart] = over;
		} else {
			this.spart--;
		}
		this.positive = positive;
	}

	public void sub(BigInt a, boolean positive) {
		Cell2 tmp = new Cell2(0);
		Cell2 over = new Cell2(0);
		BigNumberUtils.sameSize(this, a);
		this.spart++;
		for (int i = 0; i < this.spart; i++) {
			tmp.value = this.cells[i].value - a.cells[i].value + over.value;
			this.cells[i].value = tmp.getLower();
			// can't user tmp.getUpper() in the following because that screws up the sign.
			// In this rare case we actually want a negative value if there is one.
			over.value = tmp.value >> Cell.CELL_BASE;
		}
		this.cells[this.spart] = over;
		if (over.value == 0) {
			this.spart--;
		}
		this.positive = positive;
		this.reduce();
	}

	public void mul(BigInt a, boolean positive) {
		BigNumberUtils.sameSize(this, a);
		BigInt c = new BigInt(this, this.size);
		c.spart = this.spart;
		this.clearCells();
		this.spart = c.spart * 2 + 1;
		this.positive = positive;
		for (int i = 0; i < a.spart; i++) {
			for (int j = 0; j < c.spart; j++) {
				Cell2 tmp = new Cell2(c.cells[j].value * a.cells[i].value);
				this.addCell2(i + j, tmp);
			}
		}
	}

	private BigInt correctMod(BigInt a, BigInt r, boolean positive) {
		boolean rNegative = r.spart > 0 && (!this.positive && a.positive || !this.positive && !a.positive);
		if (rNegative) {
			// Add the divisor to the remainder once (so it is positive!).
			// Subtract 1 from the division result.
			r.positive = false;
			a.positive = true;
			r = BigIntUtils.add(r, a);
			BigInt one = new BigInt(true, 1, this.size);
			this.add(one, this.positive);
		}
		r.positive = true; // per definition!
		this.reduce();
		if (this.spart == 0) {
			this.positive = true;
			return r;
		}
		this.positive = positive;
		return r;
	}

	public void div(BigInt a, boolean positive) {
		this.divMod(a, positive, false);
		this.reduce();
		if (this.spart == 0) {
			this.positive = true;
		}
	}

	/**
	 * Divides this instance by a. Modifies this instance, returns the remainder.
	 * Ensures the remainder is always positive.
	 * 
	 * @param a        The divisor
	 * @param positive If the result is positive.
	 * @return the rest
	 */
	public BigInt divMod(BigInt a, boolean positive) {
		return this.divMod(a, positive, true);
	}

	/**
	 * Divides this instance by a. Modifies this instance, returns the remainder.
	 * Ensures the remainder is always positive if correctRemained is true!
	 * 
	 * @param a                The divisor
	 * @param positive         If the result is positive.
	 * @param correctRemainder If we want to ensure that the remainder is positive.
	 *                         This is only important if we require the remainder..
	 * @return the rest
	 */
	private BigInt divMod(BigInt a, boolean positive, boolean correctRemainder) {
		this.reduce();
		a.reduce();
		BigInt q = new BigInt(positive, 0, this.size);
		BigInt r = this.clone();
		r.positive = true;
		if (a.spart == 0) {
			throw new RuntimeException("Cannot divide by 0");
		}
		if (this.spart < a.spart) {
			this.clearCells();
			return correctRemainder ? this.correctMod(a, r, positive) : r;
		}
		if (this.spart == a.spart && this.compareToIgnoringSign(a) < 0) {
			this.clearCells();
			return correctRemainder ? this.correctMod(a, r, positive) : r;
		}
		if (this.spart < 3 && a.spart < 3) {
			// dividend and divisor are small enough for CPU to handle division
			Cell2 dividend = new Cell2();
			dividend.setLower(new Cell(this.cells[0].getLower()));
			dividend.setUpper(new Cell(this.cells[1].getLower()));
			Cell2 divisor = new Cell2();
			divisor.setLower(new Cell(a.cells[0].getLower()));
			divisor.setUpper(new Cell(a.cells[1].getLower()));
			this.clearCells();
			Cell2 result = new Cell2(Integer.divideUnsigned(dividend.value, divisor.value));
			this.cells[0].value = result.getLower();
			this.cells[1].value = result.getUpper();
			r = new BigInt(true, Integer.remainderUnsigned(dividend.value, divisor.value), this.size);
			r.reduce();
			return correctRemainder ? this.correctMod(a, r, positive) : r;
		}
		// do full division
		int k = a.spart;
		int l = this.spart - k;
		r.clearCells();
		for (int i = 0; i < k; i++) {
			r.cells[i] = new Cell2(this.cells[l + i]);
		}
		r.reduce();

		for (int i = l; i >= 0; i--) {
			Cell rUpper = new Cell(r.cells[1].value);
			Cell rLower = new Cell(r.cells[0].value);
			Cell aUpper = new Cell(a.cells[a.spart - 1].getLower());
			Cell estimate = BigIntUtils.estimateAlter(rUpper, rLower, aUpper);
			BigInt tmp = a.clone();
			// TODO: use cell multiplication instead!
			tmp.mul(new BigInt(true, estimate.value, this.size), true);
			tmp.reduce();

			if (tmp.compareTo(r) != 0) {
				while (tmp.compareTo(r) > 0) {
					// estimated value too high
					estimate.value--;
					tmp = BigIntUtils.sub(tmp, a);
				}
				while (BigIntUtils.sub(r, tmp).compareToIgnoringSign(a) > 0) {
					// estimated value too low
					estimate.value++;
					tmp = BigIntUtils.add(tmp, a);
				}
			}
			q.cells[i] = new Cell2(estimate);
			r = BigIntUtils.sub(r, tmp);
			r.reduce();
			if (i != 0) {
				// shift r left 1 cell
				for (int j = r.spart; j > 0; j--) {
					r.cells[j] = r.cells[j - 1];
				}
				r.cells[0] = new Cell2(this.cells[i - 1]);
				r.reduce();
			}
		}
		q.reduce();
		this.cells = q.cells;
		this.spart = q.spart;
		if (this.spart == 0) {
			this.positive = true;
		}
		return correctRemainder ? this.correctMod(a, r, positive) : r;
	}

	public void pow(BigInt n) {
		BigInt zero = new BigInt(true, 0, this.size);
		if (n.compareTo(zero) < 0) {
			throw new RuntimeException("The exponent must be >= 0.");
		}
		if (n.compareTo(zero) == 0) {
			this.positive = true;
			this.clearCells();
			this.cells[0].value = 1;
			return;
		}
		BigInt one = new BigInt(true, 1, this.size);
		if (n.compareTo(one) == 0) {
			return;
		}

		boolean sign = this.positive || n.isEven();
		BigInt t = this.clone();

		// set value of 'this' to 1
		this.clearCells();
		this.cells[0].value = 1;

		while (n.compareTo(zero) > 0) {
			boolean isBitOne = !n.isEven();
			n.shiftRight(1);
			if (isBitOne) {
				this.mul(t, true);
			}
			t.square();
		}
		this.positive = sign;
	}

	/**
	 * Power with modulo. Calculates this^n mod m. Modifies this instance.
	 * 
	 * @param n The power
	 * @param m The module
	 */
	public void powMod(BigInt n, BigInt m) {
		BigInt zero = new BigInt(true, 0, this.size);
		BigInt nClone = n.clone();
		if (nClone.compareTo(zero) < 0) {
			throw new RuntimeException("The exponent must be >= 0.");
		}
		if (nClone.compareTo(zero) == 0) {
			this.positive = true;
			this.clearCells();
			this.cells[0].value = 1;
			return;
		}
		BigInt one = new BigInt(true, 1, this.size);
//		if (nClone.compareTo(one) == 0) {
//			return;
//		}

		boolean sign = this.positive || nClone.isEven();
		BigInt t = this.clone();

		BigInt result = new BigInt(sign, 1, DEFAULT_BIG_INT_SIZE);
		while (nClone.compareTo(zero) > 0) {
			boolean isBitOne = !nClone.isEven();
			nClone.shiftRight(1);
			if (isBitOne) {
				result.mul(t, true);
				result = result.divMod(m, true);
			}
			t.square();
			t = t.divMod(m, true);
		}
		// set value of 'this' to 1
		this.clearCells();
		this.cells = result.cells;
		this.positive = sign;
	}

	/**
	 * Power with modulo where the modulo is a prime. Calculates this^n mod p.
	 * Modifies this instance.
	 * 
	 * @param n The power
	 * @param p The module
	 */
	public void powModPrim(BigInt n, BigInt p) {
		BigInt pMinus1 = p.clone();
		pMinus1.sub(new BigInt(true, 1, DEFAULT_BIG_INT_SIZE), positive);
		if (n.compareTo(pMinus1) < 0) {
			this.powMod(n, p);
			return;
		}
		if (this.compareTo(p) < 0) {
			// gcd(a,p) = 1 -> a^p = a (mod p)
			n = n.divMod(pMinus1, true);
			this.powMod(n, p);
			return;
		}
		BigInt gcd = this.clone();
		gcd.binGcd(p);
		if (gcd.spart == 1 && gcd.cells[0].value == 1) {
			n = n.divMod(pMinus1, true);
		}
		this.powMod(n, p);

	}

	/**
	 * Uses the binary euclid algorithm to determine the greatest common
	 * denominator, which is then saved to 'this'.
	 * 
	 * @param b The other BigInt.
	 */
	public void binGcd(BigInt b) {
		BigInt a = this.clone();
		BigInt bClone = b.clone();
		// use 'this' instead of 'b' from here on
		int k;
		for (k = 0; a.isEven() && bClone.isEven(); k++) {
			a.shiftRight(1);
			bClone.shiftRight(1);
		}
		while (a.spart != 0) {
			while (a.isEven()) {
				a.shiftRight(1);
			}
			while (bClone.isEven()) {
				bClone.shiftRight(1);
			}
			if (a.compareToIgnoringSign(bClone) < 0) {
				BigInt t = a;
				a = bClone;
				bClone = t;
			}
			a.sub(bClone, true);
			a.reduce();
			bClone.reduce();
		}

		for (; k > 0; k--) {
			bClone.shiftLeft(1);
		}
		this.cells = bClone.cells;
		this.positive = true;
		this.size = bClone.size;
		this.spart = bClone.spart;
	}

	public boolean isEven() {
		return (this.cells[0].value & 1) == 0;
	}

	public void square() {
		BigInt a = this.clone();
		this.clearCells();
		int tmp;
		for (int i = 0; i < a.spart; i++) {
			for (int j = i + 1; j < a.spart; j++) {
				tmp = a.cells[i].value * a.cells[j].value;
				this.addCell2(i + j, new Cell2(tmp));
				this.addCell2(i + j, new Cell2(tmp));
			}
			tmp = a.cells[i].value * a.cells[i].value;
			this.addCell2(2 * i, new Cell2(tmp));
		}
		this.positive = true;
		this.reduce();
	}

	public boolean isPrimeFermat(BigInt[] bases) {
		for (BigInt base : bases) {
			boolean isPrimeFermat = this.isPrimeFermat(base);
			if (!isPrimeFermat) {
				return false;
			}
		}
		return true;
	}

	public boolean isPrimeEuler(BigInt[] bases) {
		for (BigInt base : bases) {
			boolean isPrimeEuler = this.isPrimeEuler(base);
			if (!isPrimeEuler) {
				return false;
			}
		}
		return true;
	}

	public boolean isPrimeFermat(BigInt base) {
		BigInt rightSide = base.clone();
		rightSide = rightSide.divMod(this, true);
		BigInt thisClone = this.clone();
		BigInt leftSide = base.clone();
		leftSide.powMod(this, thisClone);
		return leftSide.equals(rightSide);
	}

	public boolean isPrimeEuler(BigInt base) {
		BigInt baseClone = base.clone();
		BigInt one = new BigInt(true, 1, this.size);
		BigInt exponent = this.clone();
		exponent.sub(one, positive);
		exponent.shiftRight(1);
		baseClone.powMod(exponent, this);
		// the value should equal +-1 -> drop the sign!
		baseClone.positive = true;
		if (baseClone.equals(one)) {
			return true;
		}
		BigInt result = BigIntUtils.sub(baseClone, this);
		result.positive = true;
		return result.equals(one);
	}

	public boolean isPrimeMillerRabin(BigInt base) {
		BigInt one = new BigInt(true, 1, this.size);
		BigInt exponent = this.clone();
		exponent.sub(one, positive);
		exponent.shiftRight(1);
		exponent.reduce();
		int exponentSquareCount = 0;
		// exponent = (n-1)/2 --> keep halving it until it is uneven to find the
		// smallest exponent
		while (exponent.isEven() && exponent.spart > 0) {
			exponent.shiftRight(1);
			++exponentSquareCount;
		}
		boolean isFirstNumber = true;
		for (int i = 0; i <= exponentSquareCount; i++) {
			BigInt baseClone = base.clone();
			baseClone.powMod(exponent, this);
			if (isFirstNumber) {
				// if the first exponent is tested return true if a^d = +-1.
				baseClone.positive = true;
				if (baseClone.equals(one)) {
					return true;
				}
				BigInt result = BigIntUtils.sub(baseClone, this);
				result.positive = true;
				if (result.equals(one)) {
					return true;
				}
			} else {
				// return true if the non-first exponent equals -1.
				if (!baseClone.positive) {
					baseClone.positive = true;
					if (baseClone.equals(one)) {
						return true;
					}
				}
				BigInt result = BigIntUtils.sub(baseClone, this);
				if (!result.positive) {
					result.positive = true;
					if (result.equals(one)) {
						return true;
					}
				}
			}
			exponent.shiftLeft(1);
			isFirstNumber = false;
		}
		return false;
	}

	public void div10() {
		BigInt ten = new BigInt(true, 10, DEFAULT_BIG_INT_SIZE);
		ten.reduce();
		BigIntUtils.div(this, ten);
	}

	public void mul10() {
		BigInt a = this.clone();
		this.shiftLeft(2);
		this.add(a, this.positive);
		this.reduce();
		this.shiftLeft(1);
	}

	public void addCell2(int index, Cell2 cell) {
		Cell2 tmp = new Cell2(cell);
		Cell2 over = new Cell2(cell);
		while (over.value != 0) {
			tmp.value = this.cells[index].value + over.getLower();
			this.cells[index++].value = tmp.getLower();
			over.value = tmp.getUpper() + over.getUpper();
		}
		this.reduce();
	}

	/**
	 * Print the hex string. Pad it with leading zeroes to match the given length.
	 * 
	 * @param length The length of the string, EXCLUDING the "+" or "-" signs.
	 * @return the hex string, e.g. "+0fffff"
	 */
	public String toHexString(int length) {
		StringBuilder builder = new StringBuilder();
		int j = 0;
		for (int i = 0; i < this.spart; i++) {
			String hexDigit = Integer.toUnsignedString(this.cells[j++].value, 16);
			// each hex number needs to be padded to 4 digits
			builder.insert(0, BigNumberUtils.padWithZeros(hexDigit, 4));
		}
		// pad to match the string length
		String pad = BigNumberUtils.padWithZeros("", length - builder.length());
		builder.insert(0, pad);
		builder.insert(0, this.positive ? '+' : '-');
		return builder.toString();
	}

	@Override
	public BigInt clone() {
		return new BigInt(this, this.size);
	}

	/**
	 * Compares this object with the specified object. Ignores the explicit
	 * 'positive' or 'negative' sign.
	 * 
	 * @param o
	 * @return
	 */
	public int compareToIgnoringSign(BigNumber o) {
		for (int i = this.cells.length - 1; i >= 0; i--) {
			if (this.cells[i].value != o.cells[i].value) {
				return Integer.compare(this.cells[i].value, o.cells[i].value);
			}
		}
		return 0;
	}

	@Override
	public int compareTo(BigNumber o) {
		int result = this.compareToIgnoringSign(o);
		if (result == 0) {
			// +0 and -0 are equal
			return 0;
		}
		if (this.positive && o.positive) {
			return result;
		} else if (this.positive && !o.positive) {
			return 1;
		} else if (!this.positive && o.positive) {
			return -1;
		} else {
			return -result;
		}
	}

}

package ITSecurity.BigInteger;

public class BigInt extends BigNumber {
	public static final int DEFAULT_BIG_INT_SIZE = 2048 / Cell.CELL_BASE;

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
			// TODO: does this make sense?
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
			// TODO: does this make sense?
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
		if (this.spart == 0) {
			this.positive = true;
		}
	}

	public BigInt divMod(BigInt a, boolean positive) {
		return this.divMod(a, positive, true);
	}

	/**
	 * Divides this instance by a. Modifies this instance, returns the remainder.
	 * Ensures the remainder is always positive!
	 * 
	 * @param a                The divisor
	 * @param positive         If the result is positive.
	 * @param correctRemainder If we want to ensure that the remainder is positive.
	 *                         This is only important if we require the remainder..
	 * @return the rest
	 */
	private BigInt divMod(BigInt a, boolean positive, boolean correctRemainder) {
		this.reduce();
//		boolean requiresCorrecting = !this.positive;
		a.reduce();
//		this.positive = positive;
		BigInt q = new BigInt(positive, 0, this.size);
		BigInt r = this.clone();
		r.positive = true;
		if (a.spart == 0) {
			throw new RuntimeException("Cannot divide by 0");
		}
		if (this.spart < a.spart) {
			// dividend < divisor
			this.clearCells();
//			this.positive = true;
			return correctRemainder ? this.correctMod(a, r, positive) : r;
		}
		if (this.spart == a.spart && this.compareToIgnoringSign(a) < 0) {
			// dividend < divisor
			this.clearCells();
//			this.positive = true;
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

	public void div10() {
		// TODO: respect sign of BigInt
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
		this.spart++;
		Cell2 tmp = new Cell2(cell);
		Cell2 over = new Cell2(cell);
		while (over.value != 0) {
			tmp.value = this.cells[index].value + over.getLower();
			this.cells[index++].value = tmp.getLower();
			over.value = tmp.getUpper() + over.getUpper();
		}
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
	protected BigInt clone() {
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

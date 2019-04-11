package ITSecurity.BigInteger;

public class BigInt extends BigNumber {
	private static final int DEFAULT_BIG_INT_SIZE = 2048 / Cell.CELL_BASE;

	public BigInt(BigInt bigInt, int size) {
		super(size, bigInt);
	}

	public BigInt(boolean positive, int value, int size) {
		super(size, positive, value);
	}

	public void fromHexString(String str) {
		// TODO: empty all cells beforehand
		this.positive = true;
		if (str.startsWith("+")) {
			str = str.substring(1);
		} else if (str.startsWith("-")) {
			str = str.substring(1);
			this.positive = false;
		}

		int subStrSize = Cell.CELL_BASE / 4;
		// 1 hex digit equals 4 bits => fit CELL_BASE / 4 characters into a cell
		int strLength = str.length();
		this.spart = (int) Math.ceil(strLength / subStrSize);
		int j = 0;
		for (int i = 0; i < strLength; i += subStrSize) {
			int startIdx = strLength - i - subStrSize;
			int endIdx = strLength - i;
			String subStr = str.substring(startIdx, endIdx);
			this.cells[j++] = new Cell2(Integer.parseUnsignedInt(subStr, 16));
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
		this.cells[this.spart] = over;
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
		this.positive = positive;
		this.reduce();
	}

//	public void mul(BigInt a, boolean positive) {
//		BigNumberUtils.sameSize(this, a);
//		this.positive = positive;
//		this.spart = 2 * this.spart + 1;
//		int tmpSpart = this.spart;
//		for (int i = 0; i < a.spart; i++) {
//			for (int j = 0; j < tmpSpart; j++) {
//				Cell2 tmp = new Cell2(this.cells[j].value * a.cells[i].value);
//				this.addCell2(i + j, tmp);
//			}
//		}
//	}

	// TODO: Modify reference and don't return a new BigInt.
	public BigInt mul(BigInt a, boolean positive) {
		BigNumberUtils.sameSize(this, a);
		BigInt c = new BigInt(positive, 0, this.size);
		c.spart = this.spart * 2 + 1;
		for (int i = 0; i < a.spart; i++) {
			for (int j = 0; j < this.spart; j++) {
				Cell2 tmp = new Cell2(this.cells[j].value * a.cells[i].value);
				c.addCell2(i + j, tmp);
			}
		}
		return c;
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

}

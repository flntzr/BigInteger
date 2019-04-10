package ITSecurity.BigInteger;

public class BigInt extends BigNumber {
	private static final int DEFAULT_BIG_INT_SIZE = 2048 / Cell.CELL_BASE;

	public BigInt(BigInt bigInt) {
		super(DEFAULT_BIG_INT_SIZE, bigInt);
	}

	public BigInt(boolean positive, int value) {
		super(DEFAULT_BIG_INT_SIZE, positive, value);
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
			String hexDigit = Integer.toString(this.cells[j++].value, 16);
			builder.insert(0, hexDigit);
		}
		for (int k = 0; k < length - this.spart; k++) {
			builder.insert(0, '0');
		}
		builder.insert(0, this.positive ? '+' : '-');
		return builder.toString();
	}

}

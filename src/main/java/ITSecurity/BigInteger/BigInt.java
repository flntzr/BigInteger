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
	
	public void add(BigInt a) {
		
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

}

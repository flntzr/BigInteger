package ITSecurity.BigInteger;

public class Cell {
	public static final int CELL_BASE = 16; // for 32 bit long integers
	public static final int CELL_BASE_MASK = (int) Math.pow(2, 16) - 1;
	public int value;

	public Cell() {
	}

	public Cell(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "" + Integer.toUnsignedString(this.value, 16);
	}
}

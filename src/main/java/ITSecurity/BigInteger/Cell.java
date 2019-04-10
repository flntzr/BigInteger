package ITSecurity.BigInteger;

public class Cell {
	public static final int CELL_BASE = 16; // for 64-bit systems
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

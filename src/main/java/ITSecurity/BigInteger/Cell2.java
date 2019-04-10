package ITSecurity.BigInteger;

public class Cell2 {
	public int value;

	public Cell2() {
	}

	public Cell2(Cell c) {
		this.value = c.value;
	}

	public Cell2(int value) {
		this.value = value;
	}
	
	public int getLower() {
		int lower = this.value << Cell.CELL_BASE;
		return lower >>> Cell.CELL_BASE;
	}
	
	public int getUpper() {
		return this.value >>> Cell.CELL_BASE;
	}

	@Override
	public String toString() {
		return "" + Integer.toUnsignedString(this.value, 16);
	}

}

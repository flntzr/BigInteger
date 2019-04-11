package ITSecurity.BigInteger;

public class Cell2 {
	public int value;

	public Cell2() {
	}

	public Cell2(Cell c) {
		this.value = c.value;
	}
	
	public Cell2(Cell2 c) {
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
	
	public void setLower(Cell c) {
		int newLower = c.value & Cell.CELL_BASE_MASK;
		this.value = (this.getUpper() << Cell.CELL_BASE) + newLower;
	}
	
	public void setUpper(Cell c) {
		int newUpper = c.value & Cell.CELL_BASE_MASK;
		this.value = (newUpper << Cell.CELL_BASE) + this.getLower();
	}

	@Override
	public String toString() {
		return "" + Integer.toUnsignedString(this.value, 16);
	}	

}

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

	@Override
	public String toString() {
		return "" + value;
	}

}

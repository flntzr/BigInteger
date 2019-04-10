package ITSecurity.BigInteger;

public abstract class BigNumber {
	protected int size;
	protected int spart;
	protected boolean positive;
	protected Cell2[] cells;

	public BigNumber(int size, BigNumber bigNumber) {
		this.size = size;
		this.spart = bigNumber.spart;
		this.positive = bigNumber.positive;
		this.cells = new Cell2[bigNumber.cells.length];
		for (int i = 0; i < bigNumber.cells.length; i++) {
			this.cells[i] = new Cell2(bigNumber.cells[i].value);
		}
	}

	public BigNumber(int size, boolean positive, int value) {
		this.size = size;
		this.positive = positive;
		this.cells = new Cell2[size];
		for (int i = 0; i < size; i++) {
			this.cells[i] = new Cell2();
		}
		this.cells[0].value = value;
	}

	public void reduce() {
		// reverse traverse each cell for the first one that is not 0
		for (int i = size - 1; i < 0; i--) {
			if (this.cells[i].value != 0) {
				this.spart = i + 1;
				return;
			}
		}
	}

	public void expand(int size) {
		if (this.size > size) {
			throw new RuntimeException("Must expand to a size greater than the previous size.");
		}
		Cell2[] newCells = new Cell2[size];
		System.arraycopy(this.cells, 0, newCells, 0, this.cells.length);
		this.cells = newCells;
		for (int i = this.size; i < size; i++) {
			this.cells[i] = new Cell2();
		}
		this.size = size;
	}

	/**
	 * Checks if the size is valid. If so expands the array of cells.
	 * 
	 * @param size the new size
	 */
	public void resize(int size) {
		if (size < 1) {
			throw new RuntimeException("Must resize to a size greater than 0.");
		}
		if (this.spart > size) {
			this.reduce();
			if (this.spart > size) {
				throw new RuntimeException("Could not resize.");
			}
		}
		if (this.spart < size) {
			this.expand(size);
		}
	}

	public void shiftLeft(int count) {
		if (count >= Cell.CELL_BASE) {
			throw new RuntimeException("Must shift left by less than " + Cell.CELL_BASE);
		}
		Cell over = new Cell(0);
//		this.expand(this.spart + 1);
		for (int i = 0; i < this.spart - 1; i++) {
			Cell2 tmp = new Cell2(this.cells[i].value << count + over.value);
			this.cells[i] = new Cell2(tmp.value % Cell.CELL_BASE);
			over.value = tmp.value / Cell.CELL_BASE;
		}
		this.cells[this.spart] = new Cell2(over);
		if (over.value != 0) {
			this.spart++;
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

}

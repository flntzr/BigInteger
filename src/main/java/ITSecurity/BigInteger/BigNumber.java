package ITSecurity.BigInteger;

public abstract class BigNumber {
	protected int size;
	protected int spart;
	protected boolean positive;
	protected Cell2[] cells;

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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	public abstract void fromString(String str);

}

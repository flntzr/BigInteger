package ITSecurity.BigInteger;

import java.util.stream.Collectors;

public abstract class BigNumber implements Comparable<BigNumber> {
	protected int size;
	protected int spart;
	protected boolean positive;
	protected Cell2[] cells;

	public BigNumber(int size, BigNumber bigNumber) {
		this.size = size;
		this.spart = bigNumber.spart;
		this.positive = bigNumber.positive;
		this.cells = new Cell2[size];
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
		for (int i = size - 1; i >= 0; i--) {
			if (this.cells[i].value != 0) {
				this.spart = i + 1;
				return;
			}
		}
		this.spart = 0;
	}

	public void expand(int size) {
		if (size <= this.spart) {
			throw new RuntimeException("Must expand to a size greater than the significant part.");
		}
		if (size <= this.size) {
			// we don't need to physically expand the cell-array
			this.spart = size;
			return;
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
		for (int i = 0; i < this.spart; i++) {
			Cell2 tmp = new Cell2((this.cells[i].value << count) + over.value);
			this.cells[i] = new Cell2(tmp.getLower());
			over.value = tmp.getUpper();
		}
		if (this.spart < this.size) {
			this.cells[this.spart] = new Cell2(over);
		}
		if (over.value != 0) {
			// if there are no more cells to overflow just ignore the 'over'
			if (this.spart != this.size) {
				this.spart++;
			}
		}
	}

	public void shiftRight(int count) {
		if (count >= Cell.CELL_BASE) {
			throw new RuntimeException("Must shift right by less than " + Cell.CELL_BASE);
		}
		Cell underflow = new Cell(0);
		int tmpShift = Cell.CELL_BASE * 2 - count;
		for (int i = this.spart - 1; i >= 0; i--) {
			this.cells[i].setUpper(underflow);
			// get lower 'count' bits of this.cells[i] as the underflow
			underflow.value = this.cells[i].value << tmpShift >>> tmpShift;
			this.cells[i].value >>>= count;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		int cellCount = Math.max(this.spart, 1);
		for (int i = cellCount; i >= 0; i--) {
			builder.append(BigNumberUtils.padWithZeros(this.cells[i].toString(), 4));
		}
		builder.insert(0, this.positive ? '+' : '-');
		return builder.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof BigNumber)) {
			return false;
		}
		BigNumber o = (BigNumber) obj;
		if (this.positive != o.positive || this.size != o.size || this.cells.length != o.cells.length) {
			return false;
		}

		for (int i = 0; i < this.cells.length; i++) {
			if (this.cells[i].value != o.cells[i].value) {
				return false;
			}
		}
		return true;
	}
}

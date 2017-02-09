package teamproject.gamelogic.domain;

public abstract class Map {

	private static int defaultNumberOfCells = 25;
	private Cell[][] cells;

	public Map(final int numberOfCells) {
		cells = new Cell[numberOfCells][numberOfCells];
	}

	public Map() {
		this(defaultNumberOfCells);
	}

	public Map(final Cell[][] cells) {
		this.cells = cells;
	}

	public Cell[][] getCells() {
		return cells;
	}

	public Cell getCell(final int x, final int y) {
		return cells[x][y];
	}

	public int getMapSize() {
		return cells.length * cells[0].length;
	}

	public void setCells(final Cell[][] cells) {
		this.cells = cells;
	}

	public void addCell(final Cell cell) {
		final int x = cell.getPosition().getX();
		final int y = cell.getPosition().getY();
		cells[x][y] = cell;
	}

}

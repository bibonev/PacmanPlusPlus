package teamproject.gamelogic.domain;

import teamproject.gamelogic.exception.ViolatedAssumptionException;

public class Map {

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

	public void setCells(final Cell[][] cells) {
		this.cells = cells;
	}

	public void addCell(final Cell cell) throws ViolatedAssumptionException {
		final int x = cell.getPosition().getX();
		final int y = cell.getPosition().getY();

		if (cells[x][y] == null) {
			cells[x][y] = cell;
		} else {
			throw new ViolatedAssumptionException("Position is not empty so cannot add cell.");
		}
	}

}

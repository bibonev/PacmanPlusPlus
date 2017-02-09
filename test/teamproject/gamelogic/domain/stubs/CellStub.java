package teamproject.gamelogic.domain.stubs;

import teamproject.constants.CellState;
import teamproject.constants.CellType;
import teamproject.gamelogic.domain.Cell;
import teamproject.gamelogic.domain.Position;

public class CellStub extends Cell {

	public CellStub(final CellType type, final CellState state, final Position position) {
		super(type, state, position);
		// TODO Auto-generated constructor stub
	}

	public CellStub(final CellType type, final Position position) {
		super(type, position);
		// TODO Auto-generated constructor stub
	}

	public CellStub(final Position position) {
		super(position);
		// TODO Auto-generated constructor stub
	}

}

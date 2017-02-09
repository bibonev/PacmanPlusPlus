package teamproject.gamelogic.domain.stubs;

import teamproject.gamelogic.domain.Cell;
import teamproject.gamelogic.domain.CellState;
import teamproject.gamelogic.domain.CellType;
import teamproject.gamelogic.domain.Position;

public class CellStub extends Cell {

	public CellStub(CellType type, CellState state, Position position) {
		super(type, state, position);
		// TODO Auto-generated constructor stub
	}

	public CellStub(CellType type, Position position) {
		super(type, position);
		// TODO Auto-generated constructor stub
	}

	public CellStub(Position position) {
		super(position);
		// TODO Auto-generated constructor stub
	}

}

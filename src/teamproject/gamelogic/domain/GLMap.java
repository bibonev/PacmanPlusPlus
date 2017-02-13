package teamproject.gamelogic.domain;

public class GLMap extends Map {

	public GLMap(final Cell[][] cells) {
		super(cells);
	}

	public GLMap() {
		super(Map.defaultNumberOfCells);
	}

}

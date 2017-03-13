package main.java.event.arguments;

import main.java.gamelogic.domain.Entity;

public class EntityMovedEventArgs {
	private int row;
	private int col;
	private Entity entity;

	public EntityMovedEventArgs(final int row, final int col, final Entity entity) {
		this.row = row;
		this.col = col;
		this.entity = entity;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public Entity getEntity() {
		return entity;
	}
}

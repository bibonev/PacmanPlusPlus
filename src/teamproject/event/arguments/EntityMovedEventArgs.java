package teamproject.event.arguments;

import teamproject.constants.EntityType;
import teamproject.gamelogic.domain.Entity;

public class EntityMovedEventArgs {
	private int row;
	private int col;
	private Entity entity;
	
	public EntityMovedEventArgs(int row, int col,Entity entity){
		this.row=row;
		this.col=col;
		this.entity=entity;
	}
	
	public int getRow(){
		return row;
	}

	public int getCol(){
		return col;
	}
	public Entity getEntity(){
		return entity;
	}
}

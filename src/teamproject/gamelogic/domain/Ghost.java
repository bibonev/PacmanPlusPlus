package teamproject.gamelogic.domain;

import teamproject.constants.EntityType;

public abstract class Ghost extends Entity {
	public Ghost() {
		super();
		setType(EntityType.GHOST);
	}
}

package teamproject.ai;

import teamproject.gamelogic.domain.*;
import teamproject.constants.*;

//TODO: to be implemented
public class AggressiveBehavior extends Behavior {

	public AggressiveBehavior(Map map, Position currentPos, int speed, Inventory stash) {
		super(map, currentPos, speed, stash);
	}

	@Override
	public Position pickTarget() {
		return null;
	}

}

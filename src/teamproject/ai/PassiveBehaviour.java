package teamproject.ai;

import teamproject.gamelogic.domain.*;
import teamproject.constants.*;

//TODO: to be implemented
public class PassiveBehaviour extends Behaviour {

	public PassiveBehaviour(Map map, Position currentPos, int speed, Inventory stash, Type type) {
		super(map, currentPos, speed, stash, type);
	}

	@Override
	public Position pickTarget() {
		return null;
	}
}

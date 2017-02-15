package teamproject.gamelogic.domain.stubs;

import teamproject.gamelogic.domain.Behaviour;
import teamproject.gamelogic.domain.Inventory;
import teamproject.gamelogic.domain.Map;
import teamproject.gamelogic.domain.Position;

/**
 * Created by boyanbonev on 15/02/2017.
 */
public class BehaviourStub extends Behaviour {
    /**
     * Instantiates a new behavior.
     *
     * @param map      the map
     * @param startPos the start position of the ai
     * @param speed    the speed of the ai
     * @param stash    the inventory
     * @param type
     */
    public BehaviourStub(Map map, Position startPos, int speed, Inventory stash, Type type) {
        super(map, startPos, speed, stash, type);
    }
}

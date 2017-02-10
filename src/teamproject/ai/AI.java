package teamproject.ai;

import teamproject.gamelogic.domain.Player;
import teamproject.gamelogic.domain.Inventory;
import teamproject.gamelogic.domain.Map;

/**
 * The AI player. Each AI player has a unique behavior that tells it which items
 * to collect or when to attack other players
 * 
 * @author Lyubomir Pashev
 */
public class AI extends Player {

	/** The behavior. */
	private Behavior behavior;

	/**
	 * Instantiates a new AI player.
	 *
	 * @param name
	 *            the name
	 * @param behavior
	 *            the behavior
	 * @param map
	 *            the map
	 */
	public AI(final long id, String name, Behavior behavior, Map map) {
		super(id, name);
		this.behavior = behavior;
	}

	/**
	 * Start the behavior thread.
	 */
	public void start() {
		behavior.start();
	}

}

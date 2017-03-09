package teamproject.ai;

import teamproject.gamelogic.domain.Behaviour;
import teamproject.gamelogic.domain.LocalPlayer;
import teamproject.gamelogic.domain.Map;

/**
 * The AI player. Each AI player has a unique behavior that tells it which items
 * to collect or when to attack other players
 *
 * @author Lyubomir Pashev
 */
public class AIPlayer extends LocalPlayer {

	/** The behavior. */
	private Behaviour behavior;

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
	public AIPlayer(final String name, final Behaviour behavior, final Map map) {
		super(name);
		this.behavior = behavior;
	}

	public Behaviour getBehaviour() {
		return behavior;
	}
}

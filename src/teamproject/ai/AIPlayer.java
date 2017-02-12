package teamproject.ai;

import java.util.Optional;

import teamproject.gamelogic.domain.Behaviour;
import teamproject.gamelogic.domain.Map;
import teamproject.gamelogic.domain.Player;

/**
 * The AI player. Each AI player has a unique behavior that tells it which items
 * to collect or when to attack other players
 *
 * @author Lyubomir Pashev
 */
public class AIPlayer extends Player {

	/** The behavior. */
	private Behaviour behavior;

	/**
	 * Instantiates a new AI player.
	 *
	 * @param id
	 *            player's id (present for real players, empty for AI)
	 * @param name
	 *            the name
	 * @param behavior
	 *            the behavior
	 * @param map
	 *            the map
	 */
	public AIPlayer(final Optional<Long> id, final String name, final Behaviour behavior, final Map map) {
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

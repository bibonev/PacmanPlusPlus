package main.java.ai;

import main.java.gamelogic.domain.Behaviour;
import main.java.gamelogic.domain.Game;
import main.java.gamelogic.domain.LocalGhost;

/**
 * The AI Ghost.
 * 
 * @author Lyubomir Pashev
 */
public class AIGhost extends LocalGhost {
	private Behaviour behaviour;

	/**
	 * Instantiates a new ghost.
	 *
	 * @param id
	 *            entity ID
	 */
	public AIGhost() {
		super();
	}

	public Behaviour getBehaviour() {
		return behaviour;
	}

	public void setBehaviour(final Behaviour behaviour) {
		this.behaviour = behaviour;
	}

	@Override
	public void gameStep(final Game game) {
		behaviour.run();
	}
}

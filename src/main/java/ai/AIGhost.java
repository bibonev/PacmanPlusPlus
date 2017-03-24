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
	
	/** The behaviour. */
	private Behaviour behaviour;

	/**
	 * Instantiates a new ghost.
	 */
	public AIGhost() {
		super();
	}

	/**
	 * Gets the behaviour.
	 *
	 * @return the behaviour
	 */
	public Behaviour getBehaviour() {
		return behaviour;
	}

	/**
	 * Sets the behaviour.
	 *
	 * @param behaviour the new behaviour
	 */
	public void setBehaviour(final Behaviour behaviour) {
		this.behaviour = behaviour;
	}

	/* (non-Javadoc)
	 * @see main.java.gamelogic.domain.Entity#gameStep(main.java.gamelogic.domain.Game)
	 */
	@Override
	public void gameStep(final Game game) {
		behaviour.run();
	}
}

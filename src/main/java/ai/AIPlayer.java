package main.java.ai;

import main.java.gamelogic.domain.Behaviour;
import main.java.gamelogic.domain.Game;
import main.java.gamelogic.domain.LocalPlayer;

/**
 * The AI player. Each AI player has a behavior that tells it where to move
 * and when to use skills.
 *
 * @author Lyubomir Pashev
 */
public class AIPlayer extends LocalPlayer {

	/** The behavior. */
	private Behaviour behavior;

	/**
	 * Instantiates a new AI player.
	 */
	public AIPlayer() {
		super("AI");
	}

	/**
	 * Sets the behaviour.
	 *
	 * @param behavior the new behaviour
	 */
	public void setBehaviour(Behaviour behavior) {

		this.behavior = behavior;
	}
	
	/* (non-Javadoc)
	 * @see main.java.gamelogic.domain.Player#gameStep(main.java.gamelogic.domain.Game)
	 */
	@Override
	public void gameStep(final Game game) {
		behavior.run();
	}
}

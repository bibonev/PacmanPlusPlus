package main.java.gamelogic.domain;

/**
 * Represent a remote player (used for multiplayer games)
 *
 * @author aml
 *
 */
public class RemotePlayer extends Player {
	public RemotePlayer(final int id, final String name) {
		super(name);
		setID(id);
	}
}

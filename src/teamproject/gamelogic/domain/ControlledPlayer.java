package teamproject.gamelogic.domain;

/**
 * Represent a controlled player
 * 
 * @author aml
 *
 */
public class ControlledPlayer extends LocalPlayer {
	public ControlledPlayer(final int id, final String name) {
		super(name);
		setID(id);
	}
}

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
	
	public void moveUp() {
		setPosition(getPosition().add(-1, 0));
	}
	
	public void moveDown() {
		setPosition(getPosition().add(1, 0));
	}
	
	public void moveLeft() {
		setPosition(getPosition().add(0, -1));
	}
	
	public void moveRight() {
		setPosition(getPosition().add(0, 1));
	}
}

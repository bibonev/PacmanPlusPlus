package teamproject.gamelogic.domain;
import teamproject.constants.*;
/**
 * Represent a controlled player
 * 
 * @author aml
 *
 */
public class ControlledPlayer extends LocalPlayer {
	private MovementDirection direction;

	public ControlledPlayer(final int id, final String name) {
		super(name);
		setID(id);
		direction = MovementDirection.RIGHT;
	}
	
	public void moveUp() {
		setPosition(getPosition().add(-1, 0));
		direction = MovementDirection.UP;
	}
	
	public void moveDown() {
		setPosition(getPosition().add(1, 0));
		direction = MovementDirection.DOWN;
	}
	
	public void moveLeft() {
		setPosition(getPosition().add(0, -1));
		direction = MovementDirection.LEFT;
	}
	
	public void moveRight() {
		setPosition(getPosition().add(0, 1));
		direction = MovementDirection.RIGHT;
	}

	public void move(){
		switch (direction){
			case UP:
				moveUp();
				break;
			case DOWN:
				moveDown();
				break;
			case LEFT:
				moveLeft();
				break;
			case RIGHT:
				moveRight();
				break;
		}
	}
}

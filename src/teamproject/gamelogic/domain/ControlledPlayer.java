package teamproject.gamelogic.domain;

public class ControlledPlayer extends LocalPlayer {
	public ControlledPlayer(int id, String name) {
		super(name);
		this.setID(id);
	}
}

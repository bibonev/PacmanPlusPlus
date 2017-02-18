package teamproject.gamelogic.domain;

public abstract class Player extends Entity {
	private String name;
	private double angle;

	public Player(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public void setAngle(double angle) {
		this.angle = angle;
	}
}

package main.java.event.arguments;

import main.java.gamelogic.domain.Player;

public class PlayerMovedEventArgs extends EntityMovedEventArgs {
	private double angle;

	public PlayerMovedEventArgs(final int row, final int col, final double angle, final Player player) {
		super(row, col, player);
		this.angle = angle;
	}

	public double getAngle() {
		return angle;
	}
}

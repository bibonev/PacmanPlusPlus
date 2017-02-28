package teamproject.event.arguments;

import teamproject.gamelogic.domain.Player;

public class PlayerMovedEventArgs extends EntityMovedEventArgs {
	private double angle;
	
	public PlayerMovedEventArgs(int row, int col,double angle, Player player){
		super(row, col, player);
		this.angle = angle;
	}
	
	public double getAngle() {
		return angle;
	}
}

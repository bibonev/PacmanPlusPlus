package teamproject.event.arguments;

public class LocalPlayerMovedEventArgs {
	private int row, col;
	private double angle;
	private int playerID;
	
	public LocalPlayerMovedEventArgs(int row, int col, double angle, int playerID) {
		this.row = row;
		this.col = col;
		this.angle = angle;
		
		this.playerID = playerID;
	}

	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public int getPlayerID() {
		return playerID;
	}
}

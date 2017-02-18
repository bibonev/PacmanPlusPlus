package teamproject.event.arguments;

public class LocalGhostMovedEventArgs {
	private int row, col;
	private int ghostID;
	
	public LocalGhostMovedEventArgs(int row, int col, int ghostID) {
		this.row = row;
		this.col = col;
		
		this.ghostID = ghostID;
	}

	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public int getGhostID() {
		return ghostID;
	}
}

package teamproject.ui;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

/**
 * Abstract class for all screens
 * 
 * @author Rose Kirtley
 *
 */
public abstract class Screen {
	
	public FlowPane pane;
	public GameUI game;
		
	public Screen(GameUI game){
		this.game = game;
		
		pane = new FlowPane();
        pane.setPadding(new Insets(5, 0, 5, 0));
        pane.setVgap(4);
        pane.setHgap(4);
        pane.setColumnHalignment(HPos.CENTER); 
        pane.setOrientation(Orientation.VERTICAL);
        pane.getStyleClass().add("paneStyle");
        pane.setAlignment(Pos.TOP_CENTER);
	}
	
	public Pane getPane(){
		return pane;
	}
}

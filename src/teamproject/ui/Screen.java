package teamproject.ui;

import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
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
	
	public void select(Button button){
		button.setDefaultButton(true);
		button.getStyleClass().clear();
		button.getStyleClass().add("selectedButton");
	}
	
	public void unselect(Button button){
		button.setDefaultButton(false);
		button.getStyleClass().clear();
		button.getStyleClass().add("unselectedButton");
	}
	
	public void selectBack(Button button){
		button.setDefaultButton(true);
		button.getStyleClass().clear();
		button.getStyleClass().add("selectedBack");
	}
	
	public void unselectBack(Button button){
		button.setDefaultButton(false);
		button.getStyleClass().clear();
		button.getStyleClass().add("unselectedBack");
	}
	
	public void reset(Button button){
		button.getStyleClass().clear();
		button.getStyleClass().add("buttonStyle");
	}
	
	public void resetBack(Button button){
		button.getStyleClass().clear();
		button.getStyleClass().add("backButtonStyle");
	}
	
	public void setUpHover(Button button){
		button.addEventHandler(MouseEvent.MOUSE_MOVED,
		        new EventHandler<MouseEvent>() {
		          @Override
		          public void handle(MouseEvent e) {
		            unselectAll();
		            button.setDefaultButton(true);
		          }
		        });
	}
	
	public abstract void changeSelection(boolean up);
	public abstract void makeSelection();
	public abstract void unselectAll();
}

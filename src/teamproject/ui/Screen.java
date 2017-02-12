package teamproject.ui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
	
	public String name;
		
	public Screen(GameUI game){
		this.game = game;
		
		pane = new FlowPane();
        pane.setPadding(new Insets(5, 0, 5, 0));
        pane.setVgap(4);
        pane.setHgap(4);
        pane.setOrientation(Orientation.VERTICAL);
        pane.getStyleClass().add("paneStyle");
        pane.setAlignment(Pos.TOP_CENTER);
	}
	
//	public void setUpButton(Node node) {
//		    node.styleProperty().bind(
//		      Bindings
//		        .when(node.hoverProperty())
//		          .then(
//		            new SimpleStringProperty(hoverbuttonStyle)
//		          )
//		          .otherwise(
//		            new SimpleStringProperty(buttonStyle)
//		          )
//		    );
//		  }
	
	public Pane getPane(){
		return pane;
	}
	
	public String getName(){
		return name;
	}
}

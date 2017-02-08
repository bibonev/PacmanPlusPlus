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
public abstract class AbstractScreen {
	
	public FlowPane pane;
	public GameUI game;
	
	public String name;
	
	public static String buttonStyle = "-fx-background-color: linear-gradient(#f2f2f2, #d6d6d6), linear-gradient(#fcfcfc 0%, #d9d9d9 20%, #d6d6d6 100%), linear-gradient(#dddddd 0%, #f6f6f6 50%); -fx-background-radius: 8,7,6; -fx-background-insets: 0,1,2; -fx-text-fill: black; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 ); -fx-font-size: 18px;";
	public static String hoverbuttonStyle = "-fx-font-weight: bold; -fx-background-color: linear-gradient(#f2f2f2, #d6d6d6), linear-gradient(#fcfcfc 0%, #d9d9d9 20%, #d6d6d6 100%), linear-gradient(#dddddd 0%, #f6f6f6 50%); -fx-background-radius: 8,7,6; -fx-background-insets: 0,1,2; -fx-text-fill: black; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 ); -fx-font-size: 18px;";
	public static String titleStyle = "-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #333333; -fx-effect: dropshadow( gaussian , rgba(255,255,255,0.5) , 0,0,0,1 );";
	public static String labelStyle = "-fx-font-size: 20px; -fx-text-fill: #333333; -fx-effect: dropshadow( gaussian, rgba(255,255,255,0.5) , 0,0,0,1);";
	
	public AbstractScreen(GameUI game){
		this.game = game;
		
		pane = new FlowPane();
        pane.setPadding(new Insets(5, 0, 5, 0));
        pane.setVgap(4);
        pane.setHgap(4);
        pane.setOrientation(Orientation.VERTICAL);
        pane.setStyle("-fx-background-color: DAE6F3;");
        pane.setAlignment(Pos.TOP_CENTER);
	}
	
	public void setUpButton(Node node) {
		    node.styleProperty().bind(
		      Bindings
		        .when(node.hoverProperty())
		          .then(
		            new SimpleStringProperty(hoverbuttonStyle)
		          )
		          .otherwise(
		            new SimpleStringProperty(buttonStyle)
		          )
		    );
		  }
	
	public Pane getPane(){
		return pane;
	}
	
	public String getName(){
		return name;
	}
}

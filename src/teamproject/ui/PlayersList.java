package teamproject.ui;

import java.util.Collection;
import java.util.Iterator;

import javafx.scene.control.Label;
import teamproject.gamelogic.domain.Player;

/**
 * Screen to be added to the multiplayer lobby to list all current players in the game
 * 
 * @author Rose Kirtley
 *
 */
public class PlayersList extends AbstractScreen {

	public PlayersList(GameUI game) {
		super(game);
        
        /*Collection<Player> players = game.getGame().getWorld().getPlayers();
                
        Iterator<Player> it = players.iterator();
        
        while(it.hasNext()){
        	Label name = new Label(it.next().getName());
        	name.setStyle(labelStyle);
        	pane.getChildren().add(name);
        }*/
        
        Label name = new Label("Name1");
    	name.setStyle(labelStyle);
    	pane.getChildren().add(name);
    	
    	Label name2 = new Label("Name2");
    	name2.setStyle(labelStyle);
    	pane.getChildren().add(name2);

	}
	
	public void addPlayer(Player name){
		Label label = new Label(name.getName());
		label.setStyle(labelStyle);
		pane.getChildren().add(label);
	}

}

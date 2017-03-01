package teamproject.ui;

import java.util.HashMap;
import javafx.application.Platform;
import javafx.scene.control.Label;
import teamproject.gamelogic.core.LobbyPlayerInfo;

/**
 * Screen to be added to the multiplayer lobby to list all current players in the game
 * 
 * @author Rose Kirtley
 *
 */
public class PlayersList extends Screen {
	private HashMap<Integer, Label> playerLabels;

	public PlayersList(GameUI game) {
		super(game);
		
		playerLabels = new HashMap<>();
        
        /*Collection<Player> players = game.getGame().getWorld().getPlayers();
                
        Iterator<Player> it = players.iterator();
        
        while(it.hasNext()){
        	Label name = new Label(it.next().getName());
        	name.setStyle(labelStyle);
        	pane.getChildren().add(name);
        }*/
	}
	
	public void addPlayer(LobbyPlayerInfo player) {
		Platform.runLater(() -> {
			Label label = new Label(player.getName());
	    	label.getStyleClass().add("labelStyle");
			pane.getChildren().add(label);
			playerLabels.put(player.getID(), label);
		});
	}

	public void removePlayer(int playerID) {
		Platform.runLater(() -> {
			Label label = playerLabels.remove(playerID);
			pane.getChildren().remove(label);
		});
	}
	
	public void clear() {
		for(int i : playerLabels.keySet()) {
			removePlayer(i);
		}
	}
}

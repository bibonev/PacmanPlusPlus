package main.java.event.arguments;

import main.java.gamelogic.domain.Player;

/**
 * Created by Simeon Kostadinov on 22/03/2017.
 */
public class PlayerShieldActivatedEventArgs {

    private Player player;
    private int shieldValue;

    public PlayerShieldActivatedEventArgs(Player player, int shieldValue) {
        this.player = player;
        this.shieldValue = shieldValue;
    }


    public Player getPlayer() {
        return player;
    }

    public int getShieldValue() {
        return shieldValue;
    }
}

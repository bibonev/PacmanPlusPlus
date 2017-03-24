package main.java.event.arguments;

import main.java.gamelogic.domain.Player;

/**
 * Created by simeonkostadinov on 23/03/2017.
 */
public class PlayerShieldRemovedEventArgs {

    private Player player;
    private int shieldValue;

    public PlayerShieldRemovedEventArgs(Player player, int shieldValue) {
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

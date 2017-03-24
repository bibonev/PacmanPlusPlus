package main.java.event.arguments;

import main.java.gamelogic.domain.Player;

/**
 * Created by simeonkostadinov on 22/03/2017.
 */
public class PlayerCooldownChangedEventArgs {
    private int cooldownLevel;
    private char slot;
    private Player player;

    public PlayerCooldownChangedEventArgs(Player player, int cooldownLevel, char slot) {
        this.player = player;
        this.cooldownLevel = cooldownLevel;
        this.slot = slot;
    }

    public char getSlot() {
        return slot;
    }

    public int getCooldownLevel() {
        return cooldownLevel;
    }

    public Player getPlayer() {
        return player;
    }
}

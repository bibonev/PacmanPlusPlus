package main.java.event.arguments;

import main.java.gamelogic.domain.Player;

/**
 * Created by simeonkostadinov on 22/03/2017.
 */
public class PlayerLaserActivatedEventArgs {
    private double direction;
    private Player player;
    private int coolDown;

    public PlayerLaserActivatedEventArgs(Player player, double direction, int coolDown) {
        this.player = player;
        this.direction = direction;
        this.coolDown = coolDown;
    }

    public double getDirection() {
        return direction;
    }

    public Player getPlayer() {
        return player;
    }

    public int getCoolDown() {
        return coolDown;
    }
}

package main.java.event.listener;


import main.java.event.arguments.PlayerShieldRemovedEventArgs;

/**
 * Created by simeonkostadinov on 23/03/2017.
 */
public interface PlayerShieldRemovedListener {
    public void onPlayerShieldRemoved(PlayerShieldRemovedEventArgs args);
}

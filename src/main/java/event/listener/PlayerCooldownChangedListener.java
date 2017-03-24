package main.java.event.listener;

import main.java.event.arguments.PlayerCooldownChangedEventArgs;

/**
 * Created by simeonkostadinov on 22/03/2017.
 */
public interface PlayerCooldownChangedListener {
    public void onPlayerCooldownChanged(PlayerCooldownChangedEventArgs args);
}

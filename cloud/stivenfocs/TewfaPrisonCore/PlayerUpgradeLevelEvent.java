package cloud.stivenfocs.TewfaPrisonCore;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerUpgradeLevelEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Integer level;

    public PlayerUpgradeLevelEvent(Player player, Integer level) {
        this.player = player;
		this.level = level;
    }

    public Player getPlayer() {
        return player;
    }
	
	public Integer getLevel() {
		return level;
	}

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
	
}
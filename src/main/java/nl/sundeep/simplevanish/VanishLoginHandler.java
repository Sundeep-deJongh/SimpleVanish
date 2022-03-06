package nl.sundeep.simplevanish;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class VanishLoginHandler implements Listener {

    private Main plugin;

    public VanishLoginHandler(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void handleLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        if (plugin.isVanished(player)) {
            plugin.showPlayer(player);
        }
        if (player.hasPermission("vanish.seeall")) return;
        for (Player p1 : plugin.getServer().getOnlinePlayers()) {
            if (plugin.isVanished(p1) && player != p1 && p1 != null) {
                player.hidePlayer(p1);
            }
        }
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (plugin.isVanished(player)) {
            plugin.showPlayer(player);
        }
    }
}

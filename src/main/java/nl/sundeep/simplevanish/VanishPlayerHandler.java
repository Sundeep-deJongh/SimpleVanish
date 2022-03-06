package nl.sundeep.simplevanish;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class VanishPlayerHandler implements Listener {

    private Main plugin;

    public VanishPlayerHandler(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getTarget() instanceof Player && plugin.isVanished((Player)event.getTarget())) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onDamageByBlock(EntityDamageByBlockEvent event) {
        if (event.getEntity() instanceof Player && plugin.isVanished((Player)event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if(!(entity instanceof Player)) return;
        Player player = (Player) entity;
        //check if vanished, if so: cancel
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        if (plugin.isVanished(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}

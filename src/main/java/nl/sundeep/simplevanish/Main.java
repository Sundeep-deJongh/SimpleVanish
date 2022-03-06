package nl.sundeep.simplevanish;


import nl.sundeep.simplevanish.utils.Format;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    public Logger log = Logger.getLogger("Minecraft");
    private ArrayList<String> hiddenUsernames = new ArrayList<String>();

    public void onEnable() {
        getServer().getPluginManager().registerEvents(new VanishLoginHandler(this), this);
        getServer().getPluginManager().registerEvents(new VanishPlayerHandler(this), this);
        getCommand("vanish").setExecutor(this);

    }

    public void onDisable() {
        for (Player p : getServer().getOnlinePlayers()) {
            if (isVanished(p)) {
                showPlayer(p);
            }
        }
        hiddenUsernames.clear();
        log.info("All players are visible again");
    }


    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("list")) {
                if (!player.hasPermission("vanish.list")) {
                    player.sendMessage((Format.chat("&7" + "You don't have permission")));
                    return true;
                }
                if (hiddenUsernames.size() > 0) {
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < hiddenUsernames.size(); i++) {
                        builder.append(hiddenUsernames.get(i));
                        if (i < hiddenUsernames.size() - 1) {
                            builder.append(", ");
                        }
                    }

                    player.sendMessage(Format.chat("&7" + "Hidden players: " + builder.toString()));
                } else {
                    player.sendMessage(Format.chat("&7" + "There are no hidden players"));
                }
                return true;
            } else if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
                return false;
            } else if (player.hasPermission("vanish.vanishother")) {
                Player target = getServer().getPlayer(args[0]);
                if (target == null) target = getServer().getPlayerExact(args[0]);
                if (target == null) {
                    player.sendMessage(Format.chat("&7" + "Player doesn't exist or too many results"));
                    return true;
                }

                if (!isVanished(target)) {
                    vanishPlayer(target);
                    target.sendMessage(Format.chat("&7" + "You have vanished"));
                    player.sendMessage(Format.chat("&7" + target.getName() + " is now invisible"));
                    getServer().broadcastMessage(Format.chat("&4" + target.getName() + " left the game."));
                    return true;
                } else {
                    showPlayer(target);
                    target.sendMessage(Format.chat("&7" + "You are no longer invisible"));
                    player.sendMessage(Format.chat("&7" + target.getName() + " is no longer invisible"));
                    getServer().broadcastMessage(Format.chat("&4" + target.getName() + " joined the game."));
                    return true;
                }
            }
        } else if (player.hasPermission("vanish.vanish")) {
            if (!isVanished(player)) {
                vanishPlayer(player);
                player.sendMessage(Format.chat("&7" + "You have vanished"));
                getServer().broadcastMessage(Format.chat("&4" + player.getName() + " left the game."));
                return true;
            } else {
                showPlayer(player);
                player.sendMessage(Format.chat("&4" + "You are visible again"));
                getServer().broadcastMessage(Format.chat("&4" + player.getName() + " joined the game."));
                return true;
            }
        }
        return false;
    }

    public boolean isVanished(Player player) {
        return hiddenUsernames.contains(player.getName());
    }

    public void vanishPlayer(Player player) {
        hiddenUsernames.add(player.getName());
        for (Player p1 : getServer().getOnlinePlayers()) {
            if (p1 == player) {
                continue;
            } else if (p1.hasPermission("vanish.seeall")) {
                p1.sendMessage(Format.chat("&7" + player.getName() + " vanished"));
                continue;
            } else if (p1.hasPermission("vanish.list")) {
                p1.hidePlayer(player);
                p1.sendMessage(Format.chat("&7" + player.getName() + " vanished"));
                continue;
            }
            p1.hidePlayer(player);
        }
    }

    public void showPlayer(Player player) {
        hiddenUsernames.remove(player.getName());
        for (Player p1 : getServer().getOnlinePlayers()) {
            p1.showPlayer(player);
        }
    }


}

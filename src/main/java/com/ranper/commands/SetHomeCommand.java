package com.ranper.commands;

import com.ranper.Home;
import com.ranper.models.PlayerHome;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

public class SetHomeCommand implements CommandExecutor {

    private final Home plugin;
    public SetHomeCommand(Home plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            Player player = (Player) sender;
            UUID id = player.getUniqueId();
            Location location = player.getLocation();
            // uso apenas /sethome
            if (!(args.length > 0)) {
                try {
                    // verificando se o player tem Home
                    PlayerHome home = this.plugin.getDatabase().findPlayerHomeByUUID(id.toString());
                    // caso o player tenha home e deseja atualizar
                    PlayerHome newhome;

                    //se não tiver home cria uma nova
                    if (home == null) {
                        home = new PlayerHome(player.getUniqueId().toString(), location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
                        player.sendMessage(ChatColor.GOLD + "Home Set.");
                        this.plugin.getDatabase().createPlayerHome(home);
                    //se tiver home substitui a antiga
                    } else {
                        newhome = new PlayerHome(player.getUniqueId().toString(), location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
                        this.plugin.getDatabase().updatePlayerHome(newhome);
                        player.sendMessage(ChatColor.AQUA + "Your home was updated!");
                        player.sendMessage();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                player.sendMessage(ChatColor.RED + "Use only: /sethome");
            }


        }
        return false;
    }
}

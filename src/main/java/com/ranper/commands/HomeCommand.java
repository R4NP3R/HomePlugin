package com.ranper.commands;

import com.ranper.Home;
import com.ranper.models.PlayerHome;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class HomeCommand implements CommandExecutor {
    private final Home plugin;
    private HashMap<UUID, Particle> players = new HashMap<>();
    public HomeCommand (Home plugin) {
        this.plugin = plugin;
    }
    int delayQuantity = 5;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //comando poder ser usado apenas por players
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID id = player.getUniqueId();

            if (args.length > 0) {
                if (player.isOp()) {
                    // comando para alterar o delay que o player volta para a casa
                    if (args[0].equalsIgnoreCase("delay")) {
                        // /home delay irá retornar uma mensagem mostrando o tempo de delay
                        if (args.length < 2) {
                            player.sendMessage(ChatColor.GOLD + "Teleport delay to home is " + delayQuantity + " seconds");
                            return false;
                        }
                        // /home delay irá retornar uma mensagem mostrando os comandos do /home delay
                        if(args[1].equalsIgnoreCase("help")) {
                            player.sendMessage(ChatColor.GOLD + "Use: /home delay", ChatColor.AQUA + "(Get delay time to teleport home)");
                            player.sendMessage(ChatColor.GOLD + "Use: /home delay <seconds>", ChatColor.AQUA + "(Set how many seconds time to teleport home)");
                            return false;
                        }
                        // bloco para colocar a quantidade o tempo do delay
                        try  {
                            setDelayQuantity(Integer.parseInt(args[1]));
                            player.sendMessage(ChatColor.GOLD + "Delay to teleport Home changed to: " + args[1]);
                        } catch (Exception e) {
                            player.sendMessage(ChatColor.RED + "* Please insert a integer number");
                        }
                    }
                } else {
                    // caso o player não tenha OP irá retornar esse mensagem:
                    player.sendMessage(ChatColor.RED + "You don't have permission to use home delay command");
                }
                // /home help irá retornar uma mensagem mostrando os comandos do /home
                if (args[0].equalsIgnoreCase("help")) {
                    player.sendMessage(ChatColor.GOLD + "Use: /home particles list", ChatColor.AQUA +"(Show particles options)");
                    player.sendMessage(ChatColor.GOLD + "Use: /home particles <particle>", ChatColor.AQUA +"(Set a particle)");
                    player.sendMessage(ChatColor.GOLD + "Use: /home particle", ChatColor.AQUA +"(Get your particle name)");
                    player.sendMessage(ChatColor.GOLD + "Use: /home", ChatColor.AQUA + "(Back to home)");
                    return false;
                }
                // se digitar /home particle irá retornar a particula que está sendo usada
                if(args[0].equalsIgnoreCase("particle")) {
                    if (hasParticle(player)) {
                        player.sendMessage(ChatColor.GOLD + "Your Particle is: " + ChatColor.AQUA + players.get(player.getUniqueId()).name());
                        return false;
                    }
                    //se ele tiver com as particulas desativadas vai retornar esse mensagem
                    player.sendMessage(ChatColor.RED + "Your Particles are off");
                    return false;
                }
                // retorna uma mensagem para direcionar o player como usar o /home
                if (args[0].equalsIgnoreCase("particles")) {
                    if (args.length < 2) {
                        player.sendMessage(ChatColor.GOLD + "Use: /home help");
                        return false;
                    };
                    // desativa as particulas
                    if(args[1].equalsIgnoreCase("off")) {
                        if(this.hasParticle(player)) {
                            this.removeParticle(player);
                            player.sendMessage(ChatColor.RED + "Home particles off");
                        } else {
                            player.sendMessage(ChatColor.RED + "You home particles are off");
                        }
                        return false;
                    };
                    // lista as particulas que o player pode usar
                    if(args[1].equalsIgnoreCase("list")) {
                        listParticles(player);
                        return true;
                    };
                    // se a particula estiver presenta na lista de particulas será alterada
                    try {
                        Particle e = Particle.valueOf(args[1].toUpperCase());
                        this.addParticle(player, e);
                        player.sendMessage(ChatColor.GREEN + "Home particle " + args[1].toLowerCase() + " set");
                    // caso não esteja irá retornar essa mensagem
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.RED + "Invalid Particle: " + args[1].toLowerCase());
                        player.sendMessage(ChatColor.RED + "Use: /home particle list");
                    }
                }
            // uso apenas do /home
            } else {
                try {
                    //vai no banco de dados ver se o player já te uma HOme
                    PlayerHome home = this.plugin.getDatabase().findPlayerHomeByUUID(id.toString());
                    // se não tiver home retornar uma mensagem
                    if(home == null) {
                        player.sendMessage(ChatColor.RED + "You do not have a home set, use: /sethome");
                    } else {
                        // se tiver home entra no delay
                        plugin.addQueue(id);
                        //timer do bukkit para ter o delay de tp
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                // se estiver na fila
                                if (plugin.isQued(id)) {
                                    if(delayQuantity == 0) {
                                        player.teleport(new Location(player.getWorld(), home.getX(), home.getY(), home.getZ(), home.getYaw(), home.getPitch()));
                                        player.sendMessage(ChatColor.GOLD + "Teleporting.");
                                        particleRun(player, player.getLocation());
                                        //remove o player da fila
                                        plugin.cancelQueue(id);
                                        this.cancel();
                                    } else {
                                        player.sendMessage(ChatColor.GOLD + "Teleporting in " + delayQuantity-- + " seconds.");
                                    }
                                }
                                // se andar ou tomar hit cancela o tp
                                else {
                                    player.sendMessage(ChatColor.RED + "Teleportation cancelled");
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(plugin, 0, 20);

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // se o player não enviar o /home retorna essa mensagem
            sender.sendMessage(ChatColor.RED + "/home is a player command only!");
        }
        return false;
    }

    // função que retorna as listas de particulas
    public void listParticles(Player player) {
        String text = "";

        //cada pa é o nome de uma particula
        for (Particle pa : Particle.values()) {
            if (text.equals("")) {
                text = pa.name();
            } else {
                text += ", " + pa.name();
            }
        }

        text += ", OFF";
        player.sendMessage(ChatColor.GRAY + "/home particle <particle>");
        player.sendMessage(ChatColor.AQUA + text);
    }

    public void addParticle(Player player, Particle particle) {
        players.put(player.getUniqueId(), particle);
    }

    public void removeParticle(Player p) {
        players.remove(p.getUniqueId());
    }
    public boolean hasParticle(Player p) {
        return players.containsKey(p.getUniqueId());
    }

    // função para colocar a particula onde o player tem a home (caso ele tenha home)
    private void particleRun (Player player, Location loc) {
        if (players.get(player.getUniqueId()) != null) {
            String particleName = players.get(player.getUniqueId()).toString();
            player.spawnParticle(Particle.valueOf(particleName), loc, 300, 3, 3, 3);
        }
        return;
    }

    public void setDelayQuantity(int delayQuantity) {
        this.delayQuantity = delayQuantity;
    }
}

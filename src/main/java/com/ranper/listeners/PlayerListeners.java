package com.ranper.listeners;

import com.ranper.Home;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class PlayerListeners implements Listener {

    private final Home plugin;
    public PlayerListeners (Home plugin) {
        this.plugin = plugin;
    }


    // listener para cancelar o tp do player caso ele se mexa
    @EventHandler
    public void OnMove(PlayerMoveEvent event) {
        // verifica se a localização do player é igual onde ele deu o /home
        if(!event.getFrom().getBlock().equals(event.getTo().getBlock())) {
            Player player = event.getPlayer();
            UUID id = player.getUniqueId();

            // remove ele da fila (caso esteja)
            if (plugin.isQued(id))
                plugin.cancelQueue(id);
        }
    }
    // verifica se player levou dano
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            UUID id = player.getUniqueId();

            // remove ele da fila (caso esteja)
            if (plugin.isQued(id))
                plugin.cancelQueue(id);
        }
    }
}

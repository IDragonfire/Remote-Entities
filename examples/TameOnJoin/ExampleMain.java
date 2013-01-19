package TameOnJoin;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.RemoteEntities;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
import de.kumpelblase2.remoteentities.api.features.RemoteTamingFeature;
import de.kumpelblase2.remoteentities.api.features.TamingFeature;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireFollowTamer;
import de.kumpelblase2.remoteentities.exceptions.PluginNotEnabledException;

public class ExampleMain extends JavaPlugin implements Listener {
    private EntityManager npcManager;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        try {
            this.npcManager = RemoteEntities.createManager(this);
        } catch (PluginNotEnabledException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent inEvent) throws Exception {
        RemoteEntity entity = this.npcManager.createNamedEntity(
                RemoteEntityType.Human, inEvent.getPlayer().getLocation(),
                "test");
        TamingFeature feature = new RemoteTamingFeature(entity);
        feature.tame(inEvent.getPlayer());
        entity.getFeatures().addFeature(feature);
        entity.getMind().addMovementDesire(
                new DesireFollowTamer(entity, 5, 15),
                entity.getMind().getHighestMovementPriority() + 1);
    }
}
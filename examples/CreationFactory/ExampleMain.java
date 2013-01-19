package CreationFactory;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import de.kumpelblase2.remoteentities.CreateEntityContext;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.RemoteEntities;
import de.kumpelblase2.remoteentities.api.RemoteEntityType;
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
        // We prepare the entity
        // note that every method - except the create method - from CreateEntityContext returns itself, so you can just continue with adding options like you see here:
        CreateEntityContext context = this.npcManager.prepareEntity(
                RemoteEntityType.Human).asStationary(true).withName("test")
                .atLocation(inEvent.getPlayer().getLocation()).withID(10);
        // Whenever you specify an id it's not 100% sure that the entity you create has that ID. It'll get the next free ID from the give ID.
        for (int i = 0; i < 10; i++) {
            // Whenever we call the create method a new entity gets created with the options we specified in the context. That means we'll create ten "test" human npcs in this case.
            context.create();
        }
    }
}
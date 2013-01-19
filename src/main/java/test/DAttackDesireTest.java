package test;

import net.minecraft.server.v1_4_R1.EntityHuman;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import de.kumpelblase2.remoteentities.*;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.thinking.*;
import de.kumpelblase2.remoteentities.api.thinking.goals.*;
import de.kumpelblase2.remoteentities.entities.*;

public class DAttackDesireTest implements Listener {

    private EntityManager em;

    public DAttackDesireTest(EntityManager em) {
        this.em = em;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Location loc = event.getPlayer().getLocation();
//        loc.setX(loc.getX() + 5);

        final RemotePlayer attackEntity = (RemotePlayer) em.createNamedEntity(
                RemoteEntityType.Human, loc, "attack", false);
        Player p = (Player) attackEntity.getBukkitEntity();
        p.setItemInHand(new ItemStack(Material.IRON_SWORD));

        final Mind mind = attackEntity.getMind();
        // mind.addMovementDesire(new DesireSwim(attackEntity), 0);
        // mind.addMovementDesire(new DesireAttackOnCollide(attackEntity,
        // EntityHuman.class, false), 2);
        // mind.addMovementDesire(new DesireMoveTowardsRestriction(attackEntity),
        // 4);
        // mind.addMovementDesire(new DesireWanderAround(attackEntity), 6);
        // mind.addMovementDesire(new DesireLookAtNearest(attackEntity,
        // EntityHuman.class, 8), 7);
        // mind.addMovementDesire(new DesireLookRandomly(attackEntity), 7);
        // DesireFindTarget targetFinderDesire = new DesireFindTarget(
        // attackEntity, 30, false, true);
        // mind.addActionDesire(targetFinderDesire, 1);
        // mind.addMovementDesire(new DesireMoveToTarget(attackEntity, 0.0f), 2);
        // mind.addActionDesire(new DesireAttackNearest(attackEntity,
        // EntityHuman.class, 16, false, true, 0), 2);
        Bukkit.getScheduler().scheduleSyncDelayedTask(
                RemoteEntities.getInstance(), new Runnable() {

                    @Override
                    public void run() {
                        mind.addActionDesire(new DAttackEntity(attackEntity,
                                Bukkit.getOnlinePlayers()[0]), 0);
                        System.out.println("add mind");
                       
                    }
                }, 20 * 5);

        // loc.setX(loc.getX() + 3);
        // em.createNamedEntity(RemoteEntityType.Human, loc, "test", false);
        Bukkit.broadcastMessage("npc spawn 2");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.em.despawnAll();
    }

}

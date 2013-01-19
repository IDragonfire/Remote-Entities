package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_4_R1.EntityLiving;
import net.minecraft.server.v1_4_R1.EntityPlayer;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;

import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

public class DAttackEntity extends DesireBase {

    private long lastUpdate;
    private Entity victim;

    public DAttackEntity(RemoteEntity attacker, Entity victim) {
        super(attacker);
        this.victim = victim;
        System.out.println("speed. " + m_entity.getSpeed());
    }

    @Override
    public boolean shouldExecute() {
        return !victim.isDead();
    }

    @Override
    public void startExecuting() {
        this.lastUpdate = System.currentTimeMillis();
        System.out.println("move and attack");
        move();
        EntityLiving attacker = this.getEntityHandle();
        EntityPlayer target = ((CraftPlayer) this.victim).getHandle();
        // try to attack target
        System.out.println(attacker);
        System.out.println(target);
        // timeout for testing
        lastUpdate = System.currentTimeMillis() + 3000;
        
        // all possible attack methods
//        attacker.m(target);
        // attacker.a(target);
        // attacker.c(target);
        // attacker.d(target);
        // attacker.e(target);
        // attacker.g(target);
        // attacker.h(target);
        // attacker.n(target);
        // attacker.l(target);
        // attacker.setGoalTarget(target);

        // swing arm
        if (attacker.bD() != null)
            attacker.bH();
        super.startExecuting();
    }

    @Override
    public boolean update() {
        if (System.currentTimeMillis() - lastUpdate > refreshRate()) {
            startExecuting();
        }
        return super.update();
    }

    public long refreshRate() {
        return refreshRate(distanceSquared(m_entity.getBukkitEntity()
                .getLocation(), victim.getLocation()));
    }

    /**
     * TODO: dynmaic calc, considered speed, ...
     * 
     * @return milis
     */
    public long refreshRate(double distanceSquared) {
        if (distanceSquared < 1.1) {
            return 500;
        }
        if (distanceSquared < 1.5) {
            return 750;
        }
        if (distanceSquared < 2) {
            return 1000;
        }
        if (distanceSquared < 4 * 4 * 4) {
            return 1250;
        }
        if (distanceSquared < 5 * 5 * 5) {
            return 1500;
        }
        if (distanceSquared < 10 * 10 * 10) {
            return 2000;
        }
        if (distanceSquared < 10 * 10 * 10) {
            return 3000;
        }
        return 5000;
    }

    public static double distanceSquared(Location loc1, Location loc2) {
        double dx = loc1.getX() - loc2.getX();
        double dy = loc1.getY() - loc2.getY();
        double dz = loc1.getZ() - loc2.getZ();

        return dx * dx + dy * dy + dz * dz;
    }

    private void move() {
        this.getRemoteEntity().move(this.victim.getLocation());
    }
}

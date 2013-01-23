package test;

import org.bukkit.Location;

public class RemoteLocation {
    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    public String world;
    
    public RemoteLocation(double x, double y, double z, float yaw, float pitch,
            String world) {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
    }
    
    public static RemoteLocation encodeBukkitLocation(Location loc) {
        return new RemoteLocation(loc.getX(), loc.getY(), loc.getY(), loc.getYaw(), loc.getPitch(), loc.getWorld().getName());
    }
    
}

package de.kumpelblase2.remoteentities.utilities;

import net.minecraft.server.v1_4_R1.*;

import org.bukkit.entity.EntityType;

public class EntityTypeToLivingEntityClass {

    @SuppressWarnings("unchecked")
    public static Class<? extends EntityLiving> map(EntityType type) {
        try {
            // TODO: dynamic package path?
            String typeClass = "" + type;
            Class<?> myClass = Class
                    .forName("net.minecraft.server.v1_4_R1.Entity"
                            + typeClass.substring(0, 1).toUpperCase()
                            + typeClass.substring(1).toLowerCase());
            return (Class<? extends EntityLiving>) myClass;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

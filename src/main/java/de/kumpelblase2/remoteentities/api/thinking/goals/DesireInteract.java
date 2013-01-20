package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.entity.EntityType;

import net.minecraft.server.v1_4_R1.EntityLiving;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.utilities.EntityTypeToLivingEntityClass;

public class DesireInteract extends DesireLookAtNearest
{
	public DesireInteract(RemoteEntity inEntity, Class<? extends EntityLiving> inTarget, float inMinDistance)
	{
		super(inEntity, inTarget, inMinDistance);
		this.m_type = 3;
	}
	
	public DesireInteract(RemoteEntity inEntity, EntityType inTargetBukkitType, float inMinDistance)
        {
                super(inEntity, EntityTypeToLivingEntityClass.map(inTargetBukkitType), inMinDistance);
                this.m_type = 3;
        }
}

package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.v1_4_R1.EntityLiving;
import net.minecraft.server.v1_4_R1.EntityTameableAnimal;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.features.TamingFeature;
import de.kumpelblase2.remoteentities.exceptions.NotTameableException;

public class DesireProtectOwner extends DesireTamedBase
{
	protected EntityLiving m_ownerAttacker;
	
	public DesireProtectOwner(RemoteEntity inEntity, float inDistance, boolean inShouldCheckSight) throws Exception
	{
		super(inEntity, inDistance, inShouldCheckSight);
		if(!(this.getEntityHandle() instanceof EntityTameableAnimal) && !this.getRemoteEntity().getFeatures().hasFeature(TamingFeature.class))
			throw new NotTameableException();
		
		this.m_animal = this.getEntityHandle();
		this.m_type = 1;
	}

	@Override
	public boolean shouldExecute()
	{
		if(this.getEntityHandle() == null)
			return false;
		
		if(!this.isTamed())
			return false;
		else
		{
			EntityLiving owner = this.getTamer();
			if(owner == null)
				return false;
			else
			{
				this.m_ownerAttacker = owner.aC();
				return this.isSuitableTarget(this.m_ownerAttacker, false);
			}
		}
	}
	
	@Override
	public void startExecuting()
	{
		this.getEntityHandle().setGoalTarget(this.m_ownerAttacker);
		super.startExecuting();
	}
}

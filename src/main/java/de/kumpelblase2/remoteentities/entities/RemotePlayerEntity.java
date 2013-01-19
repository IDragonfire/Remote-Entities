package de.kumpelblase2.remoteentities.entities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import net.minecraft.server.v1_4_R1.*;
import net.minecraft.server.v1_4_R1.Navigation;
import de.kumpelblase2.remoteentities.api.*;
import de.kumpelblase2.remoteentities.api.events.RemoteEntityInteractEvent;
import de.kumpelblase2.remoteentities.api.events.RemoteEntityTouchEvent;
import de.kumpelblase2.remoteentities.api.thinking.*;
import de.kumpelblase2.remoteentities.api.thinking.goals.DesireSwim;
import de.kumpelblase2.remoteentities.nms.*;

public class RemotePlayerEntity extends EntityPlayer implements RemoteEntityHandle
{
	protected RemoteEntity m_remoteEntity;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;
	protected EntityLiving m_target;
	
	public RemotePlayerEntity(MinecraftServer minecraftserver, World world, String s, PlayerInteractManager iteminworldmanager)
	{
		super(minecraftserver, world, s, iteminworldmanager);
		new PathfinderGoalSelectorHelper(this.goalSelector).clearGoals();
		new PathfinderGoalSelectorHelper(this.targetSelector).clearGoals();
		iteminworldmanager.setGameMode(EnumGamemode.SURVIVAL);
		this.noDamageTicks = 1;
		this.W = 1;
		this.getNavigation().e(true);
	}
	
	public RemotePlayerEntity(MinecraftServer minecraftserver, World world, String s, PlayerInteractManager iteminworldmanager, RemoteEntity inEntity)
	{
		this(minecraftserver, world, s, iteminworldmanager);
		this.m_remoteEntity = inEntity;
		try
		{
			NetworkManager manager = new RemoteEntityNetworkManager(minecraftserver);
			this.playerConnection = new NullNetServerHandler(minecraftserver, manager, this);
			manager.a(playerConnection);
		}
		catch(Exception e){}
	}

	@Override
	public RemoteEntity getRemoteEntity()
	{
		return this.m_remoteEntity;
	}

	@Override
	public Inventory getInventory()
	{
		return this.getBukkitEntity().getInventory();
	}
	
	@Override
	public void c_(EntityHuman entity)
	{
		if(this.getRemoteEntity() == null || this.getRemoteEntity().getMind() == null)
			return;
		
		if(entity instanceof EntityPlayer && this.getRemoteEntity().getMind().canFeel() && this.getRemoteEntity().getMind().hasBehaviour("Touch"))
		{
			if (this.m_lastBouncedId != entity.id || System.currentTimeMillis() - this.m_lastBouncedTime > 1000)
			{
				if(entity.getBukkitEntity().getLocation().distanceSquared(getBukkitEntity().getLocation()) <= 1)
				{
					RemoteEntityTouchEvent event = new RemoteEntityTouchEvent(this.m_remoteEntity, entity.getBukkitEntity());
					Bukkit.getPluginManager().callEvent(event);
					if(event.isCancelled())
						return;
					
					((TouchBehavior)this.getRemoteEntity().getMind().getBehaviour("Touch")).onTouch((Player)entity.getBukkitEntity());
					this.m_lastBouncedTime = System.currentTimeMillis();
					this.m_lastBouncedId = entity.id;
				}
			}
		}
		super.c_(entity);
	}
	
	@Override
	public boolean a(EntityHuman entity)
	{
		if(this.getRemoteEntity() == null || this.getRemoteEntity().getMind() == null)
			return super.a(entity);
		
		if(entity instanceof EntityPlayer && this.getRemoteEntity().getMind().canFeel())
		{
			RemoteEntityInteractEvent event = new RemoteEntityInteractEvent(this.m_remoteEntity, (Player)entity.getBukkitEntity());
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled())
				return super.a(entity);
			
			if(this.getRemoteEntity().getMind().hasBehaviour("Interact"))
				((InteractBehavior)this.getRemoteEntity().getMind().getBehaviour("Interact")).onInteract((Player)entity.getBukkitEntity());
		}
		
		return super.a(entity);
	}
	
	@Override
	public void j_()
	{
		super.j_();
		super.g();

		if(this.noDamageTicks > 0)
			this.noDamageTicks--;
		
		//Taken from Citizens2#EntityHumanNPC.java#129 - #138
        if(Math.abs(motX) < 0.001F && Math.abs(motY) < 0.001F && Math.abs(motZ) < 0.001F)
            motX = motY = motZ = 0;

		Navigation navigation = getNavigation();
        if(!navigation.f())
        {
            navigation.e();
            this.applyMovement();
        }
        //End Citizens
        
        if(this.getRemoteEntity() != null)
        	this.getRemoteEntity().getMind().tick();
	}

	public void applyMovement()
	{
		if(this.m_remoteEntity.isStationary())
			return;

		//Taken from Citizens2#NMS.java#259 - #262
		getControllerMove().c();
		getControllerLook().a();
		getControllerJump().b();
		e(this.getRemoteEntity().getSpeed());
		//End Citizens
		
		if (bF)
		{
            boolean inLiquid = H() || J();
            if (inLiquid)
            {
                motY += 0.04;
            }
            else if (onGround && bC == 0)
            {
                motY = 0.6;
                bD = 10;
            }
        }
		else
		{
            bD = 0;
        }
        bC *= 0.98F;
        bD *= 0.98F;
        bE *= 0.9F;

        float prev = aN;
        aN *= bB() * this.getRemoteEntity().getSpeed();
        e(bC, bD); 
        aN = prev;
        az = yaw;
	}
	
	@Override
	public void g(double x, double y, double z)
	{		
		if(this.m_remoteEntity != null && this.m_remoteEntity.isPushable() && !this.m_remoteEntity.isStationary())
			super.g(x, y, z);
	}
	
	@Override
	public void move(double d0, double d1, double d2)
	{
		if(this.m_remoteEntity != null && this.m_remoteEntity.isStationary())
			return;
		
		super.move(d0, d1, d2);
	}

	@Override
	public void setupStandardGoals()
	{
		this.getRemoteEntity().getMind().addMovementDesire(new DesireSwim(this.getRemoteEntity()), 0);
	}
	
	@Override
	public boolean be()
	{
		return true;
	}
	
	@Override
	public void die(DamageSource damagesource)
	{
		if(this.getRemoteEntity() != null && this.getRemoteEntity().getMind() != null)
		{
			this.getRemoteEntity().getMind().clearMovementDesires();
			this.getRemoteEntity().getMind().clearActionDesires();
		}
		super.die(damagesource);
	}
	
	@Override
	public EntityLiving getGoalTarget()
	{
		return this.m_target;
	}
	
	@Override
	public void setGoalTarget(EntityLiving inEntity)
	{
		this.m_target = inEntity;
	}
	
	// usage?
	public int c2(Entity entity) {
	        return 2;
	    }
	
	public boolean m(Entity entity) {
	        // for enchant? or use method ab() or aW() from EntityHuman?
	        int i = this.c2(entity);

	        if (this.hasEffect(MobEffectList.INCREASE_DAMAGE)) {
	            i += 3 << this.getEffect(MobEffectList.INCREASE_DAMAGE).getAmplifier();
	        }

	        if (this.hasEffect(MobEffectList.WEAKNESS)) {
	            i -= 2 << this.getEffect(MobEffectList.WEAKNESS).getAmplifier();
	        }

	        int j = 0;

	        if (entity instanceof EntityLiving) {
	            i += EnchantmentManager.a((EntityLiving) this, (EntityLiving) entity);
	            j += EnchantmentManager.getKnockbackEnchantmentLevel(this, (EntityLiving) entity);
	        }

	        boolean flag = entity.damageEntity(DamageSource.mobAttack(this), i);

	        if (flag) {
	            if (j > 0) {
	                entity.g((double) (-MathHelper.sin(this.yaw * 3.1415927F / 180.0F) * (float) j * 0.5F), 0.1D, (double) (MathHelper.cos(this.yaw * 3.1415927F / 180.0F) * (float) j * 0.5F));
	                this.motX *= 0.6D;
	                this.motZ *= 0.6D;
	            }

	            int k = EnchantmentManager.getFireAspectEnchantmentLevel(this);

	            if (k > 0) {
	                entity.setOnFire(k * 4);
	            }

	            if (entity instanceof EntityLiving) {
	                EnchantmentThorns.a(this, (EntityLiving) entity, this.random);
	            }
	        }

	        return flag;
	    }
}
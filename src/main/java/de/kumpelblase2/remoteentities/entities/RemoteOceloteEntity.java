package de.kumpelblase2.remoteentities.entities;

import java.lang.reflect.Field;
import net.minecraft.server.v1_6_R2.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.RemoteEntityHandle;
import de.kumpelblase2.remoteentities.api.features.InventoryFeature;
import de.kumpelblase2.remoteentities.api.thinking.DesireItem;
import de.kumpelblase2.remoteentities.api.thinking.Mind;
import de.kumpelblase2.remoteentities.api.thinking.goals.*;
import de.kumpelblase2.remoteentities.nms.PathfinderGoalSelectorHelper;

public class RemoteOceloteEntity extends EntityOcelot implements RemoteEntityHandle
{
	private final RemoteEntity m_remoteEntity;
	protected int m_lastBouncedId;
	protected long m_lastBouncedTime;

	public RemoteOceloteEntity(World world)
	{
		this(world, null);
	}

	public RemoteOceloteEntity(World world, RemoteEntity inRemoteEntity)
	{
		super(world);
		this.m_remoteEntity = inRemoteEntity;
		new PathfinderGoalSelectorHelper(this.goalSelector).clearGoals();
		new PathfinderGoalSelectorHelper(this.targetSelector).clearGoals();
		this.bp = new DesireSitTemp(this.getRemoteEntity());
		try
		{
			Field temptField = EntityOcelot.class.getDeclaredField("bq");
			temptField.setAccessible(true);
			temptField.set(this, new DesireTemptTemp(this.getRemoteEntity()));
		}
		catch(Exception e)
		{
		}
	}

	@Override
	public Inventory getInventory()
	{
		if(!this.m_remoteEntity.getFeatures().hasFeature(InventoryFeature.class))
			return null;

		return this.m_remoteEntity.getFeatures().getFeature(InventoryFeature.class).getInventory();
	}

	@Override
	public RemoteEntity getRemoteEntity()
	{
		return this.m_remoteEntity;
	}

	@Override
	public void setupStandardGoals()
	{
		Mind mind = this.getRemoteEntity().getMind();
		mind.addMovementDesires(getDefaultMovementDesires());
		mind.addTargetingDesires(getDefaultTargetingDesires());
	}

	@Override
	public void l_()
	{
		super.l_();
		if(this.getRemoteEntity() != null)
			this.getRemoteEntity().getMind().tick();
	}

	@Override
	public void g(double x, double y, double z)
	{
		if(this.m_remoteEntity == null)
		{
			super.g(x, y, z);
			return;
		}

		Vector vector = ((RemoteBaseEntity)this.m_remoteEntity).onPush(x, y, z);
		if(vector != null)
			super.g(vector.getX(), vector.getY(), vector.getZ());
	}

	@Override
	public void move(double d0, double d1, double d2)
	{
		if(this.m_remoteEntity != null && this.m_remoteEntity.isStationary())
			return;

		super.move(d0, d1, d2);
	}

	@Override
	public void collide(Entity inEntity)
	{
		if(this.getRemoteEntity() == null)
		{
			super.collide(inEntity);
			return;
		}

		if(((RemoteBaseEntity)this.m_remoteEntity).onCollide(inEntity.getBukkitEntity()))
			super.collide(inEntity);
	}

	@Override
	public boolean a(EntityHuman entity)
	{
		if(this.getRemoteEntity() == null)
			return super.a(entity);

		if(!(entity.getBukkitEntity() instanceof Player))
			return super.a(entity);

		return ((RemoteBaseEntity)this.m_remoteEntity).onInteract((Player)entity.getBukkitEntity()) && super.a(entity);
	}

	@Override
	public void die(DamageSource damagesource)
	{
		((RemoteBaseEntity)this.m_remoteEntity).onDeath();
		super.die(damagesource);
	}

	public static DesireItem[] getDefaultMovementDesires()
	{
		try
		{
			return new DesireItem[] {
					new DesireItem(new DesireSwim(), 1),
					new DesireItem(new DesireSit(), 2),
					new DesireItem(new DesireTempt(Item.RAW_FISH.id, true, 0.6D), 3),
					new DesireItem(new DesireAvoidSpecific(16F, 1.33D, 0.8D, EntityHuman.class), 4),
					new DesireItem(new DesireFollowTamer(5, 10), 5),
					new DesireItem(new DesireSitOnBlock(), 6),
					new DesireItem(new DesireLeapAtTarget(0.3F), 7),
					new DesireItem(new DesireOcelotAttack(), 8),
					new DesireItem(new DesireBreed(), 9),
					new DesireItem(new DesireWanderAround(), 10),
					new DesireItem(new DesireLookAtNearest(EntityHuman.class, 10F), 11)
			};
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new DesireItem[0];
		}
	}

	public static DesireItem[] getDefaultTargetingDesires()
	{
		try
		{
			return new DesireItem[] {
					new DesireItem(new DesireNonTamedFindNearest(EntityChicken.class, 14, false, true, 750), 1)
			};
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new DesireItem[0];
		}
	}
}
package de.kumpelblase2.remoteentities.api.thinking.goals;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import sun.security.action.GetLongAction;
import net.minecraft.server.v1_4_R1.EntityLiving;
import net.minecraft.server.v1_4_R1.Vec3D;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;
import de.kumpelblase2.remoteentities.nms.RandomPositionGenerator;

public class DesireMoveToTarget extends DesireBase
{
	protected float m_minDistance;
	protected float m_minDistanceSquared;
	protected EntityLiving m_target;
	protected double m_x;
	protected double m_y;
	protected double m_z;
	private long lastUpdate;
	
	public DesireMoveToTarget(RemoteEntity inEntity, float inMinDistance)
	{
		super(inEntity);
		this.m_minDistance = inMinDistance;
		this.m_minDistanceSquared = this.m_minDistance * this.m_minDistance;
		this.m_type = 1;
	}

	@Override
	public boolean shouldExecute()
	{
	        if(true) {
	            return true;
	        }
		if(this.getEntityHandle() == null)
			return false;
		
		this.m_target = this.getEntityHandle().getGoalTarget();
		if(this.m_target == null)
			return false;
		else if(this.m_target.e(this.getEntityHandle()) > this.m_minDistanceSquared)
			return false;
		else
		{
			Vec3D vec = RandomPositionGenerator.a(this.getEntityHandle(), 16, 7, this.getEntityHandle().world.getVec3DPool().create(this.m_target.locX, this.m_target.locY, this.m_target.locZ));
			
			if(vec == null)
				return false;
			else
			{
				this.m_x = vec.c;
				this.m_y = vec.d;
				this.m_z = vec.e;
				return true;
			}
		}
	}
	
	@Override
	public boolean canContinue()
	{
	        return true;
//		return !this.getEntityHandle().getNavigation().f() && this.m_target.isAlive() && this.m_target.e(this.getEntityHandle()) < this.m_minDistanceSquared;
	}
	
	@Override
	public void stopExecuting()
	{
		this.m_target = null;
	}
	
	@Override
	public void startExecuting()
	{
	        System.out.println("move");
	        lastUpdate = System.currentTimeMillis();
//		this.getRemoteEntity().move(Bukkit.getOnlinePlayers()[0].getLocation());
	        this.getRemoteEntity().move(new Location(this.getRemoteEntity().getBukkitEntity().getWorld(), this.m_x, this.m_y, this.m_z));
	}
	
	public boolean update() {
	    if(System.currentTimeMillis() - lastUpdate > 30000) {
	        startExecuting();
	    }
	    return true;
	}
}

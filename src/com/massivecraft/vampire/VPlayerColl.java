package com.massivecraft.vampire;

import java.io.File;
import java.util.Collection;

import com.massivecraft.mcore4.MCore;
import com.massivecraft.mcore4.Predictate;
import com.massivecraft.mcore4.store.DbGson;
import com.massivecraft.mcore4.store.MStore;
import com.massivecraft.mcore4.store.PlayerColl;

public class VPlayerColl extends PlayerColl<VPlayer>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public VPlayerColl(String name)
	{
		super(MStore.getDb(ConfServer.dburi), P.p, name, VPlayer.class);
	}
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
	@Override
	public boolean isDefault(VPlayer entity)
	{
		if (entity.isVampire()) return false;
		if (entity.isInfected()) return false;
		return true;
	}
	
	
	public Collection<VPlayer> getAllOnlineInfected()
	{
		return this.getAll(new Predictate<VPlayer>()
		{
			public boolean apply(VPlayer entity)
			{
				return entity.isOnline() && entity.isInfected();
			}
		});
	}
	
	public Collection<VPlayer> getAllOnlineVampires()
	{
		return this.getAll(new Predictate<VPlayer>()
		{
			public boolean apply(VPlayer entity)
			{
				return entity.isOnline() && entity.isVampire();
			}
		});
	}
	
	@Override
	public void init()
	{
		this.migrateFromOldFormat();
		super.init();
	}
	
	protected void migrateFromOldFormat()
	{
		File oldPlayerCollDir = new File(P.p.getDataFolder(), "player");
		if (!oldPlayerCollDir.isDirectory()) return;
		
		if (MCore.getDb().getDriver().getName() != "gson") return;
		DbGson dbGson = (DbGson) MCore.getDb();
		File newPlayerCollDir = new File(dbGson.dir, this.getName());
		if (newPlayerCollDir.isDirectory()) return;
		
		dbGson.dir.mkdirs();
		oldPlayerCollDir.renameTo(newPlayerCollDir);
	}
}

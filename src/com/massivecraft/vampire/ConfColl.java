package com.massivecraft.vampire;

import com.massivecraft.mcore4.store.Coll;
import com.massivecraft.mcore4.store.MStore;

public class ConfColl extends Coll<Conf, String>
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public ConfColl(String name)
	{
		super(MStore.getDb(ConfServer.dburi), P.p, "ai", name, Conf.class, String.class, true);
	}
	
	// -------------------------------------------- //
	// EXTRAS
	// -------------------------------------------- //
	
}

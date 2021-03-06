package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.mcore4.cmd.arg.ARBoolean;
import com.massivecraft.vampire.*;

public class CmdSetVampire extends CmdSetAbstract<Boolean>
{
	public CmdSetVampire()
	{
		targetMustBeOnline = false;
		argReader = ARBoolean.get();
		this.addAliases("v");
		this.setDesc("set vampire (yes or no)");
	}

	@Override
	public Boolean set(VPlayer vplayer, Player player, Boolean val)
	{
		Permission perm = val ? Permission.SET_VAMPIRE_TRUE : Permission.SET_VAMPIRE_FALSE;
		
		if ( ! perm.has(sender, true)) return null;
		
		if (vplayer.isVampire() == val) return val;
		
		vplayer.setReason(InfectionReason.OPERATOR);
		vplayer.setMaker(null);
		vplayer.setVampire(val);
		
		return val;
	}
}

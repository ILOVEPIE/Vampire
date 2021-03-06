package com.massivecraft.vampire.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.mcore4.MCore;
import com.massivecraft.mcore4.cmd.arg.ArgReader;
import com.massivecraft.mcore4.usys.Multiverse;
import com.massivecraft.mcore4.util.Txt;
import com.massivecraft.vampire.Conf;
import com.massivecraft.vampire.Lang;
import com.massivecraft.vampire.Permission;
import com.massivecraft.vampire.VPlayer;
import com.massivecraft.vampire.VPlayerColl;
import com.massivecraft.vampire.VPlayerColls;
import com.massivecraft.vampire.util.SunUtil;

public class CmdShow extends VCommand
{
	public CmdShow()
	{
		this.addAliases("s", "show");
		this.addOptionalArg("player", "you");
		this.addOptionalArg("univ", "you");
		this.setDesc("Show player info");
	}
	
	@Override
	public void perform()
	{
		if ( vme == null && ! this.argIsSet(0))
		{
			msg(Lang.consolePlayerArgRequired);
			return;
		}
		
		Multiverse mv = p.playerAspect.multiverse();
		String universe = this.arg(1, mv.argReaderUniverse(), senderIsConsole ? MCore.DEFAULT : mv.getUniverse(me));
		if (universe == null) return;
		
		VPlayerColl playerColl = VPlayerColls.i.getForUniverse(universe);
		ArgReader<VPlayer> playerReader = playerColl.argReaderPlayerStart();
		VPlayer vplayer = this.arg(0, playerReader, vme);
		if (vplayer == null) return;
		
		Player player = vplayer.getPlayer();
		Conf conf = Conf.get(player);
		
		boolean self = vplayer == vme;
		
		// Test permissions
		if (self)
		{
			if ( ! Permission.SHOW_SELF.has(sender, true)) return;
		}
		else
		{
			if ( ! Permission.SHOW_OTHER.has(sender, true)) return;
		}
		
		String You = "You";
		//String you = "you";
		String are = "are";
		if ( ! self)
		{
			You = vplayer.getId();
			//you = vplayer.getId();
			are = "is";
		}
		
		msg(Txt.titleize(Txt.upperCaseFirst(universe)+" Vampire "+vplayer.getId()));
		if (vplayer.isVampire())
		{
			msg("<i>"+You+" "+are+" a vampire.");
			msg(vplayer.getReasonDesc(self));
		}
		else if (vplayer.isInfected())
		{
			msg("<i>"+You+" "+are+" infected at <h>%d%%<i>.", percent(vplayer.getInfection()));
			msg(vplayer.getReasonDesc(self));
			return;
		}
		else
		{
			msg("<i>"+You+" "+are+" a normal human.");
			return;
		}
		
		//msg(vplayer.bloodlust() ? Lang.xIsOn : Lang.xIsOff, "Bloodlust");
		//msg(vplayer.intend() ? Lang.xIsOn : Lang.xIsOff, "Infect intent");
		
		this.msg(vplayer.bloodlustMsg());
		this.msg(vplayer.intendMsg());
		
		msg("<k>Temperature <v>%d%%", (int)Math.round(vplayer.getTemp()*100));
		if (player == null)
		{
			msg("<k>Irradiation <v>%d%%", percent(vplayer.getRad()));
		}
		else
		{
			int rad = percent(vplayer.getRad());
			int sun = percent(SunUtil.calcSolarRad(player.getWorld()));
			double terrain = 1d-SunUtil.calcTerrainOpacity(player.getLocation().getBlock());
			double armor = 1d-SunUtil.calcArmorOpacity(player);
			int base = percent(conf.baseRad);
			msg("<k>Irradiation <v>X% <k>= <yellow>sun <lime>*terrain <blue>*armor <silver>-base");
			msg("<k>Irradiation <v>%+d%% <k>= <yellow>%d <lime>*%.2f <blue>*%.2f <silver>%+d", rad, sun, terrain, armor, base);
			
		}
		
	}
	
	public static int percent(double quota)
	{
		return (int)Math.round(quota*100);
	}
}

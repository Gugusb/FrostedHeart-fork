package com.teammoeg.frostedheart.mixin.ftb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.teammoeg.frostedheart.research.data.FHResearchDataManager;
import com.teammoeg.frostedheart.util.FHUtils;
import com.teammoeg.frostedheart.util.FTBFixUtils;
import com.teammoeg.frostedheart.util.IFTBSecondWritable;

import dev.ftb.mods.ftbteams.data.ClientTeam;
import dev.ftb.mods.ftbteams.data.ClientTeamManager;
import dev.ftb.mods.ftbteams.data.KnownClientPlayer;
import dev.ftb.mods.ftbteams.data.TeamRank;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;

@Mixin(ClientTeamManager.class)
public abstract class ClientTeamManagerMixin {
	@Shadow(remap=false)
	public Map<UUID, ClientTeam> teamMap;
	@Shadow(remap=false)
	public Map<UUID, KnownClientPlayer> knownPlayers;
	@Shadow(remap=false)
	public abstract UUID getId();
	/**
	 * @reason Fix ftb teams packet too large
	 * @author khjxiaogu
	 * */
	@Overwrite(remap=false)
	public void write(PacketBuffer buffer, long now) {
		buffer.writeUniqueId(getId());
		UUID puuid=FTBFixUtils.networkPlayer.getUniqueID();
		Set<ClientTeam> tosendteam=new HashSet<>();
		Set<KnownClientPlayer> tosendplayer=new HashSet<>();
		for(ClientTeam i:teamMap.values()) {
			if(i.getHighestRank(puuid).getPower()>75) {
				tosendteam.add(i);
			}
		}
		for(KnownClientPlayer kcp:knownPlayers.values()) {
			for(ClientTeam i:tosendteam) {
				if(i.getHighestRank(kcp.uuid).getPower()>0) {
					tosendplayer.add(kcp);
				}
			}
		}
		for(ServerPlayerEntity p:FHResearchDataManager.server.getPlayerList().getPlayers()) {
			KnownClientPlayer kcp=knownPlayers.get(p.getUniqueID());
			if(kcp!=null)
				tosendplayer.add(kcp);
		}
		System.out.println("sending "+tosendteam.size()+" essential teams and "+tosendplayer.size()+" essential players.");
		buffer.writeVarInt(teamMap.size());
		
		for (ClientTeam t : teamMap.values()) {
			if(tosendteam.contains(t)) {
				t.write(buffer, now);
				//((IFTBSecondWritable)t).write2(buffer, now);
			}else {
				buffer.writeUniqueId(t.getId());
				buffer.writeByte(t.getType().ordinal());
				t.properties.write(buffer);
				buffer.writeVarInt(0);
				buffer.writeCompoundTag(t.getExtraData());
			}
		}

		buffer.writeVarInt(tosendplayer.size());

		for (KnownClientPlayer knownClientPlayer : tosendplayer) {
			knownClientPlayer.write(buffer);
		}
	}
}
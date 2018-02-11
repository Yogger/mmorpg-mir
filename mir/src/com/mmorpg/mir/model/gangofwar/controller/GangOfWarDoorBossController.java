package com.mmorpg.mir.model.gangofwar.controller;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.controllers.BossController;
import com.mmorpg.mir.model.gameobjects.Boss;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.StatusNpc;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gangofwar.config.GangOfWarConfig;
import com.mmorpg.mir.model.gangofwar.manager.GangOfWarManager;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.spawn.SpawnManager;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.WorldMapInstance;

public class GangOfWarDoorBossController extends BossController implements GangOfWarCamp {
	private Camps camps = Camps.DEFEND;

	/** 门 */
	private Boss nextGuardBoss;
	private WorldMapInstance worldMapInstance;
	private StatusNpc reliveStatusNpc;

	public GangOfWarDoorBossController(Boss nextGuardBoss, WorldMapInstance worldMapInstance) {
		this.setNextGuardBoss(nextGuardBoss);
		this.worldMapInstance = worldMapInstance;
	}

	@Override
	public void onDie(Creature lastAttacker, int skillId) {
		super.onDie(lastAttacker, skillId);

		Player player = null;
		if (lastAttacker instanceof Player) {
			player = (Player) lastAttacker;
		} else if (lastAttacker instanceof Summon) {
			player = ((Summon) lastAttacker).getMaster();
		}

		getNextGuardBoss().getEffectController().unsetAbnormal(EffectId.GOD.getEffectId(), true);
		if (!ArrayUtils.isEmpty(getOwner().getSpawn().getBlocks())) {
			worldMapInstance.clearBlocks(Arrays.asList(getOwner().getSpawn().getBlocks()));
		}
		if (GangOfWarManager.getInstance().getGangOfwars().get(player.getCountryId()).getPhase() == Phase.DEFEND) {
			reliveStatusNpc.setStatus(1);
			SpawnManager.getInstance().bringIntoWorld(reliveStatusNpc, player.getCountryValue());
		}

		// 通报
		I18nUtils i18nUtils = I18nUtils.valueOf("10204");
		int doorSeq = 0;
		if (getOwner().getSpawnKey().equals(GangOfWarConfig.getInstance().FIRST_DOOR_SPAW.getValue())) {
			doorSeq = 1;
		} else if (getOwner().getSpawnKey().equals(GangOfWarConfig.getInstance().SECOND_DOOR_SPAW.getValue())) {
			doorSeq = 2;
		} else {
			doorSeq = 3;
		}
		i18nUtils.addParm("x", I18nPack.valueOf(doorSeq + ""));
		ChatManager.getInstance().sendSystem(11008, i18nUtils, null,
				Integer.valueOf(GangOfWarConfig.getInstance().MAPID.getValue()),
				getOwner().getPosition().getInstanceId());
		// 通报另外一个
		if (GangOfWarManager.getInstance().getGangOfwars().get(player.getCountryId()).getPhase() == Phase.DEFEND) {
			I18nUtils i18nUtils1 = I18nUtils.valueOf("201001");
			ChatManager.getInstance().sendSystem(41008, i18nUtils1, null,
					Integer.valueOf(GangOfWarConfig.getInstance().MAPID.getValue()),
					getOwner().getPosition().getInstanceId());
		}
		GangOfWarManager.getInstance().getGangOfwars().get(player.getCountryId()).rank();
	}

	@Override
	protected void broadDamage() {
	}

	@Override
	public void onDespawn() {
		if (!ArrayUtils.isEmpty(getOwner().getSpawn().getBlocks())) {
			worldMapInstance.clearBlocks(Arrays.asList(getOwner().getSpawn().getBlocks()));
		}
		super.onDespawn();
	}

	@Override
	public void onRelive() {
		super.onRelive();
		// 设置障碍点
		if (!ArrayUtils.isEmpty(getOwner().getSpawn().getBlocks())) {
			worldMapInstance.addBlocks(Arrays.asList(getOwner().getSpawn().getBlocks()));
		}
		World.getInstance().despawn(reliveStatusNpc);
	}

	@Override
	public boolean inActivity() {
		return true;
	}

	@Override
	public boolean isActivityEnemy(Creature other) {
		if (other instanceof Summon) {
			other = ((Summon) other).getMaster();
		}
		if (other.getController() instanceof GangOfWarCamp) {
			GangOfWarCamp gop = (GangOfWarCamp) other.getController();
			return getCamps() != gop.getCamps() ? true : false;
		}
		return false;
	}

	@Override
	public void onSpawn(int mapId, int instanceId) {
		super.onSpawn(mapId, instanceId);
		if (!ArrayUtils.isEmpty(getOwner().getSpawn().getBlocks())) {
			worldMapInstance.addBlocks(Arrays.asList(getOwner().getSpawn().getBlocks()));
		}
	}

	@Override
	public Camps getCamps() {
		return camps;
	}

	public void setCamps(Camps camps) {
		this.camps = camps;
	}

	public Boss getNextGuardBoss() {
		return nextGuardBoss;
	}

	public void setNextGuardBoss(Boss nextGuardBoss) {
		this.nextGuardBoss = nextGuardBoss;
	}

	public WorldMapInstance getWorldMapInstance() {
		return worldMapInstance;
	}

	public void setWorldMapInstance(WorldMapInstance worldMapInstance) {
		this.worldMapInstance = worldMapInstance;
	}

	public StatusNpc getReliveStatusNpc() {
		return reliveStatusNpc;
	}

	public void setReliveStatusNpc(StatusNpc reliveStatusNpc) {
		this.reliveStatusNpc = reliveStatusNpc;
	}

}

package com.mmorpg.mir.model.nickname.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.nickname.manager.NicknameManager;
import com.mmorpg.mir.model.nickname.model.vo.NickNamePoolVO;
import com.mmorpg.mir.model.nickname.packet.SM_Nickname_Change;
import com.mmorpg.mir.model.nickname.packet.SM_Nickname_Remove;
import com.mmorpg.mir.model.nickname.packet.SM_Nickname_Waited_Active;
import com.mmorpg.mir.model.nickname.resource.NicknameResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.resource.Storage;

public class NicknamePool {
	public static final StatEffectId GAME_STATE_ID = StatEffectId.valueOf("nickname", StatEffectType.NICKNAME);
	private Player owner;
	/** 当前佩戴的称号 */
	private ArrayList<Integer> equipIds = New.arrayList();
	/** 已经激活的称号 */
	private NonBlockingHashMap<Integer, Nickname> activeds = new NonBlockingHashMap<Integer, Nickname>();

	public static NicknamePool valueOf() {
		NicknamePool pool = new NicknamePool();
		return pool;
	}

	@JsonIgnore
	public NickNamePoolVO createVO() {
		NickNamePoolVO vo = new NickNamePoolVO();
		vo.setEquips(new ArrayList<Integer>(equipIds));
		ArrayList<Nickname> nickNames = new ArrayList<Nickname>();
		for (Nickname nn : activeds.values()) {
			nickNames.add(nn);
		}
		vo.setActives(nickNames);

		return vo;
	}

	@JsonIgnore
	synchronized public void addNickname(Nickname nickname) {
		// System.out.println(nickname.getId() + "   " + new
		// Date(nickname.getDeprecatedTime()));
		if (activeds.containsKey(nickname.getId())) {
			Nickname original = activeds.get(nickname.getId());
			if (original.getDeprecatedTime() != 0L && nickname.getDeprecatedTime() == 0L) {
				activeds.get(nickname.getId()).setDeprecatedTime(0L);
				PacketSendUtility.sendPacket(owner, activeds.get(nickname.getId()));
			} else if (original.getDeprecatedTime() != 0L && nickname.getDeprecatedTime() != 0L){
				long time = nickname.getDeprecatedTime() - System.currentTimeMillis();
				if (time > 0) {
					activeds.get(nickname.getId()).addDeprecatedTime(time);
					PacketSendUtility.sendPacket(owner, activeds.get(nickname.getId()));
				}
			}
		} else {
			active(nickname);
		}
	}

	public ArrayList<Integer> getEquipIds() {
		return equipIds;
	}

	public void setEquipIds(ArrayList<Integer> equipIds) {
		this.equipIds = equipIds;
	}

	public NonBlockingHashMap<Integer, Nickname> getActiveds() {
		return activeds;
	}

	public void setActiveds(NonBlockingHashMap<Integer, Nickname> activeds) {
		this.activeds = activeds;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	@JsonIgnore
	synchronized public void checkAndRemove(Integer id) {
		if (!activeds.containsKey(id)) {
			return;
		}
		NicknameResource resource = activeds.get(id).getResource();
		if (!resource.isActiveRemove()) {
			return;
		}
		if (!resource.getActiveConditions().verify(owner, false)) {
			remove(id);
			if (equipIds.contains(id)) {
				unEquip(id, true);
			}
			refreshModifiers(true);
		}
	}

	@JsonIgnore
	synchronized public void unEquip(Integer unEquipId, boolean send) {
		equipIds.remove(unEquipId);
		if (send) {
			refreshModifiers(true);
			PacketSendUtility.broadcastPacketAndReceiver(owner, SM_Nickname_Change.valueOf(owner));
		}
	}

	@JsonIgnore
	synchronized public void equip(Integer id) {
		if (!activeds.containsKey(id)) {
			throw new ManagedException(ManagedErrorCode.NOT_FOUND_NICKNAME);
		}
		Nickname nickName = activeds.get(id);
		if (!nickName.isActived()) {
			throw new ManagedException(ManagedErrorCode.NICKNAME_NOT_ACTIVE);
		}
		if (nickName.isDeprecat()) {
			owner.getNicknamePool().checkAndRemove(nickName.getId());
			throw new ManagedException(ManagedErrorCode.NICKNAME_INVALID);
		}
		NicknameResource resource = nickName.getResource();
		if (!resource.getEquipConditions().verify(owner, true)) {
			return;
		}
		if (equipIds != null && !equipIds.contains(id)) {
			if (owner.getNicknamePool().getEquipIds().size() >= owner.getVip().getResource().getEquipNicknameNums()
					&& !equipIds.isEmpty()) {
				equipIds.remove(0);
			}
			equipIds.add(id);
			refreshModifiers(true);
			PacketSendUtility.broadcastPacketAndReceiver(owner, SM_Nickname_Change.valueOf(owner));
		} else {
			throw new ManagedException(ManagedErrorCode.NICKNAME_ALREADY_EQUIP);
		}
	}

	@JsonIgnore
	synchronized public void checkAndUnEquip(Integer id) {
		if (!activeds.containsKey(id)) {
			return;
		}
		NicknameResource resource = activeds.get(id).getResource();
		if (!resource.isEquipRemove()) {
			return;
		}
		if (!resource.getEquipConditions().verify(owner, false)) {
			unEquip(id, true);
		}
	}

	@JsonIgnore
	public void refreshModifiers(boolean send) {
		owner.getGameStats().replaceModifiers(GAME_STATE_ID, getStats(), send);
	}

	@JsonIgnore
	synchronized private List<Stat> getStats() {
		List<Stat> stats = New.arrayList();
		if (equipIds != null) {
			for (Integer equipId : equipIds) {
				stats.addAll(Arrays.asList(activeds.get(equipId).getResource().getEquipStats()));
			}
		}
		for (Nickname nickname : activeds.values()) {
			if (nickname.isActived())
				stats.addAll(Arrays.asList(nickname.getResource().getActiveStats()));
		}
		return stats;
	}

	@JsonIgnore
	synchronized public void tryActive(List<Integer> activeIds, Storage<Integer, NicknameResource> nicknameResources) {
		if (activeIds == null || activeIds.isEmpty()) {
			return;
		}
		for (Integer activeId : activeIds) {
			if (activeds.containsKey(activeId)) {
				continue;
			}
			NicknameResource resource = nicknameResources.get(activeId, true);
			if (resource.getActiveConditions().verify(owner, false)) {
				Nickname nickname = resource.creatNickname();
				active(nickname);
			}
		}
	}

	private void remove(Integer id) {
		activeds.remove(id);
		PacketSendUtility.sendPacket(owner, SM_Nickname_Remove.valueOf(id));
	}

	private void active(Nickname nickname) {
		activeds.put(nickname.getId(), nickname);
		if (nickname.getResource().isAutoEquip()) {
			NicknameManager.getInstance().activeNickName(owner, nickname.getId());
			equip(nickname.getId());
		} else {
			PacketSendUtility.sendPacket(owner, SM_Nickname_Waited_Active.valueOf(nickname));
		}
	}

	@JsonIgnore
	synchronized public void checkDeprecat(boolean send) {
		for (Nickname nickname : activeds.values()) {
			if (nickname.isDeprecat()) {
				remove(nickname.getId());
				if (equipIds.contains(nickname.getId())) {
					unEquip(nickname.getId(), send);
				}
			}
		}
	}

	public void removeSpecificNamePool(Integer nameId) {
		Nickname nickname = activeds.get(nameId);
		if (nickname != null) {
			remove(nickname.getId());
			if (equipIds.contains(nickname.getId())) {
				unEquip(nickname.getId(), true);
			}
		}
	}

}

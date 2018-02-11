package com.mmorpg.mir.model.fashion.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.fashion.FashionConfig;
import com.mmorpg.mir.model.fashion.packet.SM_FashionBroadcast;
import com.mmorpg.mir.model.fashion.packet.SM_Fashion_Exp_Change;
import com.mmorpg.mir.model.fashion.packet.SM_Fashion_Operate_Hide;
import com.mmorpg.mir.model.fashion.packet.SM_Fashion_OwnFashion_Change;
import com.mmorpg.mir.model.fashion.packet.SM_Fashion_UnWear;
import com.mmorpg.mir.model.fashion.packet.SM_Fashion_Upgrade;
import com.mmorpg.mir.model.fashion.packet.SM_Fashion_Wear;
import com.mmorpg.mir.model.fashion.resource.FashionLevelResource;
import com.mmorpg.mir.model.fashion.resource.FashionResource;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.utils.PacketSendUtility;

public class FashionPool {
	public static final StatEffectId FASHION_LEVEL_ID = StatEffectId
			.valueOf("fashion_level_id", StatEffectType.FASHION);

	@Transient
	private transient Player owner;

	/** 等级 */
	private int level;
	/** 经验 */
	private int exp;
	/** 是否隐藏模型 */
	private boolean hided;
	/** 拥有的时装 */
	private Set<Integer> ownFashions;
	/** 当前正在穿 */
	private int currentFashionId;

	public static FashionPool valueOf() {
		FashionPool result = new FashionPool();
		result.level = 0;
		result.exp = 0;
		result.currentFashionId = -1;
		result.ownFashions = new HashSet<Integer>();
		return result;
	}

	@JsonIgnore
	public boolean containFashion(int fashionId) {
		return ownFashions.contains(fashionId);
	}

	@JsonIgnore
	public Stat[] getLevelStats() {
		FashionLevelResource resource = FashionConfig.getInstance().fashionLevelStorage.get(this.level, true);
		return resource.getStats();
	}

	@JsonIgnore
	public void addExp(int value) {
		this.exp += value;
		PacketSendUtility.sendPacket(owner, SM_Fashion_Exp_Change.valueOf(exp));
	}

	@JsonIgnore
	public void gainFashion(int fashionId) {
		if (!ownFashions.contains(fashionId)) {
			FashionResource resource = FashionConfig.getInstance().fashionStorage.get(fashionId, true);
			this.exp += resource.getFirstExp();
			this.ownFashions.add(fashionId);
			PacketSendUtility.sendPacket(owner, SM_Fashion_OwnFashion_Change.valueOf(this));
		}
	}

	@JsonIgnore
	public void wear(int fashionId) {
		if (!ownFashions.contains(fashionId)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		currentFashionId = fashionId;
		PacketSendUtility.sendPacket(owner, SM_Fashion_Wear.valueOf(fashionId));
		broadCast();
	}

	@JsonIgnore
	public void upgrade() {
		FashionLevelResource resource = FashionConfig.getInstance().fashionLevelStorage.get(this.level, true);
		if (resource.getNeedExp() <= this.exp) {
			this.level++;
			this.exp -= resource.getNeedExp();
			owner.getGameStats().replaceModifiers(FASHION_LEVEL_ID, getLevelStats(), true);
			PacketSendUtility.sendPacket(owner, SM_Fashion_Upgrade.valueOf(this));
		}
	}

	@JsonIgnore
	public void operateHide() {
		this.hided = !isHided();
		PacketSendUtility.sendPacket(owner, SM_Fashion_Operate_Hide.valueOf(isHided()));
		broadCast();
	}

	@JsonIgnore
	public boolean isMaxLevel() {
		return this.level >= FashionConfig.getInstance().FASHION_MAX_LEVEL.getValue();
	}

	@JsonIgnore
	public boolean isWearFashion() {
		return this.currentFashionId != -1;
	}

	@JsonIgnore
	public void unWear() {
		this.currentFashionId = -1;
		if (owner != null) {
			PacketSendUtility.sendPacket(owner, new SM_Fashion_UnWear());
			broadCast();
		}
	}

	@JsonIgnore
	public void removeFashion(int fashionId) {
		removeFashion(fashionId, true);
	}

	@JsonIgnore
	public void removeFashion(int fashionId, boolean send) {
		if (this.currentFashionId == fashionId) {
			unWear();
		}
		ownFashions.remove(fashionId);
		if (send) {
			PacketSendUtility.sendPacket(owner, SM_Fashion_OwnFashion_Change.valueOf(this));
		}
	}

	@JsonIgnore
	private void broadCast() {
		PacketSendUtility.broadcastPacket(owner, SM_FashionBroadcast.valueOf(owner), true);
	}

	// getter-setter
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public Set<Integer> getOwnFashions() {
		return ownFashions;
	}

	public void setOwnFashions(Set<Integer> ownFashions) {
		this.ownFashions = ownFashions;
	}

	public int getCurrentFashionId() {
		return currentFashionId;
	}

	public void setCurrentFashionId(int currentFashionId) {
		this.currentFashionId = currentFashionId;
	}

	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public boolean isHided() {
		return hided;
	}

	public void setHided(boolean hided) {
		this.hided = hided;
	}

}

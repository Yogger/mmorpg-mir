package com.mmorpg.mir.model.suicide.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.ModuleKey;
import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectId;
import com.mmorpg.mir.model.gameobjects.stats.StatEffectType;
import com.mmorpg.mir.model.i18n.manager.I18NparamKey;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.i18n.model.I18nPack;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.moduleopen.manager.ModuleOpenManager;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.player.resource.PlayerLevelResource;
import com.mmorpg.mir.model.suicide.SuicideConfig;
import com.mmorpg.mir.model.suicide.event.SuicideTurnEvent;
import com.mmorpg.mir.model.suicide.packet.SM_Suicide_Common_Charge;
import com.mmorpg.mir.model.suicide.packet.SM_Suicide_Quick_Charge;
import com.mmorpg.mir.model.suicide.packet.SM_Suicide_Turn;
import com.mmorpg.mir.model.suicide.resource.SuicideElementResource;
import com.mmorpg.mir.model.suicide.resource.SuicideStatResource;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.utility.RandomUtils;

public class Suicide {

	public static final StatEffectId SUICIDE_ELEMENT = StatEffectId.valueOf("suicide_element", StatEffectType.SUICIDE);

	public static final StatEffectId SUICIDE_TURN = StatEffectId.valueOf("suicide_turn", StatEffectType.SUICIDE);

	@Transient
	private transient Player owner;
	// 一转还是二转
	private int turn;
	// 元素值
	private HashMap<Integer, Integer> elements;

	public static Suicide valueOf() {
		Suicide result = new Suicide();
		result.elements = new HashMap<Integer, Integer>();
		result.initElements();
		return result;
	}

	@JsonIgnore
	public void refreshStats(boolean recomputed) {
		if (!ModuleOpenManager.getInstance().isOpenByModuleKey(owner, ModuleKey.SUICIDE)) {
			return;
		}

		List<Stat> newStats = new ArrayList<Stat>();

		if (this.turn != 0) {
			Stat[] turnStats = SuicideConfig.getInstance().getTurnStats(this.turn);
			owner.getGameStats().replaceModifiers(SUICIDE_TURN, turnStats, recomputed);

			for (SuicideElementType type : SuicideElementType.values()) {
				SuicideElementResource eleResource = SuicideConfig.getInstance().elementStorage.getUnique(
						SuicideElementResource.TURN_TYPE_INDEX,
						SuicideElementResource.toTurnTypeIndex(this.turn, type.getValue()));
				SuicideStatResource resource = SuicideConfig.getInstance().elementStatStorage
						.get(type.getValue(), true);
				newStats.addAll(resource.multiRate(this.turn * eleResource.getCount()));
			}
		}

		for (Map.Entry<Integer, Integer> entry : elements.entrySet()) {
			SuicideStatResource resource = SuicideConfig.getInstance().elementStatStorage.get(entry.getKey(), true);
			newStats.addAll(resource.multiRate(entry.getValue()));
		}
		owner.getGameStats().replaceModifiers(SUICIDE_ELEMENT, newStats, recomputed);

	}

	@JsonIgnore
	public void turn() {
		this.turn++;
		initElements();
		refreshStats(true);
		EventBusManager.getInstance().syncSubmit(SuicideTurnEvent.valueOf(owner));
		PacketSendUtility.sendPacket(owner, SM_Suicide_Turn.valueOf(this.turn));

		I18nUtils utils = I18nUtils.valueOf("30001");
		utils.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(owner.getName()));
		utils.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(owner.getCountry().getName()));
		ChatManager.getInstance().sendSystem(11001, utils, null);

		I18nUtils utils2 = I18nUtils.valueOf("320015");
		utils2.addParm(I18NparamKey.PLAYERNAME, I18nPack.valueOf(owner.getName()));
		utils2.addParm(I18NparamKey.COUNTRY, I18nPack.valueOf(owner.getCountry().getName()));
		ChatManager.getInstance().sendSystem(0, utils2, null);
	}

	@JsonIgnore
	public boolean isAllElementFull() {
		int fullCount = 0;
		for (Map.Entry<Integer, Integer> entry : elements.entrySet()) {
			SuicideElementResource resource = SuicideConfig.getInstance().elementStorage.getUnique(
					SuicideElementResource.TURN_TYPE_INDEX,
					SuicideElementResource.toTurnTypeIndex(this.turn + 1, entry.getKey()));
			if (resource.getCount() <= entry.getValue()) {
				fullCount++;
			}
		}
		if (fullCount == SuicideElementType.values().length) {
			return true;
		}
		return false;
	}

	@JsonIgnore
	synchronized public void commonCharge() {
		Integer[] notFullType = getNotFullElementType();
		if (notFullType.length == 0) {
			throw new ManagedException(ManagedErrorCode.SUICIDE_ALL_ELEMENT_FULL);
		}
		int index = RandomUtils.nextInt(notFullType.length);
		addElementCount(notFullType[index], 1);
		refreshStats(true);
		PacketSendUtility.sendPacket(owner,
				SM_Suicide_Common_Charge.valueOf(notFullType[index], this.elements.get(notFullType[index])));
	}

	@JsonIgnore
	synchronized public void quickCharge() {
		HashMap<Integer, Integer> newElements = new HashMap<Integer, Integer>(this.elements);
		CoreActions actions = new CoreActions();
		CoreActions tempActions = new CoreActions();
		while (true) {
			Integer[] notFullType = getNotFullElementType(newElements);
			if (notFullType.length == 0) {
				break;
			}

			CoreActions acts = SuicideConfig.getInstance().getCommonChargeActions();
			tempActions.addActions(acts);
			if (!tempActions.verify(owner, false)) {
				break;
			}
			actions.addActions(acts);

			int index = RandomUtils.nextInt(notFullType.length);

			newElements.put(notFullType[index], newElements.get(notFullType[index]) + 1);
		}

		if (actions.isEmpty()) {
			throw new ManagedException(ManagedErrorCode.ITEM_COUNT);
		}

		if (!actions.verify(owner, true)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}
		actions.act(owner, ModuleInfo.valueOf(ModuleType.SUICIDE, SubModuleType.SUICIDE_QUICK_CHARGE_ACTION));
		this.elements = newElements;
		refreshStats(true);
		PacketSendUtility.sendPacket(owner, SM_Suicide_Quick_Charge.valueOf(this));
	}

	@JsonIgnore
	public int getPlayerLevelAfterTurn() {
		if (this.turn == 0) {
			return owner.getLevel();
		}
		PlayerLevelResource resource = PlayerManager.getInstance().playerLevelResource.get(owner.getLevel(), true);
		return resource.getTurnLevel();

	}

	@JsonIgnore
	private void initElements() {
		for (SuicideElementType elementType : SuicideElementType.values()) {
			this.elements.put(elementType.getValue(), 0);
		}
	}

	@JsonIgnore
	private Integer[] getNotFullElementType() {
		return getNotFullElementType(this.elements);
	}

	@JsonIgnore
	private Integer[] getNotFullElementType(HashMap<Integer, Integer> allElement) {
		List<Integer> notFullElements = new ArrayList<Integer>();
		for (Map.Entry<Integer, Integer> entry : allElement.entrySet()) {
			SuicideElementResource resource = SuicideConfig.getInstance().elementStorage.getUnique(
					SuicideElementResource.TURN_TYPE_INDEX,
					SuicideElementResource.toTurnTypeIndex(this.turn + 1, entry.getKey()));
			if (resource.getCount() > entry.getValue()) {
				notFullElements.add(entry.getKey());
			}
		}
		return notFullElements.toArray(new Integer[0]);
	}

	@JsonIgnore
	private void addElementCount(int elementType, int addCount) {
		SuicideElementResource resource = SuicideConfig.getInstance().elementStorage.getUnique(
				SuicideElementResource.TURN_TYPE_INDEX,
				SuicideElementResource.toTurnTypeIndex(this.turn + 1, elementType));
		if (resource.getCount() <= this.elements.get(elementType)) {
			throw new ManagedException(ManagedErrorCode.ERROR_MSG);
		}

		this.elements.put(elementType, this.elements.get(elementType) + 1);
	}

	// getter-setter
	@JsonIgnore
	public Player getOwner() {
		return owner;
	}

	@JsonIgnore
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public HashMap<Integer, Integer> getElements() {
		return elements;
	}

	public void setElements(HashMap<Integer, Integer> elements) {
		this.elements = elements;
	}

}

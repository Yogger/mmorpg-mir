package com.mmorpg.mir.model.relive.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.capturetown.config.TownConfig;
import com.mmorpg.mir.model.chooser.manager.ChooserManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.common.exception.ManagedErrorCode;
import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.controllers.stats.RelivePosition;
import com.mmorpg.mir.model.core.action.CoreActionType;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.action.CurrencyAction;
import com.mmorpg.mir.model.core.action.ItemAction;
import com.mmorpg.mir.model.formula.Formula;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.kingofwar.config.KingOfWarConfig;
import com.mmorpg.mir.model.log.ModuleInfo;
import com.mmorpg.mir.model.log.ModuleType;
import com.mmorpg.mir.model.log.SubModuleType;
import com.mmorpg.mir.model.purse.model.CurrencyType;
import com.mmorpg.mir.model.relive.manager.PlayerReliveManager;
import com.mmorpg.mir.model.relive.resource.ReliveBaseResource;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.effecttemplate.FreeReliveEffect;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.world.World;
import com.mmorpg.mir.model.world.resource.MapResource;
import com.windforce.common.resource.anno.Static;

@Component
public class PlayerReliveServiceImpl implements PlayerReliveService {

	@Autowired
	private PlayerReliveManager reliveManager;

	@Static("PERSON:RELIFE_COST")
	private Formula RELIFE_COST;

	@Static("PERSON:RELIFE_COST_ITEM")
	private Formula RELIFE_COST_ITEM;

	@Static("PERSON:BOSS_AREA_RELIVE_COST")
	public Formula BOSS_AREA_RELIVE_COST;

	@Static("PERSON:BOSS_AREA_RELIVE_COST_ITEM")
	public Formula BOSS_AREA_RELIVE_COST_ITEM;

	@Static("PERSON:RELIVE_COST_ITEM_KEY")
	public ConfigValue<String> RELIVE_COST_ITEM_KEY;

	@Static("PERSON:RELIVE_SKILL_ID")
	public ConfigValue<Integer> RELIVE_SKILL_ID;

	/**
	 * @param player
	 * @param reliveType
	 *            True表示买活, False表示自动复活
	 */
	public void respawn(Player player, boolean isUseItem, boolean reliveType, boolean notLogin) {
		if (!player.getLifeStats().isAlreadyDead()) {
			throw new ManagedException(ManagedErrorCode.BUY_LIFE_NOT_DEAD);
		}
		MapResource currentMap = World.getInstance().getMapResource(player.getMapId());
		if (player.getGasCopy().cannotRelive() || currentMap.cannotRelive()) {
			return;
		}
		ReliveBaseResource reliveResource = reliveManager.getReliveResource(currentMap.getReliveBaseResourceId());
		boolean localRelive = reliveType && reliveResource.isAllowBuyLife();
		if (localRelive && !TownConfig.getInstance().isInTownsCopyMap(player.getMapId())) {
			standPointRelive(player, isUseItem);
			player.getLifeStats().fullStoreHpAndMp();
		} else { // 非 原地复活的
			safeAreaRelive(player, reliveResource, notLogin);
		}

		player.getMoveController().schedule();
		boolean originalLocRelive = (reliveType && (!currentMap.isCopy()));

		if (originalLocRelive || currentMap.getMapId() == KingOfWarConfig.getInstance().MAPID.getValue()) {
			Skill skill = SkillEngine.getInstance().getSkill(null, RELIVE_SKILL_ID.getValue(), player.getObjectId(), 0,
					0, player, null);
			skill.noEffectorUseSkill();
		}
	}

	private void standPointRelive(Player player, boolean isUseItem) {
		Effect freeEffect = player.getEffectController().getAnormalEffect(FreeReliveEffect.FREERELIVEEFFECT);
		if (freeEffect != null && freeEffect.getReserved3() != 0) {
			freeEffect.reduceReserved3(1);
			if (freeEffect.getReserved3() == 0) {
				player.getEffectController().removeEffect(FreeReliveEffect.FREERELIVEEFFECT);
			}
			player.getEffectController().broadCastSelf();
		} else {
			if (PlayerReliveManager.getInstance().isInBossRange(player)) {
				if (isUseItem) {
					if (System.currentTimeMillis() < player.getLifeStats().getLastDeadTime()
							+ PlayerReliveManager.getInstance().caculateBuyCountCD(player)) {
						throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_ITEM);
					}
					ItemAction itemAction = CoreActionType.createItemCondition(RELIVE_COST_ITEM_KEY.getValue(),
							PlayerReliveManager.getInstance().caculateBuyLifeCost(player, BOSS_AREA_RELIVE_COST_ITEM));
					if (!itemAction.verify(player)) {
						throw new ManagedException(ManagedErrorCode.NOT_ENOUGH_ITEM);
					}
					itemAction.act(player, ModuleInfo.valueOf(ModuleType.BUY_LIFE, SubModuleType.RELIVE_ACT));
				} else {
					CurrencyAction action = CoreActionType.createCurrencyCondition(CurrencyType.GOLD,
							PlayerReliveManager.getInstance().caculateBuyLifeCost(player, BOSS_AREA_RELIVE_COST));
					action.verify(player);
					action.act(player, ModuleInfo.valueOf(ModuleType.BUY_LIFE, SubModuleType.RELIVE_ACT));
				}
				player.getLifeStats().postBuyLife();
			} else {
				CoreActions actions = new CoreActions();
				CurrencyAction action = CoreActionType.createCurrencyCondition(CurrencyType.GOLD, PlayerReliveManager
						.getInstance().caculateBuyLifeCost(player, RELIFE_COST));
				ItemAction itemAction = CoreActionType.createItemCondition(RELIVE_COST_ITEM_KEY.getValue(),
						PlayerReliveManager.getInstance().caculateBuyLifeCost(player, RELIFE_COST_ITEM));
				actions.addAction(itemAction);
				if (!actions.verify(player, false)) {
					action.verify(player);
					action.act(player, ModuleInfo.valueOf(ModuleType.BUY_LIFE, SubModuleType.RELIVE_ACT));
				} else {
					actions.act(player, ModuleInfo.valueOf(ModuleType.BUY_LIFE, SubModuleType.RELIVE_ACT));
				}
			}
		}
	}

	private void safeAreaRelive(Player player, ReliveBaseResource reliveResource, boolean notLogin) {
		// 选出复活点
		List<String> results = ChooserManager.getInstance().chooseValueByRequire(player,
				reliveResource.getChooserGroupId());
		World.getInstance().despawn(player);
		player.getLifeStats().fullStoreHpAndMp();
		// 设置好 玩家复活后的地图信息
		RelivePosition p = reliveResource.getRelivePositions().get(results.get(0));
		if (p.getMapId() == player.getMapId()) {
			World.getInstance().setPosition(player, p.getMapId(), player.getInstanceId(), p.getX(), p.getY(),
					player.getHeading());
			World.getInstance().spawn(player);
		} else {
			World.getInstance().setPosition(player, p.getMapId(), p.getX(), p.getY(), player.getHeading());
		}
		if (notLogin) {
			player.sendUpdatePosition();
		}
	}
}

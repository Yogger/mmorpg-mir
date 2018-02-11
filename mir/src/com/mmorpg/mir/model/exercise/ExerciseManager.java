package com.mmorpg.mir.model.exercise;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.chat.manager.ChatManager;
import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.core.condition.BetweenCronTimeCondition;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.exercise.event.ExerciseEvent;
import com.mmorpg.mir.model.exercise.packet.SM_Exercise_Start;
import com.mmorpg.mir.model.formula.Formula;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.StatusNpc;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.i18n.manager.I18nUtils;
import com.mmorpg.mir.model.player.manager.PlayerManager;
import com.mmorpg.mir.model.session.SessionManager;
import com.mmorpg.mir.model.skill.effect.Effect;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.effecttemplate.EffectTemplate;
import com.mmorpg.mir.model.utils.MathUtil;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.mmorpg.mir.model.utils.ThreadPoolManager;
import com.mmorpg.mir.model.world.World;
import com.windforce.common.event.core.EventBusManager;
import com.windforce.common.resource.anno.Static;
import com.windforce.common.scheduler.ScheduledTask;
import com.windforce.common.scheduler.impl.SimpleScheduler;

@Component
public class ExerciseManager {

	private List<VisibleObject> torchs = new ArrayList<VisibleObject>();

	/** 操练加经验的时间间隔 毫秒 */
	@Static("EXERCISE:EXP_CHECKTIME")
	private ConfigValue<Integer> EXP_CHECKTIME;

	/** 操练加经验，离火把的最大距离 格子数（超过40取40） */
	@Static("EXERCISE:EXP_SCOPE")
	private ConfigValue<Integer> EXP_SCOPE;

	/** 操练经验加层的条件 时间开始 */
	@Static("EXERCISE:EXP_UP_START")
	private ConfigValue<String> EXP_UP_START;

	/** 操练经验加层的时间开始前5分钟的通报 */
	@Static("EXERCISE:EXP_UP_START_NOTICE")
	private ConfigValue<String> EXP_UP_START_NOTICE;

	/** 操练经验加层的 时间结束 */
	@Static("EXERCISE:EXP_UP_END")
	private ConfigValue<String> EXP_UP_END;

	@Static("EXERCISE:BUFF_FORMULA")
	public ConfigValue<Map<String, String>> BUFF_FORMULA;

	@Static("EXERCISE:EXERCISE_ACTIVITY")
	public ConfigValue<Integer> EXERCISE_ACTIVITY_ADDITION;

	@Static("PRACTICE_EXPERIENCE_1")
	public Formula PRACTICE_EXPERIENCE_1;

	@Static("PRACTICE_EXPERIENCE_2")
	public Formula PRACTICE_EXPERIENCE_2;

	@Static("PRACTICE_EXPERIENCE_3")
	public Formula PRACTICE_EXPERIENCE_3;

	@Static("PRACTICE_EXPERIENCE_4")
	public Formula PRACTICE_EXPERIENCE_4;

	@Static("PRACTICE_EXPERIENCE_5")
	public Formula PRACTICE_EXPERIENCE_5;
	
	@Static("PRACTICE_EXPERIENCE_6")
	public Formula PRACTICE_EXPERIENCE_6;
	
	@Static("EXERCISE:PRACTICE_ITEMS")
	public ConfigValue<String[]> PRACTICE_ITEMS;
	
	@Static("EXERCISE:PRACTICE_DAILY_LIMIT")
	public ConfigValue<Integer> PRACTICE_DAILY_LIMIT;

	@Autowired
	private ChatManager chatManager;

	private static ExerciseManager instance;

	@Autowired
	private SimpleScheduler simpleScheduler;

	@PostConstruct
	public void init() {
		instance = this;
	}

	public Formula getExerciseFormula(int effectTemplateId) {
		switch (effectTemplateId) {
		case 20048:
			return PRACTICE_EXPERIENCE_1;
		case 20049:
			return PRACTICE_EXPERIENCE_2;
		case 20050:
			return PRACTICE_EXPERIENCE_3;
		case 20051:
			return PRACTICE_EXPERIENCE_4;
		case 20052:
			return PRACTICE_EXPERIENCE_5;
		case 20070:
			return PRACTICE_EXPERIENCE_6;
		}
		return null;
	}

	/**
	 * 生成火把 和任务
	 */
	public void spawnAll() {

		List<VisibleObject> npcs1 = World.getInstance().getWorldMap(11002).getInstances().get(1)
				.findObjectBySpawnId("prac1");
		if (npcs1.isEmpty() || npcs1.size() > 1) {
			throw new RuntimeException(String.format("torch size[%s]", npcs1.size()));
		}
		torchs.add(npcs1.get(0));
		List<VisibleObject> npcs2 = World.getInstance().getWorldMap(21002).getInstances().get(1)
				.findObjectBySpawnId("prac2");
		if (npcs2.isEmpty() || npcs2.size() > 1) {
			throw new RuntimeException(String.format("torch size[%s]", npcs2.size()));
		}
		torchs.add(npcs2.get(0));

		List<VisibleObject> npcs3 = World.getInstance().getWorldMap(31002).getInstances().get(1)
				.findObjectBySpawnId("prac3");
		if (npcs3.isEmpty() || npcs3.size() > 1) {
			throw new RuntimeException(String.format("torch size[%s]", npcs3.size()));
		}
		torchs.add(npcs3.get(0));

		ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new Runnable() {
			@Override
			public void run() {
				addExp();
			}
		}, 3000, EXP_CHECKTIME.getValue());

		ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new Runnable() {
			@Override
			public void run() {
				check();
			}
		}, 3000, 2000);

		simpleScheduler.schedule(new ScheduledTask() {

			@Override
			public void run() {
				I18nUtils i18nUtils1 = I18nUtils.valueOf("601001");
				chatManager.sendSystem(61001, i18nUtils1, null);
			}

			@Override
			public String getName() {
				return "操练开始前的公告";
			}
		}, EXP_UP_START_NOTICE.getValue());

		simpleScheduler.schedule(new ScheduledTask() {

			@Override
			public void run() {
				for (Long playerId : SessionManager.getInstance().getOnlineIdentities()) {
					SM_Exercise_Start start = new SM_Exercise_Start();
					start.setStartTime(System.currentTimeMillis());
					PacketSendUtility.sendPacket(PlayerManager.getInstance().getPlayer(playerId), start);
				}
				I18nUtils i18nUtils1 = I18nUtils.valueOf("601002");
				chatManager.sendSystem(61001, i18nUtils1, null);
			}

			@Override
			public String getName() {
				return "操练加层时间开始";
			}
		}, EXP_UP_START.getValue());

		simpleScheduler.schedule(new ScheduledTask() {

			@Override
			public void run() {
				I18nUtils i18nUtils1 = I18nUtils.valueOf("601003");
				chatManager.sendSystem(61001, i18nUtils1, null);
			}

			@Override
			public String getName() {
				return "操练加层时间结束";
			}
		}, EXP_UP_END.getValue());

	}

	private Set<Long> inScope = new HashSet<Long>();

	private void check() {
		Set<Long> onceInScope = new HashSet<Long>();
		for (VisibleObject torch : torchs) {
			for (VisibleObject vo : torch.getKnownList()) {
				if (vo instanceof Player) {
					Player player = (Player) vo;
					if (player.getCountryValue() != torch.getSpawn().getCountry()) {
						if (player.getEffectController().isAbnoramlSet(EffectId.EXERCISE)) {
							player.getEffectController().unsetAbnormal(EffectId.EXERCISE.getEffectId(), true);
						}
						continue;
					}
					if (player.getEffectController().contains("PRACTICE")) {
						if (MathUtil.isInRange(torch, player, EXP_SCOPE.getValue(), EXP_SCOPE.getValue())) {
							onceInScope.add(player.getObjectId());
							if (!player.getEffectController().isAbnoramlSet(EffectId.EXERCISE)) {
								player.getEffectController().setAbnormal(EffectId.EXERCISE.getEffectId(), true);
								EventBusManager.getInstance().submit(ExerciseEvent.valueOf(player.getObjectId()));
							}
						}
					} else {
						if (player.getEffectController().isAbnoramlSet(EffectId.EXERCISE)) {
							player.getEffectController().unsetAbnormal(EffectId.EXERCISE.getEffectId(), true);
						}
					}
				}
			}
		}

		for (Long id : inScope) {
			if (!onceInScope.contains(id)) {
				PlayerManager.getInstance().getPlayer(id).getEffectController()
						.unsetAbnormal(EffectId.EXERCISE.getEffectId(), true);
			}
		}
		inScope = onceInScope;
	}

	private CoreConditions conditions;

	public boolean isExpUpIn() {
		return getExpUpCondition().verify(null);
	}

	private CoreConditions getExpUpCondition() {
		if (conditions != null) {
			return conditions;
		}
		BetweenCronTimeCondition bctc = new BetweenCronTimeCondition();
		bctc.setStartDate(EXP_UP_START.getValue());
		bctc.setEndDate(EXP_UP_END.getValue());
		CoreConditions cronTimeConditions = new CoreConditions();
		cronTimeConditions.addCondition(bctc);
		conditions = cronTimeConditions;
		return conditions;
	}

	private void addExp() {
		for (VisibleObject torch : torchs) {
			for (VisibleObject vo : torch.getKnownList()) {
				if (vo instanceof Player) {
					Player player = (Player) vo;
					if (player.getCountryValue() != torch.getSpawn().getCountry()) {
						if (player.getEffectController().isAbnoramlSet(EffectId.EXERCISE)) {
							player.getEffectController().unsetAbnormal(EffectId.EXERCISE.getEffectId(), true);
						}
						continue;
					}
					if (player.getEffectController().contains("PRACTICE")) {
						if (MathUtil.isInRange(torch, player, EXP_SCOPE.getValue(), EXP_SCOPE.getValue())) {
							Effect effect = player.getEffectController().getAnormalEffect("PRACTICE");
							for (EffectTemplate effectTemplate : effect.getSuccessEffect()) {
								effectTemplate.onPeriodicAction(effect);
							}
						}
					}
				}
			}
			if (torch instanceof StatusNpc) {
				((StatusNpc) torch).setStatus((int) (System.currentTimeMillis() % 1000000000));
			}
		}
	}

	public void clearExecrise(Player player) {
		player.getEffectController().unsetAbnormal(EffectId.EXERCISE.getEffectId(), true);
	}

	public static ExerciseManager getInstance() {
		return instance;
	}

	public int getExerciseAvailable(Player player) {
		int useCount = player.getPack().getTodayUseSituation(PRACTICE_ITEMS.getValue());
		return PRACTICE_DAILY_LIMIT.getValue() - useCount;
	}
}

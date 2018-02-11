package com.mmorpg.mir.model.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.cliffc.high_scale_lib.NonBlockingHashMap;

import com.mmorpg.mir.model.ai.desires.impl.AttackDesire.UseState;
import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.object.resource.SkillSelectorItemSample;
import com.mmorpg.mir.model.skill.SkillEngine;
import com.mmorpg.mir.model.skill.effect.EffectId;
import com.mmorpg.mir.model.skill.model.Skill;
import com.mmorpg.mir.model.skill.model.SkillTemplate;
import com.mmorpg.mir.model.skill.target.TargetManager;

public class SkillSelector {
	private List<SkillSelectorItem> items;

	private NonBlockingHashMap<Integer, Integer> skillUseCount = new NonBlockingHashMap<Integer, Integer>();

	protected Npc owner;

	public static SkillSelector valueOf(Npc owner, SkillSelectorItemSample[] skillSelectorItemSamples) {
		SkillSelector selector = new SkillSelector();
		if (skillSelectorItemSamples == null) {
			return selector;
		}
		selector.owner = owner;
		List<SkillSelectorItemSample> itemList = Arrays.asList(skillSelectorItemSamples);
		Collections.sort(itemList, new Comparator<SkillSelectorItemSample>() {
			@Override
			public int compare(SkillSelectorItemSample o1, SkillSelectorItemSample o2) {
				return o2.getPriority() - o1.getPriority();
			}
		});

		selector.items = new ArrayList<SkillSelectorItem>();
		int min = Integer.MAX_VALUE;
		for (SkillSelectorItemSample ssis : itemList) {
			selector.items.add(SkillSelectorItem.valueOf(ssis));
			int range = SkillEngine.getInstance().getSkillAtkRange(ssis.getSkillId());
			if (range < min) {
				min = range;
			}
		}

		owner.setAtkRange(Math.max(3, min));

		return selector;
	}

	public UseState selectAndUseSkill(Creature target) {
		UseState useState = UseState.FAIL;
		for (SkillSelectorItem skillItem : items) {

			if (!canUseSkill(skillItem.getSkillId())) {
				useState = UseState.WAIT;
				continue;
			}

			if (skillItem.getConditions() != null) {
				if (!skillItem.getConditions().verify(owner)) {
					useState = UseState.WAIT;
					continue;
				}
			}

			if (skillItem.getTargetConditions() != null) {
				if (!skillItem.getTargetConditions().verify(target)) {
					useState = UseState.WAIT;
					continue;
				}
			}

			Skill skill = SkillEngine.getInstance().getSkill(owner, skillItem.getSkillId(), target.getObjectId(), -1,
					-1, target, null);
			if (!TargetManager.startChooseTarget(skill)) {
				useState = UseState.CHANGE;
				continue;
			}

			skill.useSkill();
			if (!skillUseCount.containsKey(skillItem.getSkillId())) {
				skillUseCount.put(skillItem.getSkillId(), 1);
			} else {
				skillUseCount.put(skillItem.getSkillId(), skillUseCount.get(skillItem.getSkillId()) + 1);
			}
			useState = UseState.OK;
			break;
		}

		return useState;
	}

	private boolean canUseSkill(int skillId) {
		SkillTemplate skillTemplate = SkillEngine.getInstance().loadOrCreateSkillTemplate(skillId);
		if (owner.isPublicDisabled(skillTemplate.getPublicCoolDownGroup())) {
			return false;
		}
		if (owner.isSkillDisabled(skillTemplate.getSkillId())) {
			return false;
		}
		if (owner.getEffectController().isAbnoramlSet(EffectId.STUN)) {
			return false;
		}
		if (owner.getEffectController().isAbnoramlSet(EffectId.SILENCE)) {
			return false;
		}
		return true;
	}

	public List<SkillSelectorItem> getItems() {
		return items;
	}

	public void setItems(List<SkillSelectorItem> items) {
		this.items = items;
	}

}

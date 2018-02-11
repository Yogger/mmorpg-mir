package com.mmorpg.mir.model.ai;

import com.mmorpg.mir.model.gameobjects.Creature;
import com.mmorpg.mir.model.gameobjects.Npc;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.Summon;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.quest.keyhandle.impl.MonsterHunt;
import com.mmorpg.mir.model.quest.model.Quest;
import com.mmorpg.mir.model.quest.model.QuestKey;
import com.mmorpg.mir.model.utils.MathUtil;

public final class AIUtil {

	public static boolean addCommonHeat(Npc npc) {
		for (VisibleObject visibleObject : npc.getKnownList()) {
			if (visibleObject == null)
				continue;

			if (visibleObject instanceof Creature) {
				final Creature creature = (Creature) visibleObject;

				// TODO 这里还需要有距离的判断
				if (creature.getLifeStats() != null && !creature.getLifeStats().isAlreadyDead()) {

					if (!npc.isEnemy(creature))
						continue;

					if (npc.outWarning(creature.getX(), creature.getY())) {
						continue;
					}

					npc.getAggroList().addHate(creature, 1);

					return true;
				}
			}
		}

		return false;
	}

	public static boolean addBigBrotherHeat(Summon npc, String questId) {

		Player master = npc.getMaster();

		String monsterId = null;
		Quest quest = master.getQuestPool().getQuests().get(questId);
		if (quest != null) {
			for (QuestKey questKey : quest.getKeys()) {
				if (questKey.getParms().containsKey(MonsterHunt.MONSTERID)) {
					monsterId = (String) questKey.getParms().get(MonsterHunt.MONSTERID);
					break;
				}
			}
		}

		if (monsterId == null) {
			return false;
		}

		for (VisibleObject visibleObject : npc.getKnownList()) {
			if (visibleObject == null)
				continue;

			if (visibleObject instanceof Creature) {
				final Creature creature = (Creature) visibleObject;

				if (creature.getLifeStats() != null && !creature.getLifeStats().isAlreadyDead()) {

					if (!npc.isEnemy(creature)) {
						continue;
					}

					if (!creature.getObjectKey().equals(monsterId)) {
						continue;
					}

					npc.getAggroList().addHate(creature, 10000);

					return true;
				}
			}
		}

		return false;
	}

	public static boolean addClostHead(Npc npc) {
		int maxDis = Integer.MAX_VALUE;
		Creature target = null;

		for (VisibleObject visibleObject : npc.getKnownList()) {
			if (visibleObject == null)
				continue;

			if (visibleObject instanceof Creature) {
				final Creature creature = (Creature) visibleObject;

				// TODO 这里还需要有距离的判断
				if (creature.getLifeStats() != null && !creature.getLifeStats().isAlreadyDead()) {

					if (!npc.isEnemy(creature))
						continue;

					if (npc.outWarning(creature.getX(), creature.getY())) {
						continue;
					}

					int dis = MathUtil.getStreetDistance(npc.getX(), npc.getY(), creature.getX(), creature.getY());

					if (dis < maxDis) {
						maxDis = dis;
						target = creature;
					}
				}
			}
		}

		if (target != null) {
			npc.getAggroList().addHate(target, 1);
			return true;
		}

		return false;
	}

}

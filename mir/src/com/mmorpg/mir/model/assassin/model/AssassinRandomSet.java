package com.mmorpg.mir.model.assassin.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import com.mmorpg.mir.model.assassin.packet.SM_Assassin_Random;
import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.utility.RandomUtils;

public class AssassinRandomSet {

	private CopyOnWriteArrayList<AssassinRandVO> randomArrays = new CopyOnWriteArrayList<AssassinRandVO>();

	public void randPoinst(int hpConfig, Iterator<VisibleObject> playerIterator) {
		randomArrays.clear();
		ArrayList<AssassinRandVO> tempVOs = new ArrayList<AssassinRandVO>();
		while (playerIterator.hasNext()) {
			VisibleObject obj = playerIterator.next();
			if (obj instanceof Player) {
				AssassinRandVO vo = AssassinRandVO.valueOf((Player) obj, RandomUtils.betweenInt(1, 100, true));
				tempVOs.add(vo);
				PacketSendUtility.sendPacket((Player) obj, SM_Assassin_Random.valueOf(hpConfig, vo.getRandPoints()));
			}
		}
		randomArrays.addAll(tempVOs);
	}
	
	public ArrayList<AssassinRandVO> getResult() {
		ArrayList<AssassinRandVO> tempVOs = new ArrayList<AssassinRandVO>(randomArrays);
		Collections.sort(tempVOs);
		randomArrays.clear();
		randomArrays.addAll(tempVOs);
		int toIndex = (randomArrays.size() > 10) ? 10: randomArrays.size();
		return new ArrayList<AssassinRandVO>(randomArrays.subList(0, toIndex));
	}

	public CopyOnWriteArrayList<AssassinRandVO> getRandomArrays() {
		return randomArrays;
	}

	public void setRandomArrays(CopyOnWriteArrayList<AssassinRandVO> randomArrays) {
		this.randomArrays = randomArrays;
	}
	
}

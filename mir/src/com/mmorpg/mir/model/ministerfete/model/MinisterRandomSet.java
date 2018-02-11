package com.mmorpg.mir.model.ministerfete.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.gameobjects.VisibleObject;
import com.mmorpg.mir.model.ministerfete.packet.SM_Minister_Random;
import com.mmorpg.mir.model.utils.PacketSendUtility;
import com.windforce.common.utility.RandomUtils;

public class MinisterRandomSet {

	private CopyOnWriteArrayList<MinisterRandVO> randomArrays = new CopyOnWriteArrayList<MinisterRandVO>();

	public void randPoinst(int hpConfig, Iterator<VisibleObject> playerIterator) {
		randomArrays.clear();
		ArrayList<MinisterRandVO> tempVOs = new ArrayList<MinisterRandVO>();
		while (playerIterator.hasNext()) {
			VisibleObject obj = playerIterator.next();
			if (obj instanceof Player) {
				MinisterRandVO vo = MinisterRandVO.valueOf((Player) obj, RandomUtils.betweenInt(1, 100, true));
				tempVOs.add(vo);
				PacketSendUtility.sendPacket((Player) obj, SM_Minister_Random.valueOf(hpConfig, vo.getRandPoints()));
			}
		}
		randomArrays.addAll(tempVOs);
	}
	
	public ArrayList<MinisterRandVO> getResult() {
		ArrayList<MinisterRandVO> tempVOs = new ArrayList<MinisterRandVO>(randomArrays);
		Collections.sort(tempVOs);
		randomArrays.clear();
		randomArrays.addAll(tempVOs);
		int toIndex = (randomArrays.size() > 10) ? 10: randomArrays.size();
		return new ArrayList<MinisterRandVO>(randomArrays.subList(0, toIndex));
	}

	public CopyOnWriteArrayList<MinisterRandVO> getRandomArrays() {
		return randomArrays;
	}

	public void setRandomArrays(CopyOnWriteArrayList<MinisterRandVO> randomArrays) {
		this.randomArrays = randomArrays;
	}
	
}

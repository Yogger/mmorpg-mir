package com.mmorpg.mir.model.quest.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.h2.util.New;

import com.mmorpg.mir.model.quest.manager.QuestManager;
import com.mmorpg.mir.model.quest.packet.QuestVO;
import com.mmorpg.mir.model.quest.packet.SM_QuestUpdateVO;

public class QuestUpdate {
	private Map<String, Quest> quests = New.hashMap();

	public static QuestUpdate valueOf() {
		QuestUpdate update = new QuestUpdate();
		return update;
	}

	public SM_QuestUpdateVO createVO() {
		SM_QuestUpdateVO vo = new SM_QuestUpdateVO();
		vo.setVos(new ArrayList<QuestVO>());
		List<String> removeList = new ArrayList<String>();
		for (Entry<String, Quest> entry : quests.entrySet()) {
			if (entry.getValue() == null) {
				removeList.add(entry.getKey());
			} else {
				vo.getVos().add(entry.getValue().createVO());
			}
		}
		if (!removeList.isEmpty()) {
			vo.setShortRemove(QuestManager.getInstance().convertTemplateId(removeList));
		}
		return vo;
	}

	public Map<String, Quest> getQuests() {
		return quests;
	}

	public void setQuests(Map<String, Quest> quests) {
		this.quests = quests;
	}

}

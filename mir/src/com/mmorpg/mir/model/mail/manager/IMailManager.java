package com.mmorpg.mir.model.mail.manager;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.mmorpg.mir.model.gameobjects.Player;
import com.mmorpg.mir.model.mail.entity.MailEnt;
import com.mmorpg.mir.model.mail.model.Mail;
import com.mmorpg.mir.model.mail.model.MailGroup;

public interface IMailManager {
	public void init();

	MailEnt getMailEnt(Long playerId);

	void sendMail(Mail mail, Long playerId);

	void sendMailsByPlayerId(Mail mail, Collection<Long> playerIds);

	List<MailGroup> getMailGroups();

	void receiveGroupMail(Long playerId);

	void addMailGroup(MailGroup mailGroup);

	void readMail(Player player, int index);

	void rewardMail(Player player, int index);

	void deleteMail(Player player, Set<Integer> indexs, boolean isDirectDelete);
}

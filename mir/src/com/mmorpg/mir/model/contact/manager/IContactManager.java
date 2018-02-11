package com.mmorpg.mir.model.contact.manager;

import com.mmorpg.mir.model.contact.entity.ContactEnt;
import com.mmorpg.mir.model.contact.model.ContactRelationData;
import com.mmorpg.mir.model.contact.model.SocialNetData;
import com.mmorpg.mir.model.gameobjects.Player;

public interface IContactManager {
	void updateContact(Long playerId);

	void updateContact(ContactEnt contactEnt);

	ContactEnt getContactEnt(Long playerId);

	ContactEnt loadOrCreate(Long playerId);

	ContactRelationData getMyContactRelationData(Long playerId);

	SocialNetData getMySocialNetData(Long playerId);

	void addObserver(final Player player);
}

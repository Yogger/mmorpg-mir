package com.mmorpg.mir.model.contact.manager;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.mmorpg.mir.model.contact.entity.ContactEnt;
import com.mmorpg.mir.model.contact.model.ContactRelationData;
import com.mmorpg.mir.model.contact.model.SocialNetData;
import com.mmorpg.mir.model.contact.service.ContactService;
import com.mmorpg.mir.model.controllers.observer.ActionObserver;
import com.mmorpg.mir.model.controllers.observer.ActionObserver.ObserverType;
import com.mmorpg.mir.model.gameobjects.Player;
import com.windforce.common.ramcache.anno.Inject;
import com.windforce.common.ramcache.service.EntityBuilder;
import com.windforce.common.ramcache.service.EntityCacheService;
import com.windforce.common.resource.anno.Static;


@Component
public class ContactManager implements IContactManager{

	@Static("CONTACT:ATTENTION_SIZE_LIMIT")
	private ConfigValue<Integer> ATTENTION_SIZE_LIMIT;
	
	@Static("CONTACT:ENEMY_SIZE_LIMIT")
	private ConfigValue<Integer> ENEMY_SIZE_LIMIT;
	
	@Static("CONTACT:BLACKLIST_SIZE_LIMIT")
	private ConfigValue<Integer> BLACKLIST_SIZE_LIMIT;
	
	@Static("CONTACT:ENEMITY_LIMIT")
	public ConfigValue<Integer> ENEMITY_LIMIT;
	
	@Autowired
	private ContactService contactService;
	
	@Inject
	private EntityCacheService<Long, ContactEnt> contactDB;
	
	private static ContactManager self;
	
	public int getEnemySizeLimit() {
		return ENEMY_SIZE_LIMIT.getValue();
	}
	
	public int getAttentionSizeLimit() {
		return ATTENTION_SIZE_LIMIT.getValue();
	}
	
	public int getBlacklistSizeLimit() {
		return BLACKLIST_SIZE_LIMIT.getValue();
	}
	
	public int getEnemyEnemityLimit() {
		return ENEMITY_LIMIT.getValue();
	}
	
	@PostConstruct
    void init() {
		self = this;
	}
	
	public static ContactManager getInstance() {
		return self;
	}
	
	public void updateContact(Long playerId) {
		contactDB.writeBack(playerId, getContactEnt(playerId));
	}
	
	public void updateContact(ContactEnt contactEnt) {
		contactDB.writeBack(contactEnt.getId(), contactEnt);
	}
	
	public ContactEnt getContactEnt(Long playerId) {
		return contactDB.load(playerId);
	}
	
	public ContactEnt loadOrCreate(Long playerId) { 
		return contactDB.loadOrCreate(playerId, new EntityBuilder<Long, ContactEnt>() {

			@Override
			public ContactEnt newInstance(Long id) {
				return ContactEnt.valueOf(id);
			}
			
		});
	}
	
	public ContactRelationData getMyContactRelationData(Long playerId) {
		return getContactEnt(playerId).getMyRelationData();
	}
	
	public SocialNetData getMySocialNetData(Long playerId) {
		return getContactEnt(playerId).getMySocialData();
	}

	public void addObserver(final Player player) {
		player.getObserveController().addObserver(new ActionObserver(ObserverType.SPAWN) {
			@Override
			public void spawn(int mapId, int instanceId) {
				ContactEnt contactEnt = getContactEnt(player.getObjectId());
				if (contactEnt.isOpenMapInfo()) {
					contactEnt.getMySocialData().setMapId(mapId);
					contactEnt.getMySocialData().setMapInstanceId(instanceId);
					updateContact(contactEnt);
					contactService.notifyMyFans(player);
				}
			}
		});	
	}
	
}

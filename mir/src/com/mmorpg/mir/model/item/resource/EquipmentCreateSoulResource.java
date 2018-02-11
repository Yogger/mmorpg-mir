package com.mmorpg.mir.model.item.resource;

import java.util.Map;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.action.CoreActionManager;
import com.mmorpg.mir.model.core.action.CoreActionType;
import com.mmorpg.mir.model.core.action.CoreActions;
import com.mmorpg.mir.model.core.action.ItemAction;
import com.mmorpg.mir.model.gameobjects.stats.Stat;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Index;
import com.windforce.common.resource.anno.Resource;

@Resource
public class EquipmentCreateSoulResource {
	
	public static final String ITEM_INDEX = "itemKey";
	
	@Id
	private String id;
	/** 武魂对应的道具ID */
	@Index(name = ITEM_INDEX, unique = true)
	private String itemKey;
	/** 武魂对应到装备的属性 */
	private Stat[] stats;
	/** 武魂品质 */
	private int quality;
	/** 武魂镶嵌到武器时的广播 */
	private Map<String, Integer> noticeI18n;
	
	/** 对应的NPC拥有者的ObjectKey */
	private String objectKey;
	/** 对应的NPC拥有者收集这个的最大上限 */
	private int maxCollectCount;
	/** 合成对应武魂的碎片消耗 */
	private String[] combiningActionIds;
	
	@Transient
	private CoreActions equipCreateSoulCoreActions;
	
	@Transient
	private CoreActions combiningCoreActions;
	
	@JsonIgnore
	public CoreActions getEquipCreateSoulCoreActions() {
		if (equipCreateSoulCoreActions == null) {
			ItemAction action = CoreActionType.createItemCondition(itemKey, 1);
			CoreActions actions = new CoreActions();
			actions.addActions(action);
			equipCreateSoulCoreActions = actions;
		}
		return equipCreateSoulCoreActions;
	}
	
	@JsonIgnore
	public CoreActions getCombiningCoreActions(int count) {
		if (count == 1) {
			if (combiningCoreActions == null) {
				combiningCoreActions = CoreActionManager.getInstance().getCoreActions(count, combiningActionIds);
			}
			return combiningCoreActions;
		}
		return CoreActionManager.getInstance().getCoreActions(count, combiningActionIds);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Stat[] getStats() {
		return stats;
	}

	public void setStats(Stat[] stats) {
		this.stats = stats;
	}

	public int getMaxCollectCount() {
		return maxCollectCount;
	}

	public void setMaxCollectCount(int maxCollectCount) {
		this.maxCollectCount = maxCollectCount;
	}

	public String getItemKey() {
		return itemKey;
	}

	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public Map<String, Integer> getNoticeI18n() {
		return noticeI18n;
	}

	public void setNoticeI18n(Map<String, Integer> noticeI18n) {
		this.noticeI18n = noticeI18n;
	}

	public String getObjectKey() {
		return objectKey;
	}

	public void setObjectKey(String objectKey) {
		this.objectKey = objectKey;
	}

	public String[] getCombiningActionIds() {
		return combiningActionIds;
	}

	public void setCombiningActionIds(String[] combiningActionIds) {
		this.combiningActionIds = combiningActionIds;
	}

}

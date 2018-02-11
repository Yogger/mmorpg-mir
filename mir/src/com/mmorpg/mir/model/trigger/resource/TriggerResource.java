package com.mmorpg.mir.model.trigger.resource;

import java.util.Map;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.h2.util.New;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.trigger.model.TriggerType;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;
import com.windforce.common.utility.JsonUtils;

@Resource
public class TriggerResource {
	@Id
	private String id;
	/** 触发条件 */
	private String[] conditions;
	/** 触发器类型 */
	private TriggerType type;
	/** 触发器上下文 */
	private Map<String, String> keys;
	/** 是否是客服端请求 */
	private boolean clientTrigger;

	@Transient
	private CoreConditions coreConditions;

	public static void main(String[] args) {
		TriggerResource resource = new TriggerResource();
		Map<String, String> k = New.hashMap();
		k.put("ss", "dd");
		resource.setKeys(k);
		System.out.println(JsonUtils.object2String(resource));

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getConditions() {
		return conditions;
	}

	public void setConditions(String[] conditions) {
		this.conditions = conditions;
	}

	public boolean isClientTrigger() {
		return clientTrigger;
	}

	public void setClientTrigger(boolean clientTrigger) {
		this.clientTrigger = clientTrigger;
	}

	public Map<String, String> getKeys() {
		return keys;
	}

	public void setKeys(Map<String, String> keys) {
		this.keys = keys;
	}

	public TriggerType getType() {
		return type;
	}

	public void setType(TriggerType type) {
		this.type = type;
	}

	@JsonIgnore
	public CoreConditions getCoreConditions() {
		if (coreConditions == null) {
			if (conditions == null) {
				coreConditions = new CoreConditions();
			} else {
				coreConditions = CoreConditionManager.getInstance().getCoreConditions(1, conditions);
			}
		}
		return coreConditions;
	}

	@JsonIgnore
	public void setCoreConditions(CoreConditions coreConditions) {
		this.coreConditions = coreConditions;
	}
}

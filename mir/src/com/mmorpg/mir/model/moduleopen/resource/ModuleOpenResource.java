package com.mmorpg.mir.model.moduleopen.resource;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mmorpg.mir.model.core.condition.CoreConditionManager;
import com.mmorpg.mir.model.core.condition.CoreConditions;
import com.mmorpg.mir.model.moduleopen.model.ModuleOpenFactorType;
import com.windforce.common.resource.anno.Id;
import com.windforce.common.resource.anno.Resource;

@Resource
public class ModuleOpenResource {
	@Id
	private String id;
	/** 模块号,如果有的话*/
	private int moduleKey;
	/** 是否立即推送给前端  */
	private boolean immediatelyPush;
	/** 模块开启的条件*/
	private String[] conditionIds;
	/** 模块开启的条件的且或判断 */
	private boolean conditionVerify;
	/** 模块开启的起因类型*/
	private ModuleOpenFactorType openFactortype;
	/** 登陆开启的时候 是否通知前端新开启*/
	private boolean loginOpenNotice;

	@JsonIgnore
	private CoreConditions condition;

	@JsonIgnore
	public CoreConditions getConditions() {
		if (condition == null) {
			if (conditionIds == null) {
				condition = new CoreConditions();
			} else {
				condition = CoreConditionManager.getInstance().getCoreConditions(1, conditionIds);
			}
		}
		return condition;
	}
	
	public ModuleOpenFactorType getOpenFactortype() {
		return openFactortype;
	}

	public void setOpenFactortype(ModuleOpenFactorType openFactortype) {
		this.openFactortype = openFactortype;
	}

	public int getModuleKey() {
		return moduleKey;
	}

	public void setModuleKey(int moduleKey) {
		this.moduleKey = moduleKey;
	}

	public String[] getConditionIds() {
		return conditionIds;
	}

	public void setConditionIds(String[] conditionIds) {
		this.conditionIds = conditionIds;
	}

	public String getId() {
    	return id;
    }

	public void setId(String id) {
    	this.id = id;
    }

	public boolean isImmediatelyPush() {
    	return immediatelyPush;
    }

	public void setImmediatelyPush(boolean immediatelyPush) {
    	this.immediatelyPush = immediatelyPush;
    }

	public boolean isConditionVerify() {
    	return conditionVerify;
    }

	public void setConditionVerify(boolean conditionVerify) {
    	this.conditionVerify = conditionVerify;
    }

	public boolean isLoginOpenNotice() {
		return loginOpenNotice;
	}

	public void setLoginOpenNotice(boolean loginOpenNotice) {
		this.loginOpenNotice = loginOpenNotice;
	}

}

package com.mmorpg.mir.model.core.condition;

import org.apache.commons.lang.StringUtils;

import com.mmorpg.mir.model.commonactivity.CommonActivityConfig;
import com.mmorpg.mir.model.commonactivity.resource.CommonSPServerResource;
import com.mmorpg.mir.model.serverstate.ServerState;

public class ServerNameCondition extends AbstractCoreCondition {

	@Override
	public boolean verify(Object object) {
		String serverName = ServerState.getInstance().getServerName();
		CommonSPServerResource resource = CommonActivityConfig.getInstance().getResourceContainName(serverName);
		if (resource == null) {
			return false;
		}

		if (StringUtils.isBlank(resource.getId())) {
			return false;
		}
		if (resource.getId().equals(this.code)) {
			return true;
		}
		return false;
	}
}

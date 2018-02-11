package com.mmorpg.mir.model.core.condition;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.mmorpg.mir.model.common.exception.ManagedException;
import com.mmorpg.mir.model.core.condition.model.CoreConditionResource;
import com.mmorpg.mir.model.serverstate.ServerState;
import com.windforce.common.utility.DateUtils;

public class ServerOpenCondition extends AbstractCoreCondition {

	private int low;
	private int high;
	
	private int errorCode;

	private String operator;

	@Override
	public boolean verify(Object object) {
		Date openDate = ServerState.getInstance().getOpenServerDate();
		if (openDate != null) {
			int interval = DateUtils.calcIntervalDays(openDate, new Date());
			if (!StringUtils.isBlank(this.code) && !StringUtils.equals("0", this.code)
					&& !StringUtils.equals("1", this.code)) {
				// 用code用来取余 判断是否有效
				interval %= Integer.parseInt(this.code);
			}
			if (StringUtils.isNotBlank(this.operator)) {
				if (">=".equals(this.operator)) {
					return interval >= this.value;
				} else if (">".equals(this.operator)) {
					return interval > this.value;
				} else if ("=".equals(this.operator)) {
					return interval == this.value;
				} else if ("<=".equals(this.operator)) {
					return interval <= this.value;
				} else if ("<".equals(this.operator)) {
					return interval < this.value;
				}
				throw new IllegalArgumentException("操作符配置异常：" + this.operator);
			} else {
				if (interval >= low && interval <= high) {
					return true;
				}
				throw new ManagedException(errorCode);
			}

		}
		return false;
	}

	@Override
	protected void init(CoreConditionResource resource) {
		super.init(resource);
		this.low = resource.getLow();
		this.high = resource.getHigh();
		this.operator = resource.getOperator();
		this.errorCode = resource.getSvrErrorCode();
	}

	@Override
	protected boolean check(AbstractCoreCondition condition) {
		return false;
	}
}

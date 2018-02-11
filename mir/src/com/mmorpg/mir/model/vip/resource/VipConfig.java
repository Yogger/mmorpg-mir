package com.mmorpg.mir.model.vip.resource;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.windforce.common.resource.anno.Static;

@Component
public class VipConfig {
	private static VipConfig self;

	@PostConstruct
	public void init() {
		self = this;
	}

	public static VipConfig getInstance() {
		return self;
	}

	/** 超级VIP最小充值 */
	@Static("VIP:SUPER_VIP_MINCHARGE")
	public ConfigValue<Integer> SUPER_VIP_MINCHARGE;

	/** 超级VIP周期 */
	@Static("VIP:SUPER_VIP_CIRCLE")
	public ConfigValue<Integer> SUPER_VIP_CIRCLE;

	/** 超级VIP单笔充值达成额度 */
	@Static("VIP:SUPER_VIP_ONECHARGE")
	public ConfigValue<Integer> SUPER_VIP_ONECHARGE;

	/** 超级VIP开启等级 */
	@Static("VIP:SUPER_VIP_LEVEL")
	public ConfigValue<Integer> SUPER_VIP_LEVEL;

}

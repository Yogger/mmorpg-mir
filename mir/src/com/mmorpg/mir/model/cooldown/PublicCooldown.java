package com.mmorpg.mir.model.cooldown;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.mmorpg.mir.model.common.ConfigValue;
import com.windforce.common.resource.anno.Static;

@Component
public final class PublicCooldown {

	@Static("PUBLIC:PUBLIC_COOLDOWNS")
	private ConfigValue<Integer[]> publicCooldowns;

	private static PublicCooldown self;

	@PostConstruct
	public void init() {
		self = this;
	}

	public static PublicCooldown getInstance() {
		return self;
	}

	public long getPublicCooldown(int group) {
		return publicCooldowns.getValue()[group];
	}

}

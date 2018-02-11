package com.mmorpg.mir.model.resourcecheck;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.h2.util.New;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class ResourceCheck implements Ordered {
	// 配置表完整性检查

	public static Map<Class<?>, ResourceCheckHandle> handles = New.hashMap();

	public static ResourceCheck instance;

	@PostConstruct
	public void init() {
		instance = this;
		checkAllResource();
	}

	public void checkAllResource() {
		for (ResourceCheckHandle handle : handles.values()) {
			try {
				handle.check();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getOrder() {
		return Integer.MIN_VALUE;
	}
}
